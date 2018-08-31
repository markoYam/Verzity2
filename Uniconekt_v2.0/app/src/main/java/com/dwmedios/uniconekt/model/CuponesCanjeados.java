package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Queue;

public class CuponesCanjeados implements Parcelable {
    public static final String ID="idCuponCanjeado";
    public static final String IDPERSONA="idPersona";
    public static final String ID_CUPON="idCupon";

    @SerializedName(ID)
    public int id;
    @SerializedName(ID_CUPON)
    public int id_cupon;
    @SerializedName(IDPERSONA)
    public int id_persona;

    protected CuponesCanjeados(Parcel in) {
        id = in.readInt();
        id_cupon = in.readInt();
        id_persona = in.readInt();
    }

    public CuponesCanjeados() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(id_cupon);
        dest.writeInt(id_persona);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CuponesCanjeados> CREATOR = new Creator<CuponesCanjeados>() {
        @Override
        public CuponesCanjeados createFromParcel(Parcel in) {
            return new CuponesCanjeados(in);
        }

        @Override
        public CuponesCanjeados[] newArray(int size) {
            return new CuponesCanjeados[size];
        }
    };
}
