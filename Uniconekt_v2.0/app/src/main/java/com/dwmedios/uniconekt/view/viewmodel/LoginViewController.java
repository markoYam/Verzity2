package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Usuario;

/**
 * Created by mYam on 17/04/2018.
 */

public interface LoginViewController {

    void LoginSucces(Usuario mUsuario);

    void LoginFailed(String mensaje);

    void Loading(boolean loading);


}
