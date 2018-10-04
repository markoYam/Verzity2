package com.dwmedios.uniconekt.model.cuestionarios;

import android.os.Parcel;
import android.os.Parcelable;

import com.dwmedios.uniconekt.model.Estatus;
import com.google.gson.annotations.SerializedName;

public class Preguntas implements Parcelable {
    public static final String ID = "idPregunta";
    public static final String NOMBRE = "nbPregunta";
    public static final String ESTATUS = "Estatus";
    public static final String TIPO = "CatTipoPregunta";

    @SerializedName(ID)
    public int id;

    @SerializedName(NOMBRE)
    public String nombre;

    @SerializedName(ESTATUS)
    public Estatus mEstatus;

    @SerializedName(TIPO)
    public TipoPregunta mTipoPregunta;

    protected Preguntas(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        mEstatus = in.readParcelable(Estatus.class.getClassLoader());
        mTipoPregunta = in.readParcelable(TipoPregunta.class.getClassLoader());
    }

    public Preguntas() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeParcelable(mEstatus, flags);
        dest.writeParcelable(mTipoPregunta, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Preguntas> CREATOR = new Creator<Preguntas>() {
        @Override
        public Preguntas createFromParcel(Parcel in) {
            return new Preguntas(in);
        }

        @Override
        public Preguntas[] newArray(int size) {
            return new Preguntas[size];
        }
    };
}
