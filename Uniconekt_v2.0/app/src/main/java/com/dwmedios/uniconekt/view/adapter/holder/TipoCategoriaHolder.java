package com.dwmedios.uniconekt.view.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.TipoCategoria;
import com.dwmedios.uniconekt.view.util.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Luis Cabanas on 17/05/2017.
 */

public class TipoCategoriaHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageViewCategoria) protected ImageView mImageViewCategoria;
    @BindView(R.id.textViewCategoria) protected TextView mTextViewCategoria;

    public TipoCategoriaHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void config(TipoCategoria mTipoCategoria){

        if (mTipoCategoria != null && mTipoCategoria.pathImagen != null && !mTipoCategoria.pathImagen.contentEquals("")) {
            ImageLoader.getInstance().displayImage(ImageUtils.getUrlImage(mTipoCategoria.pathImagen,itemView.getContext()), mImageViewCategoria, ImageUtils.OptionsImageLoaderRounded);
        } else {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.ic_file_image_dark, mImageViewCategoria, ImageUtils.OptionsImageLoaderRounded);
        }

        mTextViewCategoria.setText(mTipoCategoria.nombre);
    }
}
