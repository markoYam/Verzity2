package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Banners;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannersResponse extends BaseResponse {
    @SerializedName("Data")
    public List<Banners> mBannersList;
}
