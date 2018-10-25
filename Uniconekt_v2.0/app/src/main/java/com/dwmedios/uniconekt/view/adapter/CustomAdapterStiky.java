package com.dwmedios.uniconekt.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;


public class CustomAdapterStiky extends RecyclerView.Adapter<CustomAdapterStiky.CustomHolder> implements StickyRecyclerHeadersAdapter {

    public List<?> mList;
    public int Layout;
    public int layout_Header;
    private ConfigureHolder mConfigureHolder;


    public CustomAdapterStiky(List<?> mList, int layout,int layout_Header, ConfigureHolder mConfigureHolder) {
        this.mList = mList;
        this.Layout = layout;
        this.layout_Header=layout_Header;
        this.mConfigureHolder = mConfigureHolder;
    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View master = LayoutInflater.from(parent.getContext()).inflate(this.Layout, parent, false);
        return new CustomHolder(master);
    }

    @Override
    public void onBindViewHolder(CustomHolder holder, int position) {
        holder.configureHolder(mConfigureHolder, mList.get(position),false);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public long getHeaderId(int position) {
        return mConfigureHolder.getHeaderId(mList.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View master = LayoutInflater.from(parent.getContext()).inflate(this.layout_Header, parent, false);
        return new CustomHolder(master);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        CustomHolder mCustomHolder = (CustomHolder) holder;
        mCustomHolder.configureHolder(mConfigureHolder, mList.get(position), true);
    }

    @Override
    public int getItemCount() {
        return (mList != null && mList.size() > 0 ? mList.size() : 0);
    }

    public class CustomHolder extends RecyclerView.ViewHolder {

        public CustomHolder(View itemView) {
            super(itemView);
        }

        public void configureHolder(ConfigureHolder mConfigureHolder, Object mObject,boolean isHeader) {
            if (mConfigureHolder != null) {
                mConfigureHolder.Configure(itemView, mObject,isHeader);
                mConfigureHolder.Onclick(mObject);
            }
        }
    }

    public interface ConfigureHolder {
        void Configure(View itemView, Object mObject,boolean isHeader);

        void Onclick(Object mObject);

        long getHeaderId(Object mObject);

    }
}
