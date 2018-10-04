package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.cuestionarios.EvaluacionesPersona;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EvaluacionesResponse extends BaseResponse{
    @SerializedName("Data")
    public List<EvaluacionesPersona> mEvaluacionesPersonas;
}
