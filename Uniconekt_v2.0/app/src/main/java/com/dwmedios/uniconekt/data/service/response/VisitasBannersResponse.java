package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.VisitasBanners;
import com.google.gson.annotations.SerializedName;

public class VisitasBannersResponse extends BaseResponse{
    @SerializedName("Data")
    public VisitasBanners mVisitasBanners;
}
