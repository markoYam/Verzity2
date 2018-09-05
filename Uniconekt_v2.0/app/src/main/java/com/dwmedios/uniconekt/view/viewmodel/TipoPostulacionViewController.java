package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.TipoPostulacion;

import java.util.List;

public interface TipoPostulacionViewController {
    void Onsuccess(List<TipoPostulacion> mPostulacionList);

    void Onfailed(String mensaje);

    void OnLoading(boolean isloading);
}
