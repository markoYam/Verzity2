package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.PostuladosUniversidades;
import com.google.gson.annotations.SerializedName;

public class PostuladosUniversidadesResponse extends BaseResponse {
    @SerializedName("Data")
    public PostuladosUniversidades mPostuladosUniversidades;
}
