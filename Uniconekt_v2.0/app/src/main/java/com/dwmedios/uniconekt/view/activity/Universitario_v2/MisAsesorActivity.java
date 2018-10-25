package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.presenter.AsesorPresenter;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.ImageUtils;
import com.dwmedios.uniconekt.view.viewmodel.AsesoresViewController;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.internal.Utility.isNullOrEmpty;

public class MisAsesorActivity extends BaseActivity implements AsesoresViewController {
    private AsesorPresenter mAsesorPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_asesor);
        ButterKnife.bind(this);

        TextView textTolbar = mToolbar.findViewById(R.id.toolbar_title);
        mToolbar.setTitle("");
        textTolbar.setText("MI ASESOR");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAsesorPresenter = new AsesorPresenter(this, getApplicationContext());

        mCardViewSkype.setOnClickListener(mOnClickListener);
        mCardViewCorreo.setOnClickListener(mOnClickListener);
        mCardViewTelefono.setOnClickListener(mOnClickListener);
        mFloatingActionButton.setOnClickListener(mOnClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAsesorPresenter.getMisAsesores();
    }

    @Override
    public void Onsucces(List<Persona> mPersonaList) {
        mCardViewError.setVisibility(View.GONE);
        this.mPersona = mPersonaList.get(0);
        setdata(mPersona);
    }

    private void setdata(Persona mPersona) {
        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
        Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        Animation slide_des = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_in_asesor);
        fadeIn.setDuration(500);
        slide.setDuration(500);
        if (!isNullOrEmpty(mPersona.foto)) {
            Glide.with(getApplicationContext())
                    .load(ImageUtils.getUrlImage(mPersona.foto, getApplicationContext()))
                    .into(mImageView);
            mImageView.startAnimation(fadeIn);
            mImageView.setVisibility(View.VISIBLE);
        } else {
            mImageView.setVisibility(View.VISIBLE);
            mImageView.setImageResource(R.drawable.profile);
        }
        if (!isNullOrEmpty(mPersona.nombre)) {
            mTextViewNombreAsesor.setVisibility(View.VISIBLE);
            mTextViewNombreAsesor.setText(mPersona.nombre);
            mTextViewNombreAsesor.startAnimation(fadeIn);
        } else
            mTextViewNombreAsesor.setVisibility(View.GONE);
        int mostrarAnim = 0;
        if (!isNullOrEmpty(mPersona.telefono)) {
            mCardViewTelefono.setVisibility(View.VISIBLE);
            mTextViewTelefono.setText(mPersona.telefono);
            mostrarAnim++;
        } else {
            mCardViewTelefono.setVisibility(View.GONE);
        }
        if (!isNullOrEmpty(mPersona.correo)) {
            mCardViewCorreo.setVisibility(View.VISIBLE);
            mTextViewCorreo.setText(mPersona.correo);
            mostrarAnim++;
        } else {
            mCardViewCorreo.setVisibility(View.GONE);
        }
        if (!isNullOrEmpty(mPersona.skype)) {
            mCardViewSkype.setVisibility(View.VISIBLE);
            mTextViewSkype.setText(mPersona.skype);
            mostrarAnim++;
        } else {
            mCardViewSkype.setVisibility(View.GONE);
        }
        if (mostrarAnim > 0) {
            mCardViewGeneral.startAnimation(slide);
            mCardViewGeneral.setVisibility(View.VISIBLE);
        }
        if (!isNullOrEmpty(mPersona.desPersona)) {
            mCardViewDescripcion.startAnimation(slide_des);
            mTextViewDescripcion.setText(mPersona.desPersona);
            mCardViewDescripcion.setVisibility(View.VISIBLE);

        } else {
            mCardViewDescripcion.setVisibility(View.GONE);
        }
//Poner animaciones

    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cardviewTelefono:
                    call();
                    break;
                case R.id.cardviewCorreo:
                    sendMail();
                    break;
                case R.id.cardviewSkype:
                    callSkype();
                    break;
                case R.id.fab:
                    startActivity(new Intent(getApplicationContext(), PaquetesAsesoresActivity.class));
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

    @Override
    public void Onfailed(String mensaje) {
        showMessage(mensaje);
        mCardViewGeneral.setVisibility(View.GONE);
        mCardViewDescripcion.setVisibility(View.GONE);
        mImageView.setVisibility(View.GONE);
        mTextViewNombreAsesor.setVisibility(View.GONE);
        //  finish();
        Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        slide.setDuration(500);
        mCardViewError.startAnimation(slide);
        mCardViewError.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext())
                .load(R.drawable.sademoji)
                .into(mImageViewError);
    }

    @Override
    public void OnLoading(boolean isLoading) {
        if (isLoading)
            showOnProgressDialog("Cargando...");
        else
            dismissProgressDialog();
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.profile_image)
    ImageView mImageView;
    @BindView(R.id.textViewNombreAsesor)
    TextView mTextViewNombreAsesor;

    //contenedores
    @BindView(R.id.contenedor_General)
    CardView mCardViewGeneral;
    @BindView(R.id.contenedor_General_descripcion)
    CardView mCardViewDescripcion;

    //controles
    @BindView(R.id.cardviewTelefono)
    CardView mCardViewTelefono;
    @BindView(R.id.textViewTelefono)
    TextView mTextViewTelefono;

    @BindView(R.id.cardviewCorreo)
    CardView mCardViewCorreo;
    @BindView(R.id.textViewCorreo)
    TextView mTextViewCorreo;

    @BindView(R.id.cardviewSkype)
    CardView mCardViewSkype;

    @BindView(R.id.textViewSkype)
    TextView mTextViewSkype;

    @BindView(R.id.textViewDescripcionAsesor)
    TextView mTextViewDescripcion;
    Persona mPersona;

    @BindView(R.id.contenedorError)
    CardView mCardViewError;
    @BindView(R.id.imageviewError)
    ImageView mImageViewError;

    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;
}
