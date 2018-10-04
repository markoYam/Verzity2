package com.dwmedios.uniconekt.view.util;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Banners;
import com.dwmedios.uniconekt.view.adapter.CustomAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Random;

import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderDark;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;

public class BannerViewRecyclerView implements CustomAdapter.ConfigureHolder {
    private Context mContext;
    private List<?> mObjectsList;
    private CustomAdapter mCustomAdapter;
    private String TAG = "BANNER";
    private RecyclerView mRecyclerView;

    public BannerViewRecyclerView(Context mContext) {
        this.mContext = mContext;
    }

    ImageView mImageView;

    @Override
    public void Configure(View itemView, Object mObject) {
        Banners mBanners = (Banners) mObject;
        mImageView = itemView.findViewById(R.id.ImageviewRow);
        ImageLoader.getInstance().displayImage(getUrlImage(mBanners.foto, mContext), mImageView, OptionsImageLoaderDark);
        Log.e(TAG, mBanners.nombre);
    }

    @Override
    public void Onclick(Object mObject) {
        final Banners mBanners = (Banners) mObject;
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mBanners.nombre + " - " + mBanners.tiempoo, Toast.LENGTH_LONG).show();

            }
        });
    }

    public class CustomGridLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager(Context context) {
            super(context);
        }

        public CustomGridLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }

    public void start(RecyclerView mRecyclerView, List<?> mObjectsList) {
        this.mRecyclerView = mRecyclerView;
        this.mObjectsList = mObjectsList;
        configureRecyclerView(mObjectsList);
        Handle();
    }

    public void configureRecyclerView(List<?> menus) {
        if (menus != null && menus.size() > 0) {
            mCustomAdapter = new CustomAdapter(menus, R.layout.row_custom_recycler, this);
            //LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            CustomGridLayoutManager manager = new CustomGridLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            manager.setScrollEnabled(true);
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mCustomAdapter);
        } else {
            mRecyclerView.setAdapter(null);
            this.EmpyRecycler();
        }
    }

    private boolean continuar = true;
    Handler mHandler;
    Runnable mRunnable;
    int[] num;

    private void Handle() {
        if (continuar) {
            if (mObjectsList != null && mObjectsList.size() > 0) {
                int numBanners = (mObjectsList.size() - 1);
                Random mRandom = new Random();
                final int numRandom = mRandom.nextInt(numBanners);
                if (numRandom != 0) {
                    Banners mBanners = (Banners) mObjectsList.get(numBanners);
                    mHandler = new Handler();
                    mRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (mCustomAdapter != null) {
                                mCustomAdapter.notifyItemMoved(numRandom, 0);
                                mCustomAdapter.notifyItemChanged(0);
                                //mCustomAdapter.notifyDataSetChanged();
                                Handle();
                            }
                        }
                    };
                    mHandler.postDelayed(mRunnable, mBanners.tiempoo);
                } else {
                    if (mHandler != null) {
                        mHandler.removeCallbacks(mRunnable);
                    }
                    Handle();
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

    private void EmpyRecycler() {
        mRecyclerView.setAdapter(null);
    }
}
