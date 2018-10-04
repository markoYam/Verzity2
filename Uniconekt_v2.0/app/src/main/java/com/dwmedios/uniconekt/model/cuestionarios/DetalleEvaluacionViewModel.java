package com.dwmedios.uniconekt.model.cuestionarios;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetalleEvaluacionViewModel implements Parcelable{
    public static final String EVA = "Evaluaciones";
    public static final String ID_EVA = "idEvaluacion";
    public static final String ID_EVA_PER = "idEvaluacionPersona";
    public static final String ID_PER = "idPersona";
    public static final String ID_PRE = "listPreguntas";

    @SerializedName(ID_EVA)
    public int id_evaluacion;
    @SerializedName(ID_EVA_PER)
    public int id_evaluacion_persona;
    @SerializedName(ID_PER)
    public int id_persona;
    @SerializedName(EVA)
    public Evaluaciones mEvaluaciones;
    @SerializedName(ID_PRE)
    public List<PreguntasViewModel> preguntasViewModelList;

    protected DetalleEvaluacionViewModel(Parcel in) {
        id_evaluacion = in.readInt();
        id_evaluacion_persona = in.readInt();
        id_persona = in.readInt();
        mEvaluaciones = in.readParcelable(Evaluaciones.class.getClassLoader());
        preguntasViewModelList = in.createTypedArrayList(PreguntasViewModel.CREATOR);
    }

    public static final Creator<DetalleEvaluacionViewModel> CREATOR = new Creator<DetalleEvaluacionViewModel>() {
        @Override
        public DetalleEvaluacionViewModel createFromParcel(Parcel in) {
            return new DetalleEvaluacionViewModel(in);
        }

        @Override
        public DetalleEvaluacionViewModel[] newArray(int size) {
            return new DetalleEvaluacionViewModel[size];
        }
    };

    public DetalleEvaluacionViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_evaluacion);
        dest.writeInt(id_evaluacion_persona);
        dest.writeInt(id_persona);
        dest.writeParcelable(mEvaluaciones, flags);
        dest.writeTypedList(preguntasViewModelList);
    }
}
