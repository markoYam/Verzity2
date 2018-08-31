package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Usuario;

public interface ResetPasswordViewController {

    void Succes(Usuario mUsuario);
    void Failed(String mensaje);
    void Loading(boolean loading);
}
