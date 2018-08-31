package com.dwmedios.uniconekt.data.service.response;

import com.google.gson.annotations.SerializedName;

public class YoutubeResponse {
    public static final String FRAME = "html";
    public static final String PREVIEW = "thumbnail_url";

    @SerializedName(FRAME)
    public String frame;
    @SerializedName(PREVIEW)
    public String preview;
}
