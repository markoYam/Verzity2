package com.dwmedios.uniconekt.view.adapter.AdapterViewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dwmedios.uniconekt.model.FotosUniversidades;
import com.dwmedios.uniconekt.view.fragments.ViewPager.FotosViewerFragment;

import java.security.PublicKey;
import java.util.List;

public class UniversidadesAdapterViewPager extends FragmentStatePagerAdapter {
    public List<FotosUniversidades> mFotosUniversidades;

    public UniversidadesAdapterViewPager(FragmentManager fm, List<FotosUniversidades> mFotosUniversidades) {
        super(fm);
        this.mFotosUniversidades = mFotosUniversidades;
    }

    @Override
    public Fragment getItem(int position) {
        return FotosViewerFragment.NuevaInstancia(mFotosUniversidades.get(position));
    }

    @Override
    public int getCount() {
        return (mFotosUniversidades != null && mFotosUniversidades.size() > 0 ? mFotosUniversidades.size() : 0);
    }
}
