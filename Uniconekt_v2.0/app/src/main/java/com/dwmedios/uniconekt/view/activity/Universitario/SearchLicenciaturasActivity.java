package com.dwmedios.uniconekt.view.activity.Universitario;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Licenciaturas;
import com.dwmedios.uniconekt.model.NivelAcademico;
import com.dwmedios.uniconekt.model.SearchUniversidades;
import com.dwmedios.uniconekt.presenter.LicenciaturasPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.VisualizarUniversidadesActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.LicenciaturasAdapter;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.LicenciaturaViewController;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario_v2.VisualizarUniversidadesActivity.KEY_BUSQUEDA;

public class SearchLicenciaturasActivity extends BaseActivity implements LicenciaturaViewController {
    public static final String KEY_NIVEL = "jksgvjnsgkjkjgskjgs";
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.buttonBuscarLicenciaturas)
    FloatingActionButton mButton;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private LicenciaturasPresenter mLicenciaturasPresenter;
    private LicenciaturasAdapter mLicenciaturasAdapter;
    private List<Licenciaturas> licenciaturasSeleccionadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_licenciaturas);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Programas académicos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLicenciaturasPresenter = new LicenciaturasPresenter(this, getApplicationContext());
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                reload();
            }
        });
        mButton.setOnClickListener(mOnClickListener);
        loadLicenciaturas();
    }

    public void loadLicenciaturas() {
        NivelAcademico mNivelEstudios = getIntent().getExtras().getParcelable(KEY_NIVEL);
        if (mNivelEstudios != null) {
            mLicenciaturasPresenter.GetLicenciaturas(mNivelEstudios);
        } else {
            showMessage("Ups");
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
        return super.onOptionsItemSelected(item);
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Utils.tipoBusqueda_Universidad = 2;
            SearchUniversidades universidades = new SearchUniversidades();
            SharePrefManager.getInstance(getApplicationContext()).saveLicenciaturas(licenciaturasSeleccionadas);
            Intent mIntent = new Intent(getApplicationContext(), VisualizarUniversidadesActivity.class);
            mIntent.putExtra(KEY_BUSQUEDA, universidades);
            startActivity(mIntent);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //
    }

    @Override
    public void Onsucces(List<Licenciaturas> mLicenciaturasList) {
        mTextView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        configureRecyclerView(mLicenciaturasList);
    }

    StickyRecyclerHeadersDecoration headersDecor;

    public void configureRecyclerView(List<Licenciaturas> mLicenciaturas) {
        if (mLicenciaturas != null && mLicenciaturas.size() > 0) {

            mLicenciaturasAdapter = new LicenciaturasAdapter(mLicenciaturas(mLicenciaturas), mListSelected);
            headersDecor = new StickyRecyclerHeadersDecoration(mLicenciaturasAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            mLicenciaturasAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    headersDecor.invalidateHeaders();

                }
            });
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mLicenciaturasAdapter);
            mRecyclerView.addItemDecoration(headersDecor);
            mLicenciaturasAdapter.notifyDataSetChanged();
        } else {
            mRecyclerView.setAdapter(null);
            this.EmpyRecycler();
        }
    }

    public List<Licenciaturas> mLicenciaturas(List<Licenciaturas> mLicenciaturas) {
        Licenciaturas temp = new Licenciaturas();
        List<Licenciaturas> cabeceras = new ArrayList<>();
        List<Licenciaturas> contenido = new ArrayList<>();
        if (mLicenciaturas != null) {
            //buscar cabeceras
            for (Licenciaturas mLicenciatura : mLicenciaturas) {
                boolean existe = false;
                for (Licenciaturas cabecera : cabeceras) {
                    if (cabecera.mNivelEstudios.nombre.equals(mLicenciatura.mNivelEstudios.nombre)) {
                        existe = true;
                    }
                }
                if (!existe) {
                    cabeceras.add(mLicenciatura);
                }

            }
            //buscar contenido
            for (Licenciaturas mLicenciatura : cabeceras) {
                Licenciaturas todo = new Licenciaturas();
                todo.nombre = "Todos";
                todo.id = -1;
                todo.ischeCked = false;
                todo.mNivelEstudios = mLicenciatura.mNivelEstudios;
                contenido.add(todo);
                for (Licenciaturas cabecera : mLicenciaturas) {
                    if (cabecera.mNivelEstudios.id == mLicenciatura.mNivelEstudios.id)
                        contenido.add(cabecera);
                }
            }
        }
        return contenido;
    }

    LicenciaturasAdapter.listSelected mListSelected = new LicenciaturasAdapter.listSelected() {
        @Override
        public void Selected(List<Licenciaturas> mLicenciaturasList) {
            if (mLicenciaturasList.size() > 0) {
                //   mLicenciaturasAdapter.notifyDataSetChanged();
                //mButton.setVisibility(View.VISIBLE);
                mButton.show();
                licenciaturasSeleccionadas = mLicenciaturasList;
                Animation anim = android.view.animation.AnimationUtils.loadAnimation(mButton.getContext(), R.anim.floating);
                anim.setDuration(200L);
                mButton.startAnimation(anim);
            } else
                //  mButton.setVisibility(View.GONE);
                mButton.hide();
        }
    };

    public void EmpyRecycler() {
        mTextView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void Onfailed(String mensaje) {
        showMessage(mensaje);
        mTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void Onloading(boolean loading) {
        if (loading) {
            showOnProgressDialog("Cargando..");
        } else {
            dismissProgressDialog();
        }
    }
}
