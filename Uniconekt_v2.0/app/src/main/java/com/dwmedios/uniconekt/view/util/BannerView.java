package com.dwmedios.uniconekt.view.util;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Banners;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderDark;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;

public class BannerView {
    private ViewPager mViewPager;
    // private LayoutInflater mLayoutInflater;
    // private Context mContext;
    private List<?> mObjectsList;
    // private Activity mActivity;
    private FragmentManager fragmentManager;

    public BannerView(List<?> mObjectsList, FragmentManager fragmentManager) {
        this.mObjectsList = mObjectsList;
        this.fragmentManager = fragmentManager;
    }



    private class adapterBanner extends FragmentPagerAdapter {
        private int poscionVirtual = 0;

        public adapterBanner(FragmentManager fm) {
            super(fm);
        }

        public int getPoscionVirtual() {
            return poscionVirtual;
        }

        @Override
        public Fragment getItem(int position) {
            if (poscionVirtual >= mObjectsList.size() - 1) {
                poscionVirtual = 0;
            } else {
                poscionVirtual++;
            }
            this.poscionVirtual = position;
            return new myFragment().newInstance(mObjectsList.get(poscionVirtual), poscionVirtual,
                    (((Banners) mObjectsList.get(poscionVirtual)).tiempoo), mViewPager);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public int getCount() {
            return ((mObjectsList != null && mObjectsList.size() > 0 ? Integer.MAX_VALUE : 0));

        }

    }

    Handler mHandler;
    Runnable mRunnable;
    Banners mBanners;
    boolean continuar = true;

    private void load() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        } else {
            mHandler = new Handler();
        }
        if (continuar) {
            if (mObjectsList != null && mObjectsList.size() > 0) {
                if (mAdapterBanner != null) {
                    //int nummax = (mObjectsList.size() - 1);
                    //   Random mRandom = new Random();
                    // max-min
                    //final int numRandom = mRandom.nextInt(nummax);
                    int pos = mAdapterBanner.getPoscionVirtual();
                    if (pos > 0) pos = pos - 1;
                    mBanners = (Banners) mObjectsList.get(pos);
                    mRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (mViewPager != null) {
                                if (mViewPager.getAdapter() != null) {
                                    mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1));
                                    load();
                                }
                            }
                        }

                    };
                    mHandler.postDelayed(mRunnable, mBanners.tiempoo);

                }
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
    }

    @SuppressLint("ValidFragment")
    public static class myFragment extends Fragment {
        public Object mObject;
        public ViewPager mViewPager;
        public int posicion = 0, tiempo = 0;

        public static myFragment newInstance(Object mObject, int postition, int tiempo, ViewPager mViewPager) {
            final myFragment fragment = new myFragment();
            fragment.mObject = mObject;
            fragment.tiempo = tiempo;
            fragment.posicion = postition;
            fragment.mViewPager = mViewPager;
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View master = inflater.inflate(R.layout.row_custom_recycler, container, false);
            ImageView mImageView = master.findViewById(R.id.ImageviewRow);
            ImageLoader.getInstance().displayImage(getUrlImage(((Banners) mObject).foto, getContext()), mImageView, OptionsImageLoaderDark);
            Banners mBanners = (Banners) mObject;
            Log.e("Banner1", "Nombre: " + mBanners.nombre);
            Log.e("Banner2", "tiempo: " + mBanners.tiempoo);
            Log.e("Banner3", "id: " + mBanners.id);
            Log.e("Banner4", "posicion: " + posicion);
            return master;
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        public static final String TAG = "Nuevo banner";

    }


    private void handle() {
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
            mHandler = new Handler();
        } else {
            mHandler = new Handler();
        }
        if (mAdapterBanner != null) {
            mBanners = (Banners) mObjectsList.get(mAdapterBanner.poscionVirtual);

            mRunnable = new Runnable() {

                @Override
                public void run() {
                    if (mViewPager != null) {
                        if (mViewPager.getAdapter() != null) {
                            if (mViewPager.getAdapter().getCount() > 0) {
                                mViewPager.setCurrentItem((mAdapterBanner.poscionVirtual + 1));
                            }
                        }
                    }
                }

            };
            mHandler.postDelayed(mRunnable, mBanners.tiempoo);
        }
    }

    adapterBanner mAdapterBanner;

    public void start(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
        mAdapterBanner = new adapterBanner(fragmentManager);
        mViewPager.setAdapter(mAdapterBanner);
        // load();
    }

    public interface handleItem {
        void Oncreate();
    }
}
