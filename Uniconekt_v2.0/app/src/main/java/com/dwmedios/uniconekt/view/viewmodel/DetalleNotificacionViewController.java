package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.PostuladosGeneral;

public interface DetalleNotificacionViewController {
    void Onsucces(PostuladosGeneral mPersona);
    void Onfailed(String mensaje);
    void Onloading(Boolean loading);
    void SendMail();
    void call();

}
