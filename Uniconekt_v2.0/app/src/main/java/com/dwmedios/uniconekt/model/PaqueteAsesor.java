package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PaqueteAsesor implements Parcelable {
    public static final String ID = "idPaqueteAsesor";
    public static final String CLAVE = "cvPaquete";
    public static final String NOMBRE = "nbPaquete";
    public static final String DES = "desPaquete";
    public static final String DIAS = "dcDiasVigencia";
    public static final String COSTO = "dcCosto";
    public static final String NUM = "numCuestionariosLiberados";

    @SerializedName(ID)
    public int id;
    @SerializedName(CLAVE)
    public String cvPaquete;
    @SerializedName(NOMBRE)
    public String nombre;
    @SerializedName(DES)
    public String descripcion;
    @SerializedName(DIAS)
    public int vigencia;
    @SerializedName(COSTO)
    public Float costo;
    @SerializedName(NUM)
    public int numCuestionarios;

    public boolean isPaqueteActual = false;


    protected PaqueteAsesor(Parcel in) {
        id = in.readInt();
        cvPaquete = in.readString();
        nombre = in.readString();
        descripcion = in.readString();
        vigencia = in.readInt();
        if (in.readByte() == 0) {
            costo = null;
        } else {
            costo = in.readFloat();
        }
        numCuestionarios = in.readInt();
        isPaqueteActual = in.readByte() != 0;
    }

    public PaqueteAsesor() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(cvPaquete);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeInt(vigencia);
        if (costo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(costo);
        }
        dest.writeInt(numCuestionarios);
        dest.writeByte((byte) (isPaqueteActual ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaqueteAsesor> CREATOR = new Creator<PaqueteAsesor>() {
        @Override
        public PaqueteAsesor createFromParcel(Parcel in) {
            return new PaqueteAsesor(in);
        }

        @Override
        public PaqueteAsesor[] newArray(int size) {
            return new PaqueteAsesor[size];
        }
    };
}
