package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.CodigoPostal;
import com.dwmedios.uniconekt.model.Paises;
import com.dwmedios.uniconekt.model.Persona;

import java.util.List;

public interface DatosUsuarioViewController extends base {

    void OnsuccesCodigo(List<CodigoPostal> mCodigoPostalList);

    void onfailesCodigo();
    void OnsuccesPais(List<Paises> mPaisesList);

    void OnchageVisivility(boolean visibility);

    void returnData(Persona mPersona);

    void OnsuccesEditar(Persona mPersona);

    void OnsuccesActualizar(Persona mPersona);

    void OnsuccesVerificar(Persona mPersona);

    void onfailedVerficar(String mensaje);

    void onfailedActualizar(String mensaje);
}
