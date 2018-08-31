package com.dwmedios.uniconekt.view.fragments.ViewPager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.FotosUniversidades;
import com.dwmedios.uniconekt.view.fragments.base.BaseFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.data.service.ClientService.URL_MULTIMEDIA;
import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderDark;
import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderItems;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;


public class FotosViewerFragment extends BaseFragment {

    public static String KEY_FOTO = "foto_universiedad";
    public FotosUniversidades mFotosUniversidades;
    @BindView(R.id.imageviewFoto)
    ImageView mImageView;

    public FotosViewerFragment() {
    }

    public static FotosViewerFragment NuevaInstancia(FotosUniversidades mFotosUniversidades) {
        FotosViewerFragment mFotosViewerFragment = new FotosViewerFragment();
        Bundle mBundle = new Bundle();
        mFotosViewerFragment.mFotosUniversidades = mFotosUniversidades;
        mBundle.putParcelable(KEY_FOTO, mFotosUniversidades);
        mFotosViewerFragment.setArguments(mBundle);
        return mFotosViewerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View master= inflater.inflate(R.layout.fragment_fotos_viewer, container, false);
        ButterKnife.bind(this,master);
        return master;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFotosUniversidades != null && mFotosUniversidades.rutaFoto != null) {
            ImageLoader.getInstance().displayImage(getUrlImage(mFotosUniversidades.rutaFoto,getContext()), mImageView, OptionsImageLoaderItems);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFotosUniversidades = null;
    }
}
