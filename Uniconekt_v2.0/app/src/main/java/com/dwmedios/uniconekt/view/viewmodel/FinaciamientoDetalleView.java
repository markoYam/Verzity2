package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Financiamientos;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.PostuladosFinanciamientos;

public interface FinaciamientoDetalleView {

    void Onfailded(String mensaje);

    void ConfigureLoad();

    void Onsuccess(Financiamientos mFinanciamientos);

    void Onloading(boolean loading);

    void OnSuccesPostular(PostuladosFinanciamientos mPostuladosFinanciamientos);

    void Postular(Persona mPersona);
    void Postular();
}
