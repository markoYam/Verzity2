package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Persona;

import java.util.List;

public interface AsesoresViewController {
    void Onsucces(List<Persona> mPersonaList);

    void Onfailed(String mensaje);

    void OnLoading(boolean isLoading);
}
