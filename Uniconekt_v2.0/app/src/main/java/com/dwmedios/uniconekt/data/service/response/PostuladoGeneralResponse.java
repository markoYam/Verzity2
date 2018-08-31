package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.PostuladosGeneral;
import com.google.gson.annotations.SerializedName;

public class PostuladoGeneralResponse extends BaseResponse{
    @SerializedName(DATA)
    public PostuladosGeneral mPostuladosGeneral;
}
