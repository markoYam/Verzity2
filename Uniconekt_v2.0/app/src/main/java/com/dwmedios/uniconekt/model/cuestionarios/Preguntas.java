package com.dwmedios.uniconekt.model.cuestionarios;

import android.os.Parcel;
import android.os.Parcelable;

import com.dwmedios.uniconekt.model.Estatus;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Preguntas implements Parcelable {
    public static final String ID = "idPregunta";
    public static final String NOMBRE = "nbPregunta";
    public static final String DESPREGUNTA = "desPregunta";
    public static final String ESTATUS = "Estatus";
    public static final String TIPO = "TipoPregunta";
    public static final String RES = "Respuestas";
    @SerializedName(ID)
    public int id;
    @SerializedName(NOMBRE)
    public String nombre;
    @SerializedName(DESPREGUNTA)
    public String descripcion;
    @SerializedName(ESTATUS)
    public Estatus mEstatus;
    @SerializedName(TIPO)
    public TipoPregunta mTipoPregunta;
    @SerializedName(RES)
    public List<Respuestas> mRespuestasList;

    protected Preguntas(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        descripcion = in.readString();
        mEstatus = in.readParcelable(Estatus.class.getClassLoader());
        mTipoPregunta = in.readParcelable(TipoPregunta.class.getClassLoader());
        mRespuestasList = in.createTypedArrayList(Respuestas.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(descripcion);
        dest.writeParcelable(mEstatus, flags);
        dest.writeParcelable(mTipoPregunta, flags);
        dest.writeTypedList(mRespuestasList);
    }
}
