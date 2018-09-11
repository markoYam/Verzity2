package com.dwmedios.uniconekt.view.activity.Universitario;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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
import com.dwmedios.uniconekt.model.Favoritos;
import com.dwmedios.uniconekt.model.FotosUniversidades;
import com.dwmedios.uniconekt.model.Licenciaturas;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.PostuladosUniversidades;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.VentasPaquetes;
import com.dwmedios.uniconekt.presenter.UniversidadDetallePresenter;
import com.dwmedios.uniconekt.view.activity.Universidad.UbicacionUniversidadActivity;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.ProspectusActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.AdapterViewPager.UniversidadesAdapterViewPager;
import com.dwmedios.uniconekt.view.animation_demo.demoAnimation;
import com.dwmedios.uniconekt.view.util.Dialog.CustomDialogReyclerView;
import com.dwmedios.uniconekt.view.util.ImageUtils;
import com.dwmedios.uniconekt.view.viewmodel.UniversidadDetalleViewController;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

import static com.dwmedios.uniconekt.view.activity.Universidad.UbicacionUniversidadActivity.KEY_UBICACION_UNIVERSIDAD;
import static com.dwmedios.uniconekt.view.activity.Universitario.BecasActivity.KEY_BECAS;
import static com.dwmedios.uniconekt.view.activity.Universitario.BecasActivity.TYPE_VIEW;
import static com.dwmedios.uniconekt.view.activity.Universitario.DatosUniversitarioActivity.IS_UNIVERSIDAD;
import static com.dwmedios.uniconekt.view.activity.Universitario.DatosUniversitarioActivity.KEY_REGISTRO_UNIVERSITARIO;
import static com.dwmedios.uniconekt.view.activity.Universitario.DatosUniversitarioActivity.KEY_RESTAURAR;
import static com.dwmedios.uniconekt.view.activity.Universitario.DetalleBecasActivity.CODE_RESULT;
import static com.dwmedios.uniconekt.view.activity.Universitario.FinanciamientoActivity.KEY_FINANCIAMIENTOS;
import static com.dwmedios.uniconekt.view.activity.Universitario.FinanciamientoActivity.TYPE_VIEW_F;
import static com.dwmedios.uniconekt.view.activity.Universitario_v2.ProspectusActivity.KEY_PROSPECTUS;
import static com.dwmedios.uniconekt.view.util.Utils.getDrawable2;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class DetalleUniversidadActivity extends BaseActivity implements UniversidadDetalleViewController {
    public static String KEY_DETALLE_UNIVERSIDAD = "universidad key";
    @BindView(R.id.textViewNombreUniversidad)
    TextView mTextViewNombre;
    @BindView(R.id.textViewDescripcion)
    TextView mTextViewDescripcion;
    @BindView(R.id.textViewSitio)
    TextView mTextViewSitio;
    @BindView(R.id.textViewTelefono)
    TextView mTextViewTelefono;
    @BindView(R.id.textViewDireccion)
    TextView mTextViewDireccion;
    @BindView(R.id.textViewCorreo)
    TextView mTextViewCorreo;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewPagerFotos)
    ViewPager mViewPager;
    @BindView(R.id.indicador)
    CircleIndicator mCircleIndicator;
    @BindView(R.id.imageButtonVideos)
    ImageButton mImageButtonVideos;

    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapseTolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.imageButtonBecas)
    ImageButton mImageButtonBecas;
    @BindView(R.id.imageButtonFinanciamientos)
    ImageButton mImageButtonFinanciamientos;
    @BindView(R.id.imagebuttomFavorite)
    ImageButton mImageButtonFavorite;
    @BindView(R.id.fab)
    com.getbase.floatingactionbutton.FloatingActionButton mFloatingActionButton;
    @BindView(R.id.menu_fab)
    com.getbase.floatingactionbutton.FloatingActionsMenu mFloatingActionButtonMenu;
    @BindView(R.id.ImageviewVerDireccion)
    ImageView mImageViewUbicacion;

    @BindView(R.id.contenedorBecas)
    LinearLayout mLinearLayoutBecas;
    @BindView(R.id.contentenedorFinanciamientos)
    LinearLayout mLinearLayoutFinanciamientos;
    @BindView(R.id.defaultImage)
    ImageView mImageViewDeafult;
    @BindView(R.id.contenedorProspectus)
    LinearLayout mLinearLayoutProspectus;
    @BindView(R.id.profile_image)
    ImageView mImageViewLogo;
    @BindView(R.id.viewWeb)
    View mViewWeb;
    @BindView(R.id.ViewCorreo)
    View mViewCorreo;
    @BindView(R.id.viewTelefono)
    View mViewTelefono;
    private UniversidadDetallePresenter mUniversidadDetallePresenter;
    private UniversidadesAdapterViewPager mUniversidadesAdapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_universidad);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUniversidadDetallePresenter = new UniversidadDetallePresenter(this, getApplicationContext());
        final Universidad uni = getIntent().getExtras().getParcelable(KEY_DETALLE_UNIVERSIDAD);
        getSupportActionBar().setTitle("");
        mUniversidadDetallePresenter.GetDetail(uni);
        mImageButtonVideos.setOnClickListener(mOnClickListener);
        mImageButtonBecas.setOnClickListener(mOnClickListener);
        mImageButtonFinanciamientos.setOnClickListener(mOnClickListener);
        mImageButtonFavorite.setOnClickListener(mOnClickListener);
        mFloatingActionButton.setOnClickListener(mOnClickListener);
        mImageViewUbicacion.setOnClickListener(mOnClickListener);
        //open_Fab = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.open_fab);
        //close_Fab = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.close_fab);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    if (uni.nombre != null) {
                        mCollapsingToolbarLayout.setTitle(uni.nombre);
                    }
                    //  mFloatingActionButtonMenu.startAnimation(close_Fab);
                    if (isValidaPostular)
                        mFloatingActionButtonMenu.setVisibility(View.GONE);
                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                    // mFloatingActionButtonMenu.startAnimation(open_Fab);
                    if (isValidaPostular)
                        mFloatingActionButtonMenu.setVisibility(View.VISIBLE);
                }
            }
        });
        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalizedposition / 2 + 0.5f);
                page.setScaleY(normalizedposition / 2 + 0.5f);
            }
        });
    }

    int coutActual = 0;
    Handler mHandler = new Handler();
    Runnable mRunnable;
    public static boolean activate = true;
    public boolean state = false;

    public void ChangePager(int count) {
        if (activate) {
            coutActual = count;
            Log.e("Pagina", coutActual + "");
            int numPages = mViewPager.getAdapter().getCount();
            if (numPages != 0) {
                if (!state) {
                    if (coutActual < numPages) {
                        coutActual++;
                        mHandler.postDelayed(mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                Log.e("Pagina ", coutActual + "");
                                mViewPager.setCurrentItem(coutActual, true);
                                ChangePager(coutActual);
                            }
                        }, 3000);

                    } else if (coutActual == numPages) {
                        state = true;
                        coutActual--;
                        mHandler.postDelayed(mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                Log.e("Pagina", coutActual + "");
                                mViewPager.setCurrentItem(coutActual, true);
                                ChangePager(coutActual);
                            }
                        }, 3000);
                    }
                } else {
                    if (coutActual == 0) {
                        state = false;
                        coutActual++;
                        mHandler.postDelayed(mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                Log.e("Pagina <=", coutActual + "");
                                mViewPager.setCurrentItem(coutActual, true);
                                ChangePager(coutActual);
                            }
                        }, 3000);
                    } else {
                        coutActual--;
                        mHandler.postDelayed(mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                Log.e("Pagina", coutActual + "");
                                mViewPager.setCurrentItem(coutActual, true);
                                ChangePager(coutActual);
                            }
                        }, 3000);
                    }
                }
            }
        }
    }

    @Override
    protected void onStart() {
        activate = true;
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        activate = false;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        activate = false;
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        activate = false;
        super.onPause();
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
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

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageButtonVideos:
                    if (mUniversidad != null)
                        startActivity(new Intent(getApplicationContext(), ProspectusActivity.class).putExtra(KEY_PROSPECTUS, mUniversidad));
                    break;
                case R.id.imageButtonBecas:
                    if (mUniversidad != null) {
                        Intent mIntent = new Intent(getApplicationContext(), BecasActivity.class);
                        mIntent.putExtra(KEY_BECAS, mUniversidad);

                        // mIntent.putExtra(KEY_BECAS_COLOR, );
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
                    // RotateAnimation ranim = (RotateAnimation) AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                    //mImageButtonFavorite.setAnimation(ranim);
                    final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.demo2);
                    demoAnimation mDemoAnimation = new demoAnimation(0.2, 20);
                    myAnim.setInterpolator(mDemoAnimation);
                    mImageButtonFavorite.startAnimation(myAnim);
                    mUniversidadDetallePresenter.RegistrarFavorito(mUniversidad);
                    break;
                case R.id.fab:
                    postularDetalle2();
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
    private boolean favorite = false;
    private Universidad mUniversidad;

    public void AbrirMapa() {
        List<String> per = new ArrayList<>();
        per.add(Manifest.permission.ACCESS_FINE_LOCATION);
        per.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (validatePermison(per, DetalleUniversidadActivity.this, 1)) {
            startActivity(new Intent(getApplicationContext(), UbicacionUniversidadActivity.class).putExtra(KEY_UBICACION_UNIVERSIDAD, mUniversidad));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            List<String> per = new ArrayList<>();
            per.add(Manifest.permission.ACCESS_FINE_LOCATION);
            per.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkPermison(per, DetalleUniversidadActivity.this, 1)) {
                AbrirMapa();
            } else {
                showMessage("Es necesario otorgar los permisos para continuar");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean isValidaPostular = false;

    @Override
    public void OnSuccess(Universidad mUniversidad) {
        // getSupportActionBar().setTitle(mUniversidad.nombre);
        this.mUniversidad = mUniversidad;
        mUniversidadDetallePresenter.VerificarFavorito(mUniversidad);
        mUniversidadesAdapterViewPager = new UniversidadesAdapterViewPager(getSupportFragmentManager(), mUniversidad.mFotosUniversidades);
        mViewPager.setAdapter(mUniversidadesAdapterViewPager);
        if (!isNullOrEmpty(mUniversidad.logo))
            ImageLoader.getInstance().displayImage(ImageUtils.getUrlImage(mUniversidad.logo, getApplicationContext()), mImageViewLogo, ImageUtils.OptionsImageLoaderItems);
        if (mUniversidad != null && mUniversidad.mFotosUniversidades.size() > 0) {
            mCircleIndicator.setViewPager(mViewPager);
            ChangePager(0);
            //    mViewPager.setPageTransformer(DepthPageTransformer());
        } else {
            if (mUniversidad.logo != null) {
                if (!mUniversidad.logo.isEmpty()) {
                    FotosUniversidades mFotosUniversidades = new FotosUniversidades();
                    mFotosUniversidades.rutaFoto = mUniversidad.logo;
                    mFotosUniversidades.id = 0;
                    mFotosUniversidades.principal = false;
                    List<FotosUniversidades> mFotosUniversidadesList = new ArrayList<>();
                    mFotosUniversidadesList.add(mFotosUniversidades);
                    mUniversidadesAdapterViewPager = new UniversidadesAdapterViewPager(getSupportFragmentManager(), mFotosUniversidadesList);
                    mViewPager.setAdapter(mUniversidadesAdapterViewPager);

                } else
                    mImageViewDeafult.setVisibility(View.VISIBLE);
            } else
                mImageViewDeafult.setVisibility(View.VISIBLE);
        }
        if (mUniversidad.mVentasPaquetesList != null && mUniversidad.mVentasPaquetesList.size() > 0) {
            VentasPaquetes mVentasPaquetes = mUniversidad.mVentasPaquetesList.get(0);
            if (mVentasPaquetes != null) {
                if (mVentasPaquetes.mPaquetes != null) {

                    if (mVentasPaquetes.mPaquetes.aplica_Beca) {
                        mLinearLayoutBecas.setVisibility(View.VISIBLE);
                    } else {
                        mLinearLayoutBecas.setVisibility(View.GONE);
                    }

                    if (mVentasPaquetes.mPaquetes.aplica_Financiamiento) {
                        mLinearLayoutFinanciamientos.setVisibility(View.VISIBLE);
                    } else {
                        mLinearLayoutFinanciamientos.setVisibility(View.GONE);
                    }

                    if (mVentasPaquetes.mPaquetes.aplica_Postulacion) {
                        isValidaPostular = true;
                        mFloatingActionButtonMenu.setVisibility(View.VISIBLE);
                    } else {
                        isValidaPostular = false;
                        mFloatingActionButtonMenu.setVisibility(View.GONE);
                    }
                    if (mVentasPaquetes.mPaquetes.aplica_Prospectus) {
                        mLinearLayoutProspectus.setVisibility(View.VISIBLE);
                    } else {
                        mLinearLayoutProspectus.setVisibility(View.GONE);
                    }
                }
            }
        }

        if (mUniversidad.nombre != null)
            mTextViewNombre.setText(mUniversidad.nombre);
        if (mUniversidad.descripcion != null) {
            LinearLayout mLinearLayout = (LinearLayout) mTextViewDescripcion.getParent();
            mLinearLayout.setVisibility(View.VISIBLE);
            mTextViewDescripcion.setText(mUniversidad.descripcion);
            //mTextViewDescripcion.setVisibility(View.VISIBLE);
        }else {
            LinearLayout mLinearLayout = (LinearLayout) mTextViewDescripcion.getParent();
            mLinearLayout.setVisibility(View.GONE);
            //mTextViewDescripcion.setVisibility(View.GONE);
        }

        if (!isNullOrEmpty(mUniversidad.telefono))
            mTextViewTelefono.setText(mUniversidad.telefono);
        else {
            LinearLayout mLinearLayout = (LinearLayout) mTextViewTelefono.getParent();
            mLinearLayout.setVisibility(View.GONE);
            mTextViewTelefono.setVisibility(View.GONE);
            mViewTelefono.setVisibility(View.GONE);
        }
        if (!isNullOrEmpty(mUniversidad.sitio)) {
            mTextViewSitio.setText(mUniversidad.sitio);
            mTextViewSitio.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            LinearLayout mLinearLayout = (LinearLayout) mTextViewSitio.getParent();
            mLinearLayout.setVisibility(View.GONE);
            mTextViewSitio.setVisibility(View.GONE);
            mViewWeb.setVisibility(View.GONE);

        }
        if (!isNullOrEmpty(mUniversidad.correo))
            mTextViewCorreo.setText(mUniversidad.correo);
        else {
            LinearLayout mLinearLayout = (LinearLayout) mTextViewCorreo.getParent();
            mLinearLayout.setVisibility(View.GONE);
            mTextViewCorreo.setVisibility(View.GONE);
            mViewCorreo.setVisibility(View.GONE);
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
        // TODO: 08/05/2018  en proceso de construccion
        //  animateViewpager(mViewPager);
    }

    @Override
    public void Onfailed(String mensaje) {
        showMessage(mensaje);
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
    public void OnsuccesFavorito(Favoritos mFavoritos) {
        mImageButtonFavorite.setImageDrawable(getDrawable2("ic_action_star", getApplicationContext()));
        mUniversidadDetallePresenter.VerificarFavorito(mUniversidad);
    }

    @Override
    public void OnfailedFavorito(String mensaje) {
        mImageButtonFavorite.setImageDrawable(getDrawable2("ic_action_star_border", getApplicationContext()));
    }

    @Override
    public void OnsuccesCheck(Favoritos mFavoritos) {
        mImageButtonFavorite.setImageDrawable(getDrawable2("ic_action_star", getApplicationContext()));
    }

    @Override
    public void postular() {
        Intent mIntent = new Intent(getApplicationContext(), DatosUniversitarioActivity.class);
        mIntent.putExtra(IS_UNIVERSIDAD, mUniversidad);
        startActivityForResult(mIntent, CODE_RESULT);
    }


    @Override
    public void postularDetalle(final Persona mPersona) {
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
            showMessage("No se encontraron programas académicos.");
        }
    }

    public void postularDetalle2() {
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
                    id_licenciatura = temp.id;
                    mCustomDialogReyclerView2.dismisDialog();
                    mUniversidadDetallePresenter.validateUser();
                }
            });
            mCustomDialogReyclerView2.showDialogRecyclerView();
        } else {
            showMessage("No se encontraron programas académicos.");
        }
    }

    private int id_licenciatura = 0;

    public void postular2(Persona mPersona) {
        PostuladosUniversidades mPostuladosUniversidades = new PostuladosUniversidades();
        mPostuladosUniversidades.id_universidad = mUniversidad.id;
        mPostuladosUniversidades.id_persona = mPersona.id;
        mPostuladosUniversidades.mPersona = mPersona;
        mPostuladosUniversidades.id_licenciatura = id_licenciatura;
        mUniversidadDetallePresenter.PostularseUniversidad(mPostuladosUniversidades);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CODE_RESULT) {
                Bundle mBundle = data.getExtras();
                int restaurar = mBundle.getInt(KEY_RESTAURAR);
                if (restaurar == 1) {
                    mUniversidadDetallePresenter.validateUser();
                    return;
                }
                Persona mPersona = mBundle.getParcelable(KEY_REGISTRO_UNIVERSITARIO);
                PostuladosUniversidades mPostuladosUniversidades = new PostuladosUniversidades();
                mPostuladosUniversidades.id_universidad = mUniversidad.id;
                mPostuladosUniversidades.id_persona = mPersona.id;
                mPostuladosUniversidades.mPersona = mPersona;
                mPostuladosUniversidades.id_licenciatura = id_licenciatura;
                mUniversidadDetallePresenter.PostularseUniversidad(mPostuladosUniversidades);
            }
        }
    }


    @Override
    public void OnsuccesPostular(PostuladosUniversidades mPostuladosUniversidades) {
        showMessage("Operación realizada con éxito");
        mUniversidadDetallePresenter.updatePersona(mPostuladosUniversidades.mPersona);
    }
}
