package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Universidad;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UniversidadResponse extends BaseResponse {

    @SerializedName("Data")
    public List<Universidad> mUniversidadList;


}
