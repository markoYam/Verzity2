package com.dwmedios.uniconekt.view.util;

import android.app.Activity;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;

import static com.facebook.internal.Utility.isNullOrEmpty;

public class CustomToast {
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private String mensaje;
    public static CustomToast mCustomToast;
    private tipo mTipo;
    private int duration;

    public CustomToast(Activity mActivity) {
        this.mActivity = mActivity;
        this.mLayoutInflater = LayoutInflater.from(mActivity);
    }

    public static synchronized CustomToast getInstance(Activity mActivity) {
        if (mCustomToast == null) {
            mCustomToast = new CustomToast(mActivity);
        }
        return mCustomToast;
    }

    public CustomToast setype(tipo mTipo) {
        if (mCustomToast == null) {
            mCustomToast = new CustomToast(mActivity);
        }
        mCustomToast.mTipo = mTipo;
        return mCustomToast;
    }

    public CustomToast setMessage(String mensaje) {
        if (mCustomToast == null) {
            mCustomToast = new CustomToast(mActivity);
        }
        mCustomToast.mensaje = mensaje;
        return mCustomToast;
    }

    public CustomToast setDuration(int duration) {
        if (mCustomToast == null) {
            mCustomToast = new CustomToast(mActivity);
        }
        mCustomToast.duration = duration;
        return mCustomToast;
    }

    public void build() {
        View master = mLayoutInflater.inflate(R.layout.custom_toast,
                (ViewGroup) mActivity.findViewById(R.id.contenedor));
        ImageView mImageView = master.findViewById(R.id.imageType);
        TextView mTextView = master.findViewById(R.id.textViewTitulo);
        if (isNullOrEmpty(mCustomToast.mensaje)) {
            mTextView.setVisibility(View.GONE);
        } else {
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(mCustomToast.mensaje);
        }
        switch (mCustomToast.mTipo) {
            case succes:
                /*Glide.with(mCustomToast.mActivity)
                        .load(R.raw.checkmark)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .into(mImageView);*/
                break;
            case error:

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animation in = AnimationUtils.loadAnimation(mCustomToast.mActivity, android.R.anim.fade_in);
            //in.setDuration(200);
            mImageView.startAnimation(in);
            mImageView.setVisibility(View.VISIBLE);
        }
        Toast toast = new Toast(mCustomToast.mActivity);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(mCustomToast.duration);
        toast.setView(master);
        toast.show();
    }

    public enum tipo {
        succes,
        error,
        failed
    }
}
