package com.dwmedios.uniconekt.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.PostuladosGeneral;
import com.dwmedios.uniconekt.view.adapter.holder.LicenciaturasHolder;
import com.dwmedios.uniconekt.view.adapter.holder.PostuladosHolder;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

public class PostuladosAdapter extends RecyclerView.Adapter<PostuladosHolder> implements StickyRecyclerHeadersAdapter {
    private List<PostuladosGeneral> mPostuladosGenerals;

    public PostuladosAdapter(List<PostuladosGeneral> mPostuladosGenerals,onclick mOnclick) {
        this.mPostuladosGenerals = mPostuladosGenerals;
        this.mOnclick=mOnclick;
    }

    public interface onclick {
        void onclickListener(PostuladosGeneral mPostuladosGeneral);
    }
private onclick mOnclick;
    @NonNull
    @Override
    public PostuladosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View master = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_postulados_general, parent, false);
        return new PostuladosHolder(master);
    }

    @Override
    public void onBindViewHolder(@NonNull PostuladosHolder holder, int position) {
        holder.configure(mPostuladosGenerals.get(position),mOnclick);
    }

    @Override
    public long getHeaderId(int position) {
        return mPostuladosGenerals.get(position).NombreSeccion.charAt(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View master2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.preguntas_header_row, parent, false);
        PostuladosHolder mPostuladosHolder = new PostuladosHolder(master2, false);
        return mPostuladosHolder;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView itemHolder = (TextView) holder.itemView;
        itemHolder.setText(mPostuladosGenerals.get(position).NombreSeccion);
    }

    @Override
    public int getItemCount() {
        return (mPostuladosGenerals != null && mPostuladosGenerals.size() > 0 ? mPostuladosGenerals.size() : 0);
    }
}
