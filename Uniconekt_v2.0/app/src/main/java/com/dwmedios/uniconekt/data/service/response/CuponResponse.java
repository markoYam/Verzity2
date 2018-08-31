package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Cupones;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CuponResponse extends BaseResponse {
    @SerializedName("Data")
    public List<Cupones> mCuponesList;
}
