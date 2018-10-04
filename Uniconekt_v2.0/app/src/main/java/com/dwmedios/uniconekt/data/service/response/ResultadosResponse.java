package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.cuestionarios.Resultados;
import com.google.gson.annotations.SerializedName;

public class ResultadosResponse extends BaseResponse {
    @SerializedName("Data")
    public Resultados mResultados;
}
