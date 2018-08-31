package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TipoPersonas implements Parcelable{
    public static final String ID = "idTipoPersona";
    public static final String NOMBRE = "nbTipoPersona";
    @SerializedName(ID)
    public int id;
    @SerializedName(NOMBRE)
    public String nombre;

    protected TipoPersonas(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
    }

    public static final Creator<TipoPersonas> CREATOR = new Creator<TipoPersonas>() {
        @Override
        public TipoPersonas createFromParcel(Parcel in) {
            return new TipoPersonas(in);
        }

        @Override
        public TipoPersonas[] newArray(int size) {
            return new TipoPersonas[size];
        }
    };

    public TipoPersonas() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
    }
}
