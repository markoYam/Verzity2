package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Persona;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AsesoresResponse extends BaseResponse {
    @SerializedName("Data")
    public List<Persona> mPersonaList;
}
