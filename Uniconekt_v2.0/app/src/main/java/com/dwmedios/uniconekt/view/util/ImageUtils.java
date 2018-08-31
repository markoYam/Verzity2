package com.dwmedios.uniconekt.view.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.model.Configuraciones;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Luis Cabanas on 19/03/2018.
 */

public class ImageUtils {

    public static String getUrlImage(String url, Context mContext) {
        AllController mAllController = new AllController(mContext);
        Configuraciones mConfiguraciones = mAllController.getConfiguraciones();
        String urlImage;
        if (mConfiguraciones != null) {
            Log.e("multimedia bd", "true");
            urlImage = mConfiguraciones.multimedia + url.replace("~", "");
        } else {
            Log.e("multimedia bd", "false");
            urlImage = ClientService.URL_MULTIMEDIA + url.replace("~", "");
        }
        return urlImage;
    }

    public static DisplayImageOptions OptionsImageLoaderLight = new DisplayImageOptions.Builder()
            .cacheInMemory(true).showImageOnFail(R.drawable.ic_image_broken)
            .showImageOnFail(R.drawable.defaultt)
            .showImageOnLoading(R.drawable.ic_loading).cacheOnDisc(true)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    public static DisplayImageOptions OptionsImageLoaderItems = new DisplayImageOptions.Builder()
            .cacheInMemory(true).showImageOnFail(R.drawable.defaultt)
            .showImageOnFail(R.drawable.defaultt)
            .showImageOnLoading(R.drawable.ic_loading).cacheOnDisc(true)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    public static DisplayImageOptions OptionsImageLoaderDark = new DisplayImageOptions.Builder()
            .cacheInMemory(true).showImageOnFail(R.drawable.ic_image_broken_dark)
            .showImageOnFail(R.drawable.defaultt)
            .showImageOnLoading(R.drawable.ic_loading_dark).cacheOnDisc(true)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    public static DisplayImageOptions OptionsImageLoaderRounded = new DisplayImageOptions.Builder()
            .displayer(new RoundedBitmapDisplayer(20))
            .imageScaleType(ImageScaleType.EXACTLY)
            .cacheInMemory(true).showImageOnFail(R.drawable.ic_image_broken_dark)
            .showImageOnFail(R.drawable.defaultt)
            .showImageOnLoading(R.drawable.ic_loading_dark).cacheOnDisc(true)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    public static DisplayImageOptions OptionsImageLoaderUser = new DisplayImageOptions.Builder()
            .displayer(new RoundedBitmapDisplayer(20))
            .imageScaleType(ImageScaleType.EXACTLY)
            .cacheInMemory(true).showImageOnFail(R.drawable.profile)
            .showImageOnFail(R.drawable.profile)
            .showImageOnLoading(R.drawable.profile).cacheOnDisc(true)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    public static Bitmap getBitmapFromUrl(String url, Context context) {
        Bitmap x = null;

        try {
            if (url != null && !url.contentEquals("")) {
                HttpURLConnection connection = null;
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestProperty("User-agent", "Mozilla/4.0");
                connection.connect();
                InputStream input = null;
                input = connection.getInputStream();
                x = BitmapFactory.decodeStream(input);
            } else {
                x = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_file_image_dark);
            }
        } catch (Exception e) {
            x = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_image_broken_dark);
            e.printStackTrace();
        }

        //return Bitmap.createScaledBitmap(x, 800, 800, false);
        return x;
    }
}
