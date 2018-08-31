package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by mYam on 16/04/2018.
 */
@DatabaseTable
public class Direccion  implements Parcelable{

    public static final String ID="idDireccion";
    public static final String DESCRIPCION="desDireccion";
    public static final String CODIGO_POSTAL="numCodigoPostal";
    public static final String PAIS="nbPais";
    public static final String ESTADO="nbEstado";
    public static final String MUNICIPIO="nbMunicipio";
    public static final String CIUDAD="nbCiudad";
    public static final String LATITUD="dcLatitud";
    public static final String LONGITUD="dcLongitud";

    @SerializedName(ID)
    @DatabaseField(columnName = ID,id = true, index = true)
    public  int id;

    @SerializedName(DESCRIPCION)
    @DatabaseField(columnName = DESCRIPCION)
    public  String descripcion;

    @SerializedName(CODIGO_POSTAL)
    @DatabaseField(columnName = CODIGO_POSTAL)
    public  String codigo_postal;

    @SerializedName(PAIS)
    @DatabaseField(columnName = PAIS)
    public  String pais;

    @SerializedName(ESTADO)
    @DatabaseField(columnName = ESTADO)
    public  String estado;

    @SerializedName(MUNICIPIO)
    @DatabaseField(columnName = MUNICIPIO)
    public  String municipio;

    @SerializedName(CIUDAD)
    @DatabaseField(columnName = CIUDAD)
    public  String ciudad;

    @SerializedName(LONGITUD)
    @DatabaseField(columnName = LONGITUD)
    public  String longitud;

    @SerializedName(LATITUD)
    @DatabaseField(columnName = LATITUD)
    public  String latitud;

    protected Direccion(Parcel in) {
        id = in.readInt();
        descripcion = in.readString();
        codigo_postal = in.readString();
        pais = in.readString();
        estado = in.readString();
        municipio = in.readString();
        ciudad = in.readString();
        longitud = in.readString();
        latitud = in.readString();
    }

    public static final Creator<Direccion> CREATOR = new Creator<Direccion>() {
        @Override
        public Direccion createFromParcel(Parcel in) {
            return new Direccion(in);
        }

        @Override
        public Direccion[] newArray(int size) {
            return new Direccion[size];
        }
    };

    public Direccion() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(descripcion);
        parcel.writeString(codigo_postal);
        parcel.writeString(pais);
        parcel.writeString(estado);
        parcel.writeString(municipio);
        parcel.writeString(ciudad);
        parcel.writeString(longitud);
        parcel.writeString(latitud);
    }
}
