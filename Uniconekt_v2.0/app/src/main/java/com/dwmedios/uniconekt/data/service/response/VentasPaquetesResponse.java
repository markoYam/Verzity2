package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.VentasPaquetes;
import com.google.gson.annotations.SerializedName;

public class VentasPaquetesResponse extends BaseResponse {
    @SerializedName("Data")
    public VentasPaquetes mVentasPaquetes;
}
