package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.TipoCategoria;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Luis Cabanas on 19/03/2018.
 */

public class TipoCategoriaResponse extends BaseResponse {

    @SerializedName("data")
    public List<TipoCategoria> data;
}
