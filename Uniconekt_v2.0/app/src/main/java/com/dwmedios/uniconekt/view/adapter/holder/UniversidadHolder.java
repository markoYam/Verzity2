package com.dwmedios.uniconekt.view.adapter.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.view.adapter.UniversidadAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderDark;
import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderLight;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;

public class UniversidadHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageUniversidad)
    protected ImageView mImageView;
    @BindView(R.id.texviewNombreUniversidad)
    protected TextView mTextView;
    @BindView(R.id.cardviewUniversidad)
    protected CardView mCardView;
    @BindView(R.id.imagebuttom)
    ImageButton mImageButton;

    public UniversidadHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void configureUniversidad(final Universidad mUniversidad, final UniversidadAdapter.onclickCard mOnclickCard) {
        if (mUniversidad != null && mUniversidad.logo != null) {
            ImageLoader.getInstance().displayImage(getUrlImage(mUniversidad.logo, itemView.getContext()), mImageView, OptionsImageLoaderLight);
        } else mImageView.setImageResource(R.drawable.defaultt);
        mTextView.setText(mUniversidad.nombre);
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclickCard != null) {
                    mOnclickCard.onclick(mUniversidad, 1);
                }
            }
        });
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclickCard != null) {
                    mOnclickCard.onclick(mUniversidad, 1);
                }
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclickCard != null) {
                    mOnclickCard.onclick(mUniversidad, 2);
                }

            }
        });
    }
}
