package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.GuardarRespuesta;
import com.dwmedios.uniconekt.model.cuestionarios.Cuestionario;
import com.dwmedios.uniconekt.model.cuestionarios.Respuestas;

public interface PresentarCuestionarioViewController {

    void OnsuccesGetCuestionario(Cuestionario mCuestionario);

    void Onfailed(String mensaje);

    void OnLoading(boolean isLoading);

    void GuardarRespuesta(GuardarRespuesta mGuardarRespuesta, Respuestas mRespuestas);

    void OnsuccesGuardarRespuesta(Respuestas mRespuestas);

    void getResultado(Cuestionario mCuestionario);
}
