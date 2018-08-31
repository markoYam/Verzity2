package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PostuladosGeneral implements Parcelable {
    public static final String PERSONA = "persona";
    public static final String LICENCIATURA = "licenciatura";
    public static final String BECA = "beca";
    public static final String FECHA = "fechaPostulacion";
    public static final String FINACIAMIENTO = "financiamiento";
    public static final String NOMBRE = "NombreSeccion";
    @SerializedName(PERSONA)
    public Persona persona;
    @SerializedName(LICENCIATURA)
    public Licenciaturas licenciatura;
    @SerializedName(BECA)
    public Becas beca;
    @SerializedName(FECHA)
    public Date fechaPostulacion;
    @SerializedName(FINACIAMIENTO)
    public Financiamientos financiamiento;
    @SerializedName(NOMBRE)
    public String NombreSeccion;

    protected PostuladosGeneral(Parcel in) {
        persona = in.readParcelable(Persona.class.getClassLoader());
        licenciatura = in.readParcelable(Licenciaturas.class.getClassLoader());
        beca = in.readParcelable(Becas.class.getClassLoader());
        //verificar cada vez que se actualiza
        fechaPostulacion = (java.util.Date) in.readSerializable();
        financiamiento = in.readParcelable(Financiamientos.class.getClassLoader());
        NombreSeccion = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(persona, flags);
        dest.writeParcelable(licenciatura, flags);
        dest.writeParcelable(beca, flags);
        dest.writeSerializable(fechaPostulacion);
        dest.writeParcelable(financiamiento, flags);
        dest.writeString(NombreSeccion);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostuladosGeneral> CREATOR = new Creator<PostuladosGeneral>() {
        @Override
        public PostuladosGeneral createFromParcel(Parcel in) {
            return new PostuladosGeneral(in);
        }

        @Override
        public PostuladosGeneral[] newArray(int size) {
            return new PostuladosGeneral[size];
        }
    };

    @Override
    public String toString() {
        return (persona.nombre + " se ha postulado " +
                (beca != null ?"a la beca "+ beca.nombre.toUpperCase() : (financiamiento != null ? " al financiamiento " +financiamiento.nombre.toUpperCase() : (licenciatura != null ?"al programa académico "+ licenciatura.nombre +" - "+licenciatura.mNivelEstudios.nombre: "")))
        );
    }

    public String getDetalle()
    {
        return (beca != null ? beca.descripcion : (financiamiento != null ? financiamiento.descripcion : ""));

    }
}
