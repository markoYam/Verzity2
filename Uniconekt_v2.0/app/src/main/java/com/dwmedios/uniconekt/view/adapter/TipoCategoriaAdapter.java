package com.dwmedios.uniconekt.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.TipoCategoria;
import com.dwmedios.uniconekt.view.adapter.holder.TipoCategoriaHolder;

import java.util.List;

/**
 * Created by Luis Cabanas on 17/05/2017.
 */

public class TipoCategoriaAdapter extends RecyclerView.Adapter<TipoCategoriaHolder> {


    public TipoCategoriaAdapter(List<TipoCategoria> mTipoCategoriaList) {
        this.mTipoCategoriaList = mTipoCategoriaList;
    }

    @Override
    public TipoCategoriaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View master = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tipo_categoria, parent, false);
        TipoCategoriaHolder mHolder = new TipoCategoriaHolder(master);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(TipoCategoriaHolder holder, int position) {
        TipoCategoria mCategoria = getItemAtPosition(position);
        holder.config(mCategoria);
    }

    @Override
    public int getItemCount() {
        return (mTipoCategoriaList == null ? 0 : mTipoCategoriaList.size());
    }

    public TipoCategoria getItemAtPosition(int position) {
        if(position >= 0 && position < mTipoCategoriaList.size()){
            return mTipoCategoriaList.get(position);
        }
        return null;
    }

    private List<TipoCategoria> mTipoCategoriaList;
}
