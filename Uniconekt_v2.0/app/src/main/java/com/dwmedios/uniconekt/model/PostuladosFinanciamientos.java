package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PostuladosFinanciamientos implements Parcelable {
    public static final String ID = "idPostulacionFinanciamiento";
    public static final String ID_PERSONA = "idPersona";
    public static final String ID_FINANCIAMIETO = "idFinanciamiento";
    public static final String FINANCIAMIENTO = "Financiamientos";
    public static final String PERSONA = "Personas";

    @SerializedName(ID)
    public int id;
    @SerializedName(ID_FINANCIAMIETO)
    public int id_financiamiento;
    @SerializedName(ID_PERSONA)
    public int id_persona;
    @SerializedName(FINANCIAMIENTO)
    public Financiamientos mFinanciamientos;
    @SerializedName(PERSONA)
    public Persona mPersona;

    protected PostuladosFinanciamientos(Parcel in) {
        id = in.readInt();
        id_financiamiento = in.readInt();
        id_persona = in.readInt();
        mFinanciamientos = in.readParcelable(Financiamientos.class.getClassLoader());
        mPersona = in.readParcelable(Persona.class.getClassLoader());
    }

    public PostuladosFinanciamientos() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(id_financiamiento);
        dest.writeInt(id_persona);
        dest.writeParcelable(mFinanciamientos, flags);
        dest.writeParcelable(mPersona, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostuladosFinanciamientos> CREATOR = new Creator<PostuladosFinanciamientos>() {
        @Override
        public PostuladosFinanciamientos createFromParcel(Parcel in) {
            return new PostuladosFinanciamientos(in);
        }

        @Override
        public PostuladosFinanciamientos[] newArray(int size) {
            return new PostuladosFinanciamientos[size];
        }
    };
}
