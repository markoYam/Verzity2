package com.dwmedios.uniconekt.view.util.UtilsFtp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Configuraciones;
import com.dwmedios.uniconekt.presenter.ConfiguracionesPresenter;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ftpClient extends BaseActivity {
    /**
     * library compile 'com.adeel:easyFTP:1.0'
     */
    private String TAG = "FTP Client";
    private String mensaje_Error = "Ocurrió un error durante la carga. ";
    private Context mContext;

    private String urlServer = "ftp.smarterasp.net";
    private String passServer = "ftp.Verzity";
    private String userServer = "ftpVerzity";

    private String directory = "";
    private String directory_patch = "";
    private ftpServer mEasyFTP;
    private boolean isConected = false;
    private patchImageInterface mPatchImage;
    public final int codeResult = 80;
    public final int codeResult_Permison = 90;
    private Activity mActivity;
    private Uri patchImage = null;
    private String patchImageFull = null;
    private ProgressDialog mProgressDialog;
    private ImageView mImageView;
    private Configuraciones mConfiguraciones;
    private ConfiguracionesPresenter mConfiguracionesPresenter;

    public ftpClient(Context mContext, Activity mActivity) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        mEasyFTP = new ftpServer();
        configureServer();
        /** conectar al servidor**/
        new connectServer().execute();
    }

    private void configureServer() {
        mConfiguracionesPresenter = new ConfiguracionesPresenter(mContext);
        mConfiguraciones = mConfiguracionesPresenter.getConfiguraciones();
        if (mConfiguraciones != null) {
            this.userServer = mConfiguraciones.usuario;
            this.passServer = mConfiguraciones.password;
            this.directory_patch = mConfiguraciones.root_folder;
        }
    }

    public class connectServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showOnProgressDialog("Conectando...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                mEasyFTP.connect(urlServer, userServer, passServer);
                isConected = true;
                boolean status = false;
                if (!directory.isEmpty()) {
                    status = mEasyFTP.setWorkingDirectory(directory); // if User say provided any Destination then Set it , otherwise
                    if (status)
                        Log.e(TAG, "Directory Change");
                    else Log.e(TAG, "Directory not change");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e(TAG, "Fallo de inicio de sesion");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dismissProgressDialog();

        }
    }

    public void pickImage(patchImageInterface mPatchImage, ImageView mImageView) {
        this.mPatchImage = mPatchImage;
        this.mImageView = mImageView;
        List<String> permison = new ArrayList<>();
        permison.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permison.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (validatePermison(permison, mActivity, codeResult_Permison)) {

            String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            // photoPickerIntent.setType("image/*.jpg");
            //photoPickerIntent.setType("image/*.jpeg");
            photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            mActivity.startActivityForResult(Intent.createChooser(photoPickerIntent, "Seleccione una imagen"), codeResult);
        }
    }

    public void activityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case codeResult:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = mContext.getContentResolver().openInputStream(selectedImage);
                        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                        mImageView.setImageBitmap(yourSelectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    // if (mPatchImage != null) mPatchImage.patch(selectedImage);
                    this.patchImage = selectedImage;
                    configureDownload();
                } else {
                    Toast.makeText(mContext, "No se ha seleccionado una fotografía.", Toast.LENGTH_SHORT).show();
                    //if (mImageView != null)
                    // mImageView.setImageResource(R.drawable.profile);
                }
                break;
        }
    }

    public void permisonResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.codeResult) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                // photoPickerIntent.setType("image/*.jpg");
                //photoPickerIntent.setType("image/*.jpeg");
                photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                mActivity.startActivityForResult(Intent.createChooser(photoPickerIntent, "Seleccione una imagen"), codeResult);

                return;
            } else {

                showInfoDialogListener("Atención", "Es nesesario otorgar los permisos para continuar", "OK");
            }
        }
    }

    public void configureDownload() {
        dismissProgressDialog();
        if (patchImage != null) {


            patchImageFull = getPath(patchImage);
            if (!patchImageFull.isEmpty())
                new uploadFile().execute();
            else
                showMessage(mensaje_Error);
        } else
            showMessage(mensaje_Error);
    }

    public class uploadFile extends AsyncTask<Void, Void, Void> {
        ftpServer.Response mResponse = new ftpServer.Response();

        @Override
        protected void onPreExecute() {
            showOnProgressDialog("Cargando...");
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConected) {
                try {
                    if (patchImageFull != null) {
                        mResponse = mEasyFTP.uploadFile(patchImageFull);
                    }
                } catch (Exception e) {
                    mResponse = new ftpServer.Response();
                    e.printStackTrace();
                }
            } else {
                mResponse = new ftpServer.Response();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dismissProgressDialog();
            if (mResponse.status == 1) {
                showMessage("Imagen cargada con éxito ");
                if (mPatchImage != null) mPatchImage.patch(directory_patch + mResponse.patch);
            } else {
                if (mPatchImage != null) mPatchImage.patch(directory_patch + mResponse.patch);
                mImageView.setImageResource(R.drawable.profile);
                showMessage(mensaje_Error);
            }
        }
    }

    public String getPath(Uri uri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(mContext, uri, proj, null, null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(column_index);
            cursor.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new String();
        }

    }

    /***
     * Interfaces para transferencia de datos....
     */
    public interface patchImageInterface {
        void patch(String patch);
    }

    /**
     * Este metodo moverlo despues a una sola clase en general
     *
     * @param mensaje
     */
    public void showMessage(String mensaje) {
        Toast.makeText(mContext, mensaje, Toast.LENGTH_SHORT).show();
    }

    public void showOnProgressDialog(String message) {
        try {
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setMessage(message);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgress(0);
            mProgressDialog.setMax(100);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissProgressDialog() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

