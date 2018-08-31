package com.dwmedios.uniconekt.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.view.adapter.holder.UniversidadHolder;

import java.util.List;

public class UniversidadAdapter extends RecyclerView.Adapter<UniversidadHolder> {
    List<Universidad> mUniversidadLis;

    public UniversidadAdapter(List<Universidad> mUniversidadLis, onclickCard mOnclickCard) {
        this.mUniversidadLis = mUniversidadLis;
        this.mOnclickCard = mOnclickCard;
    }

    public interface onclickCard {
        void onclick(Universidad mUniversidad,int type);
    }
    public onclickCard mOnclickCard;

    @NonNull
    @Override
    public UniversidadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View master = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_universidades, parent, false);
        UniversidadHolder view = new UniversidadHolder(master);
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull UniversidadHolder holder, int position) {
        Universidad temp = mUniversidadLis.get(position);
        holder.configureUniversidad(temp, mOnclickCard);
    }

    @Override
    public int getItemCount() {
        return (mUniversidadLis != null && mUniversidadLis.size() > 0 ? mUniversidadLis.size() : 0);
    }
}
