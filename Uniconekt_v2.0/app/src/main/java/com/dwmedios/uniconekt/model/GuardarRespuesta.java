package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.dwmedios.uniconekt.model.cuestionarios.Respuestas;

import java.util.List;

public class GuardarRespuesta implements Parcelable{

    public int idPersona;
    public int idCuestionario;
    public int idPregunta;
    public List<Respuestas> mRespuestasList;

    public GuardarRespuesta(Parcel in) {
        idPersona = in.readInt();
        idCuestionario = in.readInt();
        idPregunta = in.readInt();
        mRespuestasList = in.createTypedArrayList(Respuestas.CREATOR);
    }

    public static final Creator<GuardarRespuesta> CREATOR = new Creator<GuardarRespuesta>() {
        @Override
        public GuardarRespuesta createFromParcel(Parcel in) {
            return new GuardarRespuesta(in);
        }

        @Override
        public GuardarRespuesta[] newArray(int size) {
            return new GuardarRespuesta[size];
        }
    };

    public GuardarRespuesta() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idPersona);
        dest.writeInt(idCuestionario);
        dest.writeInt(idPregunta);
        dest.writeTypedList(mRespuestasList);
    }
}
