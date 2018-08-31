package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Banners;
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.VisitasBanners;

import java.util.List;

public interface SearchUniversidadViewController {

    void OnSuccesSearch(List<Universidad> mUniversidadList);
    void OnSuccesMenu(List<ClasViewModel.menu> menuList);
    void OnFailedUniversidad(String mensaje);
    void EmpyRecycler();
    void Onfailed(String mensaje);
    void OnLoading(boolean loading);
    void SearchName();

    void OnsuccesBanners(List<Banners> mBannersList);
    void OnfailedBanners(String mensaje);

    void ViewBanner(Banners mBanners);

    void SuccesVisitedBanner(VisitasBanners mBanners);
    void FailedVisitedBanner(String mensaje);
}
