package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Financiamientos;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FinanciamientoResponse extends BaseResponse {
    @SerializedName("Data")
    public List<Financiamientos> mFinanciamientosList;
}
