package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Usuario;

public interface RegistroUniversitarioViewController {
    void OnsuccesRegistro(Usuario mUsuario);
    void OnFailedRegistro(String mensaje);
    void OnLoadingRegistro(boolean isLoading);

    void setTerminos(String contenido);
}
