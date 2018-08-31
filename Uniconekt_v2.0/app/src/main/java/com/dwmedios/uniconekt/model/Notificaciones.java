package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Notificaciones implements Parcelable {
    public static final String ID = "idNotificacion";
    public static final String FECHA = "feRegistro";
    public static final String ID_PERSONA_ENVIA = "idPersonaEnvia";
    public static final String ID_PERSONA_RECIBE = "idPersonaRecibe";
    public static final String DISCRIMINADOR = "cvDiscriminador";
    public static final String ID_DISCRIMINADOR = "idDiscriminador";
    public static final String ASUNTO = "desAsunto";
    public static final String MENSAJE = "desMensaje";
    public static final String ESTATUS_NOT = "NotificacionEstatus";
    @SerializedName(ID)
    public int id;
    @SerializedName(FECHA)
    public Date fecha;
    @SerializedName(ID_PERSONA_ENVIA)
    public int id_persona_envia;
    @SerializedName(ID_PERSONA_RECIBE)
    public int id_persona_recibe;
    @SerializedName(DISCRIMINADOR)
    public String discriminador;
    @SerializedName(ID_DISCRIMINADOR)
    public int id_discriminador;
    @SerializedName(ASUNTO)
    public String asunto;

    @SerializedName(MENSAJE)
    public String mensaje;

    @SerializedName(ESTATUS_NOT)
    public List<NotificacionEstatus> mNotificacionEstatusList;

    public int filtro;


    protected Notificaciones(Parcel in) {
        id = in.readInt();
        fecha = (java.util.Date) in.readSerializable();
        id_persona_envia = in.readInt();
        id_persona_recibe = in.readInt();
        discriminador = in.readString();
        id_discriminador = in.readInt();
        asunto = in.readString();
        mensaje = in.readString();
        mNotificacionEstatusList = in.createTypedArrayList(NotificacionEstatus.CREATOR);
        filtro = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeSerializable(fecha);
        dest.writeInt(id_persona_envia);
        dest.writeInt(id_persona_recibe);
        dest.writeString(discriminador);
        dest.writeInt(id_discriminador);
        dest.writeString(asunto);
        dest.writeString(mensaje);
        dest.writeTypedList(mNotificacionEstatusList);
        dest.writeInt(filtro);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notificaciones> CREATOR = new Creator<Notificaciones>() {
        @Override
        public Notificaciones createFromParcel(Parcel in) {
            return new Notificaciones(in);
        }

        @Override
        public Notificaciones[] newArray(int size) {
            return new Notificaciones[size];
        }
    };
}
