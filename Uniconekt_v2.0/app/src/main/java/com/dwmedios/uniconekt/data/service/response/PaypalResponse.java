package com.dwmedios.uniconekt.data.service.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PaypalResponse implements Parcelable{
    public static final String RESPONSE = "response";
    @SerializedName(RESPONSE)
    public paypalObject response;

    public class paypalObject {
       // public static final String ID = "id";
        public static final String ESTATUS = "state";
        public static final String REFERENCIA = "id";
        public static final String key = "approved";
       // @SerializedName(ID)
       // public String id;
        @SerializedName(ESTATUS)
        public String status;
        @SerializedName(REFERENCIA)
        public String referencia;

        public boolean validPay() {
            return status.equals(key);
        }
    }

    protected PaypalResponse(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PaypalResponse> CREATOR = new Creator<PaypalResponse>() {
        @Override
        public PaypalResponse createFromParcel(Parcel in) {
            return new PaypalResponse(in);
        }

        @Override
        public PaypalResponse[] newArray(int size) {
            return new PaypalResponse[size];
        }
    };
}
