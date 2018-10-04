package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.GuardarRespuesta;
import com.dwmedios.uniconekt.model.cuestionarios.Cuestionario;
import com.dwmedios.uniconekt.model.cuestionarios.DetalleEvaluacionViewModel;
import com.dwmedios.uniconekt.model.cuestionarios.Respuestas;
import com.dwmedios.uniconekt.model.cuestionarios.RespuestasPersona;

public interface PresentarCuestionarioViewController {

    void OnsuccesGetCuestionario(DetalleEvaluacionViewModel detalleEvaluacionViewModel);

    void Onfailed(String mensaje);

    void OnLoading(boolean isLoading);

    void GuardarRespuesta(GuardarRespuesta mGuardarRespuesta, Respuestas mRespuestas);

    void OnsuccesGuardarRespuesta(RespuestasPersona mRespuestas);

    void OnfailedRespuesta(String mensaje);
    void closeCuestionario(String mensaje);

    void getResultado(Cuestionario mCuestionario);
}
