package com.dwmedios.uniconekt.presenter;

import com.dwmedios.uniconekt.model.TipoPostulacion;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.view.viewmodel.TipoPostulacionViewController;

import java.util.ArrayList;
import java.util.List;

public class TipoPostulacionPresenter {
    public TipoPostulacionViewController mTipoPostulacionViewController;

    public TipoPostulacionPresenter(TipoPostulacionViewController mTipoPostulacionViewController) {
        this.mTipoPostulacionViewController = mTipoPostulacionViewController;
    }

    public void getTipoPostulacion() {
        List<TipoPostulacion> mPostulacionList = new ArrayList<>();

        TipoPostulacion universidad = new TipoPostulacion();
        universidad.nombre = "Universidad";
        universidad.tipo = 1;
        mPostulacionList.add(universidad);

        TipoPostulacion beca = new TipoPostulacion();
        beca.nombre = "Becas";
        beca.tipo = 2;
        mPostulacionList.add(beca);

        TipoPostulacion fina = new TipoPostulacion();
        fina.nombre = "Financiamientos";
        fina.tipo = 3;
        mPostulacionList.add(fina);
        mTipoPostulacionViewController.Onsuccess(mPostulacionList);
    }
}
