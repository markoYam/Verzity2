package com.dwmedios.uniconekt.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomHolder> {

    public List<?> mList;
    public int Layout;
    private ConfigureHolder mConfigureHolder;

    public CustomAdapter(List<?> mList, int layout, ConfigureHolder mConfigureHolder) {
        this.mList = mList;
        this.Layout = layout;
        this.mConfigureHolder = mConfigureHolder;
    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View master = LayoutInflater.from(parent.getContext()).inflate(this.Layout, parent, false);
        return new CustomHolder(master);
    }

    @Override
    public void onBindViewHolder(CustomHolder holder, int position) {
        holder.configureHolder(mConfigureHolder, mList.get(position));
    }

    @Override
    public int getItemCount() {
        return (mList != null && mList.size() > 0 ? mList.size() : 0);
    }

    public class CustomHolder extends RecyclerView.ViewHolder {

        public CustomHolder(View itemView) {
            super(itemView);
        }

        public void configureHolder(ConfigureHolder mConfigureHolder, Object mObject) {
            if (mConfigureHolder != null) {
                mConfigureHolder.Configure(itemView, mObject);
                mConfigureHolder.Onclick(mObject);
            }
        }
    }

    public interface ConfigureHolder {
        void Configure(View itemView, Object mObject);

        void Onclick(Object mObject);
    }
}
