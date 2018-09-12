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

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Becas;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.PostuladosBecas;
import com.dwmedios.uniconekt.presenter.BecasDetallePresenter;
import com.dwmedios.uniconekt.view.activity.PDFViewerActivity;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.UniversidadDetalleActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.ImageUtils;
import com.dwmedios.uniconekt.view.viewmodel.BecasDetalleViewController;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.dwmedios.uniconekt.view.activity.PDFViewerActivity.KEY_VIEWER;
import static com.dwmedios.uniconekt.view.activity.PDFViewerActivity.KEY_VIEWER_NOMBRE;
import static com.dwmedios.uniconekt.view.activity.Universitario.BecasActivity.TYPE_VIEW;
import static com.dwmedios.uniconekt.view.activity.Universitario.DatosUniversitarioActivity.KEY_REGISTRO_UNIVERSITARIO;
import static com.dwmedios.uniconekt.view.activity.Universitario.DetalleUniversidadActivity.KEY_DETALLE_UNIVERSIDAD;
import static com.dwmedios.uniconekt.view.util.Utils.changeColorToolbar;
import static com.dwmedios.uniconekt.view.util.Utils.setStatusBarGradiant;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class DetalleBecasActivity extends BaseActivity implements BecasDetalleViewController {
    public static String KEY_BECA_DETALLE = "key_beca_detalle";
    public static int CODE_RESULT = 2;

    @BindView(R.id.imageViewFotoBecaDetalle)
    ImageView mImageView;
    @BindView(R.id.textViewNombreBecaDetalle)
    TextView mTextViewNombreBeca;
    @BindView(R.id.textViewDescripcionBecaDetalle)
    TextView mTextViewDescripcion;
    @BindView(R.id.textViewNombreUniversidadBecaDetalle)
    TextView mTextViewUniversidad;
    @BindView(R.id.imageButtonVerUniversidadBecaDetalle)
    ImageButton mImageButtonVerUniversidad;
    @BindView(R.id.collapseTolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.imageButtonArchivoBeca)
    ImageButton mImageButtonArchivo;
    @BindView(R.id.textViewArchivoBeca)
    TextView mTextViewArchivo;
    @BindView(R.id.fab)
    com.getbase.floatingactionbutton.FloatingActionButton mFloatingActionButton;
    @BindView(R.id.menu_fab)
    com.getbase.floatingactionbutton.FloatingActionsMenu mFloatingActionButtonMenu;
    private Becas mBecas;
    private BecasDetallePresenter mBecasDetallePresenter;
    private Animation open_Fab, close_Fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_becas);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Cambiar el color del toolbar y status bar
        setStatusBarGradiant(this, R.drawable.status_beca);
        // changeColorToolbar(getSupportActionBar(), R.color.Color_becas, DetalleBecasActivity.this);
        supportPostponeEnterTransition();
        mBecasDetallePresenter = new BecasDetallePresenter(this, getApplicationContext());
        mBecasDetallePresenter.loadInfo();
        if (TYPE_VIEW == 2)
            mImageButtonVerUniversidad.setVisibility(View.GONE);
        mImageButtonVerUniversidad.setOnClickListener(mOnClickListener);
        mFloatingActionButton.setOnClickListener(mOnClickListener);
        mImageButtonArchivo.setOnClickListener(mOnClickListener);
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
                    if (mBecas != null && mBecas.nombre != null) {
                        mCollapsingToolbarLayout.setTitle(mBecas.nombre);
                        //  mFloatingActionButtonMenu.startAnimation(close_Fab);
                        mFloatingActionButtonMenu.setVisibility(View.GONE);
                    }
                    changeColorToolbar(getSupportActionBar(), R.color.Color_becas, DetalleBecasActivity.this);
                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbarLayout.setTitle(" ");
                    //mFloatingActionButtonMenu.startAnimation(open_Fab);
                    mFloatingActionButtonMenu.setVisibility(View.VISIBLE);
                    isShow = false;
                    changeColorToolbar(getSupportActionBar(), R.color.colorTrasparente, DetalleBecasActivity.this);
                }
            }
        });


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
                finish();
                break;
            case R.id.menu_inicio_universidad:
                Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void configureLoad() {
        try {
            mBecas = getIntent().getExtras().getParcelable(KEY_BECA_DETALLE);
            this.onSucces(mBecas);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mImageView.setTransitionName(BecasActivity.KEY_TRANSICION_BECA_1);
                //mTextViewNombreBeca.setTransitionName(BecasActivity.KEY_TRANSICION_BECA_2);
                supportStartPostponedEnterTransition();

            }

        } catch (Exception e) {
            e.printStackTrace();
            this.OnFailed("Ocurrio un error");
            finish();
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageButtonVerUniversidadBecaDetalle:
                    if (mBecas != null) {
                        Intent mIntent = new Intent(getApplicationContext(), UniversidadDetalleActivity.class);
                        mIntent.putExtra(KEY_DETALLE_UNIVERSIDAD, mBecas.mUniversidad);
                        startActivity(mIntent);
                    } else {
                        showMessage("No se encontró la universidad.");
                    }
                    break;
                case R.id.fab:
                    mBecasDetallePresenter.validateUser();
                    break;
                case R.id.imageButtonArchivoBeca:
                    if (mBecas.rutaArchivo != null) {
                        List<String> permiso = new ArrayList<>();
                        permiso.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        permiso.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (validatePermison(permiso, DetalleBecasActivity.this, 80))
                            // new Utils.DownloadFiles(mBecas.rutaArchivo, mBecas.nombre, getApplicationContext(), DetalleBecasActivity.this).execute();
                            startActivity(new Intent(getApplicationContext(), PDFViewerActivity.class).putExtra(KEY_VIEWER, mBecas.rutaArchivo).putExtra(KEY_VIEWER_NOMBRE, mBecas.nombre).addFlags(FLAG_ACTIVITY_NEW_TASK));

                    }
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 80) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mBecas.rutaArchivo != null) {
                    List<String> permiso = new ArrayList<>();
                    permiso.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    permiso.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (validatePermison(permiso, DetalleBecasActivity.this, 80))
                        // new Utils.DownloadFiles(mBecas.rutaArchivo, mBecas.nombre, getApplicationContext(), DetalleBecasActivity.this).execute();
                        startActivity(new Intent(getApplicationContext(), PDFViewerActivity.class).putExtra(KEY_VIEWER, mBecas.rutaArchivo).putExtra(KEY_VIEWER_NOMBRE, mBecas.nombre).addFlags(FLAG_ACTIVITY_NEW_TASK));

                }
                return;
            } else {

                showInfoDialogListener("Atención", "Es nesesario otorgar los permisos para continuar", "OK");
            }
        }
    }

    @Override
    public void onSucces(Becas mBecas) {
        if (!isNullOrEmpty(mBecas.rutaImagen)) {
            ImageLoader.getInstance().displayImage(ImageUtils.getUrlImage(mBecas.rutaImagen, getApplicationContext()), mImageView, ImageUtils.OptionsImageLoaderItems);
        }
        if (mBecas.nombre != null)
            mTextViewNombreBeca.setText(mBecas.nombre);
        if (mBecas.descripcion != null)
            mTextViewDescripcion.setText(mBecas.descripcion);
        if (mBecas.mUniversidad != null && mBecas.mUniversidad.nombre != null)
            mTextViewUniversidad.setText(mBecas.mUniversidad.nombre);
        if (mBecas.rutaArchivo != null) {
            mTextViewArchivo.setText("Ver documento adjunto.");
        } else {
            LinearLayout archivo = (LinearLayout) mTextViewArchivo.getParent();
            archivo.setVisibility(View.GONE);
        }
    }

    @Override
    public void PostularDetalle() {
        Intent mIntent = new Intent(getApplicationContext(), DatosUniversitarioActivity.class);
        startActivityForResult(mIntent, CODE_RESULT);
    }

    @Override
    public void postular(Persona mPersona) {
        PostuladosBecas mPostuladosBecas = new PostuladosBecas();
        mPostuladosBecas.mPersona = mPersona;
        mPostuladosBecas.mBecas = mBecas;
        mPostuladosBecas.id_persona = mPersona.id;
        mPostuladosBecas.id_beca = mBecas.id;
        mBecasDetallePresenter.postularBeca(mPostuladosBecas);
    }

    @Override
    public void OnsuccesPostular(PostuladosBecas mPostuladosBecas) {
        //showMessage("siiiiiiiiiiiiiiiiiiii");
        mBecasDetallePresenter.updatePersona(mPostuladosBecas.mPersona);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_RESULT) {
                Bundle mBundle = data.getExtras();
                Persona mPersona = mBundle.getParcelable(KEY_REGISTRO_UNIVERSITARIO);
                PostuladosBecas mPostuladosBecas = new PostuladosBecas();
                mPostuladosBecas.id_beca = mBecas.id;
                mPostuladosBecas.id_persona = mPersona.id;
                mPostuladosBecas.mPersona = mPersona;
                mPostuladosBecas.mBecas = mBecas;
                mBecasDetallePresenter.postularBeca(mPostuladosBecas);
            }
        }
    }

    @Override
    public void OnFailed(String mensaje) {
        showMessage(mensaje);
        //finish();
    }

    @Override
    public void Onloading(boolean loading) {
        if (loading) {
            showOnProgressDialog("Cargado...");
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void EmptyRecyclerView() {
// TODO: 01/05/2018 no hace naaaaaaaaaaaaaaaaaaaaadaaaaaaaaaaaaaaaaaaaa
    }
}
