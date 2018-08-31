package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.PostuladosBecas;
import com.google.gson.annotations.SerializedName;

public class PostularseBecaResponse extends BaseResponse {
    @SerializedName("Data")
    public PostuladosBecas mPostuladosBecas;
}
