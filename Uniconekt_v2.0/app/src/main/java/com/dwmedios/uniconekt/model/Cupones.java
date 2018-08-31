package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Cupones implements Parcelable {
    public static final String ID = "idCupon";
    public static final String NOMBRE = "nbCupon";
    public static final String DESCRIPCION = "desCupon";
    public static final String CLAVE = "cvCupon";
    public static final String INICIO = "feInicio";
    public static final String FIN = "feFin";
    public static final String USO = "numUsoCupon";
    public static final String FOTOS = "ImagenesCupones";

    @SerializedName(ID)
    public int id;
    @SerializedName(NOMBRE)
    public String nombre;
    @SerializedName(DESCRIPCION)
    public String descripcion;
    @SerializedName(CLAVE)
    public String clave;
    @SerializedName(INICIO)
    public Date inicio;
    @SerializedName(FIN)
    public Date fin;
    @SerializedName(USO)
    public int uso;
    @SerializedName(FOTOS)
    public List<FotosCupones> mFotosCuponesList;

    protected Cupones(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
        clave = in.readString();
        inicio = (java.util.Date) in.readSerializable();
        fin = (java.util.Date) in.readSerializable();
        uso = in.readInt();
        mFotosCuponesList = in.createTypedArrayList(FotosCupones.CREATOR);
    }

    public Cupones() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeString(clave);
        dest.writeSerializable(inicio);
        dest.writeSerializable(fin);
        dest.writeInt(uso);
        dest.writeTypedList(mFotosCuponesList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Cupones> CREATOR = new Creator<Cupones>() {
        @Override
        public Cupones createFromParcel(Parcel in) {
            return new Cupones(in);
        }

        @Override
        public Cupones[] newArray(int size) {
            return new Cupones[size];
        }
    };
}
