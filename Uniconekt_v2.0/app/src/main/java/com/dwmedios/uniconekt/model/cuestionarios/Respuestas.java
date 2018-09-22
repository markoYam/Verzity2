package com.dwmedios.uniconekt.model.cuestionarios;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Respuestas implements Parcelable {
    public static final String ID = "idRespuesta";
    public static final String NOMBRE = "nbRespuesta";
    public static final String FOTO = "desRutaFoto";
    public static final String SELE = "fgSeleccionado";

    @SerializedName(ID)
    public int id;
    @SerializedName(NOMBRE)
    public String nombre;
    @SerializedName(FOTO)
    public String foto;
    @SerializedName(SELE)
    public boolean isSeleccionado;


    protected Respuestas(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        foto = in.readString();
        isSeleccionado = in.readByte() != 0;
    }

    public Respuestas() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(foto);
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
