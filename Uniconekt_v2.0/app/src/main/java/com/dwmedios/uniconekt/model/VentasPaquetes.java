package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class VentasPaquetes implements Parcelable {
    public static final String ID = "idVentasPaquetes";
    public static final String ID_UNIVERSIDAD = "idUniversidad";
    public static final String ID_PAQUETE = "idPaquete";
    public static final String FECHA_VENTA = "feVenta";
    public static final String VIGENCIA = "feVigencia";
    public static final String PAQUETE_ACTUAL = "fgPaqueteActual";
    public static final String RECURRENTE = "fgRecurrente";
    public static final String PAQUETE = "Paquete";
    public static final String REFERENCIA = "numReferenciaPayPal";


    @SerializedName(ID)
    @DatabaseField(columnName = ID, id = true, index = true)
    public int id;

    @SerializedName(ID_UNIVERSIDAD)
    @DatabaseField(columnName = ID_UNIVERSIDAD)
    public int id_universidad;

    @SerializedName(ID_PAQUETE)
    @DatabaseField(columnName = ID_PAQUETE)
    public int id_paquete;

    @SerializedName(FECHA_VENTA)
    @DatabaseField(columnName = FECHA_VENTA)
    public Date fechaVenta;

    @SerializedName(VIGENCIA)
    @DatabaseField(columnName = VIGENCIA)
    public Date fechaVigencia;

    @SerializedName(PAQUETE_ACTUAL)
    @DatabaseField(columnName = PAQUETE_ACTUAL)
    public boolean paqueteActual;

    @SerializedName(RECURRENTE)
    @DatabaseField(columnName = RECURRENTE)
    public boolean recurrente;

    @SerializedName(REFERENCIA)
    @DatabaseField(columnName = REFERENCIA)
    public String referencia;

    @SerializedName(PAQUETE)
    public Paquetes mPaquetes;




    protected VentasPaquetes(Parcel in) {
        id = in.readInt();
        id_universidad = in.readInt();
        id_paquete = in.readInt();
        fechaVenta = (java.util.Date) in.readSerializable();
        fechaVigencia = (java.util.Date) in.readSerializable();
        paqueteActual = in.readByte() != 0;
        recurrente = in.readByte() != 0;
        referencia = in.readString();
        mPaquetes = in.readParcelable(Paquetes.class.getClassLoader());
    }

    public VentasPaquetes() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(id_universidad);
        dest.writeInt(id_paquete);
        dest.writeSerializable(fechaVenta);
        dest.writeSerializable(fechaVigencia);
        dest.writeByte((byte) (paqueteActual ? 1 : 0));
        dest.writeByte((byte) (recurrente ? 1 : 0));
        dest.writeString(referencia);
        dest.writeParcelable(mPaquetes, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VentasPaquetes> CREATOR = new Creator<VentasPaquetes>() {
        @Override
        public VentasPaquetes createFromParcel(Parcel in) {
            return new VentasPaquetes(in);
        }

        @Override
        public VentasPaquetes[] newArray(int size) {
            return new VentasPaquetes[size];
        }
    };
}
