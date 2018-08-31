package com.dwmedios.uniconekt.data.service.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Luis Cabanas on 19/03/2018.
 */
public class BaseResponse {
public static final String DATA="Data";
    public BaseResponse(int status, String mensaje) {
        this.status = status;
        this.mensaje = mensaje;
    }

    public BaseResponse() {
    }

    @SerializedName("Estatus")
    public int status;

    @SerializedName("Mensaje")
    public String mensaje;

}
