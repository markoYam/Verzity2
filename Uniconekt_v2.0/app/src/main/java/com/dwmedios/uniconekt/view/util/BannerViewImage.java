package com.dwmedios.uniconekt.view.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BannerViewImage {
    private List<?> mObjectsList;
    private ImageView mImageView;
    private Context mContext;
    private int time;
    private boolean continuar = true;
    public static BannerViewImage mBannerViewImage;

    public BannerViewImage(Context mContext) {
        this.mContext = mContext;
    }

    public static synchronized BannerViewImage getInstance(Context mContext) {
        if (mBannerViewImage == null) {
            if (mBannerViewImage == null) {
                mBannerViewImage = new BannerViewImage(mContext);
            }
        }
        return mBannerViewImage;
    }

    public void start(ImageView mImageView, List<?> mObjectsList, listener mListener) {
        // mBannerViewImage.start(mImageView,mObjectsList);
        this.mImageView = mImageView;
        this.mObjectsList = mObjectsList;
        this.mListener = mListener;
        this.continuar = true;
        desordenar();
        handle();
    }

    private void desordenar() {
        try {


            List<Object> mObjects = new ArrayList<>();
            List<Object> mObjects2 = new ArrayList<>();
            int pos = 1;
            for (Object m : mObjectsList) {
                if (pos == 2) {
                    mObjects.add(m);
                    pos = 0;
                } else {
                    pos++;
                    mObjects2.add(m);
                }
            }
            if (mObjects2 != null && mObjects2.size() > 0) {
                if (mObjects != null && mObjects.size() > 0) {
                    if (mObjects2 != null && mObjects2.size() > 0) {
                        mObjects2.addAll(mObjects);
                        mObjectsList = mObjects2;
                    }
                } else {

                    mObjectsList = mObjects2;

                }
            } else {
                if (mObjects != null && mObjects.size() > 0) {
                    mObjectsList = mObjects;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void oncreate() {

    }

    private boolean isFinished;

    public void finishBanner() {
        if (mBannerViewImage != null) {
            mBannerViewImage.continuar = false;
            if (mHandler != null) {
                mHandler.removeCallbacks(mRunnable);
            }
            isFinished = true;
        } else {
            this.continuar = false;
            if (mHandler != null) {
                mHandler.removeCallbacks(mRunnable);
            }
            isFinished = true;
        }
    }

    public void stop() {
        if (mBannerViewImage != null)
            mBannerViewImage.continuar = false;
        else {
            this.continuar = false;
        }
    }

    public void start() {
        if (mBannerViewImage != null)
            mBannerViewImage.continuar = true;
        else {
            this.continuar = true;
        }
        handle();
    }

    int posicion = 0;
    Handler mHandler;
    Runnable mRunnable;

    private void handle() {
        try {
            if (!isFinished) {
                mImageView.setVisibility(View.VISIBLE);
                if (continuar) {
                    final Animation out = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
                    out.setDuration(500);
                    final Animation in = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
                    in.setDuration(500);
                    if (mObjectsList != null && mObjectsList.size() > 0) {
                        if (mListener != null) {
                            if (posicion > (mObjectsList.size() - 1)) {
                                posicion = 0;
                            }
                            mImageView.startAnimation(out);
                            mImageView.setVisibility(View.INVISIBLE);
                            int tiempo = mListener.onsuccess(mObjectsList.get(posicion));
                            Log.e("Posicion", posicion + " de " + (mObjectsList.size() - 1));
                            Log.e("Tiempo", tiempo + " sec");
                            mImageView.startAnimation(in);
                            mImageView.setVisibility(View.VISIBLE);
                            if (mHandler != null) {
                                mHandler.removeCallbacks(mRunnable);
                                mHandler = new Handler();
                            }
                            mHandler = new Handler();
                            mRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    if (posicion <= (mObjectsList.size() - 1)) {
                                        posicion++;
                                        handle();
                                    } else {
                                        mHandler.removeCallbacks(mRunnable);
                                        posicion = 0;
                                    }
                                }
                            };
                            mHandler.postDelayed(mRunnable, tiempo);
                        }
                    }
                } else {
                    if (mHandler != null)
                        mHandler.removeCallbacks(mRunnable);
                }
            } else {
                mImageView.setVisibility(View.GONE);
                if (mHandler != null)
                    mHandler.removeCallbacks(mRunnable);
            }
        } catch (Exception ex) {
            mImageView.setVisibility(View.GONE);
            Toast.makeText(mContext, "Ha ocurrido un error al visualizar los banners.", Toast.LENGTH_SHORT).show();
        }
    }

    private listener mListener;

    public interface listener {
        int onsuccess(Object mObject);
    }
}
