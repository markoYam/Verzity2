package com.dwmedios.uniconekt.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Luis Cabanas on 19/03/2018.
 */

@DatabaseTable
public class Item {
    public static final String ID = "id";
    public static final String NOMBRE = "nombre";

    @DatabaseField(columnName = ID, generatedId = true, index = true)
    public int id;

    @DatabaseField(columnName = NOMBRE)
    public String nombre;
}
