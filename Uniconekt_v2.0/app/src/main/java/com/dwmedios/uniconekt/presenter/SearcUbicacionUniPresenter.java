package com.dwmedios.uniconekt.presenter;

import com.dwmedios.uniconekt.view.viewmodel.SearchUbicacionViewController;
import com.google.android.gms.maps.model.LatLng;

public class SearcUbicacionUniPresenter {
    private SearchUbicacionViewController mSearchUbicacionViewController;

    public SearcUbicacionUniPresenter(SearchUbicacionViewController mSearchUbicacionViewController) {
        this.mSearchUbicacionViewController = mSearchUbicacionViewController;
    }

    public void animateCamera(LatLng mLatLng) {
        mSearchUbicacionViewController.updateCameraLocation(mLatLng);
    }
    public void search(){
        mSearchUbicacionViewController.search();
    }
}
