package com.dwmedios.uniconekt.view.adapter.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Cupones;
import com.dwmedios.uniconekt.view.adapter.CuponAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderItems;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.dwmedios.uniconekt.view.util.Utils.configurefecha;
import static com.dwmedios.uniconekt.view.util.Utils.getDrawable;

public class CuponesHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textViewNombreCupon)
    TextView mTextViewNombre;
    @BindView(R.id.imageViewFotoBCupon)
    ImageView mImageView;
    @BindView(R.id.cardviewCupon)
    CardView mCardView;
    @BindView(R.id.buttonVerMasCupon)
    Button mButton;
    @BindView(R.id.textViewVencimiento)
    TextView mTextView;

    public CuponesHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void configure(final Cupones mCupones, final CuponAdapter.onclick mOnclick) {
        if (mCupones.nombre != null)
            mTextViewNombre.setText(mCupones.nombre);
        mTextView.setText("Vencimiento: " + configurefecha(mCupones.fin));
        if (mCupones.mFotosCuponesList != null && mCupones.mFotosCuponesList.size() > 0) {
            ImageLoader.getInstance().displayImage(getUrlImage(mCupones.mFotosCuponesList.get(0).ruta, itemView.getContext()), mImageView, OptionsImageLoaderItems);

        } else {
            mImageView.setImageResource(R.drawable.defaultt);
        }
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclick != null) {
                    mOnclick.onclick(mCupones);
                }
            }
        });
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclick != null) {
                    mOnclick.onclick(mCupones);
                }
            }
        });
    }
}
