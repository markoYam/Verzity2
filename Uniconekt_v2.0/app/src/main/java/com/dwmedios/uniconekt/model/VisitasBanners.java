package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class VisitasBanners implements Parcelable {

    public static final String ID_BANNER="idBanner";
    public static final String ID_PERSONA="idPersona";

    @SerializedName(ID_BANNER)
    public int idBanner;
    @SerializedName(ID_PERSONA)
    public int idPersona;

    protected VisitasBanners(Parcel in) {
        idBanner = in.readInt();
        idPersona = in.readInt();
    }

    public VisitasBanners() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idBanner);
        dest.writeInt(idPersona);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VisitasBanners> CREATOR = new Creator<VisitasBanners>() {
        @Override
        public VisitasBanners createFromParcel(Parcel in) {
            return new VisitasBanners(in);
        }

        @Override
        public VisitasBanners[] newArray(int size) {
            return new VisitasBanners[size];
        }
    };
}
