package com.dwmedios.uniconekt.view.adapter.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Becas;
import com.dwmedios.uniconekt.view.adapter.BecasAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario.BecasActivity.KEY_TRANSICION_BECA_1;
import static com.dwmedios.uniconekt.view.activity.Universitario.BecasActivity.KEY_TRANSICION_BECA_2;
import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderItems;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.dwmedios.uniconekt.view.util.Transitions.Transisciones.Dw_setTransaction;
import static com.dwmedios.uniconekt.view.util.Utils.getDrawable;

public class BecasHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.cardviewBeca)
    CardView mCardView;
    @BindView(R.id.imageViewFotoBeca)
    ImageView mImageView;
    @BindView(R.id.textViewNombreBeca)
    TextView mTextViewNombrebeca;
    @BindView(R.id.textViewNombreUniversidadBeca)
    TextView mTextViewNombreUniversidad;
    @BindView(R.id.buttonVerMas)
    Button mButton;
    private Becas mBecas;

    public BecasHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    BecasAdapter.onclick mOnclick;

    public void ConfigureBeca(Becas mBecas, BecasAdapter.onclick mOnclick) {
        this.mBecas = mBecas;
        this.mOnclick = mOnclick;
        if (mBecas.nombre != null)
            mTextViewNombrebeca.setText(mBecas.nombre);
        if (mBecas.mUniversidad != null)
            mTextViewNombreUniversidad.setText(mBecas.mUniversidad.nombre);
        if (mBecas.rutaImagen != null && !mBecas.rutaImagen.isEmpty()) {
            ImageLoader.getInstance().displayImage(getUrlImage(mBecas.rutaImagen, itemView.getContext()), mImageView, OptionsImageLoaderItems);
        } else {
            mImageView.setImageDrawable(getDrawable("defaultt", itemView.getContext()));
        }

        mCardView.setOnClickListener(mOnClickListener);
        mButton.setOnClickListener(mOnClickListener);
        Dw_setTransaction(mImageView, KEY_TRANSICION_BECA_1);
        Dw_setTransaction(mTextViewNombrebeca, KEY_TRANSICION_BECA_2);

    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mOnclick != null) mOnclick.onclickButton(mBecas, mImageView, mTextViewNombrebeca);
        }
    };
}
