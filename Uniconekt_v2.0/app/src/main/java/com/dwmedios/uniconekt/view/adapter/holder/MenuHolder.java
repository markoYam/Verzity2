package com.dwmedios.uniconekt.view.adapter.holder;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.view.adapter.MenuAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageMenu)
    protected ImageView mImageView;
    @BindView(R.id.texviewNombrMenu)
    protected TextView mTextView;
    @BindView(R.id.cardviewMenu)
    protected CardView mCardView;
    @BindView(R.id.imagebuttom)
    ImageButton mImageButton;
    @BindView(R.id.relative_menu)
    RelativeLayout mRelativeLayout;

    public MenuHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void ConfigureMenu(final ClasViewModel.menu menu, final MenuAdapter.onclick mOnclick, int type) {
        if (menu.drawableImage != 0)
            mImageView.setImageResource(menu.drawableImage);
     /*   ClasViewModel.tipoMenu validate = ClasViewModel.tipoMenu.valueOf(menu.tipo.toString());
        switch (validate) {
            case Becas:
                mRelativeLayout.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimaryDark));
                break;
        }*/
        mTextView.setText(menu.nombre);
        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclick != null) {
                    mOnclick.onclick(menu);
                }
            }
        });
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclick != null) {
                    mOnclick.onclick(menu);
                }
            }
        });
      /*  if (menu.color != null) {
            mCardView.setBackgroundColor(Color.parseColor(menu.color));
        }*/
        if (type == 1) {
            if (menu.color != null) {
                // mImageView.setColorFilter(Color.parseColor(menu.color), android.graphics.PorterDuff.Mode.MULTIPLY);
                mCardView.setCardBackgroundColor(Color.parseColor(menu.color));
            }
        } else {
            if (menu.color != null)
                mImageView.setColorFilter(Color.parseColor(menu.color), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }
}
