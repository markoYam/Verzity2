package com.dwmedios.uniconekt.view.adapter.holder;

import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Notificaciones;
import com.dwmedios.uniconekt.view.adapter.NotificacionAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.util.Utils.configurefechaCompleted;
import static com.dwmedios.uniconekt.view.util.Utils.getDays;

public class NotificacionHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textViewContenidoMensaje)
    TextView mTextViewContenido;
    @BindView(R.id.textViewAsunto)
    TextView mTextViewAsunto;
    @BindView(R.id.idCardviewnotificaciones)
    CardView mCardView;
    @BindView(R.id.imageviewIconoNotificaciones)
    ImageView mImageViewIcon;
    @BindView(R.id.imagebuttom)
    ImageButton mImageButton;

    public NotificacionHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void configure(final Notificaciones mNotificaciones, final NotificacionAdapter.onclick mOnclick) {
        mTextViewAsunto.setText(mNotificaciones.asunto + " - " + getDays(mNotificaciones.fecha) + " " + configurefechaCompleted(mNotificaciones.fecha));
        mTextViewContenido.setText(mNotificaciones.mensaje);
        if (mNotificaciones.mNotificacionEstatusList != null && mNotificaciones.mNotificacionEstatusList.size() > 0) {
            if (mNotificaciones.mNotificacionEstatusList.get(0).mEstatus != null) {
                if (mNotificaciones.mNotificacionEstatusList.get(0).mEstatus.estatusNot != null) {
                    switch (mNotificaciones.mNotificacionEstatusList.get(0).mEstatus.estatusNot) {
                        case "VISTO":
                            mImageViewIcon.setImageResource(R.drawable.ic_action_notifications);
                            mTextViewAsunto.setTypeface(Typeface.DEFAULT);
                            break;
                        case "PENDIENTE":
                            mImageViewIcon.setImageResource(R.drawable.ic_action_notifications_visto);
                            mTextViewAsunto.setTypeface(Typeface.DEFAULT_BOLD);
                            break;
                        default:
                            mTextViewAsunto.setTypeface(Typeface.DEFAULT_BOLD);
                            mImageViewIcon.setImageResource(R.drawable.ic_action_notifications_visto);
                            break;
                    }
                } else {
                    mTextViewAsunto.setTypeface(Typeface.DEFAULT_BOLD);
                    mImageViewIcon.setImageResource(R.drawable.ic_action_notifications_visto);
                }
            } else {
                mTextViewAsunto.setTypeface(Typeface.DEFAULT_BOLD);
                mImageViewIcon.setImageResource(R.drawable.ic_action_notifications_visto);
            }
        } else {
            mTextViewAsunto.setTypeface(Typeface.DEFAULT_BOLD);
            mImageViewIcon.setImageResource(R.drawable.ic_action_notifications_visto);
        }
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclick != null) {
                    mOnclick.onclick(mNotificaciones);
                }
            }
        });
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclick != null) {
                    mOnclick.onclick(mNotificaciones);
                }
            }
        });
    }
}






