package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.SearchUniversidades;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.presenter.UniversidadPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario.DetalleUniversidadActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.UniversidadAdapter;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.UniversidadViewController;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario.DetalleUniversidadActivity.KEY_DETALLE_UNIVERSIDAD;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class VisualizarUniversidadesActivity extends BaseActivity implements UniversidadViewController {
    public static final String KEY_BUSQUEDA = "KEY_57258242472854.0545";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextViewEmpty;

    private UniversidadAdapter mUniversidadAdapter;
    private UniversidadPresenter mUniversidadPresenter;
    List<Universidad> mUniversidadsList = null;
    private SearchView mSearchView;
    private String criterio = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_universidades);
        ButterKnife.bind(this);
        mToolbar.setTitle("Universidades");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUniversidadPresenter = new UniversidadPresenter(this, getApplicationContext());
        //falta el swipe
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                loadData();
            }
        });

    }

    SearchUniversidades mUniversidades;

    public void loadData() {
        if (mUniversidades == null)
            mUniversidades = getIntent().getExtras().getParcelable(KEY_BUSQUEDA);
        if (mUniversidades != null) {
            mUniversidades.extranjero = SharePrefManager.getInstance(getApplicationContext()).isSeachExtranjero();
            if (isNullOrEmpty(mUniversidades.pais))
                mUniversidades.pais = mUniversidadPresenter.getDatosPersona().direccion.pais;
            switch (Utils.tipoBusqueda_Universidad) {
                case 1:
                    //todas las universidades
                    mUniversidadPresenter.Search(mUniversidades);
                    break;
                case 2:
                    //programas academicos
                    mUniversidades.licenciaturas = SharePrefManager.getInstance(getApplicationContext()).getLicenciaturas();
                    mUniversidadPresenter.Search(mUniversidades);
                    break;
                case 3:
                    //estados
                    getSupportActionBar().setTitle("Universidades en " + mUniversidades.estado);
                    mUniversidadPresenter.Search(mUniversidades);
                    break;
                case 4:
                    //favoritos
                    getSupportActionBar().setTitle("Favoritos");
                    mUniversidadPresenter.getFavoritos();
                    break;
                default:
                    finish();
                    break;
            }
        } else {
            showMessage("upss");
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.busqueda, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                criterio = query;
                if (!isNullOrEmpty(query)) {
                    mUniversidadPresenter.searchUniversidades(mUniversidadsList, query);
                } else
                    loadData();
                mSearchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                criterio = newText;
                if (!isNullOrEmpty(newText)) {
                    mUniversidadPresenter.searchUniversidades(mUniversidadsList, newText);
                } else
                    loadData();

                return true;
            }
        });
        mSearchView.setQueryHint("Buscar universidad");
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setFocusable(true);
                mSearchView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mSearchView, InputMethodManager.SHOW_IMPLICIT);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                Utils.busqueda_universidades = true;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (mSearchView.isIconified()) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    Utils.busqueda_universidades = false;
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    Utils.busqueda_universidades = false;
                    mSearchView.onActionViewCollapsed();
                }
                return true;
            }
        });

        mSearchView.requestFocus();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!mSearchView.isIconified()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mSearchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.tipoBusqueda_Universidad = 1;
        Utils.busqueda_universidades = false;
    }

    @Override
    public void Onloading(boolean loading) {
        if (loading) {
            showOnProgressDialog("Cargando...");
        } else {
            dismissProgressDialog();

        }
    }

    public void configureRecyclerView(List<Universidad> mUniversidadList) {
        if (mUniversidadList != null && mUniversidadList.size() > 0) {
            mTextViewEmpty.setVisibility(View.GONE);
            mUniversidadAdapter = new UniversidadAdapter(mUniversidadList, mOnclick);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mUniversidadAdapter);
        } else {
            mRecyclerView.setAdapter(null);
            mUniversidadAdapter.notifyDataSetChanged();
            this.EmpyRecycler();
        }
    }

    UniversidadAdapter.onclickCard mOnclick = new UniversidadAdapter.onclickCard() {
        @Override
        public void onclick(Universidad mUniversidad, int type) {
            if (type == 1) {
                Intent mIntent = new Intent(getApplicationContext(), DetalleUniversidadActivity.class);
                mIntent.putExtra(KEY_DETALLE_UNIVERSIDAD, mUniversidad);
                startActivity(mIntent);
            } else if (type == 2) {
                //ShowPoupDialod(ViewUniversidadesActivity.this,R.layout.dialog_imageview,mUniversidad.logo);
            }
        }
    };

    @Override
    public void EmpyRecycler() {
        mTextViewEmpty.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setAdapter(null);
    }

    @Override
    public void OnSuccess(List<Universidad> mUniversidadList) {
        mSwipeRefreshLayout.setRefreshing(false);
        mTextViewEmpty.setVisibility(View.GONE);
        //this.mUniversidadList = mUniversidadList;
        configureRecyclerView(mUniversidadList);
    }

    @Override
    public void OnSuccessSeach(List<Universidad> mUniversidadList, int type) {
        mSwipeRefreshLayout.setRefreshing(false);
        mTextViewEmpty.setVisibility(View.GONE);
        if (type == 1) {
            this.mUniversidadsList = mUniversidadList;
            if (Utils.busqueda_universidades) {
                mUniversidadPresenter.searchUniversidades(mUniversidadList, criterio);
            } else {
                configureRecyclerView(mUniversidadList);
            }
        } else {
            configureRecyclerView(mUniversidadList);
        }


    }

    @Override
    public void Onfailed(String mensaje) {
        //showMessage(mensaje);
        mTextViewEmpty.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        EmpyRecycler();
    }
}
