package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Favoritos;
import com.dwmedios.uniconekt.model.FotosUniversidades;
import com.dwmedios.uniconekt.model.Licenciaturas;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.PostuladosUniversidades;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.VentasPaquetes;
import com.dwmedios.uniconekt.presenter.UniversidadDetallePresenter;
import com.dwmedios.uniconekt.view.activity.Universidad.UbicacionUniversidadActivity;
import com.dwmedios.uniconekt.view.activity.Universitario.BecasActivity;
import com.dwmedios.uniconekt.view.activity.Universitario.FinanciamientoActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.animation_demo.demoAnimation;
import com.dwmedios.uniconekt.view.util.Dialog.CustomDialogReyclerView;
import com.dwmedios.uniconekt.view.util.demo.CustoViewPager;
import com.dwmedios.uniconekt.view.viewmodel.UniversidadDetalleViewController;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universidad.UbicacionUniversidadActivity.KEY_UBICACION_UNIVERSIDAD;
import static com.dwmedios.uniconekt.view.activity.Universitario.BecasActivity.KEY_BECAS;
import static com.dwmedios.uniconekt.view.activity.Universitario.BecasActivity.TYPE_VIEW;
import static com.dwmedios.uniconekt.view.activity.Universitario.DetalleUniversidadActivity.KEY_DETALLE_UNIVERSIDAD;
import static com.dwmedios.uniconekt.view.activity.Universitario.FinanciamientoActivity.KEY_FINANCIAMIENTOS;
import static com.dwmedios.uniconekt.view.activity.Universitario.FinanciamientoActivity.TYPE_VIEW_F;
import static com.dwmedios.uniconekt.view.activity.Universitario_v2.ProspectusActivity.KEY_PROSPECTUS;
import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderDark;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class UniversidadDetalleActivity extends BaseActivity implements UniversidadDetalleViewController {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.profile_image)
    ImageView mImageViewLogo;
    @BindView(R.id.textViewNombreUniversidad)
    TextView mTextViewNombre;
    @BindView(R.id.imagebuttomFavorite)
    ImageButton mImageButtonFavorito;
    @BindView(R.id.textViewDireccion)
    TextView mTextViewDireccion;
    @BindView(R.id.ImageviewVerDireccion)
    ImageView mImageViewVerDireccion;
    @BindView(R.id.textViewTelefono)
    TextView mTextViewTelefono;
    @BindView(R.id.textViewSitio)
    TextView mTextViewSitio;
    @BindView(R.id.textViewCorreo)
    TextView mTextViewCorreo;
    @BindView(R.id.imageButtonProspectus)
    ImageButton mImageButtonProspectus;
    @BindView(R.id.imageButtonBecas)
    ImageButton mImageButtonBecas;
    @BindView(R.id.imageButtonFinanciamientos)
    ImageButton mImageButtonFinanciamiento;
    @BindView(R.id.textViewDescripcion)
    TextView mTextViewDescripcion;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.menu_fab)
    FloatingActionsMenu floatingActionsMenu;
    @BindView(R.id.viewPagerFotos)
    ViewPager mViewPager;
    @BindView(R.id.imageDefault)
    ImageView mImageViewDefault;
    private boolean isValidaPostular = false;
    private UniversidadDetallePresenter mUniversidadDetallePresenter;
    private Universidad mUniversidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universidad_detalle);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mUniversidadDetallePresenter = new UniversidadDetallePresenter(this, getApplicationContext());
        final Universidad uni = getIntent().getExtras().getParcelable(KEY_DETALLE_UNIVERSIDAD);
        getSupportActionBar().setTitle("");
        mUniversidadDetallePresenter.GetDetail(uni);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        configureApBar();
        configureButtons();
    }

    private void configureButtons() {
        mImageButtonProspectus.setOnClickListener(mOnClickListener);
        mImageButtonBecas.setOnClickListener(mOnClickListener);
        mImageButtonFinanciamiento.setOnClickListener(mOnClickListener);
        mImageButtonFavorito.setOnClickListener(mOnClickListener);
        mFloatingActionButton.setOnClickListener(mOnClickListener);
        mImageViewVerDireccion.setOnClickListener(mOnClickListener);
    }

    private CustoViewPager mCustoViewPager;

    @Override
    protected void onStart() {
        super.onStart();
        if (mUniversidad != null)
            ConfigureViewPager(mUniversidad.mFotosUniversidades);
    }

    private void ConfigureViewPager(List<FotosUniversidades> mFotosUniversidades) {
        if (!isListEmpty(mFotosUniversidades)) {
            mImageViewDefault.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
            mCustoViewPager = new CustoViewPager(mFotosUniversidades, UniversidadDetalleActivity.this, mViewPager);
            mCustoViewPager.setmHandleView(new CustoViewPager.handleView() {
                @Override
                public View handleView(View mView, final Object mObject, int postion) {
                    ImageView mImageView = mView.findViewById(R.id.ImageviewRow);
                    mCustoViewPager.setFadeAnimation(mImageView);
                    ImageLoader.getInstance().displayImage(getUrlImage(((FotosUniversidades) mObject).rutaFoto, UniversidadDetalleActivity.this), mImageView, OptionsImageLoaderDark);
                    mCustoViewPager.automatico(postion, 3000);
                    return mView;
                }
            });
            mCustoViewPager.start();
        } else {
            mImageViewDefault.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        mCustoViewPager.stopHandler();
        super.onPause();

    }

    private void configureApBar() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (mUniversidad.nombre != null) {
                        mCollapsingToolbarLayout.setTitle(mUniversidad.nombre);
                    }
                    if (isValidaPostular) {
                        if (floatingActionsMenu.getVisibility() == View.VISIBLE) {
                            Animation in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                            in.setDuration(500);
                            floatingActionsMenu.startAnimation(in);
                            floatingActionsMenu.setVisibility(View.GONE);
                        }
                    }
                    //mCollapsingToolbarLayout.setTitle("Marco Aurelio Yam");
                    if (mImageViewLogo.getVisibility() == View.VISIBLE) {
                        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                        in.setDuration(500);
                        mImageViewLogo.startAnimation(in);
                        mImageViewLogo.setVisibility(View.INVISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mImageViewLogo.getVisibility() == View.INVISIBLE) {
                                    Animation textAfuera = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                                    textAfuera.setDuration(500);
                                    mTextViewNombre.startAnimation(textAfuera);
                                    if (mUniversidad.nombre != null) {
                                        mTextViewNombre.setText(mUniversidad.nombre);
                                    }
                                }

                            }
                        }, 500);
                        //Ocultar
                     /*   Animation textAfuera = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                        textAfuera.setDuration(500);
                        mTextViewNombre.startAnimation(textAfuera);
                        //mTextViewNombre.setVisibility(View.INVISIBLE);
                        if (mUniversidad.nombre != null) {
                            mTextViewNombre.setText(mUniversidad.nombre);
                        }
                        //Mostrar Texto
                        Animation texEntra = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                        mTextViewNombre.startAnimation(texEntra);
                        mTextViewNombre.setVisibility(View.VISIBLE);*/

                    }

                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                    if (isValidaPostular) {
                        if (floatingActionsMenu.getVisibility() == View.GONE) {
                            Animation in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                            in.setDuration(500);
                            floatingActionsMenu.startAnimation(in);
                            floatingActionsMenu.setVisibility(View.VISIBLE);
                        }
                    }
                    if (mImageViewLogo.getVisibility() == View.INVISIBLE) {
                        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                        in.setDuration(500);
                        mImageViewLogo.startAnimation(in);
                        mImageViewLogo.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mImageViewLogo.getVisibility() == View.VISIBLE) {
                                    Animation textAfuera = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                                    textAfuera.setDuration(500);
                                    mTextViewNombre.startAnimation(textAfuera);
                                    if (mUniversidad.nombre != null) {
                                        mTextViewNombre.setText("                " + mUniversidad.nombre);
                                    }
                                }

                            }
                        }, 500);
                        //Ocultar

                       /* //Mostrar Texto
                        Animation texEntra = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                        mTextViewNombre.startAnimation(texEntra);
                        mTextViewNombre.setVisibility(View.VISIBLE);*/
                    }

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageButtonProspectus:
                    if (mUniversidad != null)
                        startActivity(new Intent(getApplicationContext(), ProspectusActivity.class).putExtra(KEY_PROSPECTUS, mUniversidad));
                    break;
                case R.id.imageButtonBecas:
                    if (mUniversidad != null) {
                        Intent mIntent = new Intent(getApplicationContext(), BecasActivity.class);
                        mIntent.putExtra(KEY_BECAS, mUniversidad);
                        TYPE_VIEW = 2;
                        startActivity(mIntent);
                    }
                    break;
                case R.id.imageButtonFinanciamientos:
                    if (mUniversidad != null) {
                        Intent mIntent = new Intent(getApplicationContext(), FinanciamientoActivity.class);
                        mIntent.putExtra(KEY_FINANCIAMIENTOS, mUniversidad);
                        TYPE_VIEW_F = 2;
                        startActivity(mIntent);
                    }
                    break;
                case R.id.imagebuttomFavorite:
                    final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.demo2);
                    demoAnimation mDemoAnimation = new demoAnimation(0.2, 20);
                    myAnim.setInterpolator(mDemoAnimation);
                    mImageButtonFavorito.startAnimation(myAnim);
                    mUniversidadDetallePresenter.RegistrarFavorito(mUniversidad);
                    break;
                case R.id.fab:
                    mUniversidadDetallePresenter.postular();
                    break;
                case R.id.ImageviewVerDireccion:
                    if (mUniversidad.mDireccion != null)
                        if (mUniversidad.mDireccion.latitud != null && mUniversidad.mDireccion.longitud != null)
                            AbrirMapa();
                        else
                            showMessage("La universidad no cuenta con ubicación.");
                    else
                        showMessage("La universidad no cuenta con ubicación.");


                    break;

            }
        }
    };

    public void AbrirMapa() {
        List<String> per = new ArrayList<>();
        per.add(Manifest.permission.ACCESS_FINE_LOCATION);
        per.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (validatePermison(per, UniversidadDetalleActivity.this, 1)) {
            startActivity(new Intent(getApplicationContext(), UbicacionUniversidadActivity.class).putExtra(KEY_UBICACION_UNIVERSIDAD, mUniversidad));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            List<String> per = new ArrayList<>();
            per.add(Manifest.permission.ACCESS_FINE_LOCATION);
            per.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkPermison(per, UniversidadDetalleActivity.this, 1)) {
                AbrirMapa();
            } else {
                showMessage("Es necesario otorgar los permisos para continuar");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void OnSuccess(Universidad mUniversidad) {
        this.mUniversidad = mUniversidad;
        mUniversidadDetallePresenter.VerificarFavorito(mUniversidad);
        ConfigureViewPager(mUniversidad.mFotosUniversidades);
        if (!isNullOrEmpty(mUniversidad.logo)) {
            ImageLoader.getInstance().displayImage(getUrlImage(mUniversidad.logo, UniversidadDetalleActivity.this), mImageViewLogo, OptionsImageLoaderDark);
        } else {
            mImageViewLogo.setImageResource(R.drawable.defaultt);
        }
        if (mUniversidad.mVentasPaquetesList != null && mUniversidad.mVentasPaquetesList.size() > 0) {
            VentasPaquetes mVentasPaquetes = mUniversidad.mVentasPaquetesList.get(0);
            if (mVentasPaquetes != null) {
                if (mVentasPaquetes.mPaquetes != null) {

                    if (mVentasPaquetes.mPaquetes.aplica_Beca) {
                        LinearLayout mLinearLayout = (LinearLayout) mImageButtonBecas.getParent();
                        mLinearLayout.setVisibility(View.VISIBLE);
                    } else {
                        LinearLayout mLinearLayout = (LinearLayout) mImageButtonBecas.getParent();
                        mLinearLayout.setVisibility(View.GONE);
                    }

                    if (mVentasPaquetes.mPaquetes.aplica_Financiamiento) {
                        LinearLayout mLinearLayout = (LinearLayout) mImageButtonFinanciamiento.getParent();
                        mLinearLayout.setVisibility(View.VISIBLE);
                    } else {
                        LinearLayout mLinearLayout = (LinearLayout) mImageButtonFinanciamiento.getParent();
                        mLinearLayout.setVisibility(View.GONE);
                    }

                    if (mVentasPaquetes.mPaquetes.aplica_Postulacion) {
                        isValidaPostular = true;
                        floatingActionsMenu.setVisibility(View.VISIBLE);
                    } else {
                        isValidaPostular = false;
                        floatingActionsMenu.setVisibility(View.GONE);
                    }

                    if (mVentasPaquetes.mPaquetes.aplica_Prospectus) {
                        LinearLayout mLinearLayout = (LinearLayout) mImageButtonProspectus.getParent();
                        mLinearLayout.setVisibility(View.VISIBLE);
                    } else {
                        LinearLayout mLinearLayout = (LinearLayout) mImageButtonProspectus.getParent();
                        mLinearLayout.setVisibility(View.GONE);
                    }
                }
            }
            if (mUniversidad.nombre != null)
                mTextViewNombre.setText("                " + mUniversidad.nombre);
            if (mUniversidad.descripcion != null) {
                CardView mLinearLayout = (CardView) mTextViewDescripcion.getParent().getParent();
                mLinearLayout.setVisibility(View.VISIBLE);
                mTextViewDescripcion.setText(mUniversidad.descripcion);
            } else {
                CardView mLinearLayout = (CardView) mTextViewDescripcion.getParent().getParent();
                mLinearLayout.setVisibility(View.GONE);
                mTextViewDescripcion.setVisibility(View.GONE);
            }

            if (!isNullOrEmpty(mUniversidad.telefono))
                mTextViewTelefono.setText(mUniversidad.telefono);
            else {
                LinearLayout mLinearLayout = (LinearLayout) mTextViewTelefono.getParent();
                mLinearLayout.setVisibility(View.GONE);
                mTextViewTelefono.setVisibility(View.GONE);

            }
            if (!isNullOrEmpty(mUniversidad.sitio)) {
                mTextViewSitio.setText(mUniversidad.sitio);
                mTextViewSitio.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                LinearLayout mLinearLayout = (LinearLayout) mTextViewSitio.getParent();
                mLinearLayout.setVisibility(View.GONE);
                mTextViewSitio.setVisibility(View.GONE);
            }
            if (!isNullOrEmpty(mUniversidad.correo))
                mTextViewCorreo.setText(mUniversidad.correo);
            else {
                LinearLayout mLinearLayout = (LinearLayout) mTextViewCorreo.getParent();
                mLinearLayout.setVisibility(View.GONE);
                mTextViewCorreo.setVisibility(View.GONE);
            }

            if (mUniversidad.mDireccion != null) {
                if (mUniversidad.mDireccion.pais != null)
                    if (mUniversidad.mDireccion.pais.equals("México")) {
                        String direccion = (mUniversidad.mDireccion.descripcion.isEmpty() ? "" : mUniversidad.mDireccion.descripcion + ", ") + mUniversidad.mDireccion.municipio + ", " + mUniversidad.mDireccion.estado + ", " + mUniversidad.mDireccion.pais;
                        mTextViewDireccion.setText(direccion);
                    } else {
                        mTextViewDireccion.setText((mUniversidad.mDireccion.descripcion != null && !mUniversidad.mDireccion.descripcion.isEmpty() ? mUniversidad.mDireccion.descripcion + ", " : new String()) + mUniversidad.mDireccion.pais);
                    }
            }
        }
    }

    @Override
    public void Onfailed(String mensaje) {
        showMessage(mensaje);
    }

    @Override
    public void Onloading(boolean loading) {
        if (loading)
            showOnProgressDialog("Cargando...");
        else
            dismissProgressDialog();
    }

    @Override
    public void OnsuccesFavorito(Favoritos mFavoritos) {
        mImageButtonFavorito.setImageResource(R.drawable.ic_action_star);
        mUniversidadDetallePresenter.VerificarFavorito(mUniversidad);
    }

    @Override
    public void OnfailedFavorito(String mensaje) {
        mImageButtonFavorito.setImageResource(R.drawable.ic_action_star_border);

    }

    @Override
    public void OnsuccesCheck(Favoritos mFavoritos) {
        mImageButtonFavorito.setImageResource(R.drawable.ic_action_star);

    }

    @Override
    public void OnsuccesPostular(PostuladosUniversidades mPostuladosUniversidades) {
        showMessage("Operación realizada con éxito");
        mUniversidadDetallePresenter.updatePersona(mPostuladosUniversidades.mPersona);
    }

    @Override
    public void postularUsuario(final Persona mPersona) {
        if (mUniversidad.mLicenciaturasList != null && mUniversidad.mLicenciaturasList.size() > 0) {
            List<String> temp = new ArrayList<>();
            for (Licenciaturas mLicenciaturas : mUniversidad.mLicenciaturasList) {
                temp.add(mLicenciaturas.nombre);
            }
            final CustomDialogReyclerView mCustomDialogReyclerView2 = new CustomDialogReyclerView(getApplicationContext(), this, R.layout.custom_view);

            mCustomDialogReyclerView2.setmHandleView(new CustomDialogReyclerView.handleView() {
                @Override
                public void handle(TextView mTextView, Object m) {
                    Licenciaturas temp = (Licenciaturas) m;
                    if (temp.mNivelEstudios != null)
                        mTextView.setText(temp.nombre.toUpperCase() + " - " + temp.mNivelEstudios.nombre.toUpperCase());
                    else
                        mTextView.setText(temp.nombre.toUpperCase());
                }
            }, mUniversidad.mLicenciaturasList);
            mCustomDialogReyclerView2.setTitulo("Seleccione programa académico de interés.");
            mCustomDialogReyclerView2.setmHandleOnclick(new CustomDialogReyclerView.handleOnclick() {
                @Override
                public void onclick(Object m) {

                    Licenciaturas temp = (Licenciaturas) m;
                    PostuladosUniversidades mPostuladosUniversidades = new PostuladosUniversidades();
                    mPostuladosUniversidades.id_universidad = mUniversidad.id;
                    mPostuladosUniversidades.id_persona = mPersona.id;
                    mPostuladosUniversidades.mPersona = mPersona;
                    mPostuladosUniversidades.id_licenciatura = temp.id;
                    mCustomDialogReyclerView2.dismisDialog();
                    mUniversidadDetallePresenter.PostularseUniversidad(mPostuladosUniversidades);

                }
            });
            mCustomDialogReyclerView2.showDialogRecyclerView();
        } else {
            showMessage("No se cuenta con programas académicos para postularse");
        }
    }

}
