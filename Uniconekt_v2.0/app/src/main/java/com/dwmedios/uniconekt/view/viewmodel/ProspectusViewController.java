package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.ClasViewModel;

import java.util.List;

public interface ProspectusViewController {
    void Onsucces(List<ClasViewModel.menu> menuList);

    void OnFailed(String mensaje);
}
