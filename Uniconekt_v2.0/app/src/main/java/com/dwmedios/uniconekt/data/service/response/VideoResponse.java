package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Videos;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse extends BaseResponse {
    @SerializedName("Data")
    public List<Videos> mvideosList;
}
