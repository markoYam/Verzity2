package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Universidad;

public interface DatosUniversidadViewController {
     void ConfigureLoad();
     void LoadInfofromData();
     Universidad getInfoFromview();
     void Onsucces(Universidad mUniversidad);
     void Onfailed(String mensaje);
     void Onloading(boolean loading);
}
