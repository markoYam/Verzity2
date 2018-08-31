package com.dwmedios.uniconekt.view.util.demo;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.view.adapter.MenuAdapter;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CustomRecycler {

    private List<?> mObjectList;

    private View mView;
    private handleView mHandleView;
    private onclickItem mOnclickItem;
    private adapterView mAdapterView;
    private RecyclerView mRecyclerView;
    private Context mContext;

    public CustomRecycler(RecyclerView mRecyclerView, Context mContext) {
        this.mRecyclerView = mRecyclerView;
        this.mContext = mContext;
    }

    private void configureRecyclerView() {
        if (mObjectList != null && mObjectList.size() > 0) {
            mAdapterView = new adapterView();
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapterView);
        } else {
            mRecyclerView.setAdapter(null);
            //this.EmpyRecycler();
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    public void start() {
        configureRecyclerView();
    }

    public void notifyItemMoved(int in, int to) {
        if (mAdapterView != null)
            mAdapterView.notifyItemMoved(in, to);
    }

    /**
     * Asiganar la lista de datos
     *
     * @param mObjectList
     */
    public void setmObjectList(List<?> mObjectList) {
        this.mObjectList = mObjectList;

    }

    /**
     * Evento de manejo de la vista
     *
     * @param mHandleView
     */
    public void setmHandleView(handleView mHandleView) {
        this.mHandleView = mHandleView;
    }

    /**
     * Evento de manejo del clic
     *
     * @param mOnclickItem
     */
    public void setmOnclickItem(onclickItem mOnclickItem) {
        this.mOnclickItem = mOnclickItem;
    }

    Handler mHandler = new Handler();
    Runnable mRunnable;

    public void automatico(final int postion, int tiempo) {
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }

        mRunnable = new Runnable() {
            @Override
            public void run() {
                int siguiente = 0;
                if (mRecyclerView != null && mAdapterView != null) {
                    if (mObjectList != null && mObjectList.size() > 0) {
                        if (postion >= 0 && postion < mObjectList.size()) {
                            siguiente = (postion + 1);
                        }
                        if (postion == mObjectList.size()) {
                            siguiente = (postion - 1);
                        }
                        if (postion == 0) {
                            siguiente = (postion + 1);
                        }
                        mRecyclerView.scrollToPosition(siguiente);
                    } else {
                        mHandler.removeCallbacks(this);
                    }
                } else {
                    mHandler.removeCallbacks(this);
                }
            }
        };
        mHandler.postDelayed(mRunnable, tiempo);


    }

    /**
     * Holder
     */
    private class viewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        public viewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.ImageviewRow);
            setFadeAnimation(mImageView);
            setIsRecyclable(false);
        }

        public void configureView(final Object m, handleView mHandleView, final onclickItem mOnclickItem,int postion) {
            if (mHandleView != null) {
                mHandleView.handle(m, mImageView,postion);
            }
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnclickItem != null) {
                        mOnclickItem.onclick(m);
                    }
                }
            });
        }
    }

    public void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    /**
     * Adaptador
     */
    private class adapterView extends RecyclerView.Adapter<viewHolder> {

        @Override
        public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View master = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_custom_recycler, parent, false);
            return new viewHolder(master);
        }

        @Override
        public void onBindViewHolder(viewHolder holder, int position) {
            holder.configureView(mObjectList.get(position), mHandleView, mOnclickItem, position);
        }

        @Override
        public int getItemCount() {
            return (mObjectList != null && mObjectList.size() > 0 ? mObjectList.size() : 0);
        }
    }

    /**
     * interfaces
     */
    public interface handleView {
        void handle(Object mobject, ImageView mImageView,int position);
    }

    public interface onclickItem {
        void onclick(Object mObject);
    }
}
