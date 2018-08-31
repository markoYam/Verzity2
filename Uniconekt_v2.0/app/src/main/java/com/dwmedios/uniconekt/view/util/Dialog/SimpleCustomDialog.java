package com.dwmedios.uniconekt.view.util.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.view.activity.Universitario.DatosUniversitarioActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;

public class SimpleCustomDialog {
    private Dialog mDialog;
    private int layout;
    private Activity mActivity;
    private Context mContext;
    private View master;
    @BindView(R.id.profile_image)
    ImageView mImageViewProfile;
    @BindView(R.id.textView_nombre_cuenta)
    TextView mTextViewNombre;
    @BindView(R.id.buttonCancel)
    Button mImageButtonCancel;
    @BindView(R.id.buttonOk)
    Button mImageButtonOk;
    @BindView(R.id.titulo_custom_dialog)
    TextView mTextViewTitulo;
    private String urlFoto = null;

    public SimpleCustomDialog(Activity mActivity, Context mContext) {
        this.mActivity = mActivity;
        this.mContext = mContext;
        this.layout = R.layout.simple_custom_dialog;
        master = View.inflate(mActivity, layout, null);
        ButterKnife.bind(this, master);
    }



    public void setOnClickListenerOK(View.OnClickListener mOnClickListener) {
        if (mOnClickListener != null) {
            mImageButtonOk.setOnClickListener(mOnClickListener);
        } else {
            mImageButtonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDialog != null) mDialog.dismiss();
                    Toast.makeText(mContext, "Ok", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setOnClickListenerCancel(View.OnClickListener mOnClickListener) {
        if (mOnClickListener != null) {
            mImageButtonOk.setOnClickListener(mOnClickListener);
        } else {
            mImageButtonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDialog != null) mDialog.dismiss();
                    Toast.makeText(mContext, "Ok", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void show() {
        mDialog = new Dialog(mActivity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setCancelable(false);
        mDialog.setContentView(master);
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.x = -100;
        mDialog.getWindow().setAttributes(params);
        mDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        new taskImageP(urlFoto).execute();
        mDialog.show();
    }

    private class taskImageP extends AsyncTask<Void, Void, Void> {

        Bitmap bmp;
        public String key;

        public taskImageP(String key) {
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(key);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (bmp != null)
                mImageViewProfile.setImageBitmap(bmp);
            super.onPostExecute(aVoid);
        }
    }
}
