package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CodigoPostal implements Parcelable {
    public static final String ID = "Cp_IdCodigoPostal";
    public static final String MUNICIPIO = "Cp_Municipio";
    public static final String ESTADO = "Cp_Estado";
    public static final String CIUDAD = "Cp_Ciudad";
    public static final String CODIGO = "Cp_CodigoPostal";
    public static final String ASENTAMIENTO = "Cp_Asentamiento";

    @SerializedName(ID)
    public String id;
    @SerializedName(MUNICIPIO)
    public String municipio;
    @SerializedName(ESTADO)
    public String estado;
    @SerializedName(CIUDAD)
    public String ciudad;
    @SerializedName(CODIGO)
    public String codigo_postal;
    @SerializedName(ASENTAMIENTO)
    public String asentamiento;

    protected CodigoPostal(Parcel in) {
        id = in.readString();
        municipio = in.readString();
        estado = in.readString();
        ciudad = in.readString();
        codigo_postal = in.readString();
        asentamiento = in.readString();
    }

    public CodigoPostal() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(municipio);
        dest.writeString(estado);
        dest.writeString(ciudad);
        dest.writeString(codigo_postal);
        dest.writeString(asentamiento);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CodigoPostal> CREATOR = new Creator<CodigoPostal>() {
        @Override
        public CodigoPostal createFromParcel(Parcel in) {
            return new CodigoPostal(in);
        }

        @Override
        public CodigoPostal[] newArray(int size) {
            return new CodigoPostal[size];
        }
    };
}
