package com.dwmedios.uniconekt.view.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.ClientService;
import com.dwmedios.uniconekt.model.Configuraciones;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.ByteArrayOutputStream;
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

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static void setTintView(Context mContext, View mView, int color, int drawable) {
        Drawable drawable2 = mContext.getResources().getDrawable(drawable);
        drawable2 = DrawableCompat.wrap(drawable2);
        DrawableCompat.setTint(drawable2, mContext.getResources().getColor(color));
        DrawableCompat.setTintMode(drawable2, PorterDuff.Mode.SRC_IN);
        if (mView instanceof EditText) {
            EditText mImageView2 = (EditText) mView;
            mImageView2.setCompoundDrawablesWithIntrinsicBounds(drawable2, null, null, null);
        }
        if (mView instanceof TextInputEditText) {
            TextInputEditText mTextInputEditText = (TextInputEditText) mView;
            mTextInputEditText.setCompoundDrawablesWithIntrinsicBounds(drawable2, null, null, null);
        }
    }

    public static String getUrlFacebook(String key) {
        return "https://graph.facebook.com/" + key + "/picture?type=large";
    }

    public String getRealPathFromUri(Uri contentUri,Context mContext) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
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
