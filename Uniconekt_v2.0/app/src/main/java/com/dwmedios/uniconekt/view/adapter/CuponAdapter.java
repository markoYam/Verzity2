package com.dwmedios.uniconekt.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Cupones;
import com.dwmedios.uniconekt.view.adapter.holder.CuponesHolder;

import java.util.List;

public class CuponAdapter extends RecyclerView.Adapter<CuponesHolder> {
    List<Cupones> mCuponesList;

    public CuponAdapter(List<Cupones> mCuponesList, onclick mOnclick) {
        this.mCuponesList = mCuponesList;
        this.mOnclick = mOnclick;
    }

    public interface onclick{
    void onclick(Cupones mCupones);
}
onclick mOnclick;
    @NonNull
    @Override
    public CuponesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View master = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cupones, parent, false);
        return new CuponesHolder(master);
    }

    @Override
    public void onBindViewHolder(@NonNull CuponesHolder holder, int position) {
        holder.configure(mCuponesList.get(position),mOnclick);
    }

    @Override
    public int getItemCount() {
        return (mCuponesList != null && mCuponesList.size() > 0 ? mCuponesList.size() : 0);
    }
}
