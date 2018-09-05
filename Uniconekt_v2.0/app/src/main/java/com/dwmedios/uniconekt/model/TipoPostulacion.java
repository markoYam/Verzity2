package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TipoPostulacion implements Parcelable {
    public String nombre;
    public int tipo;

    public TipoPostulacion(Parcel in) {
        nombre = in.readString();
        tipo = in.readInt();
    }

    public TipoPostulacion() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeInt(tipo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TipoPostulacion> CREATOR = new Creator<TipoPostulacion>() {
        @Override
        public TipoPostulacion createFromParcel(Parcel in) {
            return new TipoPostulacion(in);
        }

        @Override
        public TipoPostulacion[] newArray(int size) {
            return new TipoPostulacion[size];
        }
    };
}
