package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.cuestionarios.DetalleEvaluacionViewModel;
import com.google.gson.annotations.SerializedName;

public class DetalleEvaluacionResponse extends BaseResponse{
    @SerializedName("Data")
    public DetalleEvaluacionViewModel detalleEvaluacionViewModel;
}
