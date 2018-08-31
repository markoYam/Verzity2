package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NotificacionStatus implements Parcelable{
    public static final String ID_PERSONA="idNotificacion";
    public static final String ID_DISPOSITIVO="idDispositivo";

    @SerializedName(ID_PERSONA)
    public  int id_notificacion;
    @SerializedName(ID_DISPOSITIVO)
    public  int id_dispositivo;

    public NotificacionStatus(Parcel in) {
        id_notificacion = in.readInt();
        id_dispositivo = in.readInt();
    }

    public static final Creator<NotificacionStatus> CREATOR = new Creator<NotificacionStatus>() {
        @Override
        public NotificacionStatus createFromParcel(Parcel in) {
            return new NotificacionStatus(in);
        }

        @Override
        public NotificacionStatus[] newArray(int size) {
            return new NotificacionStatus[size];
        }
    };

    public NotificacionStatus() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id_notificacion);
        parcel.writeInt(id_dispositivo);
    }
}
