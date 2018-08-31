package com.dwmedios.uniconekt.data.service.response;

import com.dwmedios.uniconekt.model.Usuario;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mYam on 16/04/2018.
 */

public class UsuarioResponse extends BaseResponse {

    @SerializedName("Data")
    public Usuario mUsuario;

}
