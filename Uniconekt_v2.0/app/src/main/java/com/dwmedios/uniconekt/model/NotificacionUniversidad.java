package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificacionUniversidad implements Parcelable {
    public static final String NOT = "Notificaciones";
    public static final String SECCION = "NombreSeccion";
    @SerializedName(NOT)
    public List<Notificaciones> mNotificacionesList;
    @SerializedName(SECCION)
    public String nombreSeccion;

    public int filtro;


    protected NotificacionUniversidad(Parcel in) {
        mNotificacionesList = in.createTypedArrayList(Notificaciones.CREATOR);
        nombreSeccion = in.readString();
        filtro = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mNotificacionesList);
        dest.writeString(nombreSeccion);
        dest.writeInt(filtro);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificacionUniversidad> CREATOR = new Creator<NotificacionUniversidad>() {
        @Override
        public NotificacionUniversidad createFromParcel(Parcel in) {
            return new NotificacionUniversidad(in);
        }

        @Override
        public NotificacionUniversidad[] newArray(int size) {
            return new NotificacionUniversidad[size];
        }
    };
}
