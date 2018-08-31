package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Cupones;
import com.dwmedios.uniconekt.model.Paquetes;
import com.dwmedios.uniconekt.model.VentasPaquetes;

import java.util.List;

public interface PaquetesViewController extends base {
    void OnSucces(List<Paquetes> mPaquetesList);
    void OnSuccesPaquete(VentasPaquetes mVentasPaquetes);
}
