package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Becas;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class becasResponse extends BaseResponse {
    @SerializedName(DATA)
    public List<Becas> mBecasList;
}
