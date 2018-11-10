package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
@DatabaseTable
public class Paquetes implements Parcelable {
    public static final String ID = "idPaquete";
    public static final String CLAVE = "cvPaquete";
    public static final String NOMBRE = "nbPaquete";
    public static final String DESCRIPCIO = "desPaquete";
    public static final String BECA = "fgAplicaBecas";
    public static final String FINAN = "fgAplicaFinanciamiento";
    public static final String POS = "fgAplicaPostulacion";
    public static final String A_CONTACTO = "fgAplicaContacto";
    public static final String A_DIRECCION = "fgAplicaDireccion";
    public static final String A_FAVORITOS = "fgAplicaFavoritos";
    public static final String A_IMAGENES = "fgAplicaImagenes";
    public static final String A_LOGO = "fgAplicaLogo";
    public static final String A_REDES_S = "fgAplicaRedes";
    public static final String A_GELOCA = "fgAplicaUbicacion";
    public static final String A_VIDEO_1 = "fgAplicaProspectusVideo";
    public static final String A_VIDEO_2 = "fgAplicaProspectusVideos";
    public static final String A_DESCRIPCION = "fgAplicaDescripcion";
    public static final String PROSPECTUS = "fgProspectus";
    public static final String COSTO = "dcCosto";
    public static final String DIAS = "dcDiasVigencia";
    public static final String REGISTRO = "feRegistro";

    @DatabaseField(columnName = ID, id = true, index = true)
    @SerializedName(ID)
    public int id;

    @DatabaseField(columnName = CLAVE)
    @SerializedName(CLAVE)
    public String clave;

    @DatabaseField(columnName = NOMBRE)
    @SerializedName(NOMBRE)
    public String nombre;

    @DatabaseField(columnName = DESCRIPCIO)
    @SerializedName(DESCRIPCIO)
    public String descripcion;

    @DatabaseField(columnName = BECA)
    @SerializedName(BECA)
    public boolean aplica_Beca;

    @DatabaseField(columnName = A_CONTACTO)
    @SerializedName(A_CONTACTO)
    public boolean aplica_contacto;

    @DatabaseField(columnName = A_DIRECCION)
    @SerializedName(A_DIRECCION)
    public boolean aplica_direccion;

    @DatabaseField(columnName = A_FAVORITOS)
    @SerializedName(A_FAVORITOS)
    public boolean aplica_favoritos;

    @DatabaseField(columnName = A_GELOCA)
    @SerializedName(A_GELOCA)
    public boolean aplica_geolocalizacion;

    @DatabaseField(columnName = A_IMAGENES)
    @SerializedName(A_IMAGENES)
    public boolean aplica_imagenes;

    @DatabaseField(columnName = A_LOGO)
    @SerializedName(A_LOGO)
    public boolean aplica_logo;

    @DatabaseField(columnName = A_REDES_S)
    @SerializedName(A_REDES_S)
    public boolean aplica_redes;

    @DatabaseField(columnName = A_VIDEO_1)
    @SerializedName(A_VIDEO_1)
    public boolean aplica_video_1;

    @DatabaseField(columnName = A_VIDEO_2)
    @SerializedName(A_VIDEO_2)
    public boolean aplica_video_2;

    @DatabaseField(columnName = A_DESCRIPCION)
    @SerializedName(A_DESCRIPCION)
    public boolean aplica_descripcion;

    @DatabaseField(columnName = FINAN)
    @SerializedName(FINAN)
    public boolean aplica_Financiamiento;

    @DatabaseField(columnName = POS)
    @SerializedName(POS)
    public boolean aplica_Postulacion;

    @DatabaseField(columnName = PROSPECTUS)
    @SerializedName(PROSPECTUS)
    public boolean aplica_Prospectus;


    @SerializedName(COSTO)
    public Float costo;
    @SerializedName(DIAS)
    public int dias_vigencia;
    @SerializedName(REGISTRO)
    @Deprecated
    public Date fechaRegistro;
    public boolean actual;

    protected Paquetes(Parcel in) {
        id = in.readInt();
        clave = in.readString();
        nombre = in.readString();
        descripcion = in.readString();
        aplica_Beca = in.readByte() != 0;
        aplica_contacto = in.readByte() != 0;
        aplica_direccion = in.readByte() != 0;
        aplica_favoritos = in.readByte() != 0;
        aplica_geolocalizacion = in.readByte() != 0;
        aplica_imagenes = in.readByte() != 0;
        aplica_logo = in.readByte() != 0;
        aplica_redes = in.readByte() != 0;
        aplica_video_1 = in.readByte() != 0;
        aplica_video_2 = in.readByte() != 0;
        aplica_descripcion = in.readByte() != 0;
        aplica_Financiamiento = in.readByte() != 0;
        aplica_Postulacion = in.readByte() != 0;
        aplica_Prospectus = in.readByte() != 0;
        if (in.readByte() == 0) {
            costo = null;
        } else {
            costo = in.readFloat();
        }
        dias_vigencia = in.readInt();
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
        dest.writeByte((byte) (aplica_contacto ? 1 : 0));
        dest.writeByte((byte) (aplica_direccion ? 1 : 0));
        dest.writeByte((byte) (aplica_favoritos ? 1 : 0));
        dest.writeByte((byte) (aplica_geolocalizacion ? 1 : 0));
        dest.writeByte((byte) (aplica_imagenes ? 1 : 0));
        dest.writeByte((byte) (aplica_logo ? 1 : 0));
        dest.writeByte((byte) (aplica_redes ? 1 : 0));
        dest.writeByte((byte) (aplica_video_1 ? 1 : 0));
        dest.writeByte((byte) (aplica_video_2 ? 1 : 0));
        dest.writeByte((byte) (aplica_descripcion ? 1 : 0));
        dest.writeByte((byte) (aplica_Financiamiento ? 1 : 0));
        dest.writeByte((byte) (aplica_Postulacion ? 1 : 0));
        dest.writeByte((byte) (aplica_Prospectus ? 1 : 0));
        if (costo == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(costo);
        }
        dest.writeInt(dias_vigencia);
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
