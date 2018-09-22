package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
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
    CardView mImageButtonTelefono;
    @BindView(R.id.buttonCorreo)
    CardView mImageButtonCorreo;
    @BindView(R.id.buttonskpe)
    CardView mImageButtonSkype;

    @BindView(R.id.buttonskpe2)
    ImageButton skype;
    @BindView(R.id.buttonCorreo2)
    ImageButton correo;
    @BindView(R.id.buttonTelefono2)
    ImageButton telefono;

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
        skype.setOnClickListener(mOnClickListener);
        correo.setOnClickListener(mOnClickListener);
        telefono.setOnClickListener(mOnClickListener);
        mTextViewNombre.setText(mPersona.nombre);
        if (!isNullOrEmpty(mPersona.foto))
            Glide
                    .with(DetalleAsesorActivity.this)
                    .load(getUrlImage(mPersona.foto, getApplicationContext()))
                    .into(mImageView);
      /*  Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
        in.setDuration(500);
        mImageView.startAnimation(in);*/
        mImageView.setVisibility(View.VISIBLE);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonTelefono:
                    call();

                    break;
                case R.id.buttonTelefono2:
                    call();

                    break;
                case R.id.buttonCorreo:
                    sendMail();
                    break;
                case R.id.buttonCorreo2:
                    sendMail();
                    break;
                case R.id.buttonskpe:
                    callSkype();
                    break;
                case R.id.buttonskpe2:
                    callSkype();
                    break;
            }

        }
    };

    private void sendMail() {
        if (!isNullOrEmpty(mPersona.correo)) {
            Intent send = new Intent(Intent.ACTION_SENDTO);
            String uriText = "mailto:" + Uri.encode(mPersona.correo) +
                    "?subject=" + Uri.encode("Hola " + mPersona.nombre) +
                    "&body=" + Uri.encode("");
            Uri uri = Uri.parse(uriText);

            send.setData(uri);
            startActivity(Intent.createChooser(send, "Enviar correo electrónico."));
        }
    }

    private void callSkype() {
        try {
            showMessage("Skype: " + mPersona.skype);
            Intent sky = new Intent("android.intent.action.VIEW");
            sky.setData(Uri.parse("skype:" + mPersona.skype));
            Log.d("UTILS", "tel:" + mPersona.skype);
            startActivity(sky);
        } catch (ActivityNotFoundException e) {
            showMessage("No se encuentra instalado la aplicación de Skype");
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.skype.raider")));
        }
    }

    private void call() {
        if (!isNullOrEmpty(mPersona.telefono)) {
            String uri = "tel:" + mPersona.telefono;
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }
    }
}
