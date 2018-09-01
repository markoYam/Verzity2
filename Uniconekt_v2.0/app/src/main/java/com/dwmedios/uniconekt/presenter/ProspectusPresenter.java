package com.dwmedios.uniconekt.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.view.viewmodel.ProspectusViewController;

import java.util.ArrayList;
import java.util.List;

public class ProspectusPresenter {
    ProspectusViewController mProspectusViewController;
    private Context mContext;

    public ProspectusPresenter(ProspectusViewController mProspectusViewController, Context mContext) {
        this.mProspectusViewController = mProspectusViewController;
        this.mContext = mContext;
    }

    @SuppressLint("ResourceType")
    public void crearMenu() {

        List<ClasViewModel.menu> menuList = new ArrayList<>();

        ClasViewModel.menu videos = new ClasViewModel.menu();
        videos.color = mContext.getString(R.color.colorrojo);
        videos.drawableImage = R.drawable.ic_action_video;
        videos.nombre = "Videos";
        videos.tipo = ClasViewModel.tipoMenu.videos;

        menuList.add(videos);

        ClasViewModel.menu prospectus = new ClasViewModel.menu();
        prospectus.color = mContext.getString(R.color.Color_buscarUniversidad);
        prospectus.drawableImage = R.drawable.ic_action_folleto2;
        prospectus.nombre = "Folletos digitales";
        prospectus.tipo = ClasViewModel.tipoMenu.folletos;

        menuList.add(prospectus);

        mProspectusViewController.Onsucces(menuList);
    }
}
