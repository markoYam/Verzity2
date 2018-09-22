package com.dwmedios.uniconekt.model.cuestionarios;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cuestionario implements Parcelable {
    public static final String PRE = "Preguntas";
    public static final String ID = "idCuestionario";
    @SerializedName(ID)
    public int id;
    @SerializedName(PRE)
    public List<Preguntas> mPreguntasList;

    public Cuestionario(Parcel in) {
        id = in.readInt();
        mPreguntasList = in.createTypedArrayList(Preguntas.CREATOR);
    }

    public static final Creator<Cuestionario> CREATOR = new Creator<Cuestionario>() {
        @Override
        public Cuestionario createFromParcel(Parcel in) {
            return new Cuestionario(in);
        }

        @Override
        public Cuestionario[] newArray(int size) {
            return new Cuestionario[size];
        }
    };

    public Cuestionario() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeTypedList(mPreguntasList);
    }
}
