package com.dwmedios.uniconekt.view.activity.Universitario;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Financiamientos;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.PostuladosFinanciamientos;
import com.dwmedios.uniconekt.presenter.FinanciamientoDetallePresenter;
import com.dwmedios.uniconekt.view.activity.PDFViewerActivity;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.UniversidadDetalleActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.ImageUtils;
import com.dwmedios.uniconekt.view.viewmodel.FinaciamientoDetalleView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.dwmedios.uniconekt.view.activity.PDFViewerActivity.KEY_VIEWER;
import static com.dwmedios.uniconekt.view.activity.PDFViewerActivity.KEY_VIEWER_NOMBRE;
import static com.dwmedios.uniconekt.view.activity.Universitario.DatosUniversitarioActivity.KEY_REGISTRO_UNIVERSITARIO;
import static com.dwmedios.uniconekt.view.activity.Universitario.DetalleBecasActivity.CODE_RESULT;
import static com.dwmedios.uniconekt.view.activity.Universitario.DetalleUniversidadActivity.KEY_DETALLE_UNIVERSIDAD;
import static com.dwmedios.uniconekt.view.activity.Universitario.FinanciamientoActivity.TYPE_VIEW_F;
import static com.dwmedios.uniconekt.view.util.Utils.setStatusBarGradiant;

public class DetalleFinanciamientoActivity extends BaseActivity implements FinaciamientoDetalleView {
    public static final String KEY_FINANCIAMIENTO_DETALLE = "key_financiamientos";
    @BindView(R.id.textViewNombreUniFinanciamiento)
    TextView mTextViewUniversidad;
    @BindView(R.id.textViewNombreFinanciamientoDetalle)
    TextView mTextViewnombre;
    @BindView(R.id.textViewDesFinanciamiento)
    TextView mTextViewDescripcion;
    @BindView(R.id.imageViewFotoFinanciamientoDetalle)
    ImageView mImageView;
    @BindView(R.id.imageButtonVerUniversidadFinanciamiento)
    ImageButton mImageButtonUniversidad;
    @BindView(R.id.textViewSitio)
    TextView mTextViewSitio;
    @BindView(R.id.textViewArchivonanciamiento)
    TextView mTextViewArchivo;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.imageButtonArchivoFinanciamiento)
    ImageButton imageButtonArchivo;
    @BindView(R.id.fab)
    com.getbase.floatingactionbutton.FloatingActionButton mFloatingActionButton;
    @BindView(R.id.menu_fab)
    com.getbase.floatingactionbutton.FloatingActionsMenu mFloatingActionButtonMenu;

    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapseTolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    private FinanciamientoDetallePresenter mFinanciamientoDetallePresenter;
    private Financiamientos mFinanciamientos;
    private Animation open_Fab, close_Fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_financiamiento);
        supportPostponeEnterTransition();
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Cambiar el color del toolbar y status bar
        setStatusBarGradiant(this, R.drawable.status_financiamiento);

        mFinanciamientoDetallePresenter = new FinanciamientoDetallePresenter(this, getApplicationContext());
        mFinanciamientoDetallePresenter.LoadInfo();
        if (TYPE_VIEW_F == 2)
            mImageButtonUniversidad.setVisibility(View.GONE);
        mImageButtonUniversidad.setOnClickListener(mOnClickListener);
        imageButtonArchivo.setOnClickListener(mOnClickListener);
        mFloatingActionButton.setOnClickListener(mOnClickListener);
        open_Fab = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.open_fab);
        close_Fab = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.close_fab);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (mFinanciamientos.nombre != null) {
                        mCollapsingToolbarLayout.setTitle(mFinanciamientos.nombre);
                    }
                    //mFloatingActionButtonMenu.startAnimation(close_Fab);
                    mFloatingActionButtonMenu.setVisibility(View.GONE);
                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    //mFloatingActionButtonMenu.startAnimation(open_Fab);
                    mFloatingActionButtonMenu.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });
    }

    @Override
    protected void onStop() {
        TYPE_VIEW_F=1;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        TYPE_VIEW_F=1;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                break;
            case R.id.menu_inicio_universidad:
                Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageButtonVerUniversidadFinanciamiento:
                    if (mFinanciamientos != null) {
                        Intent mIntent = new Intent(getApplicationContext(), UniversidadDetalleActivity.class);
                        mIntent.putExtra(KEY_DETALLE_UNIVERSIDAD, mFinanciamientos.universidad);
                        startActivity(mIntent);
                    }
                    break;
                case R.id.imageButtonArchivoFinanciamiento:
                    if (mFinanciamientos.archivo != null) {
                        List<String> permiso = new ArrayList<>();
                        permiso.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        permiso.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (validatePermison(permiso, DetalleFinanciamientoActivity.this, 80)) {
                            startActivity(new Intent(getApplicationContext(), PDFViewerActivity.class).putExtra(KEY_VIEWER, mFinanciamientos.archivo).putExtra(KEY_VIEWER_NOMBRE, mFinanciamientos.nombre).addFlags(FLAG_ACTIVITY_NEW_TASK));
                        }
                    }
                    break;
                case R.id.fab:
                    mFinanciamientoDetallePresenter.validateUser();
                    break;
            }
        }
    };

    @Override
    public void Onfailded(String mensaje) {
        showMessage(mensaje);
    }

    @Override
    public void ConfigureLoad() {
        try {
            Financiamientos mFinanciamientos = getIntent().getExtras().getParcelable(KEY_FINANCIAMIENTO_DETALLE);
            this.Onsuccess(mFinanciamientos);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No es posible visualizar el detalle", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void Onsuccess(Financiamientos mFinanciamientos) {
        this.mFinanciamientos = mFinanciamientos;
        if (mFinanciamientos.imagen != null)
            ImageLoader.getInstance().displayImage(ImageUtils.getUrlImage(mFinanciamientos.imagen, getApplicationContext()), mImageView, ImageUtils.OptionsImageLoaderItems);

        mTextViewnombre.setText(mFinanciamientos.nombre);
        mTextViewDescripcion.setText(mFinanciamientos.descripcion);
        if (mFinanciamientos.universidad != null) {
            mTextViewUniversidad.setText(mFinanciamientos.universidad.nombre);
            mImageButtonUniversidad.setVisibility(View.VISIBLE);
        } else {
            mImageButtonUniversidad.setVisibility(View.GONE);
        }
        if (mFinanciamientos.sitio != null)
            mTextViewSitio.setText(mFinanciamientos.sitio);
        else {
            LinearLayout sitio = (LinearLayout) mTextViewSitio.getParent();
            sitio.setVisibility(View.GONE);
        }
        if (mFinanciamientos.archivo != null) {
            mTextViewArchivo.setText("Ver documento adjunto");
            imageButtonArchivo.setVisibility(View.VISIBLE);
        } else {
            // imageButtonArchivo.setVisibility(View.VISIBLE);
            LinearLayout archivo = (LinearLayout) mTextViewArchivo.getParent();
            archivo.setVisibility(View.GONE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImageView.setTransitionName(FinanciamientoActivity.KEY_TRANSICIONES_1);
            supportStartPostponedEnterTransition();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void Onloading(boolean loading) {
        if (loading) {
            showOnProgressDialog("Cargando...");
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void OnSuccesPostular(PostuladosFinanciamientos mPostuladosFinanciamientos) {
        mFinanciamientoDetallePresenter.updatePersona(mPostuladosFinanciamientos.mPersona);
    }

    @Override
    public void Postular(Persona mPersona) {
        PostuladosFinanciamientos mPostuladosFinanciamientos = new PostuladosFinanciamientos();
        mPostuladosFinanciamientos.id_financiamiento = mFinanciamientos.id;
        mPostuladosFinanciamientos.id_persona = mPersona.id;
        mPostuladosFinanciamientos.mPersona = mPersona;
        mPostuladosFinanciamientos.mFinanciamientos = mFinanciamientos;
        mFinanciamientoDetallePresenter.solicitarFinanciamiento(mPostuladosFinanciamientos);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 80) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mFinanciamientos.archivo != null) {
                    List<String> permiso = new ArrayList<>();
                    permiso.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    permiso.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (validatePermison(permiso, DetalleFinanciamientoActivity.this, 80)) {
                        startActivity(new Intent(getApplicationContext(), PDFViewerActivity.class).putExtra(KEY_VIEWER, mFinanciamientos.archivo).putExtra(KEY_VIEWER_NOMBRE, mFinanciamientos.nombre).addFlags(FLAG_ACTIVITY_NEW_TASK));
                    }
                }
                return;
            } else {

                showInfoDialogListener("Atención", "Es nesesario otorgar los permisos para continuar", "OK");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_RESULT) {
                Bundle mBundle = data.getExtras();
                Persona mPersona = mBundle.getParcelable(KEY_REGISTRO_UNIVERSITARIO);
                PostuladosFinanciamientos mPostuladosFinanciamientos = new PostuladosFinanciamientos();
                mPostuladosFinanciamientos.id_financiamiento = mFinanciamientos.id;
                mPostuladosFinanciamientos.id_persona = mPersona.id;
                mPostuladosFinanciamientos.mPersona = mPersona;
                mPostuladosFinanciamientos.mFinanciamientos = mFinanciamientos;
                mFinanciamientoDetallePresenter.solicitarFinanciamiento(mPostuladosFinanciamientos);
            }
        }
    }

    @Override
    public void Postular() {
        Intent mIntent = new Intent(getApplicationContext(), DatosUniversitarioActivity.class);
        startActivityForResult(mIntent, CODE_RESULT);
    }
}
