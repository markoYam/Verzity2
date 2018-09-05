package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.PaqueteAsesor;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaqueteAsesorResponse extends BaseResponse {
    @SerializedName("Data")
    public List<PaqueteAsesor> mPaqueteAsesors;
}
