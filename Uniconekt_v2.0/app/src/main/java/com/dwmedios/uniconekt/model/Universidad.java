package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by mYam on 17/04/2018.
 */
@DatabaseTable
public class Universidad implements Parcelable {
    public static final String ID = "idUniversidad";
    public static final String NOMBRE = "nbUniversidad";
    public static final String NOMBRE_REPRECENTANTE = "nbReprecentante";
    public static final String DESCRIPCION = "desUniversidad";
    public static final String SITIO = "desSitioWeb";
    public static final String TELEFONO = "desTelefono";
    public static final String CORREO = "desCorreo";
    public static final String LOGO = "pathLogo";
    public static final String FOTOS = "FotosUniversidades";
    public static final String DIRECCION = "Direcciones";
    public static final String ID_DIRECCION = "idDireccion";
    public static final String VENTAS = "VentasPaquetes";
    public static final String LICENCIATURAS = "Licenciaturas";
    public static final String PERSONAS = "Personas";
    public static final String FOLLETOS = "urlFolletosDigitales";
    public static final String FACEBOOK = "urlFaceBook";
    public static final String INSTAGRAM = "urlInstagram";
    public static final String TWITTER = "urlTwitter";


    @SerializedName(ID)
    @DatabaseField(columnName = ID, id = true, index = true)
    public int id;

    @SerializedName(NOMBRE)
    @DatabaseField(columnName = NOMBRE)
    public String nombre;

    @SerializedName(NOMBRE_REPRECENTANTE)
    @DatabaseField(columnName = NOMBRE_REPRECENTANTE)
    public String reprecentante;

    @SerializedName(DESCRIPCION)
    @DatabaseField(columnName = DESCRIPCION)
    public String descripcion;

    @SerializedName(SITIO)
    @DatabaseField(columnName = SITIO)
    public String sitio;

    @SerializedName(TELEFONO)
    @DatabaseField(columnName = TELEFONO)
    public String telefono;

    @SerializedName(CORREO)
    @DatabaseField(columnName = CORREO)
    public String correo;

    @SerializedName(FOTOS)
    public List<FotosUniversidades> mFotosUniversidades;

    @SerializedName(LOGO)
    @DatabaseField(columnName = LOGO)
    public String logo;

    @SerializedName(DIRECCION)
    public Direccion mDireccion;

    @SerializedName(FOLLETOS)
    @DatabaseField(columnName = FOLLETOS)
    public String folleto;

    @SerializedName(FACEBOOK)
    @DatabaseField(columnName = FACEBOOK)
    public String facebook;

    @SerializedName(INSTAGRAM)
    @DatabaseField(columnName = INSTAGRAM)
    public String instagram;

    @SerializedName(TWITTER)
    @DatabaseField(columnName = TWITTER)
    public String twitter;

    @SerializedName(PERSONAS)
    public Persona mPersona;

    @SerializedName(VENTAS)
    public List<VentasPaquetes> mVentasPaquetesList;

    @SerializedName(ID_DIRECCION)
    @DatabaseField(columnName = ID_DIRECCION)
    public int id_direccion;

    @SerializedName(LICENCIATURAS)
    public List<Licenciaturas> mLicenciaturasList;


    protected Universidad(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        reprecentante = in.readString();
        descripcion = in.readString();
        sitio = in.readString();
        telefono = in.readString();
        correo = in.readString();
        mFotosUniversidades = in.createTypedArrayList(FotosUniversidades.CREATOR);
        logo = in.readString();
        mDireccion = in.readParcelable(Direccion.class.getClassLoader());
        folleto = in.readString();
        facebook = in.readString();
        instagram = in.readString();
        twitter = in.readString();
        mPersona = in.readParcelable(Persona.class.getClassLoader());
        mVentasPaquetesList = in.createTypedArrayList(VentasPaquetes.CREATOR);
        id_direccion = in.readInt();
        mLicenciaturasList = in.createTypedArrayList(Licenciaturas.CREATOR);
    }

    public Universidad() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(reprecentante);
        dest.writeString(descripcion);
        dest.writeString(sitio);
        dest.writeString(telefono);
        dest.writeString(correo);
        dest.writeTypedList(mFotosUniversidades);
        dest.writeString(logo);
        dest.writeParcelable(mDireccion, flags);
        dest.writeString(folleto);
        dest.writeString(facebook);
        dest.writeString(instagram);
        dest.writeString(twitter);
        dest.writeParcelable(mPersona, flags);
        dest.writeTypedList(mVentasPaquetesList);
        dest.writeInt(id_direccion);
        dest.writeTypedList(mLicenciaturasList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Universidad> CREATOR = new Creator<Universidad>() {
        @Override
        public Universidad createFromParcel(Parcel in) {
            return new Universidad(in);
        }

        @Override
        public Universidad[] newArray(int size) {
            return new Universidad[size];
        }
    };
}
