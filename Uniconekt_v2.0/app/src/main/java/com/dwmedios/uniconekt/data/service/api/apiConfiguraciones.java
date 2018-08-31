package com.dwmedios.uniconekt.data.service.api;


import com.dwmedios.uniconekt.data.service.response.ConfiguracionesResponse;
import com.dwmedios.uniconekt.data.service.response.YoutubeResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by marko on 05/12/2017.
 */

public interface apiConfiguraciones {
    String BASE_URL_API="";

   // @GET("UrlAppMGS.json")
  //  Call<Configuraciones> getConfiguraciones();
    //
     @GET("oembed")
     Call<YoutubeResponse> getConfiguraciones(@Query("url") String url);

    @GET("UrlAppVerzity2.json")
    Call<ConfiguracionesResponse> GetConfiguracionesGenerales();
}
