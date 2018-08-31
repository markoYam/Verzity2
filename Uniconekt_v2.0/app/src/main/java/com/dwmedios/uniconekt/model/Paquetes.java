package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.nostra13.universalimageloader.utils.L;

import java.util.Date;

public class Paquetes implements Parcelable{
    public static final String ID = "idPaquete";
    public static final String CLAVE = "cvPaquete";
    public static final String NOMBRE = "nbPaquete";
    public static final String DESCRIPCIO = "desPaquete";
    public static final String BECA = "fgAplicaBecas";
    public static final String FINAN = "fgAplicaFinanciamiento";
    public static final String POS = "fgAplicaPostulacion";
    public static final String PROSPECTUS = "fgProspectus";
    public static final String COSTO = "dcCosto";
    public static final String DIAS = "dcDiasVigencia";

    public static final String REGISTRO = "feRegistro";

    @SerializedName(ID)
    public int id;
    @SerializedName(CLAVE)
    public String clave;
    @SerializedName(NOMBRE)
    public String nombre;
    @SerializedName(DESCRIPCIO)
    public String descripcion;
    @SerializedName(BECA)
    public boolean aplica_Beca;
    @SerializedName(FINAN)
    public boolean aplica_Financiamiento;
    @SerializedName(POS)
    public boolean aplica_Postulacion;
    @SerializedName(COSTO)
    public Float costo;
    @SerializedName(DIAS)
    public int dias_vigencia;
    @SerializedName(REGISTRO)
    public Date fechaRegistro;

    @SerializedName(PROSPECTUS)
    public boolean aplica_Prospectus;

    public boolean actual;


    protected Paquetes(Parcel in) {
        id = in.readInt();
        clave = in.readString();
        nombre = in.readString();
        descripcion = in.readString();
        aplica_Beca = in.readByte() != 0;
        aplica_Financiamiento = in.readByte() != 0;
        aplica_Postulacion = in.readByte() != 0;
        if (in.readByte() == 0) {
            costo = null;
        } else {
            costo = in.readFloat();
        }
        dias_vigencia = in.readInt();
        aplica_Prospectus = in.readByte() != 0;
        actual = in.readByte() != 0;
    }

    public Paquetes() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(clave);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeByte((byte) (aplica_Beca ? 1 : 0));
        dest.writeByte((byte) (aplica_Financiamiento ? 1 : 0));
        dest.writeByte((byte) (aplica_Postulacion ? 1 : 0));
        if (costo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(costo);
        }
        dest.writeInt(dias_vigencia);
        dest.writeByte((byte) (aplica_Prospectus ? 1 : 0));
        dest.writeByte((byte) (actual ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Paquetes> CREATOR = new Creator<Paquetes>() {
        @Override
        public Paquetes createFromParcel(Parcel in) {
            return new Paquetes(in);
        }

        @Override
        public Paquetes[] newArray(int size) {
            return new Paquetes[size];
        }
    };
}
