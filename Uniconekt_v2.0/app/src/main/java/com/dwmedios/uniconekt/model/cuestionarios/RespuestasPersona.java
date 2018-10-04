package com.dwmedios.uniconekt.model.cuestionarios;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RespuestasPersona implements Parcelable{
    public static final String ID_EVA = "idEvaluacion";
    public static final String ID_EVA_PE = "idEvaluacionPersona";
    public static final String ID_PE = "idPersona";
    public static final String ID_PRE = "idPregunta";
    public static final String ID_RES = "idRespuesta";
    public static final String NOMBRE = "nbRespuesta";

    @SerializedName(ID_EVA)
    public int idEvaluacion;
    @SerializedName(ID_EVA_PE)
    public int idEvaluacion_Persona;
    @SerializedName(ID_PE)
    public int idPersona;
    @SerializedName(ID_PRE)
    public int idPregunta;
    @SerializedName(ID_RES)
    public int idRespuesta;
    @SerializedName(NOMBRE)
    public String descripcion;

    public RespuestasPersona(Parcel in) {
        idEvaluacion = in.readInt();
        idEvaluacion_Persona = in.readInt();
        idPersona = in.readInt();
        idPregunta = in.readInt();
        idRespuesta = in.readInt();
        descripcion = in.readString();
    }

    public static final Creator<RespuestasPersona> CREATOR = new Creator<RespuestasPersona>() {
        @Override
        public RespuestasPersona createFromParcel(Parcel in) {
            return new RespuestasPersona(in);
        }

        @Override
        public RespuestasPersona[] newArray(int size) {
            return new RespuestasPersona[size];
        }
    };

    public RespuestasPersona() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idEvaluacion);
        dest.writeInt(idEvaluacion_Persona);
        dest.writeInt(idPersona);
        dest.writeInt(idPregunta);
        dest.writeInt(idRespuesta);
        dest.writeString(descripcion);
    }
}
