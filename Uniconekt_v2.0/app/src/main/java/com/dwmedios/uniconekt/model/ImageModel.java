package com.dwmedios.uniconekt.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageModel implements Parcelable {
    public String patch;
    public Bitmap mBitmap;
    public byte[] imageData;

    public ImageModel() {
    }

    protected ImageModel(Parcel in) {
        patch = in.readString();
        mBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        imageData = in.createByteArray();
    }

    public static final Creator<ImageModel> CREATOR = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel in) {
            return new ImageModel(in);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(patch);
        dest.writeParcelable(mBitmap, flags);
        dest.writeByteArray(imageData);
    }
}
