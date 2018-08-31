package com.dwmedios.uniconekt.view.util;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.model.Configuraciones;

public class Paypal {

    public static String getTokenPaypal(Context mContext){
        AllController mAllController= new AllController(mContext);
       Configuraciones mConfiguraciones= mAllController.getConfiguraciones();
       return (mConfiguraciones!=null?mConfiguraciones.paypal:null);
    }
}
