package com.dwmedios.uniconekt.view.util;

import android.content.Context;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.util.Log;

import com.dwmedios.uniconekt.model.Licenciaturas;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.dwmedios.uniconekt.view.util.ImageUtils.decodeBase64;
import static com.dwmedios.uniconekt.view.util.ImageUtils.encodeTobase64;

/**
 * Created by marko on 23/11/2017.
 */

public class SharePrefManager {
    private static final String NAMESHARE = "namesharepref";
    public static final String URL_APISERVER = "url_api";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_SPLASH = "splash";
    private static final String KEY_ACTIVITY = "activity";
    private static final String NOTIFICATIONOBJECT = "notificationObject";
    private static final String NOTIFICATIONID = "notificationId";
    private static final String INFOUSER = "infouser_mgs";
    private static final String TYPE_USER_LOGIN = "user_temp";
    private static final String IME_USER_SAVE = "USER_IME";
    private static final String KEY_GUARDAR_LICEN = "USER_IMSSSSSSSSSSSSSSSSSSSSSSSSSE";
    private static final String BUSCAR_EXTRANJERO = "BUSCAR EXTRANJERO-YTNGJGFJGRJGGFDSCFD524";
    private static final String IMAGE = "TEMPOSDSDSD";

    private static Context mCxt;
    private static SharePrefManager sharePrefManager;

    private SharePrefManager(Context mContext) {
        mCxt = mContext;
    }

    public static synchronized SharePrefManager getInstance(Context mContext) {
        if (sharePrefManager == null) {
            sharePrefManager = new SharePrefManager(mContext);
        }
        return sharePrefManager;
    }

    public boolean saveToken(String token) {
        SharedPreferences sharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putString(KEY_TOKEN, token);
        mEditor.apply();
        Log.e("Token saved", token);
        return true;
    }

    public boolean saveImageTemp(Bitmap image) {
        String imageSave = encodeTobase64(image);
        SharedPreferences sharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putString(IMAGE, imageSave);
        mEditor.apply();
        return true;
    }

    public Bitmap getImageTemp() {
        SharedPreferences mSharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        String image = mSharedPreferences.getString(IMAGE, null);
        if (image != null) {
            return decodeBase64(image);
        } else
            return null;
    }

    public String getToken() {
        SharedPreferences mSharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(KEY_TOKEN, null);
    }

    public boolean saveTypeUser(int user) {
        SharedPreferences sharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putInt(TYPE_USER_LOGIN, user);
        mEditor.apply();
        Log.e("Type user saved", user + "");
        return true;
    }

    public int getTypeUser() {
        SharedPreferences mSharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        Log.e("Type user saved", mSharedPreferences.getInt(TYPE_USER_LOGIN, 0) + "");
        return mSharedPreferences.getInt(TYPE_USER_LOGIN, 1);
    }

    public boolean SearchExtranjero(boolean isExtranjero) {
        SharedPreferences sharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putBoolean(BUSCAR_EXTRANJERO, isExtranjero);
        mEditor.apply();
        Log.e("buscar Extranjero", String.valueOf(isExtranjero));
        return true;
    }

    public boolean isSeachExtranjero() {
        SharedPreferences mSharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        Log.e("buscar Extranjero", String.valueOf(mSharedPreferences.getBoolean(BUSCAR_EXTRANJERO, false)));
        return mSharedPreferences.getBoolean(BUSCAR_EXTRANJERO, false);
    }

    public boolean saveImei(String imei) {
        SharedPreferences sharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putString(IME_USER_SAVE, imei);
        mEditor.apply();
        Log.e("IMEI user saved", imei + "");
        return true;
    }


    public String getImei() {
        SharedPreferences mSharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        Log.e("IMEI user saved", mSharedPreferences.getString(IME_USER_SAVE, null) + "");
        return mSharedPreferences.getString(IME_USER_SAVE, null);
    }

    public void configSplash(int status) {
        SharedPreferences sharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putInt(KEY_SPLASH, status);
        mEditor.apply();
        Log.e("status splash", status + "");
    }

    public int getConfigSplash() {

        SharedPreferences mSharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(KEY_SPLASH, 0);
    }

    public void statusActivity(int status) {
        SharedPreferences sharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putInt(KEY_ACTIVITY, status);
        mEditor.apply();
        Log.e("status activity", status + "");
    }

    public int statusActivity() {
        SharedPreferences mSharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        return mSharedPreferences.getInt(KEY_ACTIVITY, 0);
    }

    /*public void saveNotification(List<notificacion> mNotificacion) {
        Gson mGson = new Gson();
        String json = mGson.toJson(mNotificacion);
        SharedPreferences sharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putString(NOTIFICATIONOBJECT, json);
        mEditor.apply();
        Log.e("status save noti", "yes");
        Log.e("content not send", (json == null ? "null" : json));
    }*/
    public void saveLicenciaturas(List<Licenciaturas> mLicenciaturas) {
        Gson mGson = new Gson();
        String json = mGson.toJson(mLicenciaturas);
        SharedPreferences sharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putString(KEY_GUARDAR_LICEN, json);
        mEditor.apply();
        Log.e("SAVE LICEN", "yes");
        Log.e("content ", (json == null ? "null" : json));
    }

    public List<Licenciaturas> getLicenciaturas() {
        Gson mGson = new Gson();
        List<Licenciaturas> mLicenciaturas = new ArrayList<>();
        SharedPreferences mSharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        String json = mSharedPreferences.getString(KEY_GUARDAR_LICEN, null);
        Type mType = new TypeToken<List<Licenciaturas>>() {
        }.getType();
        Log.e("content not return", (json == null ? "null" : json));
        return (mGson.fromJson(json, mType));
    }

    /*
        public List<notificacion> getnotification() {
            Gson mGson = new Gson();
            List<notificacion> mNotificacions = new ArrayList<>();
            SharedPreferences mSharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
            String json = mSharedPreferences.getString(NOTIFICATIONOBJECT, null);
            Type mType = new TypeToken<List<notificacion>>() {
            }.getType();
            Log.e("content not return", (json == null ? "null" : json));
            return (mNotificacions = mGson.fromJson(json, mType));
        }
    */
    public void saveIdNotification(List<Integer> mIdNotificacion) {
        Gson mGson = new Gson();
        String json = mGson.toJson(mIdNotificacion);
        SharedPreferences sharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putString(NOTIFICATIONID, json);
        mEditor.apply();
        Log.e("content Idnot send", (json == null ? "null" : json));
    }
/*
    //todo- guardar informacion del usuario
    public void saveInfoUser(Dispositivo mDispositivo) {
        Gson mGson = new Gson();
        String json = mGson.toJson(mDispositivo);
        SharedPreferences sharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putString(INFOUSER, json);
        mEditor.apply();
        Log.e("infoUser", (json == null ? "null" : json));
        Log.e("infoUser", "save succelly");
    }
*//*
    public Dispositivo getInfoUser() {
        Dispositivo infoUser = new Dispositivo();
        Gson mGson = new Gson();
        SharedPreferences mSharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        String json = mSharedPreferences.getString(INFOUSER, null);

        infoUser = (json != null && json != "") ? mGson.fromJson(json, Dispositivo.class) : null;

        Log.e("content user return", (json == null ? "null" : json));
        return infoUser;
    }*/

    public void deleteInfoUser() {
        SharedPreferences sharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putString(INFOUSER, null);
        mEditor.apply();
        Log.e("infoUser", "Clear");
    }

    public List<Integer> getIdnotification() {
        Gson mGson = new Gson();
        List<Integer> mIdNotificacions = new ArrayList<>();
        SharedPreferences mSharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        String json = mSharedPreferences.getString(NOTIFICATIONID, null);
        Type mType = new TypeToken<List<Integer>>() {
        }.getType();
        Log.e("content Idnot return", (json == null ? "null" : json));
        return (mIdNotificacions = mGson.fromJson(json, mType));
    }

    // TODO: 12/12/2017 obtener las url de conexiones de la app
    public boolean saveUrlApi(String api) {
        SharedPreferences sharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putString(URL_APISERVER, api);
        mEditor.apply();
        Log.e("Token saved", api);
        return true;
    }

    public String getUrlApi() {
        SharedPreferences mSharedPreferences = mCxt.getSharedPreferences(NAMESHARE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(URL_APISERVER, null);
    }

}
