package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.PostuladosFinanciamientos;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostuladoFinanciamientoResponse extends BaseResponse {
    @SerializedName("Data")
    public PostuladosFinanciamientos mPostuladosFinanciamientos;
}
