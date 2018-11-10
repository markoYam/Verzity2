package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Favoritos;
import com.dwmedios.uniconekt.model.FotosUniversidades;
import com.dwmedios.uniconekt.model.Licenciaturas;
import com.dwmedios.uniconekt.model.Paquetes;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.PostuladosUniversidades;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.presenter.UniversidadDetallePresenter;
import com.dwmedios.uniconekt.view.activity.Universidad.UbicacionUniversidadActivity;
import com.dwmedios.uniconekt.view.activity.Universitario.BecasActivity;
import com.dwmedios.uniconekt.view.activity.Universitario.FinanciamientoActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.animation_demo.demoAnimation;
import com.dwmedios.uniconekt.view.util.Dialog.CustomDialogReyclerView;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.util.ViewPagerCarrusel;
import com.dwmedios.uniconekt.view.viewmodel.UniversidadDetalleViewController;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

import static com.dwmedios.uniconekt.view.activity.Universidad.UbicacionUniversidadActivity.KEY_UBICACION_UNIVERSIDAD;
import static com.dwmedios.uniconekt.view.activity.Universitario.BecasActivity.KEY_BECAS;
import static com.dwmedios.uniconekt.view.activity.Universitario.BecasActivity.TYPE_VIEW;
import static com.dwmedios.uniconekt.view.activity.Universitario.DetalleUniversidadActivity.KEY_DETALLE_UNIVERSIDAD;
import static com.dwmedios.uniconekt.view.activity.Universitario.FinanciamientoActivity.KEY_FINANCIAMIENTOS;
import static com.dwmedios.uniconekt.view.activity.Universitario.FinanciamientoActivity.TYPE_VIEW_F;
import static com.dwmedios.uniconekt.view.activity.Universitario_v2.ProspectusActivity.KEY_PROSPECTUS;
import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderDark;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.dwmedios.uniconekt.view.util.Utils.changeColorToolbar;
import static com.dwmedios.uniconekt.view.util.Utils.setStatusBarGradiant;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class UniversidadDetalleActivity extends BaseActivity implements UniversidadDetalleViewController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universidad_detalle);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        extranjero = SharePrefManager.getInstance(getApplicationContext()).isSeachExtranjero();
        if (extranjero) {
            setStatusBarGradiant(UniversidadDetalleActivity.this, R.drawable.status_uni_extra);
        }
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
        mNestedScrollView.setOnScrollChangeListener(mOnScrollChangeListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mUniversidad != null)
            ConfigureViewPager(mUniversidad.mFotosUniversidades);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void ConfigureViewPager(List<FotosUniversidades> mFotosUniversidades) {
        if (!isListEmpty(mFotosUniversidades)) {
            mImageViewDefault.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
            ViewPagerCarrusel
                    .getInstance(getApplicationContext(), R.layout.row_custom_recycler)
                    .setTiempo(3000)
                    .setListener(new ViewPagerCarrusel.listener() {
                        @Override
                        public View view(Object m, View mView) {
                            FotosUniversidades mFotosUniversidades = (FotosUniversidades) m;
                            ImageView mImageView = mView.findViewById(R.id.ImageviewRow);
                            ImageLoader.getInstance().displayImage(getUrlImage(mFotosUniversidades.rutaFoto, UniversidadDetalleActivity.this), mImageView, OptionsImageLoaderDark);
                            return mView;
                        }
                    })
                    .start(mViewPager, mFotosUniversidades);
            // mCircleIndicator.setViewPager(mViewPager);
        } else {
            mImageViewDefault.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        ViewPagerCarrusel.getInstance(getApplicationContext(), R.layout.row_custom_recycler).stop();
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
                    if (mUniversidad != null) {
                        if (mUniversidad.nombre != null) {
                            mCollapsingToolbarLayout.setTitle(mUniversidad.nombre);
                        }
                        if (UniversidadDetalleActivity.aplica_postulacion && UniversidadDetalleActivity.aplica_imagen) {
                            if (floatingActionsMenu.getVisibility() == View.VISIBLE) {
                                Animation in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                                in.setDuration(500);
                                floatingActionsMenu.startAnimation(in);
                                floatingActionsMenu.setVisibility(View.GONE);

                            }
                        }
                        // TODO: 06/11/2018 Validar logo de la universidad
                        if (UniversidadDetalleActivity.aplica_logo) {
                            if (mImageViewLogo.getVisibility() == View.VISIBLE) {
                                Animation in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                                in.setDuration(500);
                                mImageViewLogo.startAnimation(in);
                                mImageViewLogo.setVisibility(View.INVISIBLE);
                            }
                        }

                        if (extranjero) {
                            setStatusBarGradiant(UniversidadDetalleActivity.this, R.drawable.status_uni_extra);
                            changeColorToolbar(getSupportActionBar(), R.color.Color_extranjero, UniversidadDetalleActivity.this);
                        }
                    }

                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                    if (UniversidadDetalleActivity.aplica_postulacion && UniversidadDetalleActivity.aplica_imagen) {
                        if (floatingActionsMenu.getVisibility() == View.GONE) {
                            Animation in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                            in.setDuration(500);
                            floatingActionsMenu.startAnimation(in);
                            floatingActionsMenu.setVisibility(View.VISIBLE);
                        }
                    }
                    // TODO: 06/11/2018 mostrar logo oculto
                    if (UniversidadDetalleActivity.aplica_logo) {
                        if (mImageViewLogo.getVisibility() == View.INVISIBLE) {
                            Animation in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                            in.setDuration(500);
                            mImageViewLogo.startAnimation(in);
                            mImageViewLogo.setVisibility(View.VISIBLE);
                        }
                    }

                    if (extranjero) {
                        setStatusBarGradiant(UniversidadDetalleActivity.this, R.drawable.status_uni_extra);
                        changeColorToolbar(getSupportActionBar(), R.color.colorTrasparente, UniversidadDetalleActivity.this);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        ViewPagerCarrusel.getInstance(getApplicationContext(), R.layout.row_custom_recycler).stop();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    //region acciones de botones
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
            }
        }
    };

    //endregion

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
    public void OnSuccess(final Universidad mUniversidad) {
        this.mUniversidad = mUniversidad;
        mUniversidadDetallePresenter.VerificarFavorito(mUniversidad);
        if (mUniversidad.mVentasPaquetesList != null && mUniversidad.mVentasPaquetesList.size() > 0) {
            Paquetes mPaquetes = mUniversidad.mVentasPaquetesList.get(0).mPaquetes;
            //region animaciones
            final Animation slide_out_right = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left);
            slide_out_right.setDuration(700);

            // TODO: 02/11/2018 fade in
            Animation fade_in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
            fade_in.setDuration(700);
            // TODO: 05/11/2018  fade out
            final Animation fade_out = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
            fade_out.setDuration(400);

            Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
            slide_down.setDuration(700);

            Animation animationIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.demo2);
            demoAnimation mDemoAnimation = new demoAnimation(0.2, 20);
            animationIn.setInterpolator(mDemoAnimation);
            //endregion

            //region nombre
            // TODO: 06/11/2018 Validar nombre universidad
            if (mUniversidad.nombre != null) {
                mTextViewNombre.setText(mUniversidad.nombre);
                mCardViewNombreUniversidad.startAnimation(fade_in);
                mCardViewNombreUniversidad.setVisibility(View.VISIBLE);
            } else {
                mCardViewNombreUniversidad.setVisibility(View.GONE);
            }
            //endregion

            if (mPaquetes != null) {


                // region validacion de logo
                // TODO: 02/11/2018 logo
                if (mPaquetes.aplica_logo && mPaquetes.aplica_imagenes) {
                    UniversidadDetalleActivity.aplica_logo = true;
                    mImageViewLogo.startAnimation(slide_down);
                    mImageViewLogo.setVisibility(View.VISIBLE);
                    if (!isNullOrEmpty(mUniversidad.logo)) {
                        Glide.with(getApplicationContext())
                                .load(getUrlImage(mUniversidad.logo, UniversidadDetalleActivity.this))
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        mImageViewLogo.setImageResource(R.drawable.defaultt);
                                        return true;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        return false;
                                    }
                                })
                                .into(mImageViewLogo);

                        //Ajustar como se muestra el texto del nombre de la universidad
                        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        mLayoutParams.setMargins(mImageViewLogo.getLayoutParams().height, 0, (mPaquetes.aplica_favoritos ? mImageButtonFavorito.getLayoutParams().height : 0), 10);
                        mLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                        mTextViewNombre.setLayoutParams(mLayoutParams);


                    } else {
                        mImageViewLogo.setImageResource(R.drawable.defaultt);
                    }
                } else {
                    UniversidadDetalleActivity.aplica_logo = false;
                    mImageViewLogo.setVisibility(View.GONE);
                    if (!mPaquetes.aplica_logo && !mPaquetes.aplica_imagenes) {
                        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        mLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                        mTextViewNombre.setLayoutParams(mLayoutParams);
                    }
                    if (!mPaquetes.aplica_logo && mPaquetes.aplica_imagenes) {
                        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        mLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                        mTextViewNombre.setLayoutParams(mLayoutParams);
                    }
                    //cuando solo se muestra el segundo logo centrado
                    if (!mPaquetes.aplica_imagenes && mPaquetes.aplica_logo) {
                        UniversidadDetalleActivity.aplica_logo = true;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            mTextViewNombre.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        }
                        mImageViewProfile2.startAnimation(fade_in);
                        mImageViewProfile2.setVisibility(View.VISIBLE);
                        if (!isNullOrEmpty(mUniversidad.logo)) {
                            Glide.with(getApplicationContext())
                                    .load(getUrlImage(mUniversidad.logo, UniversidadDetalleActivity.this))
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            mImageViewProfile2.setImageResource(R.drawable.defaultt);
                                            return true;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            return false;
                                        }
                                    })
                                    .into(mImageViewProfile2);
                        } else {
                            mImageViewProfile2.startAnimation(fade_in);
                            mImageViewProfile2.setVisibility(View.VISIBLE);
                            mImageViewProfile2.setImageResource(R.drawable.defaultt);
                        }
                    } else {
                        mImageViewLogo.setImageResource(R.drawable.defaultt);

                    }
                }
                //endregion

                //region validacion de imagenes
                // TODO: 02/11/2018 imagenes
                if (mPaquetes.aplica_imagenes) {
                    UniversidadDetalleActivity.aplica_imagen = true;
                    mAppBarLayout.setExpanded(true, true);
                    mAppBarLayout.startAnimation(fade_in);
                    mAppBarLayout.setVisibility(View.VISIBLE);
                    ConfigureViewPager(mUniversidad.mFotosUniversidades);
                } else {
                    UniversidadDetalleActivity.aplica_imagen = false;
                    mImageViewDefault.setVisibility(View.GONE);
                    mViewPager.setVisibility(View.GONE);
                    mAppBarLayout.getLayoutParams().height = 112;
                    mAppBarLayout.setExpanded(false, false);
                    CoordinatorLayout.LayoutParams coordinatorLayoutParams = (CoordinatorLayout.LayoutParams) mNestedScrollView.getLayoutParams();
                    coordinatorLayoutParams.setBehavior(null);
                    coordinatorLayoutParams.topMargin = 118;
                    // mAppBarLayout.setActivated(false);
                    AppBarLayout.LayoutParams toolbarLayoutParams = (AppBarLayout.LayoutParams) mCollapsingToolbarLayout.getLayoutParams();
                    toolbarLayoutParams.setScrollFlags(0);
                    if (extranjero) {
                        mAppBarLayout.setBackgroundColor(getResources().getColor(R.color.Color_extranjero));
                        mCollapsingToolbarLayout.setBackgroundColor(getResources().getColor(R.color.Color_extranjero));
                        mToolbar.setBackgroundColor(getResources().getColor(R.color.Color_extranjero));
                    }
                    TextView mTextView = mToolbar.findViewById(R.id.title_toolbar);
                    mTextView.setText(mUniversidad.nombre);
                    mTextView.setVisibility(View.VISIBLE);
                    mAppBarLayout.setVisibility(View.VISIBLE);

                }

                //endregion

                //region validacion de favoritos
                // TODO: 02/11/2018  favoritos
                if (mPaquetes.aplica_favoritos) {

                    //mostrar boton de favoritos en la parte superior para que se ajuste igual que el logo
                    if (mPaquetes.aplica_favoritos && mPaquetes.aplica_logo) {
                        RelativeLayout.LayoutParams btnfavorito = new RelativeLayout.LayoutParams(60, 60);
                        btnfavorito.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                        btnfavorito.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                        btnfavorito.setMargins(0, 15, 15, 0);
                        mImageButtonFavorito.setLayoutParams(btnfavorito);
                    }

                    //validar el boton de favorito este centrado y el nombre tenga el margen segun el tamaño del boton
                    if (mPaquetes.aplica_favoritos && !mPaquetes.aplica_logo) {
                        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        mLayoutParams.setMargins(0, 10, (mImageButtonFavorito.getLayoutParams().height), 20);
                        mLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                        mTextViewNombre.setLayoutParams(mLayoutParams);
                    }

                    mImageButtonFavorito.startAnimation(animationIn);
                    mImageButtonFavorito.setVisibility(View.VISIBLE);
                } else {
                    mImageButtonFavorito.setVisibility(View.GONE);
                }
                //endregion

                //region validacion de ubicacion
                // TODO: 02/11/2018 ubicacion
                if (mPaquetes.aplica_geolocalizacion) {
                    mCardViewUbicacion.startAnimation(slide_out_right);
                    mCardViewUbicacion.setVisibility(View.VISIBLE);
                    try {
                        mapView.setVisibility(View.VISIBLE);
                        mTextViewNoUbicacion.setVisibility(View.GONE);
                        Double lat = Double.parseDouble(mUniversidad.mDireccion.latitud);
                        Double lon = Double.parseDouble(mUniversidad.mDireccion.longitud);
                        mLatLng = new LatLng(lat, lon);
                        mapView.onCreate(null);
                        mapView.onStart();
                        mapView.getMapAsync(mapReadyCallback);
                    } catch (Exception ex) {
                        mapView.setVisibility(View.GONE);
                        mTextViewNoUbicacion.setVisibility(View.VISIBLE);
                    }
                } else {
                    CardView imageViewVerDireccionParent = (CardView) mapView.getParent().getParent();
                    imageViewVerDireccionParent.setVisibility(View.GONE);
                    imageViewVerDireccionParent.setVisibility(View.GONE);
                }

                //endregion

                //region validacion de direccion
                // TODO: 02/11/2018 direccion
                if (mPaquetes.aplica_direccion) {
                    if (mCardViewPrincipal.getVisibility() == View.GONE) {
                        mCardViewPrincipal.startAnimation(slide_out_right);
                        mCardViewPrincipal.setVisibility(View.VISIBLE);
                    }
                    LinearLayout mLinearLayout = (LinearLayout) mTextViewDireccion.getParent();
                    mLinearLayout.startAnimation(slide_out_right);
                    mLinearLayout.setVisibility(View.VISIBLE);
                    if (mUniversidad.mDireccion != null) {
                        if (mUniversidad.mDireccion.pais != null)
                            if (mUniversidad.mDireccion.pais.equals("México")) {
                                String direccion = (mUniversidad.mDireccion.descripcion.isEmpty() ? "" : mUniversidad.mDireccion.descripcion + ", ") + mUniversidad.mDireccion.municipio + ", " + mUniversidad.mDireccion.estado + ", " + mUniversidad.mDireccion.pais;
                                mTextViewDireccion.setText(direccion);
                            } else {
                                mTextViewDireccion.setText((mUniversidad.mDireccion.descripcion != null && !mUniversidad.mDireccion.descripcion.isEmpty() ? mUniversidad.mDireccion.descripcion + ", " : new String()) + mUniversidad.mDireccion.pais);
                            }
                    }
                } else {
                    LinearLayout mLinearLayout = (LinearLayout) mTextViewDireccion.getParent();
                    mLinearLayout.setVisibility(View.GONE);
                }
                //endregion

                //region validacion de contacto
                // TODO: 02/11/2018 contacto
                if (mPaquetes.aplica_contacto) {
                    LinearLayout telefonoParent = (LinearLayout) mTextViewTelefono.getParent();
                    LinearLayout correoParent = (LinearLayout) mTextViewCorreo.getParent();
                    LinearLayout sitioParent = (LinearLayout) mTextViewSitio.getParent();
                    telefonoParent.startAnimation(slide_out_right);
                    correoParent.startAnimation(slide_out_right);
                    sitioParent.startAnimation(slide_out_right);
                    telefonoParent.setVisibility(View.VISIBLE);
                    correoParent.setVisibility(View.VISIBLE);
                    sitioParent.setVisibility(View.VISIBLE);
                    if (!isNullOrEmpty(mUniversidad.sitio)) {
                        mTextViewSitio.setText(mUniversidad.sitio);
                        mTextViewSitio.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                    if (!isNullOrEmpty(mUniversidad.correo)) {
                        mTextViewCorreo.setText(mUniversidad.correo);
                        mTextViewCorreo.setMovementMethod(LinkMovementMethod.getInstance());
                    }

                    if (!isNullOrEmpty(mUniversidad.telefono)) {
                        mTextViewTelefono.setText(mUniversidad.telefono);
                        mTextViewTelefono.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                } else {
                    LinearLayout telefonoParent = (LinearLayout) mTextViewTelefono.getParent();
                    LinearLayout correoParent = (LinearLayout) mTextViewCorreo.getParent();
                    LinearLayout sitioParent = (LinearLayout) mTextViewSitio.getParent();
                    telefonoParent.setVisibility(View.GONE);
                    correoParent.setVisibility(View.GONE);
                    sitioParent.setVisibility(View.GONE);
                }

                //endregion

                //region valiacion de becas
                // TODO: 02/11/2018 becas 
                if (mPaquetes.aplica_Beca) {
                    if (mCardViewFlujosAlternos.getVisibility() == View.GONE) {
                        mCardViewFlujosAlternos.startAnimation(slide_out_right);
                        mCardViewFlujosAlternos.setVisibility(View.VISIBLE);
                    }
                    LinearLayout mLinearLayout = (LinearLayout) mImageButtonBecas.getParent();
                    mLinearLayout.startAnimation(slide_out_right);
                    mLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    LinearLayout mLinearLayout = (LinearLayout) mImageButtonBecas.getParent();
                    mLinearLayout.setVisibility(View.GONE);
                }
                //endregion

                //region validacion de financiamientos
                // TODO: 02/11/2018 financiamiento 
                if (mPaquetes.aplica_Financiamiento) {
                    if (mCardViewFlujosAlternos.getVisibility() == View.GONE) {
                        mCardViewFlujosAlternos.startAnimation(slide_out_right);
                        mCardViewFlujosAlternos.setVisibility(View.VISIBLE);
                    }
                    LinearLayout mLinearLayout = (LinearLayout) mImageButtonFinanciamiento.getParent();
                    mLinearLayout.startAnimation(slide_out_right);
                    mLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    LinearLayout mLinearLayout = (LinearLayout) mImageButtonFinanciamiento.getParent();
                    mLinearLayout.setVisibility(View.GONE);
                }
                //endregion

                //region validacion prospectus
                // TODO: 02/11/2018  prospectus
                if (mPaquetes.aplica_Prospectus) {
                    if (mCardViewFlujosAlternos.getVisibility() == View.GONE) {
                        mCardViewFlujosAlternos.startAnimation(slide_out_right);
                        mCardViewFlujosAlternos.setVisibility(View.VISIBLE);
                    }
                    LinearLayout mLinearLayout = (LinearLayout) mImageButtonProspectus.getParent();
                    mLinearLayout.startAnimation(slide_out_right);
                    mLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    LinearLayout mLinearLayout = (LinearLayout) mImageButtonProspectus.getParent();
                    mLinearLayout.setVisibility(View.GONE);
                }
                //endregion

                //region validacion de redes sociales
                // TODO: 02/11/2018 redes 
                if (mPaquetes.aplica_redes) {
                    mCardViewRedes.startAnimation(slide_out_right);
                    mCardViewRedes.setVisibility(View.VISIBLE);
                } else {
                    mCardViewRedes.setVisibility(View.GONE);
                }
                //endregion

                //region validacion de descripcion
                // TODO: 02/11/2018 descripcion
                if (mPaquetes.aplica_descripcion) {
                    CardView mLinearLayout = (CardView) mTextViewDescripcion.getParent().getParent();
                    if (!isNullOrEmpty(mUniversidad.descripcion)) {
                        mLinearLayout.startAnimation(slide_out_right);
                        mLinearLayout.setVisibility(View.VISIBLE);
                        mTextViewDescripcion.setText(mUniversidad.descripcion);
                    } else {
                        mLinearLayout.setVisibility(View.GONE);
                    }
                } else {
                    CardView mLinearLayout = (CardView) mTextViewDescripcion.getParent().getParent();
                    mLinearLayout.setVisibility(View.GONE);
                    mTextViewDescripcion.setVisibility(View.GONE);
                }
                //endregion

                //region validacion de postulacion
                // TODO: 02/11/2018 postulacion
                if (mPaquetes.aplica_Postulacion) {
                    UniversidadDetalleActivity.aplica_postulacion = true;
                    floatingActionsMenu.startAnimation(animationIn);
                    floatingActionsMenu.setVisibility(View.VISIBLE);
                } else {
                    UniversidadDetalleActivity.aplica_postulacion = false;
                    floatingActionsMenu.setVisibility(View.GONE);
                }
                //endregion

            } else {
                // TODO: 02/11/2018 --- ocultar toda la informacion...
            }

        } else {
            //todo --- ocultar toda la informacion...
        }
    }
//region acciones redes sociales
    public void onclickFacebook(View mView) {
        if (mUniversidad != null) {
            if (!isNullOrEmpty(mUniversidad.facebook))
                viewUrl(mUniversidad.facebook);
            else
                showMessage("No cuenta con dirección de Facebook");
        } else
            showMessage("No cuenta con dirección de Facebook");
    }

    public void onclickInstagram(View mView) {
        if (mUniversidad != null) {
            if (!isNullOrEmpty(mUniversidad.instagram))
                viewUrl(mUniversidad.instagram);
            else
                showMessage("No cuenta con dirección de Instagram  ");
        } else
            showMessage("No cuenta con dirección de Instagram  ");
    }

    public void onclickTwitter(View mView) {
        if (mUniversidad != null) {
            if (!isNullOrEmpty(mUniversidad.twitter))
                viewUrl(mUniversidad.twitter);
            else
                showMessage("No cuenta con dirección de Twitter");
        } else
            showMessage("No cuenta con dirección de Twitter");
    }
//endregion
    NestedScrollView.OnScrollChangeListener mOnScrollChangeListener = new NestedScrollView.OnScrollChangeListener() {
        @Override
        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            Log.e("Scroll", "x " + scrollX + " y " + scrollY);
            if (!UniversidadDetalleActivity.aplica_imagen) {
                if (scrollY == 0) {
                    if (floatingActionsMenu.getVisibility() == View.GONE) {
                        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                        in.setDuration(500);
                        floatingActionsMenu.startAnimation(in);
                        floatingActionsMenu.setVisibility(View.VISIBLE);
                    }
                }
                if (scrollY >= 200) {
                    if (floatingActionsMenu.getVisibility() == View.VISIBLE) {
                        Animation in = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                        in.setDuration(500);
                        floatingActionsMenu.startAnimation(in);
                        floatingActionsMenu.setVisibility(View.GONE);

                    }
                }
            }
        }
    };
    //region configuracion mapa
    LatLng mLatLng;
    OnMapReadyCallback mapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if (mLatLng != null) {
                googleMap.addMarker(new MarkerOptions()
                        .position(mLatLng)
                        .title("Universidad")
                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.border_maker1, R.layout.marker_demo, false))));


                CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(mLatLng, 16f);
                googleMap.animateCamera(miUbicacion);
                googleMap.getUiSettings().setScrollGesturesEnabled(false);
                googleMap.getUiSettings().setRotateGesturesEnabled(false);
                googleMap.getUiSettings().setCompassEnabled(false);
                googleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
                googleMap.getUiSettings().setTiltGesturesEnabled(false);
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.getUiSettings().setZoomGesturesEnabled(false);
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if (mUniversidad != null) {
                            if (mUniversidad.mDireccion != null)
                                if (mUniversidad.mDireccion.latitud != null && mUniversidad.mDireccion.longitud != null)
                                    AbrirMapa();
                                else
                                    showMessage("La universidad no cuenta con ubicación.");
                            else
                                showMessage("La universidad no cuenta con ubicación.");
                        } else
                            showMessage("La universidad no cuenta con ubicación.");
                        return true;
                    }
                });
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        if (mUniversidad != null) {
                            if (mUniversidad.mDireccion != null)
                                if (mUniversidad.mDireccion.latitud != null && mUniversidad.mDireccion.longitud != null)
                                    AbrirMapa();
                                else
                                    showMessage("La universidad no cuenta con ubicación.");
                            else
                                showMessage("La universidad no cuenta con ubicación.");
                        } else
                            showMessage("La universidad no cuenta con ubicación.");
                    }
                });
            }
        }
    };

    private Bitmap getMarkerBitmapFromView(int resId, int layout, boolean isUser) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        if (!isUser)
            markerImageView.setImageResource(resId);
        //configureCabeceras(markerImageView);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }
//endregion

    //region metodos
    private void viewUrl(String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            startActivity(i);
        } catch (Exception ex) {
            ex.printStackTrace();
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

    //endregion
    //region controles
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
    private UniversidadDetallePresenter mUniversidadDetallePresenter;
    private Universidad mUniversidad;
    @BindView(R.id.indicador)
    CircleIndicator mCircleIndicator;
    boolean extranjero = false;

    // TODO: 02/11/2018 Redes
    @BindView(R.id.contenedorRedes)
    CardView mCardViewRedes;
    @BindView(R.id.layout_facebook)
    LinearLayout mLinearLayoutFacebook;
    @BindView(R.id.layout_twitter)
    LinearLayout mLinearLayoutTwitter;
    @BindView(R.id.layout_instagram)
    LinearLayout mLinearLayoutInstagram;

    // TODO: 05/11/2018 previewLoad
    @BindView(R.id.cardUbicacionUnivesidad)
    CardView mCardViewUbicacion;
    @BindView(R.id.textViewNotFoundUbicacion)
    TextView mTextViewNoUbicacion;
    @BindView(R.id.cardContenedorPrincipal)
    CardView mCardViewPrincipal;
    //nested scroll
    @BindView(R.id.nestedScrollView)
    NestedScrollView mNestedScrollView;

    @BindView(R.id.cardNombreUniversidad)
    CardView mCardViewNombreUniversidad;
    @BindView(R.id.profile_image2)
    ImageView mImageViewProfile2;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.contenedorFlujosAlternos)
    CardView mCardViewFlujosAlternos;
    //endregion

    public static boolean aplica_logo = false;
    public static boolean aplica_postulacion = false;
    public static boolean aplica_imagen = false;

}
