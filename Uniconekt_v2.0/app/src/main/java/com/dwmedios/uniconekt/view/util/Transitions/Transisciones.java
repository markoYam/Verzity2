package com.dwmedios.uniconekt.view.util.Transitions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Transisciones {
    public static final String TRANSITION_VIDEO = "video_transitions";

    public static void Dw_getTransation(Activity mActivity, View mView, String key) {
        try {
            String transaction = mActivity.getIntent().getExtras().getString(key);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mView.setTransitionName(transaction);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void Dw_setTransaction(View mTransaction, String key) {
        ViewCompat.setTransitionName(mTransaction, key);
    }

    public static void Dw_CreateTransactions(Intent mIntent, String keyTransactions, View mImageView, Activity mContext) {

        if (mIntent != null) {
            try {
                mIntent.putExtra(keyTransactions, ViewCompat.getTransitionName(mImageView));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        mContext,
                        mImageView,//new Pair<View,String>("vista","nombre transicion");
                        ViewCompat.getTransitionName(mImageView));
                mContext.startActivity(mIntent, options.toBundle());
            } catch (Exception ex) {
                Toast.makeText(mContext, "No es posible visualizar el detalle.", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }

        } else {
            Log.e("set transaction", "error intent is null");
        }
    }
}
