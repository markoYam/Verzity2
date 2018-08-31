package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Financiamientos;

import java.util.List;

public interface FinanciamientoViewController extends base {

    void OnSucces(List<Financiamientos> mFinanciamientosList,int first);
}
