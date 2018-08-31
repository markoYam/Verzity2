package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Paquetes;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaquetesResponse extends BaseResponse {
    @SerializedName("Data")
    public List<Paquetes> mPaquetesList;
}
