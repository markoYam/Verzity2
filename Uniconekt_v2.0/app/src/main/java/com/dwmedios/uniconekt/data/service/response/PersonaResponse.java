package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Persona;
import com.google.gson.annotations.SerializedName;

public class PersonaResponse extends BaseResponse {
    @SerializedName(DATA)
    public Persona mPersona;
}
