package com.dwmedios.uniconekt.view.util.demo;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.dwmedios.uniconekt.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CustoViewPager {
    private List<?> mObjectsList;
    private ViewPager mViewPager;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private adapter mAdapter;

    public CustoViewPager(List<?> mObjectsList, Context mContext, ViewPager mViewPager) {
        this.mObjectsList = mObjectsList;
        this.mContext = mContext;
        this.mViewPager = mViewPager;
    }

    private class adapter extends PagerAdapter {
        int pos = 0;

        public adapter() {
            mLayoutInflater= LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return (mObjectsList != null && mObjectsList.size() > 0 ? Integer.MAX_VALUE : 0);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
          //  mLayoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            View master = mLayoutInflater.inflate(R.layout.row_custom_recycler, container,false);
            if (pos == (mObjectsList.size() - 1)) {
                pos = 0;
            } else {
                ++pos;
            }

            return master;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    Handler mHandler = new Handler();
    Runnable mRunnable;
    Timer mTimer;
    private Float tiempoCorrido = 0.0f;
    private String TAG = "Item banner";

    public void automatico(int postion, final int tiempo) {
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
            mHandler = new Handler();
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = new Timer();
        }
        Log.e(TAG, "------------------------------------------------------");
        Log.e(TAG + 1, "posicion actual " + postion + "\n");
        Log.e(TAG + 2, "tiempo banner actual " + tiempo + "\n");

        mTimer = new Timer();
        final int[] tiempo2 = {1};
        final int finalPostion = postion;
        final TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.e(TAG, "tiempo: " + String.valueOf(tiempo2[0]) + " -segundos" + " Posicion banner: " + mViewPager.getCurrentItem());

                if ((tiempo2[0] * 1000) == tiempo) {
                    Log.e(TAG, "Tiempo Finalizado -posicion banner: " + finalPostion);
                    mTimer.cancel();
                    mTimer.purge();
                } else {
                    tiempo2[0]++;
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mViewPager != null && mAdapter != null) {
                    if (mObjectsList != null && mObjectsList.size() > 0) {
                        //mHandler.postDelayed(this, tiempo);
                        Log.e(TAG, "cambio de posicion " + mViewPager.getCurrentItem() + " a posicion " + (mViewPager.getCurrentItem() + 1));
                        mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1));
                    } else {
                        mHandler.removeCallbacksAndMessages(this);
                    }
                } else {
                    mHandler.removeCallbacksAndMessages(this);
                }
            }

        };
        mHandler.postDelayed(mRunnable, tiempo);


    }

    public void stopHandler() {
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacksAndMessages(mRunnable);
            if (mAdapter != null) {
                mObjectsList = null;
                start();
            }
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
    }

    public void start() {
        mAdapter = new adapter();
        mViewPager.setAdapter(mAdapter);
        if (mViewPager.getAdapter().getCount() > 0)
            mViewPager.setCurrentItem(0);
        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalizedposition / 2 + 0.5f);
                page.setScaleY(normalizedposition / 2 + 0.5f);
            }
        });
    }

    private handleView mHandleView;

    public void setmHandleView(handleView mHandleView) {
        this.mHandleView = mHandleView;
    }

    public interface handleView {
        View handleView(View mView, Object mObject, int postion);
    }

    public void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    private void setAnimation() {
        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalizedposition / 2 + 0.5f);
                page.setScaleY(normalizedposition / 2 + 0.5f);
            }
        });
    }
}
