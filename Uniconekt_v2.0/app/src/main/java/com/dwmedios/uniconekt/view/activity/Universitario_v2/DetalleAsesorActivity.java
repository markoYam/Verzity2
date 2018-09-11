package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class DetalleAsesorActivity extends BaseActivity {
    public static final String KEY_USUARIO = "KEY-ASESOR";
    @BindView(R.id.relative_asesor)
    RelativeLayout mRelativeLayout;
    Persona mPersona;
    @BindView(R.id.nombre)
    TextView mTextViewNombre;
    @BindView(R.id.profile_image)
    ImageView mImageView;
    @BindView(R.id.buttonTelefono)
    ImageButton mImageButtonTelefono;
    @BindView(R.id.buttonCorreo)
    ImageButton mImageButtonCorreo;
    @BindView(R.id.buttonskpe)
    ImageButton mImageButtonSkype;
    @BindView(R.id.textViewInfo)
    TextView mTextView;
    @BindView(R.id.cardview)
    CardView mCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_asesor);
        ButterKnife.bind(this);
        mPersona = getIntent().getExtras().getParcelable(KEY_USUARIO);
        setMargin();
        setInfo();
    }

    private void setMargin() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(40, 0, 40, 0);
        mRelativeLayout.setLayoutParams(layoutParams);
    }

    private void setInfo() {
        mImageButtonCorreo.setOnClickListener(mOnClickListener);
        mImageButtonTelefono.setOnClickListener(mOnClickListener);
        mImageButtonSkype.setOnClickListener(mOnClickListener);
        mTextViewNombre.setText(mPersona.nombre);
        if (!isNullOrEmpty(mPersona.foto))
            Glide
                    .with(DetalleAsesorActivity.this)
                    .load(getUrlImage(mPersona.foto, getApplicationContext()))
                    .into(mImageView);
        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
        in.setDuration(500);
        mImageView.startAnimation(in);
        mImageView.setVisibility(View.VISIBLE);
    }

    Handler mHandler = new Handler();
    Runnable mRunnable;

    private void changeVisivility(final String text) {
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }

        if (mCardView.getVisibility() == View.VISIBLE) {
            Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
            out.setDuration(500);
            mCardView.startAnimation(out);
            mCardView.setVisibility(View.INVISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText("información no disponible");
                    Animation in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                    in.setDuration(500);
                    mCardView.startAnimation(in);
                    mCardView.setVisibility(View.VISIBLE);
                    if (!isNullOrEmpty(text))
                        mTextView.setText(text);
                    else
                        mTextView.setText("información no disponible");

                }
            }, 500);

        } else {
            Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
            in.setDuration(500);
            mCardView.startAnimation(in);
            mCardView.setVisibility(View.VISIBLE);
            if (!isNullOrEmpty(text))
                mTextView.setText(text);
            else
                mTextView.setText("información no disponible");
        }

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mCardView.getVisibility() == View.VISIBLE) {
                    Animation out = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                    out.setDuration(500);
                    mCardView.startAnimation(out);
                    mCardView.setVisibility(View.INVISIBLE);
                    mImageButtonTelefono.setColorFilter(getResources().getColor(R.color.colorBlanco));
                    mImageButtonCorreo.setColorFilter(getResources().getColor(R.color.colorBlanco));
                    mImageButtonSkype.setColorFilter(getResources().getColor(R.color.colorBlanco));
                } else {
                    mHandler.removeCallbacksAndMessages(this);
                }
            }
        };
        mHandler.postDelayed(mRunnable, 5000);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonTelefono:
                    changeVisivility(mPersona.telefono);
                    mImageButtonTelefono.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
                    mImageButtonCorreo.setColorFilter(getResources().getColor(R.color.colorBlanco));
                    mImageButtonSkype.setColorFilter(getResources().getColor(R.color.colorBlanco));
                    break;
                case R.id.buttonCorreo:
                    changeVisivility(mPersona.correo);
                    mImageButtonTelefono.setColorFilter(getResources().getColor(R.color.colorBlanco));
                    mImageButtonCorreo.setColorFilter(getResources().getColor(R.color.colorGris));
                    mImageButtonSkype.setColorFilter(getResources().getColor(R.color.colorBlanco));
                    break;
                case R.id.buttonskpe:
                    changeVisivility(mPersona.skype);
                    mImageButtonTelefono.setColorFilter(getResources().getColor(R.color.colorBlanco));
                    mImageButtonCorreo.setColorFilter(getResources().getColor(R.color.colorBlanco));
                    mImageButtonSkype.setColorFilter(getResources().getColor(R.color.Color_skype));
                    break;
            }

        }
    };
}
