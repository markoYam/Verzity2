package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchUniversidades implements Parcelable{

    public static final String NOMREUNIVERSIDAD = "nombreUniversidad";
    public static final String LICENCIATURAS = "licenciaturas";
    public static final String ESTADO = "nombreEstado";
    public static final String EXTRANJERO = "extranjero";
    public static final String PAIS = "nbPais";

    @SerializedName(NOMREUNIVERSIDAD)
    public String nombreUniversidad;

    @SerializedName(LICENCIATURAS)
    public List<Licenciaturas> licenciaturas;

    @SerializedName(ESTADO)
    public String estado;

    @SerializedName(EXTRANJERO)
    public Boolean extranjero;

   @SerializedName(PAIS)
    public String pais;

    protected SearchUniversidades(Parcel in) {
        nombreUniversidad = in.readString();
        licenciaturas = in.createTypedArrayList(Licenciaturas.CREATOR);
        estado = in.readString();
        byte tmpExtranjero = in.readByte();
        extranjero = tmpExtranjero == 0 ? null : tmpExtranjero == 1;
        pais = in.readString();
    }

    public SearchUniversidades() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombreUniversidad);
        dest.writeTypedList(licenciaturas);
        dest.writeString(estado);
        dest.writeByte((byte) (extranjero == null ? 0 : extranjero ? 1 : 2));
        dest.writeString(pais);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchUniversidades> CREATOR = new Creator<SearchUniversidades>() {
        @Override
        public SearchUniversidades createFromParcel(Parcel in) {
            return new SearchUniversidades(in);
        }

        @Override
        public SearchUniversidades[] newArray(int size) {
            return new SearchUniversidades[size];
        }
    };
}
