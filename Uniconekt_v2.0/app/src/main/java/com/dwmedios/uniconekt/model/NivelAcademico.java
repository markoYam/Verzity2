package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NivelAcademico implements Parcelable {
    public static final String ID = "idCatNivelEstudios";
    public static final String NOMBRE = "nbNivelEstudios";
    @SerializedName(ID)
    public int id;
    @SerializedName(NOMBRE)
    public String nombre;

    protected NivelAcademico(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
    }

    public NivelAcademico() {
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

    public static final Creator<NivelAcademico> CREATOR = new Creator<NivelAcademico>() {
        @Override
        public NivelAcademico createFromParcel(Parcel in) {
            return new NivelAcademico(in);
        }

        @Override
        public NivelAcademico[] newArray(int size) {
            return new NivelAcademico[size];
        }
    };
}
