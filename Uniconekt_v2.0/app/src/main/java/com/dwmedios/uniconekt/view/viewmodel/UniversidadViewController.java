package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Banners;
import com.dwmedios.uniconekt.model.Universidad;

import java.util.List;

public interface UniversidadViewController {
    void Onloading(boolean loading);
    void EmpyRecycler();
    void OnSuccess(List<Universidad> mUniversidadList);
    void OnSuccessSeach(List<Universidad> mUniversidadList,int type);
    void Onfailed(String mensaje);

}
