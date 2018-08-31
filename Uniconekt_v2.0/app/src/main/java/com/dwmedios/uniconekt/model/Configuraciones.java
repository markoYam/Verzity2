package com.dwmedios.uniconekt.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Configuraciones implements Parcelable {
    public static final String ID = "id_Configuraciones";
    public static final String WS = "desRutaWebServices";
    public static final String MULTIMEDIA = "desRutaMultimedia";

    public static final String PAYPAL = "cvPaypal";

    public static final String RUTA_FTP = "desRutaFTP";
    public static final String USUARIO = "nbUsuarioFTP";
    public static final String PASS = "pdwContraseniaFTP";
    public static final String ROOT = "desCarpetaMultimediaFTP";

    @DatabaseField(id = true, columnName = ID, index = true)
    @SerializedName(ID)
    public int id;
    @SerializedName(WS)
    @DatabaseField(columnName = WS)
    public String webService;
    @DatabaseField(columnName = MULTIMEDIA)
    @SerializedName(MULTIMEDIA)
    public String multimedia;

    @SerializedName(PAYPAL)
    @DatabaseField(columnName = PAYPAL)
    public String paypal;

    @SerializedName(RUTA_FTP)
    @DatabaseField(columnName = RUTA_FTP)
    public String ruta_ftp;

    @SerializedName(USUARIO)
    @DatabaseField(columnName = USUARIO)
    public String usuario;

    @SerializedName(PASS)
    @DatabaseField(columnName = PASS)
    public String password;

    @SerializedName(ROOT)
    @DatabaseField(columnName = ROOT)
    public String root_folder;


    protected Configuraciones(Parcel in) {
        id = in.readInt();
        webService = in.readString();
        multimedia = in.readString();
        paypal = in.readString();
        ruta_ftp = in.readString();
        usuario = in.readString();
        password = in.readString();
        root_folder = in.readString();
    }

    public Configuraciones() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(webService);
        dest.writeString(multimedia);
        dest.writeString(paypal);
        dest.writeString(ruta_ftp);
        dest.writeString(usuario);
        dest.writeString(password);
        dest.writeString(root_folder);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Configuraciones> CREATOR = new Creator<Configuraciones>() {
        @Override
        public Configuraciones createFromParcel(Parcel in) {
            return new Configuraciones(in);
        }

        @Override
        public Configuraciones[] newArray(int size) {
            return new Configuraciones[size];
        }
    };
}
