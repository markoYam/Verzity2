package com.dwmedios.uniconekt.view.adapter.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Financiamientos;
import com.dwmedios.uniconekt.view.adapter.FinanciamientoAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderDark;
import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderItems;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;

public class FinanciamientoHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.cardviewFinanciamiento)
    CardView mCardView;
    @BindView(R.id.imageViewFotoFinanciamiento)
    ImageView mImageView;
    @BindView(R.id.textViewNombreFinanciamiento)
    TextView mTextViewNombre;
    @BindView(R.id.textViewNombreUniversidadFinanciamiento)
    TextView mTextViewNombreUniversidad;
    @BindView(R.id.buttonVerMas)
    Button mButton;

    public FinanciamientoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void ConfigureFinanciamiento(final Financiamientos mFinanciamientos, final FinanciamientoAdapter.onclick mOnclick) {
        if (mFinanciamientos.nombre != null) {
            mTextViewNombre.setText(mFinanciamientos.nombre);
        }
        if (mFinanciamientos.imagen != null)
            ImageLoader.getInstance().displayImage(getUrlImage(mFinanciamientos.imagen, itemView.getContext()), mImageView, OptionsImageLoaderItems);
        else
            mImageView.setImageResource(R.drawable.defaultt);
        if (mFinanciamientos.universidad != null)
            mTextViewNombreUniversidad.setText(mFinanciamientos.universidad.nombre);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclick != null) mOnclick.onclickButton(mFinanciamientos);
            }
        });
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclick != null) mOnclick.onclickButton(mFinanciamientos);
            }
        });
    }
}
