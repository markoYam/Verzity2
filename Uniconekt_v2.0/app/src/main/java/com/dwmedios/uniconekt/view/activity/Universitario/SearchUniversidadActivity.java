package com.dwmedios.uniconekt.view.activity.Universitario;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Banners;
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.model.SearchUniversidades;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.VisitasBanners;
import com.dwmedios.uniconekt.presenter.SearchUniversidadesPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.FiltrarEstadosActivity;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.FiltrarNivelAcademicoActivity;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.VisualizarUniversidadesActivity;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.mapsActivity.UniversidadesMapsActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.MenuAdapter;
import com.dwmedios.uniconekt.view.util.BannerViewImage;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.SearchUniversidadViewController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario_v2.VisualizarUniversidadesActivity.KEY_BUSQUEDA;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.dwmedios.uniconekt.view.util.Utils.changeColorToolbar;
import static com.dwmedios.uniconekt.view.util.Utils.setStatusBarGradiant;

public class SearchUniversidadActivity extends BaseActivity implements SearchUniversidadViewController {
    public static int TYPE_VIEW_MAPS = 0;
    private SearchUniversidadesPresenter mSearchUniversidadesPresenter;
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextViewEmpy;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    /*  @BindView(R.id.banner)
      Banner mBanner;*/
    @BindView(R.id.ViewPagerCustom)
    ImageView mImageView;
    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_universidad);
        mSearchUniversidadesPresenter = new SearchUniversidadesPresenter(this, this);
        ButterKnife.bind(this);
        mSwipeRefreshLayout.setBackgroundColor(Color.parseColor("#000000"));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Buscar universidades");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (SharePrefManager.getInstance(getApplicationContext()).isSeachExtranjero()) {
            setStatusBarGradiant(SearchUniversidadActivity.this, R.drawable.status_uni_extra);
            changeColorToolbar(getSupportActionBar(), R.color.Color_extranjero, SearchUniversidadActivity.this);
        } else {
            setStatusBarGradiant(SearchUniversidadActivity.this, R.drawable.status_uni);
            //changeColorToolbar(getSupportActionBar(), R.color.Color_buscarUniversidad, SearchUniversidadActivity.this);
        }
        mSearchUniversidadesPresenter.ConfigureMenu();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mSearchUniversidadesPresenter.ConfigureMenu();

            }
        });
    }

    public boolean GO_TO_MAP = false;

    @Override
    protected void onStart() {
        super.onStart();
        //estatusBanner = true;
        if (GO_TO_MAP) {
            abrirMapa();
        }
        mSearchUniversidadesPresenter.getBanners();
        //BannerViewImage.getInstance(getApplicationContext()).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BannerViewImage.getInstance(getApplicationContext()).finishBanner();
        back();
    }

    private void back() {
        Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back();
                break;
            case R.id.menu_inicio_universidad:
                Intent intent = new Intent(SearchUniversidadActivity.this, MainUniversitarioActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnSuccesSearch(List<Universidad> mUniversidadList) {

    }

    @Override
    public void OnSuccesMenu(List<ClasViewModel.menu> menuList) {
        mTextViewEmpy.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        configureRecyclerView(menuList);
    }

    public void configureRecyclerView(List<ClasViewModel.menu> menus) {
        if (menus != null && menus.size() > 0) {
            menuAdapter = new MenuAdapter(menus, mOnclick, 2);
            LinearLayoutManager layoutManager = new LinearLayoutManager(SearchUniversidadActivity.this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(menuAdapter);
            Utils.setAnimRecyclerView(getApplicationContext(),R.anim.layout_animation,mRecyclerView);
        } else {
            mRecyclerView.setAdapter(null);
            this.EmpyRecycler();
        }
    }

    MenuAdapter.onclick mOnclick = new MenuAdapter.onclick() {
        @Override
        public void onclick(ClasViewModel.menu menu) {
            ClasViewModel.tipoMenu validate = ClasViewModel.tipoMenu.valueOf(menu.tipo.toString());
            switch (validate) {
                case nombre:
                    Utils.tipoBusqueda_Universidad = 1;
                    Intent mIntent = new Intent(SearchUniversidadActivity.this, VisualizarUniversidadesActivity.class);
                    mIntent.putExtra(KEY_BUSQUEDA, new SearchUniversidades());
                    //  SEARCH_KEY = 1;
                    startActivity(mIntent);
                    break;
                case academicos:
                    startActivity(new Intent(SearchUniversidadActivity.this, FiltrarNivelAcademicoActivity.class));
                    break;
                case cercaMi:
                    abrirMapa();
                    break;
                case favoritos:
                    Utils.tipoBusqueda_Universidad = 4;
                    Intent mIntent2 = new Intent(SearchUniversidadActivity.this, VisualizarUniversidadesActivity.class);
                    mIntent2.putExtra(KEY_BUSQUEDA, new SearchUniversidades());
                    startActivity(mIntent2);
                    break;
                case buscar_pais:
                    startActivity(new Intent(SearchUniversidadActivity.this, FiltrarEstadosActivity.class));
                    break;
                default:
                    showMessage("En proceso....");
                    break;
            }

        }
    };

    public void abrirMapa() {
        List<String> per = new ArrayList<>();
        per.add(Manifest.permission.ACCESS_FINE_LOCATION);
        per.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        per.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        per.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (validatePermison(per, SearchUniversidadActivity.this, 1)) {//falta el estatus

            LocationManager manager = (LocationManager) SearchUniversidadActivity.this.getSystemService(Context.LOCATION_SERVICE);
            boolean gps = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean red = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!gps || !red) {
                GO_TO_MAP = true;
                AlertNoGps();
            } else {
                GO_TO_MAP = false;
                startActivity(new Intent(SearchUniversidadActivity.this, UniversidadesMapsActivity.class));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    abrirMapa();
                    return;
                } else {

                    showInfoDialogListener("Atención", "Es nesesario otorgar los permisos para continuar", "OK");
                }
                return;
            }
        }
    }

    @Override
    public void OnFailedUniversidad(String mensaje) {
        mSwipeRefreshLayout.setRefreshing(false);
        showMessage(mensaje);
    }

    @Override
    public void EmpyRecycler() {
        mTextViewEmpy.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void Onfailed(String mensaje) {
        mSwipeRefreshLayout.setRefreshing(false);
        //showMessage(mensaje);
    }

    @Override
    public void OnLoading(boolean loading) {
        if (loading) {
            showOnProgressDialog("Cargando...");
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void SearchName() {
    }

    private List<Banners> mBannersList;

    @Override
    public void OnsuccesBanners(List<Banners> mBannersList) {
        this.mBannersList = mBannersList;
        configureBanner();
    }


    private void configureBanner() {
        mImageView.setVisibility(View.VISIBLE);
       /* mCustoViewPager = new CustoViewPager(mBannersList, SearchUniversidadActivity.this, mViewPager);
        mCustoViewPager.setmHandleView(new CustoViewPager.handleView() {
            @Override
            public View handleView(View mView, final Object mObject, int postion) {
                ImageView mImageView = mView.findViewById(R.id.ImageviewRow);
                mCustoViewPager.setFadeAnimation(mImageView);
                ImageLoader.getInstance().displayImage(getUrlImage(((Banners) mObject).foto, SearchUniversidadActivity.this), mImageView, OptionsImageLoaderDark);
                mCustoViewPager.automatico(postion, ((Banners) mObject).tiempoo);

                mSearchUniversidadesPresenter.RegistrarVisitaBanner(((Banners) mObject));
                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Banners temp = (Banners) mObject;
                        mSearchUniversidadesPresenter.RegistrarVisita(temp);
                    }
                });
                return mView;
            }
        });
        mCustoViewPager.start();
        BannerView mBannerView= new BannerView(mBannersList,getSupportFragmentManager());
        mBannerView.start(mViewPager);*/
        BannerViewImage.getInstance(getApplicationContext()).start(mImageView, mBannersList, new BannerViewImage.listener() {
            @Override
            public int onsuccess(Object mObject) {
                final Banners mBanners = (Banners) mObject;
                Glide.with(getApplicationContext()).load(getUrlImage(((Banners) mObject).foto, SearchUniversidadActivity.this))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                               mImageView.setImageResource(R.drawable.defaultt);
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(mImageView);
                //ImageLoader.getInstance().displayImage(getUrlImage(((Banners) mObject).foto, SearchUniversidadActivity.this), mImageView, OptionsImageLoaderDark);
                mSearchUniversidadesPresenter.RegistrarVisitaBanner(mBanners);
                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mSearchUniversidadesPresenter.RegistrarVisita(mBanners);
                    }
                });
                return mBanners.tiempoo;
            }
        });
       /* BannerViewRecyclerView mBannerViewRecyclerView= new BannerViewRecyclerView(getApplicationContext());
        mBannerViewRecyclerView.start(mRecyclerViewBanner,mBannersList);*/
    }

    @Override
    public void OnfailedBanners(String mensaje) {
        mImageView.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BannerViewImage.getInstance(getApplicationContext()).stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BannerViewImage.getInstance(getApplicationContext()).finishBanner();
    }

    @Override
    public void ViewBanner(Banners mBanners) {
        if (mBanners.sitio != null) {
            if (!mBanners.sitio.startsWith("https://") && !mBanners.sitio.startsWith("http://")) {
                mBanners.sitio = "http://" + mBanners.sitio;
            }
            Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mBanners.sitio));
            startActivity(openUrlIntent);
        } else {
            showMessage("El banner no cuenta con un sitio web");
        }
    }

    @Override
    public void SuccesVisitedBanner(VisitasBanners mBanners) {

    }

    @Override
    public void FailedVisitedBanner(String mensaje) {
        showMessage(mensaje);
    }


}
