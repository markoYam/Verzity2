package com.dwmedios.uniconekt.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Becas;
import com.dwmedios.uniconekt.view.adapter.holder.BecasHolder;

import java.util.List;

public class BecasAdapter extends RecyclerView.Adapter<BecasHolder> {
    public List<Becas> mBecasList;

    public BecasAdapter(List<Becas> mBecasList, onclick mOnclick) {
        this.mBecasList = mBecasList;
        this.mOnclick = mOnclick;
    }

    public interface onclick {
        void onclickButton(Becas mBecas, ImageView mImageView);
    }

    public onclick mOnclick;

    @NonNull
    @Override
    public BecasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View master = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_becas, parent, false);
        return new BecasHolder(master);
    }

    @Override
    public void onBindViewHolder(@NonNull BecasHolder holder, int position) {
        holder.ConfigureBeca(mBecasList.get(position), mOnclick);
    }

    @Override
    public int getItemCount() {
        return (mBecasList != null && mBecasList.size() > 0 ? mBecasList.size() : 0);
    }
}
