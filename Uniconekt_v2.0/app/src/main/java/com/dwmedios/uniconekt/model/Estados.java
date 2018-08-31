package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Estados implements Parcelable {
    public static final String ID = "idEstado";
    public static final String NOMBRE = "nbEstado";
    public static final String PAIS = "idPais";

    @SerializedName(ID)
    public int id;
    @SerializedName(NOMBRE)
    public String nombre;
    @SerializedName(PAIS)
    public int idPais;

    protected Estados(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        idPais = in.readInt();
    }

    public static final Creator<Estados> CREATOR = new Creator<Estados>() {
        @Override
        public Estados createFromParcel(Parcel in) {
            return new Estados(in);
        }

        @Override
        public Estados[] newArray(int size) {
            return new Estados[size];
        }
    };

    public Estados() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeInt(idPais);
    }
}
