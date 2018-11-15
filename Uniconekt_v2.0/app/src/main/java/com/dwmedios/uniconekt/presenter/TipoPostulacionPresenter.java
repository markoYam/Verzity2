package com.dwmedios.uniconekt.presenter;

import android.content.Context;

import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.model.Paquetes;
import com.dwmedios.uniconekt.model.TipoPostulacion;
import com.dwmedios.uniconekt.model.VentasPaquetes;
import com.dwmedios.uniconekt.view.viewmodel.TipoPostulacionViewController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TipoPostulacionPresenter {
    public TipoPostulacionViewController mTipoPostulacionViewController;
    private AllController mAllController;
    private Context mContext;

    public TipoPostulacionPresenter(TipoPostulacionViewController mTipoPostulacionViewController, Context mContext) {
        this.mTipoPostulacionViewController = mTipoPostulacionViewController;
        this.mContext = mContext;
        mAllController = new AllController(this.mContext);
    }


    public void getTipoPostulacion() {
        List<TipoPostulacion> mPostulacionList = new ArrayList<>();
        Paquetes mPaquetes = getPaquetes();
        if (mPaquetes != null) {
            if (mPaquetes.aplica_Beca) {
                TipoPostulacion beca = new TipoPostulacion();
                beca.nombre = "Becas";
                beca.tipo = 2;
                mPostulacionList.add(beca);
            }

            if (mPaquetes.aplica_Financiamiento) {
                TipoPostulacion fina = new TipoPostulacion();
                fina.nombre = "Financiamientos";
                fina.tipo = 3;
                mPostulacionList.add(fina);
            }

            if (mPaquetes.aplica_Postulacion) {
                TipoPostulacion universidad = new TipoPostulacion();
                universidad.nombre = "Universidad";
                universidad.tipo = 1;
                mPostulacionList.add(universidad);
            }

        }
        Collections.sort(mPostulacionList, new Comparator<TipoPostulacion>() {
            @Override
            public int compare(TipoPostulacion o1, TipoPostulacion o2) {
               return o1.nombre.compareToIgnoreCase(o2.nombre);
            }
        });
        mTipoPostulacionViewController.Onsuccess(mPostulacionList);
    }

    public Paquetes getPaquetes() {
        try {
            VentasPaquetes mVentasPaquetes = mAllController.getPaqueteUniversidad();
            return (mVentasPaquetes != null ? mVentasPaquetes.mPaquetes != null ? mVentasPaquetes.mPaquetes : null : null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
