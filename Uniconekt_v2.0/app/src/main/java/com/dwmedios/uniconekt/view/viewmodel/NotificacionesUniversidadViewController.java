package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.NotificacionUniversidad;

import java.util.List;

public interface NotificacionesUniversidadViewController {
    void Onsucces(List<NotificacionUniversidad> mNotificacionUniversidads);
    void Onloading(boolean isLoading);
    void Onfailed(String mensaje);
}
