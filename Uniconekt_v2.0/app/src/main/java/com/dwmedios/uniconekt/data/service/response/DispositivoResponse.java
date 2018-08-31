package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Dispositivo;
import com.google.gson.annotations.SerializedName;

public class DispositivoResponse extends BaseResponse{

    @SerializedName("Data")
    public Dispositivo mDispositivo;
}
