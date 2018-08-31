package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FotosCupones implements Parcelable{
    public static final String ID = "idImagenCupon";
    public static final String RUTA = "desRutaFoto";

    @SerializedName(ID)
    public int id;
    @SerializedName(RUTA)
    public String ruta;

    protected FotosCupones(Parcel in) {
        id = in.readInt();
        ruta = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(ruta);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FotosCupones> CREATOR = new Creator<FotosCupones>() {
        @Override
        public FotosCupones createFromParcel(Parcel in) {
            return new FotosCupones(in);
        }

        @Override
        public FotosCupones[] newArray(int size) {
            return new FotosCupones[size];
        }
    };
}
