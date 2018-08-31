package com.dwmedios.uniconekt.view.fragments.base;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.widget.Toast;

public class BaseFragment extends android.support.v4.app.Fragment {
    public void showMessaje(String mensaje)
    {
        Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();
    }

    private ProgressDialog mProgressDialog;
    public void showOnProgressDialog(String message) {
        try {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(message);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgress(0);
            mProgressDialog.setMax(100);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissProgressDialog() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
