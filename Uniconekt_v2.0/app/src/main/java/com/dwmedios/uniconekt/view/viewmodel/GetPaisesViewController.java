package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.CodigoPostal;
import com.dwmedios.uniconekt.model.Estados;
import com.dwmedios.uniconekt.model.Paises;

import java.util.List;

public interface GetPaisesViewController {
    void onSucces(List<Paises> mPaisesList);

    void onFailed(String mensaje);

    void onSuccesEstado(List<Estados> mPaisesList);

    void onFailedEstado(String mensaje);

    void OnLoading(boolean isLoading);

    void onSuccesCodigo(List<CodigoPostal> mCodigoPostals);

    void OnfailedCodigo(String mensaje);

    void emptyControls();
}
