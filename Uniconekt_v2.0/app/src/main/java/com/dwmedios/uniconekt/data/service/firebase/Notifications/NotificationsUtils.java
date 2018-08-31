package com.dwmedios.uniconekt.data.service.firebase.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Notificaciones;
import com.dwmedios.uniconekt.view.activity.Universidad.DetalleNotificacionActivity;

public class NotificationsUtils extends ContextWrapper {
    private NotificationManager manager;
    private static final String CHANEL_ID_HIGH = "1";
    private static final String TYPE_CHANEL_ID_HIGH = "HIGH";

    private static final String CHANEL_ID_LOW = "2";
    private static final String TYPE_CHANEL_ID_LOW = "lOW";

    private static final String GROUP_NAME = "NAME_GROUP";
    private static final int GROUP_ID = 8523;

    public static final String KEY_NOTIFICATIOS = "msg";

    public NotificationsUtils(Context base) {
        super(base);
        CrearCanales();
       // createGroup("Mensajes", R.drawable.ic_action_ic_school);
    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public void CrearCanales() {
        if (Build.VERSION.SDK_INT >= 26) {
            //canal alto
            NotificationChannel high_channel = new NotificationChannel(CHANEL_ID_HIGH, TYPE_CHANEL_ID_HIGH, NotificationManager.IMPORTANCE_HIGH);
            NotificationChannel low_Chanel = new NotificationChannel(CHANEL_ID_HIGH, TYPE_CHANEL_ID_LOW, NotificationManager.IMPORTANCE_LOW);

            //encender el led de algunos dispositivos para indicar una nueva notificacion.
            high_channel.enableLights(true);
            //al seleccionar el icono de la aplicacion ahi mismo se podra mostrar las notificaciones.....
            high_channel.setShowBadge(true);
            //habilitar la vibracion del dispositivo...
            high_channel.enableVibration(true);
            //patron de vibracion de las notificaciones
            high_channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 300, 400});
            //ver notificaciones con la pantalla bloqueada
            high_channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            //sonido por defecto
            Uri song = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            high_channel.setSound(song, null);
            //creacion de los canales
            getManager().createNotificationChannel(high_channel);
            getManager().createNotificationChannel(low_Chanel);
        }
    }


    public Notification.Builder createNotification(String titulo, String mensaje, boolean isImportant, Notificaciones mNotificaciones) {
        Log.e("id not",mNotificaciones.id+"");
        if (Build.VERSION.SDK_INT >= 26) {

            return (isImportant) ? this.createNotificationChannel(titulo, mensaje, CHANEL_ID_HIGH, mNotificaciones) :
                    this.createNotificationChannel(titulo, mensaje, CHANEL_ID_LOW, mNotificaciones);

        }
        return this.createNotificationOutChannel(titulo, mensaje, mNotificaciones);

    }

    private Notification.Builder createNotificationChannel(String titulo, String mensaje, String channel, Notificaciones mNotificaciones) {
        if (Build.VERSION.SDK_INT >= 26) {
            return new Notification.Builder(this, channel)
                    .setContentTitle(titulo)
                    .setContentIntent(createPendingIntent(mNotificaciones))
                    .setColor(getColor(R.color.colorPrimaryDark))
                    .setContentText(mensaje)
                 //   .setGroup(GROUP_NAME)
                    .setSmallIcon(R.drawable.ic_action_ic_school)
                    .setAutoCancel(true);
        }
        return null;
    }

    private PendingIntent createPendingIntent(Notificaciones mNotificaciones) {
        Intent mIntent = new Intent(this, DetalleNotificacionActivity.class);
        mIntent.putExtra(KEY_NOTIFICATIOS, mNotificaciones);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);

    }

    private Notification.Builder createNotificationOutChannel(String titulo, String mensaje, Notificaciones mNotificaciones) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return new Notification.Builder(this)
                    .setContentTitle(titulo)
                    .setContentText(mensaje)
                    .setContentIntent(createPendingIntent(mNotificaciones))
                    .setVibrate(new long[]{100})
                    .setSmallIcon(R.drawable.ic_action_ic_school)
                    .setPriority(Notification.PRIORITY_HIGH)
                   // .setGroup(GROUP_NAME)
                    .setAutoCancel(true);
        }
        return null;
    }

    private void createGroup(String titulo, int Icono) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification mNotification = new Notification.Builder(getApplicationContext(), "1234")
                    .setSmallIcon(Icono)
                    .setGroup(GROUP_NAME)
                    .setGroupSummary(true)
                    .build();
            getManager().notify(GROUP_ID, mNotification);
        } else {
            Notification newMessageNotification = new NotificationCompat.Builder(this, CHANEL_ID_HIGH)
                    .setSmallIcon(Icono)
                    .setContentTitle(titulo)
                    .setGroup(GROUP_NAME)
                    .setGroupSummary(true)
                    .build();
            getManager().notify(GROUP_ID, newMessageNotification);
        }
    }
}
