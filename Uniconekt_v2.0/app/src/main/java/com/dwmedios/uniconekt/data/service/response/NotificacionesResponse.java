package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Notificaciones;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificacionesResponse extends BaseResponse {
    @SerializedName(DATA)
    public List<Notificaciones> mNotificacionesList;
}
