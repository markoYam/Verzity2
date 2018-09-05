package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class VentaPaqueteAsesor implements Parcelable {
    public static final String ID = "idVentaPaqueteAsesor";
    public static final String ID_PAQUETE = "idPaqueteAsesor";
    public static final String ID_COMPRA = "idPersona";
    public static final String ID_ASESOR = "idPersonaAsesor";
    public static final String FEHCA_COMPRA = "feVenta";
    public static final String FECHA_VIGENCIA = "feVigencia";
    public static final String REFERENCIA = "numReferenciaPayPal";

    @DatabaseField(columnName = ID, id = true, index = true)
    @SerializedName(ID)
    public int id;

    @DatabaseField(columnName = ID_PAQUETE)
    @SerializedName(ID_PAQUETE)
    public int idPaquete;

    @DatabaseField(columnName = ID_COMPRA)
    @SerializedName(ID_COMPRA)
    public int idPersona;

    @DatabaseField(columnName = ID_ASESOR)
    @SerializedName(ID_ASESOR)
    public int idAsesor;

    @DatabaseField(columnName = FEHCA_COMPRA)
    @SerializedName(FEHCA_COMPRA)
    public Date fecha_Compra;

    @DatabaseField(columnName = FECHA_VIGENCIA)
    @SerializedName(FECHA_VIGENCIA)
    public Date fecha_vigencia;

    @DatabaseField(columnName = REFERENCIA)
    @SerializedName(REFERENCIA)
    public String referencia;

    public VentaPaqueteAsesor(Parcel in) {
        id = in.readInt();
        idPaquete = in.readInt();
        idPersona = in.readInt();
        idAsesor = in.readInt();
        fecha_Compra = (java.util.Date) in.readSerializable();
        fecha_vigencia = (java.util.Date) in.readSerializable();
        referencia = in.readString();
    }

    public static final Creator<VentaPaqueteAsesor> CREATOR = new Creator<VentaPaqueteAsesor>() {
        @Override
        public VentaPaqueteAsesor createFromParcel(Parcel in) {
            return new VentaPaqueteAsesor(in);
        }

        @Override
        public VentaPaqueteAsesor[] newArray(int size) {
            return new VentaPaqueteAsesor[size];
        }
    };

    public VentaPaqueteAsesor() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idPaquete);
        dest.writeInt(idPersona);
        dest.writeInt(idAsesor);
        dest.writeSerializable(fecha_Compra);
        dest.writeSerializable(fecha_vigencia);
        dest.writeString(referencia);
    }
}
