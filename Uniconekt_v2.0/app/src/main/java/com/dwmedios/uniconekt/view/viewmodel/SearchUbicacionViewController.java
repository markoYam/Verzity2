package com.dwmedios.uniconekt.view.viewmodel;

import com.google.android.gms.maps.model.LatLng;

public interface SearchUbicacionViewController {
     void updateCameraLocation(LatLng mLatLng);
     void search();
}
