package com.dwmedios.uniconekt.data.service.firebase;

import com.dwmedios.uniconekt.data.service.firebase.Notifications.NotificationsUtils;
import com.dwmedios.uniconekt.model.Notificaciones;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by mYam on 18/04/2018.
 */

public class FirebaseMessaging extends FirebaseMessagingService {
    NotificationsUtils mNotificationsUtils;
    public static int count = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //  Log.e(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            // Log.e(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //  scheduleJob();
            } else {
                // Handle message within 10 seconds
                // handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        handlerNotifications(remoteMessage.getData().get("msg"));

    }

    private void handlerNotifications(String data) {
        Gson mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

        // mNotificationsUtils = new NotificationsUtils(getBaseContext());
        // String json =getApplication().getExtras().get("msg").toString();
        Notificaciones mNotificaciones = mGson.fromJson(data, Notificaciones.class);
        if (mNotificaciones != null) {
            //  Notification.Builder newMessage = mNotificationsUtils.createNotification(mNotificaciones.asunto, mNotificaciones.mensaje, true, mNotificaciones);
            //  mNotificationsUtils.getManager().notify(count++, newMessage.build());
            //  showNotification(getApplicationContext(),mNotificaciones);

        }
    }

}


