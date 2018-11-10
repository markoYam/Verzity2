package com.dwmedios.uniconekt.view.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.dwmedios.uniconekt.data.service.api.apiConfiguraciones;
import com.dwmedios.uniconekt.data.service.response.YoutubeResponse;
import com.dwmedios.uniconekt.model.Notificaciones;
import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.internal.Utility.isNullOrEmpty;

/**
 * Created by mYam on 16/04/2018.
 */

public class Utils {
    public static int current_item = 0;
    public static int filtro_licenciaturas = 1;
    public static int filtro_becas = 1;
    public static int filtro_financiamientos = 1;
    public static int tipoBusqueda_Universidad = 1;
    public static boolean busqueda_universidades = false;

    public static void showNotification(Context mContext, Notificaciones mNotification) {
        try {

            // toast mToast = new toast(mContext);
            // mToast.setview2();
            //  mToast.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setAnimRecyclerView(Context mContext, int anim, RecyclerView mRecyclerView) {
        LayoutAnimationController mLayoutAnimationController = AnimationUtils.loadLayoutAnimation(mContext, anim);
        mRecyclerView.setLayoutAnimation(mLayoutAnimationController);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mRecyclerView.scheduleLayoutAnimation();

    }

    public static void startActivityUp(Context mContext, Class<?> mActivity) {
        try {
            Intent intent = new Intent(mContext, mActivity);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static String ConvertModelToStringGson(Object mObject) {
        try {
            Gson gson = new Gson();
            return gson.toJson(mObject);
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    public static void setTypeFace(TextView textView, Context mContext) {
        try {
            Typeface type = Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSans-Light.ttf");
            textView.setTypeface(type);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Drawable getDrawable(String name, Context mContext) {
        Context context = mContext;
        int resourceId = context.getResources().getIdentifier(name, "drawable", mContext.getPackageName());
        return context.getResources().getDrawable(resourceId);
    }

    public static Drawable getDrawable2(String name, Context mContext) {
        Context context = mContext;
        int resourceId = context.getResources().getIdentifier(name, "drawable", mContext.getPackageName());
        return context.getResources().getDrawable(resourceId);
    }

    /*  public static void ShowPoupDialod(Context mContext, int view, String url) {
          Dialog mDialog = new Dialog(mContext);
          mDialog.setContentView(view);
          ImageView mImageView = mDialog.findViewById(R.id.viewImageDialog);
          ImageLoader.getInstance().displayImage(getUrlImage(url), mImageView, OptionsImageLoaderLight);
          mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
          mDialog.show();
      }
  */
    public static String ERROR_CONECTION = "Ocurrió un error en la conexión";

    public static String configurefecha(Date mDate) {
        String fecha = null;
        SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {

            fecha = mDateFormat.format(mDate);

        } catch (Exception e) {
            Log.e("fecha", e.getMessage() + mDate);
            fecha = "No Asignado";
        }
        return fecha;
    }

    public static String configurefechaCompleted(Date mDate) {
        String fecha = null;
        SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        try {

            fecha = mDateFormat.format(mDate);

        } catch (Exception e) {
            Log.e("fecha", e.getMessage() + mDate);
            fecha = "No Asignado";
        }
        return fecha;
    }

    public static String getDays(Date mDate) {
        try {
            return String.valueOf(android.text.format.DateFormat.format("EEEE", mDate));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void taskVideoYutube(String codeYoutube) {

        Retrofit mRetrofit;
        final YoutubeResponse[] mYoutubeResponse = new YoutubeResponse[1];
        String url = "https://www.youtube.com/";

        mRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiConfiguraciones mApiConfiguraciones = mRetrofit.create(apiConfiguraciones.class);
        mApiConfiguraciones.getConfiguraciones("http://www.youtube.com/watch?v=" + codeYoutube + "&format=json").enqueue(new Callback<YoutubeResponse>() {
            @Override
            public void onResponse(Call<YoutubeResponse> call, Response<YoutubeResponse> response) {
                mYoutubeResponse[0] = response.body();
            }

            @Override
            public void onFailure(Call<YoutubeResponse> call, Throwable t) {

            }
        });
    }

    public static class DownloadFiles extends AsyncTask<Void, Void, Void> {
        private String urlDownload;
        private String name;
        private Context mContext;
        private Dialog mLovelyProgressDialog;
        private boolean success = false;
        private Activity mActivity;
        private String nombreArchivo;
        private String type;

        public DownloadFiles(String urlDownload, String name, Context mContext, Activity mActivity) {
            this.urlDownload = urlDownload;
            this.name = name;
            this.mContext = mContext;
            this.mActivity = mActivity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mActivity.runOnUiThread(new Runnable() {
                public void run() {
                   /* mLovelyProgressDialog = new Dialog(mActivity)
                            .setIcon(R.drawable.ic_action_information)
                            .setTitle("Descargando...")
                            .setTopColorRes(R.color.colorPrimaryDark)
                            .show();*/
                    Toast.makeText(mContext, "Descargando...", Toast.LENGTH_LONG).show();
                }
            });


        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = urlDownload;
                if (url != null) {

                    URL u = new URL(url);
                    InputStream is = u.openStream();

                    DataInputStream dis = new DataInputStream(is);

                    byte[] buffer = new byte[1024];
                    int length;
                    String[] nombre = urlDownload.split("/");
                    String nb = nombre[(nombre.length - 1)];
                    //   String[] ty = nb.split("\\.");

                    String rutaFisica = Environment.getExternalStorageDirectory() +
                            File.separator + "DW_MEDIOS" + File.separator + "VERZITY" + File.separator + name + File.separator + nb;
                    File folder = new File(rutaFisica);

                    boolean succ = true;
                    if (!folder.exists()) {
                        succ = folder.mkdirs();
                    }
                    if (succ) {
                        nombreArchivo = rutaFisica + "/" + nb;
                        FileOutputStream fos = new FileOutputStream(new File(nombreArchivo));
                        while ((length = dis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                        success = true;
                    } else {

                    }

                }
            } catch (MalformedURLException mue) {
                Log.e("SYNC getUpdate", "malformed url error", mue);
            } catch (IOException ioe) {
                Log.e("SYNC getUpdate", "io error", ioe);
            } catch (SecurityException se) {
                Log.e("SYNC getUpdate", "security error", se);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mActivity.runOnUiThread(new Runnable() {
                public void run() {
                    if (mLovelyProgressDialog != null) {
                        mLovelyProgressDialog.dismiss();
                    }
                }
            });

            if (success) {
                Toast.makeText(mContext, "Operación realizada con éxito.", Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, "El archivo se guardo en la ruta" + nombreArchivo, Toast.LENGTH_LONG).show();
                //mContext.startActivity(new Intent(mContext, PDFViewerActivity.class).putExtra(KEY_VIEWER, nombreArchivo).addFlags(FLAG_ACTIVITY_NEW_TASK));
            } else {
                Toast.makeText(mContext, "Ocurrió un error en la descarga.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static final String Folder_rutaPerfil = Environment.getExternalStorageDirectory() + File.separator + "DW_MEDIOS" + File.separator + "VERZITY" + File.separator + "PROFILE" + File.separator;

    public static class DownloadImage extends AsyncTask<Void, Void, Void> {
        private String urlDownload;
        private String nombreArchivo = "Perfil.jpg";
        private boolean success = false;
        private boolean Existe = false;
        private downloadImageInterface mDownloadImage;

        public DownloadImage(String urlDownload, downloadImageInterface mDownloadImage, String nombreArchivo) {
            this.urlDownload = urlDownload;
            this.mDownloadImage = mDownloadImage;
            this.nombreArchivo = nombreArchivo;
        }

        public interface downloadImageInterface {
            void Onsucces(String patch);
        }

        public DownloadImage(String urlDownload, String nombreArchivo) {
            this.urlDownload = urlDownload;
            this.nombreArchivo = nombreArchivo;
        }

        public DownloadImage(String urlDownload) {
            this.urlDownload = urlDownload;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (!isNullOrEmpty(urlDownload) && !isNullOrEmpty(nombreArchivo)) {
                    File foto = new File(Folder_rutaPerfil + "/" + nombreArchivo);
                    if (!foto.exists()) {
                        //eliminar
                        String url = urlDownload;
                        if (url != null) {

                            URL u = new URL(url);
                            InputStream is = u.openStream();

                            DataInputStream dis = new DataInputStream(is);

                            byte[] buffer = new byte[1024];
                            int length;
                            File folder = new File(Folder_rutaPerfil);

                            boolean succ = true;
                            if (!folder.exists()) {
                                succ = folder.mkdirs();
                            } else {
                                File[] files = folder.listFiles();
                                for (File mFile : files) {
                                    mFile.delete();
                                }
                            }

                            if (succ) {
                                FileOutputStream fos = new FileOutputStream(new File((Folder_rutaPerfil + "/" + nombreArchivo)));
                                while ((length = dis.read(buffer)) > 0) {
                                    fos.write(buffer, 0, length);
                                }
                                success = true;
                            } else {

                            }
                        }

                    } else
                        Existe = true;
                } else {
                    success = false;
                }
            } catch (MalformedURLException mue) {
                Log.e("SYNC getUpdate", "malformed url error", mue);
            } catch (IOException ioe) {
                Log.e("SYNC getUpdate", "io error", ioe);
            } catch (SecurityException se) {
                Log.e("SYNC getUpdate", "security error", se);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (success) {
                if (mDownloadImage != null)
                    mDownloadImage.Onsucces((Folder_rutaPerfil + "/" + nombreArchivo));
                else
                    mDownloadImage.Onsucces(null);
            } else {
                if (Existe) {
                    mDownloadImage.Onsucces((Folder_rutaPerfil + "/" + nombreArchivo));
                } else {
                    mDownloadImage.Onsucces(null);
                }
            }
        }
    }

    public static class searchList extends AsyncTask<Void, Void, Void> {
        private resultSearch mResultSearch;
        private handleSearch mHandleSearch;
        private List<?> mObjects;
        private String criterio;
        List<Object> resultado = new ArrayList<>();

        public void setmHandleSearch(handleSearch mHandleSearch) {
            this.mHandleSearch = mHandleSearch;
        }

        public void setmResultSearch(resultSearch mResultSearch) {
            this.mResultSearch = mResultSearch;
        }

        public void setdata(List<?> mObjects, String criterio) {
            this.mObjects = mObjects;
            this.criterio = criterio;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < mObjects.size(); i++) {
                if (mHandleSearch != null) {
                    if (mHandleSearch.hadle(mObjects.get(i), criterio)) {
                        resultado.add(mObjects.get(i));
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mResultSearch != null) mResultSearch.resultSearch(resultado);
        }
    }

    public interface resultSearch {
        void resultSearch(List<?> mObjects);
    }

    public interface handleSearch {
        boolean hadle(Object object, String criterio);
    }

    public static String ParseString(String criterio) {
        return
                Normalizer
                        .normalize(criterio, Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "");
    }

    public static void changeColorToolbar(ActionBar mToolbar, int color, Activity mActivity) {
        try {
            int colorResult = ContextCompat.getColor(getApplicationContext(), color);
            mToolbar.setBackgroundDrawable(new ColorDrawable(colorResult));
            mToolbar.setBackgroundDrawable(new ColorDrawable(mActivity.getResources().getColor(color)));
        } catch (Exception ex) {
            ex.toString();
        }
    }

    public static void changeColorStatusBarOnli(Activity mActivity, int color) {
        try {
            Window window = mActivity.getWindow();


            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(ContextCompat.getColor(mActivity, color));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity, int recurso) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(recurso);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }


}

