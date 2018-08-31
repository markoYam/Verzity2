package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Becas;

import java.util.List;

public interface BecasViewController extends base {
    void Onsucces(List<Becas> mBecasList,int first);
    void ConfigureLoad();
}
