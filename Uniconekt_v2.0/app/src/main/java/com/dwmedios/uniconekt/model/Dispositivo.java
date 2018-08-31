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
public class Dispositivo implements Parcelable{
    public static final String ID="idDispositivo";
    public static final String CLAVE_DISPOSITIVO="cvDispositivo";
    public static final String CLAVE_FIREBASE="cvFirebase";
    public static final String PERSONA="Personas";

    @SerializedName(ID)
    @DatabaseField(columnName = ID,id = true, index = true)
    public int id;

    @SerializedName(CLAVE_DISPOSITIVO)
    @DatabaseField(columnName = CLAVE_DISPOSITIVO)
    public String clave_dispositivo;

    @SerializedName(CLAVE_FIREBASE)
    @DatabaseField(columnName = CLAVE_FIREBASE)
    public String clave_firebase;

    @SerializedName(PERSONA)
    public Persona mPersona;


    public Dispositivo() {
    }

    protected Dispositivo(Parcel in) {
        id = in.readInt();
        clave_dispositivo = in.readString();
        clave_firebase = in.readString();
        mPersona = in.readParcelable(Persona.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(clave_dispositivo);
        dest.writeString(clave_firebase);
        dest.writeParcelable(mPersona, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Dispositivo> CREATOR = new Creator<Dispositivo>() {
        @Override
        public Dispositivo createFromParcel(Parcel in) {
            return new Dispositivo(in);
        }

        @Override
        public Dispositivo[] newArray(int size) {
            return new Dispositivo[size];
        }
    };
}
