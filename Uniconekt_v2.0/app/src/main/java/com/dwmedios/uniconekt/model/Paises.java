package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Paises implements Parcelable{
    public static final String ID="idPais";
    public static final String NOMBRE="nbPais";
    public static final String CLAVE="cvPais";

    @SerializedName(ID)
    public  int id;
    @SerializedName(NOMBRE)
    public String nombre;
    @SerializedName(CLAVE)
    public String clave;

    protected Paises(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        clave = in.readString();
    }

    public Paises() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(clave);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Paises> CREATOR = new Creator<Paises>() {
        @Override
        public Paises createFromParcel(Parcel in) {
            return new Paises(in);
        }

        @Override
        public Paises[] newArray(int size) {
            return new Paises[size];
        }
    };
}
