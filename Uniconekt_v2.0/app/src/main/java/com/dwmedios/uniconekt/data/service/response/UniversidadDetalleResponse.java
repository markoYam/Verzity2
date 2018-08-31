package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Universidad;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UniversidadDetalleResponse extends BaseResponse {

    @SerializedName("Data")
    public Universidad mUniversidad;


}
