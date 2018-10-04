package com.dwmedios.uniconekt.model.cuestionarios;

import android.os.Parcel;
import android.os.Parcelable;

import com.dwmedios.uniconekt.model.Estatus;
import com.google.gson.annotations.SerializedName;

public class EvaluacionesPersona implements Parcelable {
    public static final String ID = "idEvaluacionPersona";
    public static final String ID_E = "idEvaluacion";
    public static final String ID_P = "idPersona";
    public static final String ID_ES = "idEstatus";
    public static final String ESTATUS = "Estatus";
    public static final String EVALUACIONES = "Evaluaciones";

    @SerializedName(ID)
    public int idEvaluacionPersona;
    @SerializedName(ID_E)
    public int idEvaluacion;
    @SerializedName(ID_P)
    public int idPersona;
    @SerializedName(ID_ES)
    public int idEstatus;
    @SerializedName(ESTATUS)
    public Estatus mEstatus;
    @SerializedName(EVALUACIONES)
    public Evaluaciones mEvaluaciones;

    public EvaluacionesPersona(Parcel in) {
        idEvaluacionPersona = in.readInt();
        idEvaluacion = in.readInt();
        idPersona = in.readInt();
        idEstatus = in.readInt();
        mEstatus = in.readParcelable(Estatus.class.getClassLoader());
        mEvaluaciones = in.readParcelable(Evaluaciones.class.getClassLoader());
    }

    public static final Creator<EvaluacionesPersona> CREATOR = new Creator<EvaluacionesPersona>() {
        @Override
        public EvaluacionesPersona createFromParcel(Parcel in) {
            return new EvaluacionesPersona(in);
        }

        @Override
        public EvaluacionesPersona[] newArray(int size) {
            return new EvaluacionesPersona[size];
        }
    };

    public EvaluacionesPersona() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idEvaluacionPersona);
        dest.writeInt(idEvaluacion);
        dest.writeInt(idPersona);
        dest.writeInt(idEstatus);
        dest.writeParcelable(mEstatus, flags);
        dest.writeParcelable(mEvaluaciones, flags);
    }
}
