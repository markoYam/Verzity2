package com.dwmedios.uniconekt.Utils_app_Running;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dwmedios.uniconekt.UniconektApplication;
import com.dwmedios.uniconekt.model.Persona;


public class BaseApp extends AppCompatActivity {
    protected UniconektApplication mMyApp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyApp = (UniconektApplication) this.getApplicationContext();
        //para soportas animaciones.........


    }

    protected void onResume() {
        super.onResume();
        mMyApp.setCurrentActivity(this);
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
    }

    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences() {
        Activity currActivity = mMyApp.getCurrentActivity();
        if (this.equals(currActivity))
            mMyApp.setCurrentActivity(null);
    }
}
