package com.dwmedios.uniconekt;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.dwmedios.uniconekt.Utils_app_Running.BaseApp;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


/**
 * Created by Luis Cabanas on 17/05/2017.
 */

public class UniconektApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(5)
                .build();
        ImageLoader.getInstance().init(configuration);
        BaseApp mBaseApp= new BaseApp();
        //FacebookSdk.sdkInitialize(getApplicationContext());
       // AppEventsLogger.activateApp(this);
    }

    private Activity mCurrentActivity = null;

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

}
