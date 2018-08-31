package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Paises;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaisesResponse extends BaseResponse {
    @SerializedName("Data")
    public List<Paises> mPaisesList;
}
