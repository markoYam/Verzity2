package com.dwmedios.uniconekt.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Paquetes;
import com.dwmedios.uniconekt.view.adapter.holder.PaquetesHolder;

import java.util.List;

public class PaquetesAdapter extends RecyclerView.Adapter<PaquetesHolder> {
    public List<Paquetes> mPaquetesList;

    public PaquetesAdapter(List<Paquetes> mPaquetesList, onclick mOnclick) {
        this.mPaquetesList = mPaquetesList;
        this.mOnclick = mOnclick;
    }

    public interface onclick {
        void onclickButton(Paquetes mPaquetes);
    }

    onclick mOnclick;

    @NonNull
    @Override
    public PaquetesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View master = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_paquetes, parent, false);
        return new PaquetesHolder(master);
    }

    @Override
    public void onBindViewHolder(@NonNull PaquetesHolder holder, int position) {
        holder.Configure(mPaquetesList.get(position), mOnclick);
    }

    @Override
    public int getItemCount() {
        return (mPaquetesList != null && mPaquetesList.size() > 0 ? mPaquetesList.size() : 0);
    }
}
