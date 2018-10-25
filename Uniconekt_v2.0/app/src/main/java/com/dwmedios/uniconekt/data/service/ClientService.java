package com.dwmedios.uniconekt.data.service;


import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.api.ClientAPI;
import com.dwmedios.uniconekt.model.Configuraciones;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Luis Cabanas on 19/03/2018.
 */

public class ClientService {
    private Retrofit mRetrofit = null;
    private AllController mAllController;
    private Context mContext;
    private String url = new String();
    public static final String URL_API = "http://192.168.100.38/Uniconekt/service/UNICONEKT.asmx/";
    public static final String URL_MULTIMEDIA = "http://192.168.100.38/Uniconekt";

    public static final String URL_BASE_TEMP = "http://www.dwmedios.com/Apps/";
    //public static final String URL_BASE_TEMP = "http://www.dwmedios.com/";

    public ClientService(Context mContext) {
        this.mContext = mContext;
        this.mAllController = new AllController(mContext);
        getDta();
    }
    public ClientAPI getAPI() {

        if (mRetrofit == null) {

            OkHttpClient client = new OkHttpClient
                    .Builder().connectTimeout(10, TimeUnit.HOURS)
                    .writeTimeout(10, TimeUnit.HOURS)
                    .readTimeout(10, TimeUnit.HOURS)
                    .build();


            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .serializeNulls()
                    .create();


            mRetrofit = new Retrofit
                    .Builder()
                    .baseUrl((url != null && !url.isEmpty() ? url : URL_API))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }

        return mRetrofit.create(ClientAPI.class);
    }

    private void getDta() {
        Configuraciones mConfiguraciones = mAllController.getConfiguraciones();
        if (mConfiguraciones != null)
            if (mConfiguraciones.webService != null && !mConfiguraciones.webService.isEmpty()) {
                url = mConfiguraciones.webService;
            } else {
                url = URL_API;
            }
    }
}
