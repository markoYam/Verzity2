package com.dwmedios.uniconekt.view.activity.Universidad;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.service.firebase.Notifications.NotificationsUtils;
import com.dwmedios.uniconekt.model.Notificaciones;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.PostuladosGeneral;
import com.dwmedios.uniconekt.presenter.DetalleNotificacionPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario.MainUniversitarioActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.viewmodel.DetalleNotificacionViewController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jackandphantom.blurimage.BlurImage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderItems;
import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderLight;
import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderUser;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.dwmedios.uniconekt.view.util.Utils.configurefecha;
import static com.dwmedios.uniconekt.view.util.Utils.configurefechaCompleted;
import static com.dwmedios.uniconekt.view.util.Utils.getDays;

public class DetalleNotificacionActivity extends BaseActivity implements DetalleNotificacionViewController {
    public static final String KEY_POSTULADO_DETALLE = "DETALLE_POSTULADO";
    public static int VIEW_POSTULADO_DETALLE = 0;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.texviewNombrePostulado)
    TextView mTextViewNombre;
    @BindView(R.id.textViewNombreNotificacionD)
    TextView mTextViewAsunto;
    @BindView(R.id.textViewDescripcionNotificaionD)
    TextView mTextViewDescripcion;

    @BindView(R.id.collapseTolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    //------------
    @BindView(R.id.texviewNombrePeNotDetalle)
    TextView mTextViewNombrePersona;
    @BindView(R.id.texviewCorreoPeNotDe)
    TextView mTextViewCorreo;
    @BindView(R.id.texviewTelefonoPeNotDe)
    TextView mTextViewTelefono;
    @BindView(R.id.texviewDireccionPeNotDe)
    TextView mTextViewDireccion;
    @BindView(R.id.profile_image)
    ImageView mImageView;
    @BindView(R.id.imageViewFotoDetalle)
    ImageView mImageViewBack;
    @BindView(R.id.imageButtonCorreo)
    ImageButton mImageButtonCorreo;
    @BindView(R.id.imageButtonTelefono)
    ImageButton mImageButtonTelefono;
    @BindView(R.id.textViewFechaNotificaionD)
    TextView mTextViewFecha;
    private DetalleNotificacionPresenter mDetalleNotificacionPresenter;
    private PostuladosGeneral mPostuladosGeneral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_notificacion);
        ButterKnife.bind(this);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDetalleNotificacionPresenter = new DetalleNotificacionPresenter(this, getApplicationContext());
        loadInfo();
        mImageButtonCorreo.setOnClickListener(mOnClickListener);
        mImageButtonTelefono.setOnClickListener(mOnClickListener);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (mPostuladosGeneral != null && mPostuladosGeneral.persona != null) {
                        mCollapsingToolbarLayout.setTitle(mPostuladosGeneral.persona.nombre);
                    }
                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadInfo() {
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
        if (VIEW_POSTULADO_DETALLE == 1) {
            try {
                mPostuladosGeneral = getIntent().getExtras().getParcelable(KEY_POSTULADO_DETALLE);
                if (mPostuladosGeneral != null) {
                    mTextViewAsunto.setText(mPostuladosGeneral.toString());
                    if (mPostuladosGeneral.fechaPostulacion != null)
                        mTextViewFecha.setText(getDays(mPostuladosGeneral.fechaPostulacion) + " " + configurefechaCompleted(mPostuladosGeneral.fechaPostulacion));
                    mTextViewDescripcion.setText(mPostuladosGeneral.getDetalle());
                    this.Onsucces(mPostuladosGeneral);
                }
            } catch (Exception e) {
                dismissProgressDialog();
                showMessage("No es posible visualizar el detalle de la notificación.");
                e.printStackTrace();
                finish();

            }
        } else {
            try {
                Notificaciones mNotificaciones = getIntent().getExtras().getParcelable("msg1");
                setDatos(mNotificaciones);
                mDetalleNotificacionPresenter.getDetalleNotificacion(mNotificaciones);
            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    Gson mGson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
                    String json = getIntent().getExtras().get("msg").toString();
                    Notificaciones mNotificaciones = mGson.fromJson(json, Notificaciones.class);
                    if (mNotificaciones != null) {
                        setDatos(mNotificaciones);
                        mDetalleNotificacionPresenter.getDetalleNotificacion(mNotificaciones);
                    }
                } catch (Exception e) {
                    dismissProgressDialog();
                    showMessage("No es posible visualizar el detalle de la notificación.");
                    e.printStackTrace();
                    finish();
                }

            }
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageButtonCorreo:
                    SendMail();
                    break;
                case R.id.imageButtonTelefono:
                    call();
                    break;
            }
        }
    };

    public void setDatos(Notificaciones mNotificaciones) {
        mTextViewAsunto.setText(mNotificaciones.asunto);
        if (mNotificaciones.fecha != null)
            mTextViewFecha.setText(getDays(mNotificaciones.fecha) + " " + configurefechaCompleted(mNotificaciones.fecha));
        mTextViewDescripcion.setText(mNotificaciones.mensaje);
    }

    @Override
    public void Onsucces(PostuladosGeneral not) {
        this.mPostuladosGeneral = not;
        if (not.persona != null) {
            mTextViewNombrePersona.setText(not.persona.nombre);
            mTextViewNombre.setText(not.persona.nombre);
            mTextViewCorreo.setText(not.persona.correo);
            mTextViewTelefono.setText(not.persona.telefono);
            if (not.persona.foto != null) {
                new taskImage(getUrlImage(not.persona.foto, getApplicationContext())).execute();
            }

            if (mPostuladosGeneral.beca != null && mPostuladosGeneral.beca.rutaImagen != null)
                //  ImageLoader.getInstance().displayImage(getUrlImage(mPostuladosGeneral.beca.rutaImagen, getApplicationContext()), mImageViewBack, OptionsImageLoaderLight);
                new taskImageAll(getUrlImage(mPostuladosGeneral.beca.rutaImagen,getApplicationContext()), mImageViewBack).execute();
            if (mPostuladosGeneral.financiamiento != null && mPostuladosGeneral.financiamiento.imagen != null)
                //ImageLoader.getInstance().displayImage(getUrlImage(mPostuladosGeneral.financiamiento.imagen, getApplicationContext()), mImageViewBack, OptionsImageLoaderLight);
                new taskImageAll(getUrlImage(mPostuladosGeneral.financiamiento.imagen,getApplicationContext()), mImageViewBack).execute();

            if (not.persona.direccion != null) {
                if (not.persona.direccion.pais != null)
                    if (not.persona.direccion.pais.equals("México")) {
                        String direccion = (not.persona.direccion.descripcion.isEmpty() ? "" : not.persona.direccion.descripcion + ", ") + not.persona.direccion.municipio + ", " + not.persona.direccion.estado + ", " + not.persona.direccion.pais;
                        mTextViewDireccion.setText(direccion);
                    } else {
                        mTextViewDireccion.setText(not.persona.direccion.pais);
                    }
            }
        }
    }

    @Override
    public void Onfailed(String mensaje) {
        showMessage("No es posible visualizar el detalle de la notificación.");
        finish();
    }

    public class taskImage extends AsyncTask<Void, Void, Void> {

        Bitmap bmp;
        public String key;

        public taskImage(String key) {
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
                mImageView.setImageBitmap(bmp);
            super.onPostExecute(aVoid);
        }
    }

    public class taskImageAll extends AsyncTask<Void, Void, Void> {

        Bitmap bmp;
        public String key;
        public ImageView mImageView;

        public taskImageAll(String key, ImageView mImageView) {
            this.key = key;
            this.mImageView = mImageView;
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
                BlurImage.with(getApplicationContext()).load(bmp).intensity(20).Async(true).into(mImageView);
              //  mImageView.setImageBitmap(bmp);
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void Onloading(Boolean loading) {
        if (loading) {
            showOnProgressDialog("Cargando...");

        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void SendMail() {
        if (mPostuladosGeneral != null) {
            Intent send = new Intent(Intent.ACTION_SENDTO);
            String uriText = "mailto:" + Uri.encode(mPostuladosGeneral.persona.correo) +
                    "?subject=" + Uri.encode("Hola " + mPostuladosGeneral.persona.nombre) +
                    "&body=" + Uri.encode("");
            Uri uri = Uri.parse(uriText);

            send.setData(uri);
            startActivity(Intent.createChooser(send, "Enviar correo electrónico."));
        }
    }

    @Override
    public void call() {
        if (mPostuladosGeneral != null) {
            String uri = "tel:" + mPostuladosGeneral.persona.telefono.trim();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        VIEW_POSTULADO_DETALLE = 0;
        super.onDestroy();
    }
}



