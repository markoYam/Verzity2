package com.dwmedios.uniconekt.view.viewmodel;

import com.dwmedios.uniconekt.model.Videos;

import java.util.List;

public interface VideoViewController {
    void Onsucces(List<Videos> videosList, int i);
    void Onfailed(String mensaje);
    void Onloading(boolean loading);
    void EmptyRecyclerView();

    void configureView();
}
