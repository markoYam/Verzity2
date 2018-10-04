package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.cuestionarios.RespuestasPersona;
import com.google.gson.annotations.SerializedName;

public class RespuestaPersonaResponse extends BaseResponse {
    @SerializedName("Data")
    public RespuestasPersona mRespuestasPersona;
}
