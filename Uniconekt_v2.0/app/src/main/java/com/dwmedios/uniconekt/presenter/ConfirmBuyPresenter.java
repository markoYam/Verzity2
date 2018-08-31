package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.model.Direccion;
import com.dwmedios.uniconekt.model.Universidad;

public class ConfirmBuyPresenter {
    private Context mContext;
    private AllController mAllController;

    public ConfirmBuyPresenter(Context mContext) {
        this.mContext = mContext;
        mAllController = new AllController(mContext);
    }

    public Universidad getUniversidad() {
        Universidad mUniversidad = mAllController.getuniversidadPersona();
        if (mUniversidad != null) {
            Direccion mDireccion = mAllController.getDireccionUniversidad(mUniversidad.id_direccion);
            if (mDireccion != null) mUniversidad.mDireccion = mDireccion;
        }
        return mUniversidad;
    }
}
