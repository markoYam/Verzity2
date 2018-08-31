package com.dwmedios.uniconekt.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Notificaciones;
import com.dwmedios.uniconekt.view.adapter.holder.NotificacionHolder;

import java.util.List;

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionHolder> {
    List<Notificaciones> mNotificacionesList;

    public NotificacionAdapter(List<Notificaciones> mNotificacionesList, onclick mOnclick) {
        this.mNotificacionesList = mNotificacionesList;
        this.mOnclick = mOnclick;
    }

    public interface onclick {
        void onclick(Notificaciones mNotificaciones);
    }

    private onclick mOnclick;

    @NonNull
    @Override
    public NotificacionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificacionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notificaciones, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionHolder holder, int position) {
        holder.configure(mNotificacionesList.get(position), mOnclick);
    }

    @Override
    public int getItemCount() {
        return (mNotificacionesList != null && mNotificacionesList.size() > 0 ? mNotificacionesList.size() : 0);
    }
}
