package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Favoritos;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.PostuladosUniversidades;
import com.dwmedios.uniconekt.model.Universidad;

public interface UniversidadDetalleViewController {
    void OnSuccess(Universidad mUniversidad);
    void Onfailed(String mensaje);
    void Onloading(boolean loading);
    void OnsuccesFavorito(Favoritos mFavoritos);
    void OnfailedFavorito(String mensaje);
    void OnsuccesCheck(Favoritos mFavoritos);
    void OnsuccesPostular(PostuladosUniversidades mPostuladosUniversidades);


    void postularUsuario(Persona mPersona);
    //void postularDetalle(Persona mPersona);
   // void postularDetalle2();
   // void postular2(Persona mPersona);
}
