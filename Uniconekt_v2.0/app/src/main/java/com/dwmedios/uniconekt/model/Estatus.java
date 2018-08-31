package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Estatus implements Parcelable {
    public static final String desNot = "desEstatus";
    @SerializedName(desNot)
    public String estatusNot;

    protected Estatus(Parcel in) {
        estatusNot = in.readString();
    }

    public static final Creator<Estatus> CREATOR = new Creator<Estatus>() {
        @Override
        public Estatus createFromParcel(Parcel in) {
            return new Estatus(in);
        }

        @Override
        public Estatus[] newArray(int size) {
            return new Estatus[size];
        }
    };

    public Estatus() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(estatusNot);
    }
}
