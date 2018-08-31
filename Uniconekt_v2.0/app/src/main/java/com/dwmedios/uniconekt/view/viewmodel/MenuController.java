package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.Usuario;

import java.util.List;

public interface MenuController {
    void OnSucces(List<ClasViewModel.menu> menuList);

    void OnFailed(String mensaje);

    void empyRecycler();

    void OnLoading(boolean loading);

    void OnLoadHeaders(Persona mPersona, Usuario mUsuario);

    void updateNumMensajes(int count);

    void configureCabeceras();

    void succesCerrarSession();
}
