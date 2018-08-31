package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by mYam on 16/04/2018.
 */
@DatabaseTable
public class Usuario implements Parcelable {
    public static final String ID="idUsuario";
    public static final String NOMBRE="nbUsuario";
    public static final String CONTRASEÑA="pwdContrasenia";
    public static final String PERSONAS="Personas";
    public static final String CVFACEBOOK="cvFacebook";

    @SerializedName(ID)
    @DatabaseField(columnName = ID,id = true, index = true)
    public int id;

    @SerializedName(NOMBRE)
    @DatabaseField(columnName = NOMBRE)
    public String nombre;

    @SerializedName(CONTRASEÑA)
    @DatabaseField(columnName = CONTRASEÑA)
    public String contrasenia;

    @SerializedName(CVFACEBOOK)
    @DatabaseField(columnName = CVFACEBOOK)
    public String cv_facebook;

    @SerializedName(PERSONAS)
    public Persona persona;

    protected Usuario(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        contrasenia = in.readString();
        cv_facebook = in.readString();
        persona = in.readParcelable(Persona.class.getClassLoader());
    }

    public Usuario() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(contrasenia);
        dest.writeString(cv_facebook);
        dest.writeParcelable(persona, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
}
