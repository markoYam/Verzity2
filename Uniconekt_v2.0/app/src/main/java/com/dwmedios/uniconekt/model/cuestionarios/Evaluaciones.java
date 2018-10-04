package com.dwmedios.uniconekt.model.cuestionarios;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Evaluaciones implements Parcelable{
    public static final String ID = "idEvaluacion";
    public static final String NB = "nbEvaluacion";
    public static final String FI = "feInicio";
    public static final String FF = "feFinaliza";
    public static final String DES = "desEvaluacion";

    @SerializedName(ID)
    public int id;
    @SerializedName(NB)
    public String nombre;
    @SerializedName(FI)
    public Date fechaInicio;
    @SerializedName(FF)
    public Date fechaFin;
    @SerializedName(DES)
    public String descripcion;

    protected Evaluaciones(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        fechaInicio = (java.util.Date) in.readSerializable();
        fechaFin = (java.util.Date) in.readSerializable();
        descripcion = in.readString();
    }

    public static final Creator<Evaluaciones> CREATOR = new Creator<Evaluaciones>() {
        @Override
        public Evaluaciones createFromParcel(Parcel in) {
            return new Evaluaciones(in);
        }

        @Override
        public Evaluaciones[] newArray(int size) {
            return new Evaluaciones[size];
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
        dest.writeSerializable(fechaInicio);
        dest.writeSerializable(fechaFin);
        dest.writeString(descripcion);
    }
}
