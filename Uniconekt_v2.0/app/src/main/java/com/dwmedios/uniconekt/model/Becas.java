package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Becas implements Parcelable {
    public static final String ID = "idBeca";
    public static final String NOBRE = "nbBeca";
    public static final String DESCRIPCION = "desBeca";
    public static final String RUTA_ARCHIVO = "desRutaArchivo";
    public static final String I_UNIVERSIDAD = "idUniversidad";
    public static final String FECHA_INICIO = "feInicio";
    public static final String FECHA_FIN = "feFin";
    public static final String UNIVERSIDAD = "Universidades";
    public static final String IMAGEN = "pathImagen";

    @SerializedName(ID)
    public int id;
    @SerializedName(NOBRE)
    public String nombre;
    @SerializedName(DESCRIPCION)
    public String descripcion;
    @SerializedName(RUTA_ARCHIVO)
    public String rutaArchivo;
    @SerializedName(I_UNIVERSIDAD)
    public int idUniversidad;
    @SerializedName(FECHA_INICIO)
    public Date fecha_inicio;
    @SerializedName(FECHA_FIN)
    public Date fecha_fin;
    @SerializedName(IMAGEN)
    public String rutaImagen;
    @SerializedName(UNIVERSIDAD)
    public Universidad mUniversidad;


    public Becas(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
        rutaArchivo = in.readString();
        idUniversidad = in.readInt();
        rutaImagen = in.readString();
        mUniversidad = in.readParcelable(Universidad.class.getClassLoader());
    }

    public Becas() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeString(rutaArchivo);
        dest.writeInt(idUniversidad);
        dest.writeString(rutaImagen);
        dest.writeParcelable(mUniversidad, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Becas> CREATOR = new Creator<Becas>() {
        @Override
        public Becas createFromParcel(Parcel in) {
            return new Becas(in);
        }

        @Override
        public Becas[] newArray(int size) {
            return new Becas[size];
        }
    };
}
