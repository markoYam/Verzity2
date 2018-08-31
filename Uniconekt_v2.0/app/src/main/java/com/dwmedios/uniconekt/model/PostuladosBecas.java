package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PostuladosBecas implements Parcelable{
    public static final String ID_PERSONA = "idPersona";
    public static final String ID_BECA = "idBeca";
    public static final String PERSONAS = "Personas";
    public static final String BECAS = "Becas";

    @SerializedName(ID_PERSONA)
    public int id_persona;
    @SerializedName(ID_BECA)
    public int id_beca;
    @SerializedName(PERSONAS)
    public Persona mPersona;
    @SerializedName(BECAS)
    public Becas mBecas;

    protected PostuladosBecas(Parcel in) {
        id_persona = in.readInt();
        id_beca = in.readInt();
        mPersona = in.readParcelable(Persona.class.getClassLoader());
        mBecas = in.readParcelable(Becas.class.getClassLoader());
    }

    public PostuladosBecas() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_persona);
        dest.writeInt(id_beca);
        dest.writeParcelable(mPersona, flags);
        dest.writeParcelable(mBecas, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostuladosBecas> CREATOR = new Creator<PostuladosBecas>() {
        @Override
        public PostuladosBecas createFromParcel(Parcel in) {
            return new PostuladosBecas(in);
        }

        @Override
        public PostuladosBecas[] newArray(int size) {
            return new PostuladosBecas[size];
        }
    };
}
