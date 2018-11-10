package com.dwmedios.uniconekt.view.adapter.holder;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.view.adapter.MenuAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.internal.Utility.isNullOrEmpty;

public class MenuHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageMenu)
    protected ImageView mImageView;
    @BindView(R.id.texviewNombrMenu)
    protected TextView mTextView;
    @BindView(R.id.cardviewMenu)
    protected CardView mCardView;
    @BindView(R.id.imagebuttom)
    ImageButton mImageButton;

    @BindView(R.id.texviewDesItemMenu)
    TextView mTextViewDesItem;

    public MenuHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void ConfigureMenu(final ClasViewModel.menu menu, final MenuAdapter.onclick mOnclick, int type) {
        if (menu.drawableImage != 0)
            mImageView.setImageResource(menu.drawableImage);
        if (type == 1) {
            if (menu.drawableBaground != 0)
                mImageView.setBackgroundResource(menu.drawableBaground);
            if (!isNullOrEmpty(menu.descripcion)) {
                mTextViewDesItem.setText(menu.descripcion);
            }
        }
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
                //   mImageView.setColorFilter(Color.parseColor(menu.color));
            }
        } else {
            if (menu.color != null) {
                mImageView.setColorFilter(Color.parseColor(menu.color), android.graphics.PorterDuff.Mode.MULTIPLY);
                mImageView.setColorFilter(Color.parseColor(menu.color), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }
    }
}
