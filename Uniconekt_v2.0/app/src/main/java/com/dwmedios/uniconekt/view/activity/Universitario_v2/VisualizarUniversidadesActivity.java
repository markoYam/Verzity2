package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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
    List<Universidad> mUniversidads = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_universidades);
        ButterKnife.bind(this);
        mToolbar.setTitle("Universidades");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUniversidadPresenter = new UniversidadPresenter(this, getApplicationContext());
        loadData();
    }

    public void loadData() {
        SearchUniversidades mUniversidades = getIntent().getExtras().getParcelable(KEY_BUSQUEDA);
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
                    mUniversidadPresenter.Search(mUniversidades);
                    break;
                case 3:
                    //estados
                    getSupportActionBar().setTitle("Estados");
                    mUniversidadPresenter.Search(mUniversidades);
                    break;
                case 4:
                    //favoritos
                    mUniversidadPresenter.getFavoritos();
                    break;
                default:
                    finish();
                    break;
            }
        } else {
            showMessage("upss" +
                    "");
            finish();
        }

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

    }

    @Override
    public void Onfailed(String mensaje) {
        showMessage(mensaje);
        mTextViewEmpty.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        EmpyRecycler();
    }
}
