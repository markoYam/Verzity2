package com.dwmedios.uniconekt.view.util;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;

public class toast {
    private Activity mActivity;
    private Toast mToast;
    private handleActions mHandleActions;
    private View viewMaster;
    private int layout;
    private int layoutRoot;
    private Object mObject;
    private CountDownTimer toastCountDown;
    public interface handleActions {
        void Onclick(View mView, Object object);
    }

    public toast(Activity mActivity) {
        this.mActivity = mActivity;
        mToast = new Toast(mActivity);
    }

    public void setObject(Object mObject) {
        this.mObject = mObject;
    }

    public void setview(int layout, int layoutRoot) {
        this.layout = layout;
        this.layoutRoot = layoutRoot;
    }

    public void setdleActions(toast.handleActions mHandleActions) {
        this.mHandleActions = mHandleActions;
    }

    public void setDurattion(int duration) {
        mToast.setDuration(duration);
    }
public void duratioCustom(int i)
{

    toastCountDown = new CountDownTimer(i, 1000 /*Tick duration*/) {
        public void onTick(long millisUntilFinished) {
            mToast.show();
        }
        public void onFinish() {
            mToast.cancel();
        }

    };
}
    public void show() {
        if (layout != 0 && layoutRoot != 0) {
            viewMaster = LayoutInflater.from(mActivity).inflate(layout, (ViewGroup) mActivity.findViewById(layoutRoot));
            if (viewMaster != null) {
                if (mHandleActions != null) mHandleActions.Onclick(viewMaster, mObject);
                mToast = new Toast(mActivity);
                mToast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
                mToast.setView(viewMaster);
                mToast.show();
                if(toastCountDown!=null) {
                    toastCountDown.start();
                }
            } else {
                Toast.makeText(mActivity, "Ingrese una vista valida", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mActivity, "Ingrese una vista valida", Toast.LENGTH_SHORT).show();
        }
    }
}
