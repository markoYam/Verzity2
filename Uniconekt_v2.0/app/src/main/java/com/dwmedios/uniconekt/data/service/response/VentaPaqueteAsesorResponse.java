package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.VentaPaqueteAsesor;
import com.google.gson.annotations.SerializedName;

public class VentaPaqueteAsesorResponse extends BaseResponse {
    @SerializedName("Data")
    public VentaPaqueteAsesor mVentaPaqueteAsesor;
}
