package com.dwmedios.uniconekt.view.viewmodel;

import java.util.List;

public interface CustomViewController {

    void OnSucces(List<?> mObjectsList);

    void OnSucces(Object mObjectsList);

    void OnSucces(List<?> mObjectsList, int tipo);

    void Onfailed(String mensaje);

    void Onfailed2(String mensaje);

    void OnLoading(boolean isLoading);
}
