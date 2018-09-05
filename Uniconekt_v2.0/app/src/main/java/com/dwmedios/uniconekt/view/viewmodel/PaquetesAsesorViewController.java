package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.PaqueteAsesor;
import com.dwmedios.uniconekt.model.VentaPaqueteAsesor;

import java.util.List;

public interface PaquetesAsesorViewController {
    void Onsucces(List<PaqueteAsesor> mPaqueteAsesorList);

    void Onfailed(String mensaje);

    void OnLoading(boolean isLoading);

    void onfailedVenta(String mensaje);

    void OnsuccesVenta(VentaPaqueteAsesor mVentaPaqueteAsesor);
}
