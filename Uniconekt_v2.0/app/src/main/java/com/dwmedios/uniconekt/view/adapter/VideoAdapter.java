package com.dwmedios.uniconekt.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Videos;
import com.dwmedios.uniconekt.view.adapter.holder.VideoHolder;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoHolder> {
    public List<Videos> mVideosList;

    public VideoAdapter(List<Videos> mVideosList, onclick mOnclick) {
        this.mVideosList = mVideosList;
        this.mOnclick = mOnclick;
    }

    public interface onclick {
        void onclick(Videos mVideos, ImageView mImageView);
    }

    public onclick mOnclick;

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View master = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_videos, parent, false);
        return new VideoHolder(master);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        holder.configureVideo(mVideosList.get(position), mOnclick);
    }

    @Override
    public int getItemCount() {
        return (mVideosList != null && mVideosList.size() > 0 ? mVideosList.size() : 0);
    }
}
