package com.dwmedios.uniconekt.model.cuestionarios;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PreguntasViewModel implements Parcelable {
    public static final String ID = "idPregunta";
    public static final String PRE = "Preguntas";
    public static final String RES = "RespuestasList";
    public static final String RESUELTO = "isResuelto";

    @SerializedName(ID)
    public int id;
    @SerializedName(PRE)
    public Preguntas mPreguntas;
    @SerializedName(RES)
    public List<Respuestas> mRespuestasList;
    @SerializedName(RESUELTO)
    public boolean isResuelto;

    protected PreguntasViewModel(Parcel in) {
        id = in.readInt();
        mPreguntas = in.readParcelable(Preguntas.class.getClassLoader());
        mRespuestasList = in.createTypedArrayList(Respuestas.CREATOR);
        isResuelto = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(mPreguntas, flags);
        dest.writeTypedList(mRespuestasList);
        dest.writeByte((byte) (isResuelto ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PreguntasViewModel> CREATOR = new Creator<PreguntasViewModel>() {
        @Override
        public PreguntasViewModel createFromParcel(Parcel in) {
            return new PreguntasViewModel(in);
        }

        @Override
        public PreguntasViewModel[] newArray(int size) {
            return new PreguntasViewModel[size];
        }
    };
}
