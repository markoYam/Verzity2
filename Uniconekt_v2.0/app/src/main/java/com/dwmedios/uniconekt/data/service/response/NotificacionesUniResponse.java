package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.NotificacionUniversidad;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificacionesUniResponse extends BaseResponse {
    @SerializedName("Data")
    public List<NotificacionUniversidad> mNotificacionUniversidads;


}
