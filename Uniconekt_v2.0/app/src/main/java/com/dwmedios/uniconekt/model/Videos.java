package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Videos implements Parcelable{
    public static final String ID = "idVideo";
    public static final String RUTA = "desRutaVideo";
    public static final String URL = "urlVideo";
    public static final String NOMBRE = "nbVideo";
    public static final String DESCRIPCION = "desVideo";

    @SerializedName(ID)
    public int id;
    @SerializedName(RUTA)
    public String ruta;
    @SerializedName(URL)
    public String url;
    @SerializedName(NOMBRE)
    public String nombre;
    @SerializedName(DESCRIPCION)
    public String descripcion;
public boolean isPlaying=false;


    protected Videos(Parcel in) {
        id = in.readInt();
        ruta = in.readString();
        url = in.readString();
        nombre = in.readString();
        descripcion = in.readString();
        isPlaying = in.readByte() != 0;
    }

    public static final Creator<Videos> CREATOR = new Creator<Videos>() {
        @Override
        public Videos createFromParcel(Parcel in) {
            return new Videos(in);
        }

        @Override
        public Videos[] newArray(int size) {
            return new Videos[size];
        }
    };

    public Videos() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(ruta);
        parcel.writeString(url);
        parcel.writeString(nombre);
        parcel.writeString(descripcion);
        parcel.writeByte((byte) (isPlaying ? 1 : 0));
    }
}
