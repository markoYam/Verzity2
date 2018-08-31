package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.PostuladosGeneral;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostuladosGeneralesResponse extends BaseResponse {
    @SerializedName("Data")
    public List<PostuladosGeneral> mPostuladosGenerals;
}
