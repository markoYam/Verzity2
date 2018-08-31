package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PostuladosUniversidades implements Parcelable {
    public static final String ID = "idPostuladoUniversidad";
    public static final String ID_PERSONA = "idPersona";
    public static final String ID_LICENCIATURA = "idLicenciatura";
    public static final String ID_UNIVERSIDAD = "idUniversidad";
    public static final String PERSONAS = "Personas";

    @SerializedName(ID)
    public int id;
    @SerializedName(ID_PERSONA)
    public int id_persona;
    @SerializedName(ID_LICENCIATURA)
    public int id_licenciatura;
    @SerializedName(ID_UNIVERSIDAD)
    public int id_universidad;
    @SerializedName(PERSONAS)
    public Persona mPersona;

    protected PostuladosUniversidades(Parcel in) {
        id = in.readInt();
        id_persona = in.readInt();
        id_licenciatura = in.readInt();
        id_universidad = in.readInt();
        mPersona = in.readParcelable(Persona.class.getClassLoader());
    }

    public PostuladosUniversidades() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(id_persona);
        dest.writeInt(id_licenciatura);
        dest.writeInt(id_universidad);
        dest.writeParcelable(mPersona, flags);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostuladosUniversidades> CREATOR = new Creator<PostuladosUniversidades>() {
        @Override
        public PostuladosUniversidades createFromParcel(Parcel in) {
            return new PostuladosUniversidades(in);
        }

        @Override
        public PostuladosUniversidades[] newArray(int size) {
            return new PostuladosUniversidades[size];
        }
    };
}
