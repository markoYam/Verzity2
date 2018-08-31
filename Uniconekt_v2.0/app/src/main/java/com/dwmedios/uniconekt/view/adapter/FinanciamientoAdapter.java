package com.dwmedios.uniconekt.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Financiamientos;
import com.dwmedios.uniconekt.view.adapter.holder.FinanciamientoHolder;

import java.util.List;

public class FinanciamientoAdapter  extends RecyclerView.Adapter<FinanciamientoHolder> {
    List<Financiamientos> mFinanciamientosList;

    public FinanciamientoAdapter(List<Financiamientos> mFinanciamientosList, onclick mOnclick) {
        this.mFinanciamientosList = mFinanciamientosList;
        this.mOnclick = mOnclick;
    }

    public interface onclick {
        void onclickButton(Financiamientos financiamientos);
    }

    private onclick mOnclick;

    @NonNull
    @Override
    public FinanciamientoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View master = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_financiamientos, parent, false);
        return new FinanciamientoHolder(master);
    }

    @Override
    public void onBindViewHolder(@NonNull FinanciamientoHolder holder, int position) {
        holder.ConfigureFinanciamiento(mFinanciamientosList.get(position), mOnclick);
    }

    @Override
    public int getItemCount() {
        return (mFinanciamientosList != null && mFinanciamientosList.size() > 0 ? mFinanciamientosList.size() : 0);
    }
}
