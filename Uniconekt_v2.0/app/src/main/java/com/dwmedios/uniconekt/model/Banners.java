package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Banners implements Parcelable{
    public static final String ID = "idBanner";
    public static final String NOMBRE = "nbBanner";
    public static final String SITIO = "desSitioWeb";
    public static final String FOTO = "desRutaFoto";
    public static final String INICIO = "feInicio";
    public static final String FIN = "feFin";
    public static final String TIEMPO = "dcTiempoPresentacion";
    public static final String FECHA = "feRegistro";

    @SerializedName(ID)
    public int id;
    @SerializedName(NOMBRE)
    public String nombre;
    @SerializedName(SITIO)
    public String sitio;
    @SerializedName(FOTO)
    public String foto;
    @SerializedName(TIEMPO)
    public int tiempoo;

    protected Banners(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        sitio = in.readString();
        foto = in.readString();
        tiempoo = in.readInt();
    }

    public Banners() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(sitio);
        dest.writeString(foto);
        dest.writeInt(tiempoo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Banners> CREATOR = new Creator<Banners>() {
        @Override
        public Banners createFromParcel(Parcel in) {
            return new Banners(in);
        }

        @Override
        public Banners[] newArray(int size) {
            return new Banners[size];
        }
    };
}
