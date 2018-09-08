package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Notificaciones;

import java.util.List;

public interface NotificacionUniversitarioViewController {
    void Onsucces(List<Notificaciones> mNotificacionesList);

    void OnFailed(String mensaje);

    void OnLoading(boolean isLoading);

}
