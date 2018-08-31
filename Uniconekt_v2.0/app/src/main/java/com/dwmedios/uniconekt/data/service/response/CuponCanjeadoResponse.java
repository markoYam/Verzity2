package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.CuponesCanjeados;
import com.google.gson.annotations.SerializedName;

public class CuponCanjeadoResponse  extends BaseResponse {
    @SerializedName(DATA)
    public CuponesCanjeados mCuponesCanjeados;

}
