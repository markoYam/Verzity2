package com.dwmedios.uniconekt.view.activity.Universitario;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
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

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Banners;
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.model.SearchUniversidades;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.VisitasBanners;
import com.dwmedios.uniconekt.presenter.SearchUniversidadesPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario.ActivityMaps.MapsUniversityActivity;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.FiltrarEstadosActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.MenuAdapter;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.util.demo.CustoViewPager;
import com.dwmedios.uniconekt.view.viewmodel.SearchUniversidadViewController;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario.ViewUniversidadesActivity.SEARCH_KEY;
import static com.dwmedios.uniconekt.view.activity.Universitario.ViewUniversidadesActivity.UNIVERSIDAD_KEY;
import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderDark;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.dwmedios.uniconekt.view.util.Utils.changeColorToolbar;

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
    ViewPager mViewPager;
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
        if (SharePrefManager.getInstance(getApplicationContext()).isSeachExtranjero())
            changeColorToolbar(getSupportActionBar(), R.color.Color_extranjero, SearchUniversidadActivity.this);
        else
            changeColorToolbar(getSupportActionBar(), R.color.Color_buscarUniversidad, SearchUniversidadActivity.this);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mCustoViewPager != null) mCustoViewPager.stopHandler();
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
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
                    Intent mIntent = new Intent(SearchUniversidadActivity.this, ViewUniversidadesActivity.class);
                    SEARCH_KEY = 1;
                    startActivity(mIntent);
                    break;
                case academicos:
                    startActivity(new Intent(SearchUniversidadActivity.this, SearchLicenciaturasActivity.class));
                    break;
                case cercaMi:
                    abrirMapa();
                    TYPE_VIEW_MAPS = 1;
                    break;
                case favoritos:
                    Intent mIntent2 = new Intent(SearchUniversidadActivity.this, ViewUniversidadesActivity.class);
                    SEARCH_KEY = 3;
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
        if (validatePermison(per, SearchUniversidadActivity.this, 1)) {//falta el estatus

            LocationManager manager = (LocationManager) SearchUniversidadActivity.this.getSystemService(Context.LOCATION_SERVICE);
            boolean gps = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean red = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!gps || !red) {
                GO_TO_MAP = true;
                AlertNoGps();
            } else {
                GO_TO_MAP = false;
                startActivity(new Intent(SearchUniversidadActivity.this, MapsUniversityActivity.class));
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
        showdialogInputMaterial("Buscar Universidad", "Ingrese el nombre de la universidad.", R.drawable.ic_search, "Buscar", new LovelyTextInputDialog.OnTextInputConfirmListener() {
            @Override
            public void onTextInputConfirmed(String text) {
                if (!text.isEmpty()) {
                    SearchUniversidades universidades = new SearchUniversidades();
                    universidades.nombreUniversidad = text;
                    Intent mIntent = new Intent(SearchUniversidadActivity.this, ViewUniversidadesActivity.class);
                    mIntent.putExtra(UNIVERSIDAD_KEY, universidades);
                    SEARCH_KEY = 2;
                    startActivity(mIntent);
                }
            }
        });


    }

    private List<Banners> mBannersList;

    @Override
    public void OnsuccesBanners(List<Banners> mBannersList) {
        this.mBannersList = mBannersList;
        configureBanner();
    }

    CustoViewPager mCustoViewPager;

    private void configureBanner() {
        mViewPager.setVisibility(View.VISIBLE);
        mCustoViewPager = new CustoViewPager(mBannersList, SearchUniversidadActivity.this, mViewPager);
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
    }

    @Override
    public void OnfailedBanners(String mensaje) {
        mViewPager.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
      /*  mBanner.stopBanner();
        estatusBanner = false;*/
        if (mCustoViewPager != null) mCustoViewPager.stopHandler();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        /*estatusBanner = false;
        mBanner.stopBanner();*/
        if (mCustoViewPager != null) mCustoViewPager.stopHandler();
        super.onDestroy();

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
