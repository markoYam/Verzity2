package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by mYam on 16/04/2018.
 */
@DatabaseTable
public class Persona implements Parcelable {
    public static final String ID = "idPersona";
    public static final String NOMBRE = "nbCompleto";
    public static final String TELEFONO = "desTelefono";
    public static final String CORREO = "desCorreo";
    public static final String DIRECCIONES = "Direcciones";
    public static final String DISPOSITIVOS = "Dispositivos";
    public static final String UNIVERSIDADES = "Universidades";
    public static final String FOTO = "pathFoto";
    public static final String ID_DIRECCION = "idDireccion";
    public static final String TIPO_PERSONA = "CatTiposPersonas";

    public static final String PAQUETE_ASESOR = "VentasPaquetesAsesores";
    public static final String SKYPE = "desSkype";

    @SerializedName(ID)
    @DatabaseField(columnName = ID, id = true, index = true)
    public int id;

    @SerializedName(ID_DIRECCION)
    @DatabaseField(columnName = ID_DIRECCION)
    public int id_direccion;

    @SerializedName(NOMBRE)
    @DatabaseField(columnName = NOMBRE)
    public String nombre;

    @SerializedName(TELEFONO)
    @DatabaseField(columnName = TELEFONO)
    public String telefono;

    @SerializedName(CORREO)
    @DatabaseField(columnName = CORREO)
    public String correo;

    @SerializedName(DIRECCIONES)
    public Direccion direccion;

    @SerializedName(FOTO)
    @DatabaseField(columnName = FOTO)
    public String foto;

    @SerializedName(DISPOSITIVOS)
    public List<Dispositivo> dispositivosList;

    @SerializedName(UNIVERSIDADES)
    public List<Universidad> universidad;

    @SerializedName(TIPO_PERSONA)
    public TipoPersonas mTipoPersonas;

    @SerializedName(PAQUETE_ASESOR)
    public List<VentaPaqueteAsesor> mVentaPaqueteAsesors;

    @SerializedName(SKYPE)
    public String skype;


    protected Persona(Parcel in) {
        id = in.readInt();
        id_direccion = in.readInt();
        nombre = in.readString();
        telefono = in.readString();
        correo = in.readString();
        direccion = in.readParcelable(Direccion.class.getClassLoader());
        foto = in.readString();
        dispositivosList = in.createTypedArrayList(Dispositivo.CREATOR);
        universidad = in.createTypedArrayList(Universidad.CREATOR);
        mTipoPersonas = in.readParcelable(TipoPersonas.class.getClassLoader());
        mVentaPaqueteAsesors = in.createTypedArrayList(VentaPaqueteAsesor.CREATOR);
        skype = in.readString();
    }

    public Persona() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(id_direccion);
        dest.writeString(nombre);
        dest.writeString(telefono);
        dest.writeString(correo);
        dest.writeParcelable(direccion, flags);
        dest.writeString(foto);
        dest.writeTypedList(dispositivosList);
        dest.writeTypedList(universidad);
        dest.writeParcelable(mTipoPersonas, flags);
        dest.writeTypedList(mVentaPaqueteAsesors);
        dest.writeString(skype);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Persona> CREATOR = new Creator<Persona>() {
        @Override
        public Persona createFromParcel(Parcel in) {
            return new Persona(in);
        }

        @Override
        public Persona[] newArray(int size) {
            return new Persona[size];
        }
    };
}
