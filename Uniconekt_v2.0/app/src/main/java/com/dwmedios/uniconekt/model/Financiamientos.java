package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Financiamientos implements Parcelable {
    public static final String ID = "idFinanciamiento";
    public static final String ID_UNIVERSIDAD = "idUniversidad";
    public static final String NOMBRE = "nbFinanciamiento";
    public static final String DESCRIPCION = "desFinancimiento";
    public static final String SITIO = "urlSitio";
    public static final String ARCHIVO = "pathArchivo";
    public static final String IMAGEN = "pathImagen";
    public static final String UNIVERSIDAD = "Universidades";
    public static final String FECHA = "feRegistro";

    @SerializedName(ID)
    public int id;
    @SerializedName(ID_UNIVERSIDAD)
    public int id_universidad;
    @SerializedName(NOMBRE)
    public String nombre;
    @SerializedName(SITIO)
    public String sitio;
    @SerializedName(ARCHIVO)
    public String archivo;
    @SerializedName(IMAGEN)
    public String imagen;
    @SerializedName(DESCRIPCION)
    public String descripcion;
    @SerializedName(FECHA)
    public Date fecha;
    @SerializedName(UNIVERSIDAD)
    public Universidad universidad;


    protected Financiamientos(Parcel in) {
        id = in.readInt();
        id_universidad = in.readInt();
        nombre = in.readString();
        sitio = in.readString();
        archivo = in.readString();
        imagen = in.readString();
        descripcion = in.readString();
        universidad = in.readParcelable(Universidad.class.getClassLoader());
    }

    public Financiamientos() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(id_universidad);
        dest.writeString(nombre);
        dest.writeString(sitio);
        dest.writeString(archivo);
        dest.writeString(imagen);
        dest.writeString(descripcion);
        dest.writeParcelable(universidad, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Financiamientos> CREATOR = new Creator<Financiamientos>() {
        @Override
        public Financiamientos createFromParcel(Parcel in) {
            return new Financiamientos(in);
        }

        @Override
        public Financiamientos[] newArray(int size) {
            return new Financiamientos[size];
        }
    };
}
