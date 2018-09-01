package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.NivelAcademico;

import java.util.List;

public interface NivelAcademicoViewController {

    void Onsucces(List<NivelAcademico> mNivelAcademicoList);

    void Onloading(boolean isLoading);

    void Onfailed(String mensaje);
}
