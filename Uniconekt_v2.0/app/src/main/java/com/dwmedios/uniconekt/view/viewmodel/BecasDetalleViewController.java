package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Becas;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.PostuladosBecas;

public interface BecasDetalleViewController extends base{
    void configureLoad();
    void onSucces(Becas mBecas);
    void PostularDetalle();
    void postular(Persona mPersona);
    void OnsuccesPostular(PostuladosBecas mPostuladosBecas);
}
