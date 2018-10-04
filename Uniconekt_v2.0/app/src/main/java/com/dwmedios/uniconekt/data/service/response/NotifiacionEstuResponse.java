package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Notificaciones;
import com.google.gson.annotations.SerializedName;

public class NotifiacionEstuResponse extends BaseResponse{
    @SerializedName("Data")
    public Notificaciones mNotificaciones;
}
