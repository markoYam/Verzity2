package com.dwmedios.uniconekt.data.service.firebase.Notifications;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Notificaciones;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;

import static com.dwmedios.uniconekt.data.service.firebase.Notifications.FirebaseMesassingReciver.configure2;

public class NotHanActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_han);

        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Notificaciones mNotificaciones = getIntent().getExtras().getParcelable("msg1");
            if (mNotificaciones != null) {
                configure2(mNotificaciones, NotHanActivity.this, true, NotHanActivity.this);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 5000);
                //  Thread.sleep(5000);

            } else finish();
        } catch (Exception ex) {
            ex.printStackTrace();
            onResume();
            //finish();
        }
    }

    public void close(View v) {
        finish();
    }
}
