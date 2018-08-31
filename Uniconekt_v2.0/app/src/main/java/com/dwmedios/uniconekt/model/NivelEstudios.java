package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NivelEstudios implements Parcelable {
    public static final String ID = "idCatNivelEstudios";
    public static final String NOMBRE = "nbNivelEstudios";

    @SerializedName(ID)
    public int id;
    @SerializedName(NOMBRE)
    public String nombre;

    protected NivelEstudios(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NivelEstudios> CREATOR = new Creator<NivelEstudios>() {
        @Override
        public NivelEstudios createFromParcel(Parcel in) {
            return new NivelEstudios(in);
        }

        @Override
        public NivelEstudios[] newArray(int size) {
            return new NivelEstudios[size];
        }
    };
}
