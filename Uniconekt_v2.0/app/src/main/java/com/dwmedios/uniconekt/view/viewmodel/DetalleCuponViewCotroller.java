package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.CuponesCanjeados;

public interface DetalleCuponViewCotroller {
    void OnSuccess(CuponesCanjeados mCuponesCanjeados);

    void Onfailed(String mensaje);

    void Onloading(boolean loading);

    void LoadQr(CuponesCanjeados mCuponesCanjeados);
    void configureViewPager();
}
