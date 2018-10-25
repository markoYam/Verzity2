package com.dwmedios.uniconekt.model.cuestionarios;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Evaluaciones implements Parcelable {
    public static final String ID = "idEvaluacion";
    public static final String NB = "nbEvaluacion";
    public static final String FI = "feInicio";
    public static final String FF = "feFinaliza";
    public static final String DES = "desEvaluacion";
    public static final String PAGA = "idPruebaAplica";

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

    @SerializedName(PAGA)
    public int isPaga;

    public String defaultValue;

    protected Evaluaciones(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        fechaInicio = (java.util.Date) in.readSerializable();
        fechaFin = (java.util.Date) in.readSerializable();
        descripcion = in.readString();
        isPaga = in.readInt();
        defaultValue = in.readString();
    }

    public void changeNameSection() {
        defaultValue = (isPaga==1 ? "DE PAGA" : "BÁSICO");
    }

    public Evaluaciones() {
        changeNameSection();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeSerializable(fechaInicio);
        dest.writeSerializable(fechaFin);
        dest.writeString(descripcion);
        dest.writeInt(isPaga);
        dest.writeString(defaultValue);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
