package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.model.Configuraciones;

public class ConfiguracionesPresenter {
    private AllController mAllController;
    private Context mContext;

    public ConfiguracionesPresenter(Context mContext) {
        this.mContext = mContext;
        mAllController= new AllController(this.mContext);
    }

    public Configuraciones getConfiguraciones()
    {
        return mAllController.getConfiguraciones();
    }
}
