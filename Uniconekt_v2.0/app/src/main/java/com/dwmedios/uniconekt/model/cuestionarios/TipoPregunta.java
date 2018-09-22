package com.dwmedios.uniconekt.model.cuestionarios;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TipoPregunta implements Parcelable{
    public static final String ID = "idTipoPregunta";
    public static final String DES = "desTipoPregunta";

    @SerializedName(ID)
    public int id;
    @SerializedName(DES)
    public String nombre;

    protected TipoPregunta(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
    }

    public static final Creator<TipoPregunta> CREATOR = new Creator<TipoPregunta>() {
        @Override
        public TipoPregunta createFromParcel(Parcel in) {
            return new TipoPregunta(in);
        }

        @Override
        public TipoPregunta[] newArray(int size) {
            return new TipoPregunta[size];
        }
    };

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
