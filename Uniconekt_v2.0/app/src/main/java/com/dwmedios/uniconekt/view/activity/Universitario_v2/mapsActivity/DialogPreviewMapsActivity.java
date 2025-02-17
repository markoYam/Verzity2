package com.dwmedios.uniconekt.view.activity.Universitario_v2.mapsActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.internal.Utility.isNullOrEmpty;

public class DialogPreviewMapsActivity extends BaseActivity {

    public static final String KEY_UNIVERSIDAD_DIALOGO = "KEY_:UNI56852453582154535";
    public static final String KEY_RESULT = "SDSDSDDS";
    private Universidad mUniversidad;
    @BindView(R.id.textViewNombreUniversidad)
    TextView mTextViewNombre;
    @BindView(R.id.textViewDescripcionUniversidad)
    TextView mTextViewDescripcion;
    @BindView(R.id.buttonCancelar)
    Button mButtonCancelar;
    @BindView(R.id.buttonVer)
    Button mButtonVer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_preview_maps);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setGravity(Gravity.TOP);
        //getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ButterKnife.bind(this);
        loadDialog();
    }

    private void loadDialog() {
        try {
            mUniversidad = getIntent().getExtras().getParcelable(KEY_UNIVERSIDAD_DIALOGO);
            mTextViewNombre.setText(mUniversidad.nombre);
            if (!isNullOrEmpty(mUniversidad.descripcion)) {
                String des = mUniversidad.descripcion.replace("\r", "");
                String des2 = des.replace("\n", "");
                mTextViewDescripcion.setText(des2);
            } else {
                mTextViewDescripcion.setText("");
            }
            mButtonCancelar.setOnClickListener(mOnClickListener);
            mButtonVer.setOnClickListener(mOnClickListener);
        } catch (Exception ex) {
            ex.printStackTrace();
            showMessage("No es posible visualizar el detalle de la universidad");
            finish();
        }

    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonCancelar:
                    finish();
                    break;
                case R.id.buttonVer:
                    Intent mIntent = getIntent();
                    mIntent.putExtra(KEY_RESULT, mUniversidad);
                    setResult(RESULT_OK, mIntent);
                    exit = true;
                    finish();
                    break;
            }

        }
    };
    private boolean exit = false;

    @Override
    public void finish() {
        if (!exit)
            finalizar();
        super.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //finalizar();
    }

    public void finalizar() {
        Intent mIntent = getIntent();
        setResult(RESULT_CANCELED, mIntent);
        // finish();
    }
}
