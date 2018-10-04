package com.dwmedios.uniconekt.view.util;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerCarrusel {
    private int position = 0, mLayout, tiempo = 0;
    private Context mContext;
    private List<?> mObjectsList;
    public static ViewPagerCarrusel mViewPagerCarrusel;
    private listener mListener;
    private LayoutInflater mLayoutInflater;
    private ViewPager mViewPager;
    private adapterViewPager mAdapter;

    public ViewPagerCarrusel(Context mContext, int mLayout) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public static synchronized ViewPagerCarrusel getInstance(Context mContext, int mLayout) {
        if (mViewPagerCarrusel == null) {
            if (mViewPagerCarrusel == null) {
                mViewPagerCarrusel = new ViewPagerCarrusel(mContext, mLayout);
            }
        }
        return mViewPagerCarrusel;
    }

    public ViewPagerCarrusel setTiempo(int tiempo) {
        if (mViewPagerCarrusel == null) {
            mViewPagerCarrusel = new ViewPagerCarrusel(mContext, mLayout);
            mViewPagerCarrusel.tiempo = tiempo;
            this.tiempo = tiempo;
        } else {
            mViewPagerCarrusel.tiempo = tiempo;
            this.tiempo = tiempo;
        }
        return mViewPagerCarrusel;
    }

    public ViewPagerCarrusel setListener(listener mListener) {
        if (mViewPagerCarrusel == null) {
            mViewPagerCarrusel = new ViewPagerCarrusel(mContext, mLayout);
            mViewPagerCarrusel.mListener = mListener;
            this.mListener = mListener;
        } else {
            mViewPagerCarrusel.mListener = mListener;
            this.mListener = mListener;
        }
        return mViewPagerCarrusel;
    }

    public void start(ViewPager mViewPager, List<?> mObjectsList) {
        if (mViewPagerCarrusel == null) {
            mViewPagerCarrusel = new ViewPagerCarrusel(mContext, mLayout);
            mViewPagerCarrusel.mObjectsList = mObjectsList;
            mViewPagerCarrusel.mAdapter = new adapterViewPager();
            mViewPagerCarrusel.mViewPager = mViewPager;
            mViewPagerCarrusel.mViewPager.setPageTransformer(false, mPageTransformer);
            mViewPagerCarrusel.mViewPager.setAdapter(mViewPagerCarrusel.mAdapter);
        } else {
            mViewPagerCarrusel.mObjectsList = mObjectsList;
            mViewPagerCarrusel.mAdapter = new adapterViewPager();
            mViewPagerCarrusel.mViewPager = mViewPager;
            mViewPagerCarrusel.mViewPager.setPageTransformer(false, mPageTransformer);
            mViewPagerCarrusel.mViewPager.setAdapter(mViewPagerCarrusel.mAdapter);
        }
        this.mObjectsList = mObjectsList;
        this.mViewPager = mViewPager;
        this.mViewPager.setPageTransformer(false, mPageTransformer);
        mAdapter = new adapterViewPager();
        this.mViewPager.setAdapter(mAdapter);
        this.automatico();
    }

    Handler mHandler;
    Runnable mRunnable;

    public void stop() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            position = 0;
        }
    }

    private void automatico() {
        try {
            if (mViewPager != null) {
                if (mAdapter != null) {
                    mHandler = new Handler();
                    mRunnable = new Runnable() {
                        @Override
                        public void run() {
                            position++;
                            if (position >= Integer.MAX_VALUE) {
                                position = 0;
                            }
                            Log.e("Posicion carrucel", position + "");
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                            automatico();
                        }
                    };
                    mHandler.postDelayed(mRunnable, tiempo);
                } else {
                    if (mHandler != null) {
                        mHandler.removeCallbacks(mRunnable);
                    }
                }
            } else {
                if (mHandler != null) {
                    mHandler.removeCallbacks(mRunnable);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ViewPager.PageTransformer mPageTransformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(@NonNull View page, float position) {
            final float normalizedposition = Math.abs(Math.abs(position) - 1);
            page.setScaleX(normalizedposition / 2 + 0.5f);
            page.setScaleY(normalizedposition / 2 + 0.5f);
        }
    };

    private class adapterViewPager extends PagerAdapter {
        private int posReal = 0;

        public adapterViewPager() {
        }

        @Override
        public int getCount() {
            return (mObjectsList != null ? Integer.MAX_VALUE : 0);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View master = mLayoutInflater.inflate(mLayout, container, false);
            try {
                if (posReal > (mObjectsList.size() - 1)) posReal = 0;
                container.addView(mListener.view(mObjectsList.get(posReal), master));
                posReal++;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return master;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }

    public interface listener {
        View view(Object m, View mView);
    }
}
