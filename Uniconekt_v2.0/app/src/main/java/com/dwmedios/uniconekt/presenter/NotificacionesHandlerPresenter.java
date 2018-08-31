package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.model.Persona;

public class NotificacionesHandlerPresenter {
    private Context mContext;
    private AllController mAllController;

    public NotificacionesHandlerPresenter(Context mContext) {
        this.mContext = mContext;
        mAllController= new AllController(mContext);
    }

    public Persona getPersona()
    {
        return mAllController.getPersonaUsuario();
    }
}
