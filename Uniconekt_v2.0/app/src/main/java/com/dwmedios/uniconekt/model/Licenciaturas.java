package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Licenciaturas implements Parcelable {
    public static final String ID = "idLicenciatura";
    public static final String NOMBRE = "nbLicenciatura";
    public static final String NIVEL = "CatNivelEstudios";

    @SerializedName(ID)
    public int id;
    @SerializedName(NOMBRE)
    public String nombre;
    @SerializedName(NIVEL)
    public NivelEstudios mNivelEstudios;
    public boolean ischeCked;


    public Licenciaturas(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        mNivelEstudios = in.readParcelable(NivelEstudios.class.getClassLoader());
        ischeCked = in.readByte() != 0;
    }

    public Licenciaturas() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeParcelable(mNivelEstudios, flags);
        dest.writeByte((byte) (ischeCked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Licenciaturas> CREATOR = new Creator<Licenciaturas>() {
        @Override
        public Licenciaturas createFromParcel(Parcel in) {
            return new Licenciaturas(in);
        }

        @Override
        public Licenciaturas[] newArray(int size) {
            return new Licenciaturas[size];
        }
    };
}
