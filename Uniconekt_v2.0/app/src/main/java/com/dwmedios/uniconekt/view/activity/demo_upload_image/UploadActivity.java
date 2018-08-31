package com.dwmedios.uniconekt.view.activity.demo_upload_image;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.service.api.apiConfiguraciones;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.google.gson.annotations.SerializedName;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.dwmedios.uniconekt.data.service.ClientService.URL_BASE_TEMP;

public class UploadActivity extends BaseActivity {
    @BindView(R.id.cargar)
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);
        mButton.setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pickImage();
        }
    };

    public void pickImage() {
        List<String> permison = new ArrayList<>();
        permison.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permison.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (validatePermison(permison, this, 500)) {

            String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            // photoPickerIntent.setType("image/*.jpg");
            //photoPickerIntent.setType("image/*.jpeg");
            photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(Intent.createChooser(photoPickerIntent, "Seleccione una imagen"), 500);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 500:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    try {
                        enviarArchivo(selectedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "No se ha seleccionado una fotografía.", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    void enviarArchivo(Uri mUri) {
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl("http://verzity.dwmedios.com/SITE/UniversidadView/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiMultipart apiMultipart = mRetrofit.create(apiMultipart.class);
        MultipartBody.Part mPart = prepareFilePart("filepatch", mUri);
        apiMultipart.upload(mPart).enqueue(new Callback<responseUpload>() {
            @Override
            public void onResponse(Call<responseUpload> call, Response<responseUpload> response) {
                showMessage(response.body().mensaje);
            }

            @Override
            public void onFailure(Call<responseUpload> call, Throwable t) {
                showMessage(t.getMessage());
            }
        });

    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        String urireal = getRealPathFromUri(fileUri);
        File file = new File(urireal);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public interface apiMultipart {
        @Multipart
        @POST("UploadFoto")
        Call<responseUpload> upload(@Part MultipartBody.Part file);
    }

    public class responseUpload implements Parcelable {
        public static final String estatus1 = "Estatus";
        public static final String data1 = "Data";
        public static final String mensaje1 = "Mensaje";
        @SerializedName(estatus1)
        public int estatus;
        @SerializedName(data1)
        public String data;
        @SerializedName(mensaje1)
        public String mensaje;

        protected responseUpload(Parcel in) {
            estatus = in.readInt();
            data = in.readString();
            mensaje = in.readString();
        }

        public final Creator<responseUpload> CREATOR = new Creator<responseUpload>() {
            @Override
            public responseUpload createFromParcel(Parcel in) {
                return new responseUpload(in);
            }

            @Override
            public responseUpload[] newArray(int size) {
                return new responseUpload[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(estatus);
            dest.writeString(data);
            dest.writeString(mensaje);
        }
    }

    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
