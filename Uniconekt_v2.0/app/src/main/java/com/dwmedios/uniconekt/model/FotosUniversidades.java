package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FotosUniversidades implements Parcelable{
    public static final String ID = "idFotoUniversidad";
    public static final String ID_UNIVERSIDAD = "idUniversidad";
    public static final String RUTAFOTO = "desRutaFoto";
    public static final String PRINCIPAL = "fgPrincipal";

    @SerializedName(ID)
    public int id;
    @SerializedName(RUTAFOTO)
    public String rutaFoto;
    @SerializedName(PRINCIPAL)
    public boolean principal;

    protected FotosUniversidades(Parcel in) {
        id = in.readInt();
        rutaFoto = in.readString();
        principal = in.readByte() != 0;
    }

    public FotosUniversidades() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(rutaFoto);
        dest.writeByte((byte) (principal ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FotosUniversidades> CREATOR = new Creator<FotosUniversidades>() {
        @Override
        public FotosUniversidades createFromParcel(Parcel in) {
            return new FotosUniversidades(in);
        }

        @Override
        public FotosUniversidades[] newArray(int size) {
            return new FotosUniversidades[size];
        }
    };
}
