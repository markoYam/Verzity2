package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Estados;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EstadosResponse extends BaseResponse {
    @SerializedName("Data")
    public List<Estados> mEstadosList;
}
