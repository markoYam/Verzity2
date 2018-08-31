package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.SearchUniversidades;
import com.google.gson.annotations.SerializedName;

public class SearchUniversidadesResponse extends BaseResponse {

    @SerializedName("Data")
    public SearchUniversidades mSearchUniversidades;
}
