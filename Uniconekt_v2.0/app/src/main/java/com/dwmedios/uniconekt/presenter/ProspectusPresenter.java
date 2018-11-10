package com.dwmedios.uniconekt.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.model.Paquetes;
import com.dwmedios.uniconekt.model.Universidad;
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
    public void crearMenu(Universidad mUniversidad) {
        if (mUniversidad.mVentasPaquetesList != null && mUniversidad.mVentasPaquetesList.size() > 0) {
            Paquetes mPaquetes = mUniversidad.mVentasPaquetesList.get(0).mPaquetes;
            if (mPaquetes != null) {
                List<ClasViewModel.menu> menuList = new ArrayList<>();
                if (mPaquetes.aplica_video_1 || mPaquetes.aplica_video_2) {
                    ClasViewModel.menu videos = new ClasViewModel.menu();
                    videos.color = mContext.getString(R.color.colorrojo);
                    videos.drawableImage = R.drawable.ic_video_24;
                    videos.nombre = "Videos";
                    videos.tipo = ClasViewModel.tipoMenu.videos;
                    menuList.add(videos);
                }

                ClasViewModel.menu prospectus = new ClasViewModel.menu();
                prospectus.color = mContext.getString(R.color.Color_buscarUniversidad);
                prospectus.drawableImage = R.drawable.ic_folleto_24;
                prospectus.nombre = "Folletos digitales";
                prospectus.tipo = ClasViewModel.tipoMenu.folletos;

                menuList.add(prospectus);

                mProspectusViewController.Onsucces(menuList);
            } else
                mProspectusViewController.OnFailed("");
        } else
            mProspectusViewController.OnFailed("");
    }
}
