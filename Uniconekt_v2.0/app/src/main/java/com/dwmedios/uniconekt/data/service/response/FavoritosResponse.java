package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Favoritos;
import com.google.gson.annotations.SerializedName;

public class FavoritosResponse extends BaseResponse {
    @SerializedName("Data")
    public Favoritos mFavoritos;
}
