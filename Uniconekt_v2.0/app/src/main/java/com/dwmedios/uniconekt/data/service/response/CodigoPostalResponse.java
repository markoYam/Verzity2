package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.CodigoPostal;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CodigoPostalResponse extends BaseResponse {
    @SerializedName("Data")
    public List<CodigoPostal> mCodigoPostalList;
}
