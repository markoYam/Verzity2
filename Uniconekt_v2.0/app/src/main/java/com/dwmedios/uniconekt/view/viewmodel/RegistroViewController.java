package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Item;
import com.dwmedios.uniconekt.model.TipoCategoria;
import com.dwmedios.uniconekt.model.Usuario;

import java.util.List;

/**
 * Created by mYam on 16/04/2018.
 */

public interface RegistroViewController {
    // TODO: 16/04/2018  esta clase sus metodos se implementan en los activity. como respuesta del presentador.
    void successRegister(Usuario data);

    void failureRegister(String message);

    void showProgress(boolean visible);

    void UserReturn(Usuario usuario);

}
