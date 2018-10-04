package com.dwmedios.uniconekt.data.service.firebase.Notifications;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Gravity;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.UniconektApplication;
import com.dwmedios.uniconekt.model.Notificaciones;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.presenter.NotificacionesHandlerPresenter;
import com.dwmedios.uniconekt.view.activity.Universidad.DetalleNotificacionActivity;
import com.dwmedios.uniconekt.view.activity.Universidad_v2.NotificacionesUniversidadActivity;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.EvaluacionesActivity;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.NotificacionUniversitarioActivity;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.NotificacionesUniversitarioActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aviran.cookiebar2.CookieBar;
import org.aviran.cookiebar2.OnActionClickListener;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class FirebaseMesassingReciver extends BroadcastReceiver {
    NotificationsUtils mNotificationsUtils;
    private Context mContext;
    private Intent mIntent;
    private NotificacionesHandlerPresenter mNotificacionesHandlerPresenter;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        this.mIntent = intent;
        mNotificacionesHandlerPresenter = new NotificacionesHandlerPresenter(mContext);
        handlerNotifications();


    }

    private void handlerNotifications() {
        Gson mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        mNotificationsUtils = new NotificationsUtils(mContext);
        String json = mIntent.getExtras().get("msg").toString();
        //Log.e("Data not", json);
        Notificaciones mNotificaciones = mGson.fromJson(json, Notificaciones.class);
        NotificationManager nMgr = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

     /*  try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        if (mNotificaciones != null) {
            Persona temp = mNotificacionesHandlerPresenter.getPersona();

            if (temp != null) {
                if (temp.id == mNotificaciones.id_persona_recibe) {
                    configure(mNotificaciones);

                } else {
                    nMgr.cancelAll();
                    // mContext.startActivity(new Intent(mContext, NotHanActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK));
                    Log.e("Clear not", "Ok");
                }
            } else {
                nMgr.cancelAll();
                // mContext.startActivity(new Intent(mContext, NotHanActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK));
                Log.e("Clear not", "Ok");
            }

        } else {
            nMgr.cancelAll();
            //mContext.startActivity(new Intent(mContext, NotHanActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK));

            Log.e("Clear not", "Ok");
        }
    }

    public static void configure2(final Notificaciones mNotificaciones, final Context mContext, final boolean notOpen, Activity activity) {
       /* PowerManager manager = (PowerManager) mContext.getSystemService(mContext.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = manager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "DEMO");
        mWakeLock.acquire();*/
        final Activity finalCurrentActivity;
        if (activity == null)
            finalCurrentActivity = ((UniconektApplication) mContext.getApplicationContext()).getCurrentActivity();
        else
            finalCurrentActivity = activity;

        if (finalCurrentActivity != null) {

            //  final Activity finalCurrentActivity = currentActivity;
            CookieBar.build(finalCurrentActivity)
                    //.setCustomView(customView)
                    .setLayoutGravity(Gravity.TOP)
                    .setTitle(mNotificaciones.asunto)
                    .setMessage(mNotificaciones.mensaje)
                    .setDuration(5000)

                    // .setTitleColor(R.color.uvv_black)
                    // .setMessageColor(R.color.colorGris)
                    //.setActionColor(R.color.colorGris
                    // )
                    .setIcon(R.drawable.ic_action_ic_school)
                    .setBackgroundColor(R.color.colorPrimaryDark)
                    .setAction("Ver", new OnActionClickListener() {
                        @Override
                        public void onClick() {
                            mContext.startActivity(new Intent(mContext, DetalleNotificacionActivity.class).putExtra("msg1", mNotificaciones)
                                    .addFlags(FLAG_ACTIVITY_NEW_TASK));
                            CookieBar.dismiss(finalCurrentActivity);
                            if (notOpen)
                                finalCurrentActivity.finish();
                        }
                    })
                    .show();

        } else {
            if (!notOpen) {
                mContext.startActivity(new Intent(mContext, NotHanActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK).putExtra("msg1", mNotificaciones));
            }
        }
    }


    public void configure(final Notificaciones mNotificaciones) {


       /* PowerManager manager = (PowerManager) mContext.getSystemService(mContext.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = manager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "DEMO");
        mWakeLock.acquire();*/

        if (mNotificaciones.discriminador.equals("Cuestionarios")) {
            final Activity finalCurrentActivity = ((UniconektApplication) mContext.getApplicationContext()).getCurrentActivity();
            if (finalCurrentActivity != null) {
                // Log.e("Clase actual", finalCurrentActivity.getClass().getSimpleName());
                try {
                    if (finalCurrentActivity instanceof EvaluacionesActivity) {
                        if (((EvaluacionesActivity) finalCurrentActivity).mEvaluacionesPresenter != null) {
                            ((EvaluacionesActivity) finalCurrentActivity).mEvaluacionesPresenter.getEvaluaciones();

                        }
                    }
                    if (finalCurrentActivity instanceof NotificacionesUniversitarioActivity) {

                        if (((NotificacionesUniversitarioActivity) finalCurrentActivity).mNotificacionUniversitarioPresenter != null) {
                            ((NotificacionesUniversitarioActivity) finalCurrentActivity).mNotificacionUniversitarioPresenter.getNotificaciones();

                        }

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                MediaPlayer mPlayer = MediaPlayer.create(mContext.getApplicationContext(), R.raw.notification);
                mPlayer.start();

                mContext.startActivity(new Intent(mContext, NotificacionUniversitarioActivity.class).putExtra("msg1", mNotificaciones).addFlags(FLAG_ACTIVITY_NEW_TASK));
            }

        } else {
            final Activity finalCurrentActivity = ((UniconektApplication) mContext.getApplicationContext()).getCurrentActivity();


            if (finalCurrentActivity != null) {
                MediaPlayer mPlayer = MediaPlayer.create(mContext.getApplicationContext(), R.raw.notification);
                mPlayer.start();

                try {
                    if (finalCurrentActivity instanceof NotificacionesUniversidadActivity) {
                        if (((NotificacionesUniversidadActivity) finalCurrentActivity).mNotificacionesUniPresenter != null) {
                            ((NotificacionesUniversidadActivity) finalCurrentActivity).mNotificacionesUniPresenter.mNotificacionesUniPresenter();

                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //  final Activity finalCurrentActivity = currentActivity;
                CookieBar.build(finalCurrentActivity)
                        //.setCustomView(customView)
                        .setLayoutGravity(Gravity.TOP)
                        .setTitle(mNotificaciones.asunto)
                        .setMessage(mNotificaciones.mensaje)
                        .setDuration(5000)

                        // .setTitleColor(R.color.uvv_black)
                        // .setMessageColor(R.color.colorGris)
                        //.setActionColor(R.color.colorGris
                        // )
                        .setIcon(R.drawable.ic_action_ic_school)
                        .setBackgroundColor(R.color.colorPrimaryDark)
                        .setAction("Ver", new OnActionClickListener() {
                            @Override
                            public void onClick() {
                                mContext.startActivity(new Intent(mContext, DetalleNotificacionActivity.class).putExtra("msg1", mNotificaciones)
                                        .addFlags(FLAG_ACTIVITY_NEW_TASK));
                                CookieBar.dismiss(finalCurrentActivity);

                            }
                        })
                        .show();

            }
        }
    }
}
