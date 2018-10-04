package com.dwmedios.uniconekt.model.cuestionarios;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Respuestas implements Parcelable {
    public static final String ID = "idRespuesta";
    public static final String NOMBRE = "nbRespuesta";
    public static final String ID_PREGUNTA = "idPregunta";
    public static final String IMAGEN = "fgImagen"; //BOL

    @SerializedName(ID)
    public int id;

    @SerializedName(ID_PREGUNTA)
    public int id_pregunta;

    @SerializedName(NOMBRE)
    public String nombre;

    @SerializedName(IMAGEN)
    public boolean isImagen;

    public boolean isSeleccionado;


    public Respuestas(Parcel in) {
        id = in.readInt();
        id_pregunta = in.readInt();
        nombre = in.readString();
        isImagen = in.readByte() != 0;
        isSeleccionado = in.readByte() != 0;
    }

    public Respuestas() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(id_pregunta);
        dest.writeString(nombre);
        dest.writeByte((byte) (isImagen ? 1 : 0));
        dest.writeByte((byte) (isSeleccionado ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Respuestas> CREATOR = new Creator<Respuestas>() {
        @Override
        public Respuestas createFromParcel(Parcel in) {
            return new Respuestas(in);
        }

        @Override
        public Respuestas[] newArray(int size) {
            return new Respuestas[size];
        }
    };
}
