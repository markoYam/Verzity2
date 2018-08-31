package com.dwmedios.uniconekt.view.activity.View_Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;

public class Dialog_user extends BaseActivity {
    public static final String KEY_CUENTA = "cuenta";
    public static final String RESULTADO = "res";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog_user);
        ButterKnife.bind(this);

        configureView();
    }

    private void configureView() {
        Persona mPersona = getIntent().getExtras().getParcelable(KEY_CUENTA);
        mTextViewNombre.setText(mPersona.nombre);
        if (mPersona.foto != null)
            this.urlFoto = getUrlImage(mPersona.foto, getApplicationContext());
        if (urlFoto != null) new taskImageP(urlFoto).execute();
        mImageButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = getIntent();
                setResult(Activity.RESULT_CANCELED, mIntent);
                finish();
            }
        });

        mImageButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = getIntent();
                setResult(Activity.RESULT_OK, mIntent);
                finish();
            }
        });
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
