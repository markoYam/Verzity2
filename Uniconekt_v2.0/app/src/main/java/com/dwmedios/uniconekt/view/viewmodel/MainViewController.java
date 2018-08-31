package com.dwmedios.uniconekt.view.viewmodel;



import com.dwmedios.uniconekt.model.Item;
import com.dwmedios.uniconekt.model.TipoCategoria;

import java.util.List;

/**
 * Created by Luis Cabanas on 19/03/2018.
 */

public interface MainViewController {

    void successTiposCategoriasWS(List<TipoCategoria> data);
    void emptyTiposCategoriasWS();
    void failureLoadTiposCategoriasWS(String message);
    void showProgress(boolean visible);
    void getItems(List<Item> data);
}
