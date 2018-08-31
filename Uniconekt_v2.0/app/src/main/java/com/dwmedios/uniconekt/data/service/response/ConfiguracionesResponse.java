package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Configuraciones;
import com.google.gson.annotations.SerializedName;

public class ConfiguracionesResponse extends BaseResponse {
    @SerializedName(DATA)
    public Configuraciones mConfiguraciones;
}
