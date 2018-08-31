package com.dwmedios.uniconekt.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Luis Cabanas on 19/03/2018.
 */

public class TipoCategoria {
    public static final String ID = "idTipoCategoria";
    public static final String NOMBRE = "nbTipoCategoria";
    public static final String PATH_IMAGEN = "pathImagen";

    @SerializedName(ID)
    public int id;
    @SerializedName(NOMBRE)
    public String nombre;
    @SerializedName(PATH_IMAGEN)
    public String pathImagen;
}
