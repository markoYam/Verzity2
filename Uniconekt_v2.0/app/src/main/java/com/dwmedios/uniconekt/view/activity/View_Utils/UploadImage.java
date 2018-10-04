package com.dwmedios.uniconekt.view.activity.View_Utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

public class UploadImage extends BaseActivity {
    private static final String URL = "http://verzity.dwmedios.com/SITE/UniversidadView/";
    private String patchPhoto;
    private int request = 3000;
    private Activity mActivity;
    private resultInfo mResultInfo;

    public UploadImage(Activity mActivity, resultInfo mResultInfo) {
        this.mActivity = mActivity;
        this.mResultInfo = mResultInfo;
    }

    public void pickImage() {
        List<String> permison = new ArrayList<>();
        permison.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permison.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (validatePermison(permison, mActivity, 50)) {
            String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            mActivity.startActivityForResult(Intent.createChooser(photoPickerIntent, "Seleccione una imagen"), 500);
        }
    }

    private ImageView mImageView;

    public void OnActivityResult(int requestCode, int resultCode, Intent data, ImageView mImageView) {
        if (mImageView != null) this.mImageView = mImageView;
        switch (requestCode) {
            case 500:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    try {
                        String patch = getRealPathFromUri(selectedImage);
                        Glide
                                .with(mActivity)
                                .load(patch)
                                .into(mImageView);
                        enviarArchivo(selectedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                        this.mImageView.setImageResource(R.drawable.profile);
                    }
                } else {
                    this.mImageView.setImageResource(R.drawable.profile);
                    Toast.makeText(mActivity, "No se ha seleccionado una fotografía.", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                this.mImageView.setImageResource(R.drawable.profile);
                break;
        }
    }

    public void onRequestPermison(int requestCode) {
        switch (requestCode) {
            case 50:
                pickImage();
                break;
        }
    }

    void enviarArchivo(Uri mUri) {
        mResultInfo.Onloading(true);
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UploadImage.apiMultipart apiMultipart = mRetrofit.create(UploadImage.apiMultipart.class);
        MultipartBody.Part mPart = prepareFilePart("filepatch", mUri);
        apiMultipart.upload(mPart).enqueue(new Callback<UploadImage.responseUpload>() {
            @Override
            public void onResponse(Call<UploadImage.responseUpload> call, Response<UploadImage.responseUpload> response) {
                mResultInfo.Onloading(false);
                responseUpload res = response.body();
                switch (res.estatus) {
                    case 1:
                        mResultInfo.Onsucces(res.data.toString(),"Fotografía cargada con éxito");
                        break;
                    case 0:
                        mResultInfo.Onfailed(res.mensaje.toString());
                        break;
                    case -1:
                        mResultInfo.Onfailed(error);
                        break;
                }
            }

            @Override
            public void onFailure(Call<UploadImage.responseUpload> call, Throwable t) {
                mResultInfo.Onfailed(error);
                mResultInfo.Onloading(false);

            }
        });
    }

    private String error = "Ocurrió un error al subir la fotografía";

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        String urireal = getRealPathFromUri(fileUri);
        File file = new File(urireal);
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(mActivity.getContentResolver().getType(fileUri)),
                        file
                );

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public interface apiMultipart {
        @Multipart
        @POST("UploadFoto")
        Call<UploadImage.responseUpload> upload(@Part MultipartBody.Part file);
    }

    public interface resultInfo {
        void Onsucces(String patch,String mensaje);

        void Onfailed(String mensaje);

        void Onloading(boolean isLoading);
    }

    public class responseUpload implements Parcelable {
        public static final String estatus1 = "Estatus";
        public static final String data1 = "data";
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

        public final Creator<UploadImage.responseUpload> CREATOR = new Creator<UploadImage.responseUpload>() {
            @Override
            public UploadImage.responseUpload createFromParcel(Parcel in) {
                return new UploadImage.responseUpload(in);
            }

            @Override
            public UploadImage.responseUpload[] newArray(int size) {
                return new UploadImage.responseUpload[size];
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
            cursor = mActivity.getContentResolver().query(contentUri, proj, null, null, null);
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
