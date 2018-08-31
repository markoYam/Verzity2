package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Favoritos  implements Parcelable {
    public static final String ID = "idFavoritos";
    public static final String ID_PERSONA = "idPersona";
    public static final String ID_UNIVERSIDAD = "idUniversidad";

    @SerializedName(ID)
    public int id;
    @SerializedName(ID_PERSONA)
    public int id_persona;
    @SerializedName(ID_UNIVERSIDAD)
    public int id_universidad;

    protected Favoritos(Parcel in) {
        id = in.readInt();
        id_persona = in.readInt();
        id_universidad = in.readInt();
    }

    public Favoritos() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(id_persona);
        dest.writeInt(id_universidad);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Favoritos> CREATOR = new Creator<Favoritos>() {
        @Override
        public Favoritos createFromParcel(Parcel in) {
            return new Favoritos(in);
        }

        @Override
        public Favoritos[] newArray(int size) {
            return new Favoritos[size];
        }
    };
}
