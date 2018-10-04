package com.dwmedios.uniconekt.model.cuestionarios;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Resultados implements Parcelable{
    public static final String ID = "idEvaluacion";
    public static final String NOMBRE = "nombreEvaluacion";
    public static final String PUNTAJE = "puntaje";
    public static final String MENSAJE = "mensaje";

    @SerializedName(ID)
    public int id;
    @SerializedName(NOMBRE)
    public String nombre;
    @SerializedName(PUNTAJE)
    public String puntaje;
    @SerializedName(MENSAJE)
    public String mensaje;

    protected Resultados(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        puntaje = in.readString();
        mensaje = in.readString();
    }

    public static final Creator<Resultados> CREATOR = new Creator<Resultados>() {
        @Override
        public Resultados createFromParcel(Parcel in) {
            return new Resultados(in);
        }

        @Override
        public Resultados[] newArray(int size) {
            return new Resultados[size];
        }
    };

    public Resultados() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(puntaje);
        dest.writeString(mensaje);
    }
}
