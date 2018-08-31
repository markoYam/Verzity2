package com.dwmedios.uniconekt.view.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.FotosCupones;
import com.dwmedios.uniconekt.view.activity.Universitario.CuponesViewActivity;
import com.dwmedios.uniconekt.view.fragments.ViewPager.FotosViewerFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderItems;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;

public class ViewPagerUtils {
    /**
     * esta es la lista generica que trae desde donde se llame
     *
     * @List
     */
    public List<?> mObjectsList;
    private adapter mAdapter;
    private FragmentManager manager;
    private LayoutInflater layoutInflater;
    private Context mContext;
    ViewPager.OnPageChangeListener onPageChangeListener;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int timeAnim = 4000;
    private int currentPage = 0;
    /**
     * este metodo permite controlar la vista del adaptador desde donde se llame
     */
    private HandlerView mHandlerView;

    public ViewPagerUtils(List<?> mStringList, FragmentManager fm, Context mContext) {
        this.mObjectsList = mStringList;
        this.mContext = mContext;
        this.manager = fm;
    }


    public class adapter extends PagerAdapter {

        public adapter() {

        }


        @Override
        public int getCount() {
            return (mObjectsList != null && mObjectsList.size() > 0 ? mObjectsList.size() : 0);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            View master = layoutInflater.inflate(mLayout, null);
            // ImageView mImageview = master.findViewById(R.id.imageviewFotoUtil);
            //  List<FotosCupones> list = (List<FotosCupones>) mObjectsList;
            // ImageLoader.getInstance().displayImage(getUrlImage(list.get(position).ruta), mImageview, OptionsImageLoaderItems);

            if (mHandlerView != null) {
                container.addView(mHandlerView.handler(master, mObjectsList, position, container));
            }
            //  ((ViewPager) container).addView(master, 0);
            // container.addView(master);
            return master;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((View) object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
        //implementar el titulo....
    }


    /**
     * este metodo es para el manejo de la vista
     */
    @LayoutRes
    int mLayout;

    public void setLayoutInflater(@LayoutRes int resource) {
        this.mLayout = resource;
    }

    public void setmHandlerView(HandlerView mHandlerView) {
        this.mHandlerView = mHandlerView;
    }

    public interface HandlerView {
        View handler(View mView, List<?> mObjects, int position, ViewGroup container);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    /**
     *
     */
    ViewPager mViewPager;

    public void build(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
        if (onPageChangeListener != null)
            mViewPager.setOnPageChangeListener(onPageChangeListener);
        mAdapter = new adapter();
        mViewPager.setAdapter(mAdapter);
     //   demo();
    }

    private ViewPager.PageTransformer mPageTransformer = new ViewPager.PageTransformer() {
        @Override
        public void transformPage(@NonNull View page, float position) {
            final float normalizedposition = Math.abs(Math.abs(position) - 1);
            page.setScaleX(normalizedposition / 2 + 0.5f);
            page.setScaleY(normalizedposition / 2 + 0.5f);
        }
    };
    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class ola {
        int position;
        boolean loadAnim;
    }

    public void finishAnim() {
        finishBanner = true;
    }

    private boolean direction = false;
    private boolean finishBanner = false;

    private void demo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!finishBanner) {
                    if (direction) {
                        if (currentPage < mAdapter.getCount()) {
                            direction = true;
                            if (currentPage == mAdapter.getCount()) {
                                direction = false;
                                mViewPager.setCurrentItem(currentPage--, true);
                            } else {
                                mViewPager.setCurrentItem(currentPage++, true);
                            }
                            Log.e("Pages Banners", currentPage + "");
                        }
                        if (currentPage == mAdapter.getCount()) {
                            direction = false;
                            mViewPager.setCurrentItem(currentPage--, true);
                            Log.e("Pages Banners", currentPage + "");
                        }
                    } else {
                        if (currentPage > 0) {
                            direction = false;
                            if (currentPage == 0) {
                                direction = true;
                                mViewPager.setCurrentItem(currentPage++, true);
                            } else {
                                mViewPager.setCurrentItem(currentPage--, true);
                            }
                            Log.e("Pages Banners", currentPage + "");
                        }
                        if (currentPage == 0) {
                            direction = true;
                            mViewPager.setCurrentItem(currentPage++, true);
                            Log.e("Pages Banners", currentPage + "");
                        }
                    }
                    demo();
                }
            }
        }, timeAnim);
    }

    public void setAnimate(boolean load) {
        if (load) {
            if (mViewPager != null) {
                mViewPager.setPageTransformer(false, mPageTransformer);
                mViewPager.setOnPageChangeListener(mOnPageChangeListener);
            }
        }
    }
}
