package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Licenciaturas;

import java.util.List;

public interface LicenciaturaViewController {
    void Onsucces(List<Licenciaturas> mLicenciaturasList);
    void Onfailed(String mensaje);
    void Onloading(boolean loading);
}
