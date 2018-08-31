package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NotificacionEstatus implements Parcelable {
    public static final String estatus = "Estatus";
    @SerializedName(estatus)
    public Estatus mEstatus;

    protected NotificacionEstatus(Parcel in) {
        mEstatus = in.readParcelable(Estatus.class.getClassLoader());
    }

    public static final Creator<NotificacionEstatus> CREATOR = new Creator<NotificacionEstatus>() {
        @Override
        public NotificacionEstatus createFromParcel(Parcel in) {
            return new NotificacionEstatus(in);
        }

        @Override
        public NotificacionEstatus[] newArray(int size) {
            return new NotificacionEstatus[size];
        }
    };

    public NotificacionEstatus() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mEstatus, flags);
    }
}
