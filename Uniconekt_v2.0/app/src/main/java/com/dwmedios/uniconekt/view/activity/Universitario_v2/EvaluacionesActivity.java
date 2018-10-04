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
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.cuestionarios.EvaluacionesPersona;
import com.dwmedios.uniconekt.model.cuestionarios.Resultados;
import com.dwmedios.uniconekt.presenter.EvaluacionesPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario.MainUniversitarioActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.CustomAdapter;
import com.dwmedios.uniconekt.view.viewmodel.CustomViewController;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario_v2.PresentarCuestionarioActivity.KEY_PRESENTAR;
import static com.dwmedios.uniconekt.view.activity.Universitario_v2.ResultadoActivity.KEY_RESULTADO;

public class EvaluacionesActivity extends BaseActivity implements CustomViewController {

    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextViewEmpty;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private CustomAdapter mCustomAdapter;
    public EvaluacionesPresenter mEvaluacionesPresenter;
    private SearchView mSearchView;
    private List<EvaluacionesPersona> mEvaluacionesPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluaciones);
        ButterKnife.bind(this);
        mToolbar.setTitle("Cuestionarios");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEvaluacionesPresenter = new EvaluacionesPresenter(this, getApplicationContext());
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mEvaluacionesPresenter.getEvaluaciones();
            }
        });
    }

    @Override
    protected void onStart() {
        mEvaluacionesPresenter.getEvaluaciones();
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!mSearchView.isIconified()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mSearchView.onActionViewCollapsed();
        } else {
            Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
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
                mEvaluacionesPresenter.SearchEvaluaciones(mEvaluacionesPersonas, query);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mEvaluacionesPresenter.SearchEvaluaciones(mEvaluacionesPersonas, newText);
                return true;
            }
        });

        mSearchView.setQueryHint("Buscar cuestionarios");


        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchView.setFocusable(true);
                mSearchView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mSearchView, InputMethodManager.SHOW_IMPLICIT);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (mSearchView.isIconified()) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    mSearchView.onActionViewCollapsed();

                }
                return true;
            }
        });

        mSearchView.requestFocus();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void OnSucces(List<?> mObjectsList) {

    }

    @Override
    public void OnSucces(Object mObjectsList) {
        Resultados mResultados = (Resultados) mObjectsList;
        startActivity(new Intent(getApplicationContext(), ResultadoActivity.class).putExtra(KEY_RESULTADO, mResultados));
    }

    @Override
    public void OnSucces(List<?> mObjectsList, int tipo) {
        if (tipo == 0) this.mEvaluacionesPersonas = (List<EvaluacionesPersona>) mObjectsList;
        mTextViewEmpty.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        configureRecyclerView(mObjectsList);
    }

    public void configureRecyclerView(List<?> mEstados) {
        if (mEstados != null && mEstados.size() > 0) {
            mCustomAdapter = new CustomAdapter(mEstados, R.layout.row_evaluaciones, mConfigureHolder);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mCustomAdapter);
        } else {
            mRecyclerView.setAdapter(null);
            this.empyRecycler();
        }
    }

    public void empyRecycler() {
        mTextViewEmpty.setVisibility(View.VISIBLE);
        mRecyclerView.setAdapter(null);
        //   mSwipeRefreshLayout.setRefreshing(false);
    }

    CustomAdapter.ConfigureHolder mConfigureHolder = new CustomAdapter.ConfigureHolder() {
        TextView mTextViewNombre;
        TextView mTextViewResultado;
        Button mButton0; //res
        Button mButton1; //continuar
        Button mButton2;//presentar

        @Override
        public void Configure(View itemView, Object mObject) {

            EvaluacionesPersona mEvaluacionesPersona = (EvaluacionesPersona) mObject;
            mButton0 = itemView.findViewById(R.id.buttonEpresentar0);
            mButton1 = itemView.findViewById(R.id.buttonEpresentar1);
            mButton2 = itemView.findViewById(R.id.buttonEpresentar2);

            mTextViewNombre = itemView.findViewById(R.id.textViewEnombre);
            mTextViewResultado = itemView.findViewById(R.id.textViewEestatus);
            mTextViewNombre.setText(mEvaluacionesPersona.mEvaluaciones.nombre);
            switch (mEvaluacionesPersona.mEstatus.estatusNot) {
                case pe:
                    mButton2.setVisibility(View.VISIBLE);
                    mButton0.setVisibility(View.GONE);
                    mButton1.setVisibility(View.GONE);
                    break;
                case en:
                    mButton1.setVisibility(View.VISIBLE);
                    mButton0.setVisibility(View.GONE);
                    mButton2.setVisibility(View.GONE);
                    break;
                case fi:
                    mButton0.setVisibility(View.VISIBLE);
                    mButton1.setVisibility(View.GONE);
                    mButton2.setVisibility(View.GONE);
                    break;

            }
            mTextViewResultado.setText(mEvaluacionesPersona.mEstatus.estatusNot);
        }

        @Override
        public void Onclick(Object mObject) {
            final EvaluacionesPersona mEvaluacionesPersona = (EvaluacionesPersona) mObject;
            mButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (mEvaluacionesPersona.mEstatus.estatusNot) {
                        case pe:
                            startActivity(new Intent(getApplicationContext(), PresentarCuestionarioActivity.class).putExtra(KEY_PRESENTAR, mEvaluacionesPersona));
                            break;
                        case en:
                            startActivity(new Intent(getApplicationContext(), PresentarCuestionarioActivity.class).putExtra(KEY_PRESENTAR, mEvaluacionesPersona));
                            break;
                        case fi:
                            //startActivity(new Intent(getApplicationContext(), ResultadoActivity.class));
                            //showMessage("VER RESULTADO");
                            mEvaluacionesPresenter.getResultado(mEvaluacionesPersona);
                            break;

                    }
                }
            });
            mButton0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (mEvaluacionesPersona.mEstatus.estatusNot) {
                        case pe:
                            startActivity(new Intent(getApplicationContext(), PresentarCuestionarioActivity.class).putExtra(KEY_PRESENTAR, mEvaluacionesPersona));
                            break;
                        case en:
                            startActivity(new Intent(getApplicationContext(), PresentarCuestionarioActivity.class).putExtra(KEY_PRESENTAR, mEvaluacionesPersona));
                            break;
                        case fi:
                            //startActivity(new Intent(getApplicationContext(), ResultadoActivity.class));
                            //showMessage("VER RESULTADO");
                            mEvaluacionesPresenter.getResultado(mEvaluacionesPersona);
                            break;

                    }
                }
            });
            mButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (mEvaluacionesPersona.mEstatus.estatusNot) {
                        case pe:
                            startActivity(new Intent(getApplicationContext(), PresentarCuestionarioActivity.class).putExtra(KEY_PRESENTAR, mEvaluacionesPersona));
                            break;
                        case en:
                            startActivity(new Intent(getApplicationContext(), PresentarCuestionarioActivity.class).putExtra(KEY_PRESENTAR, mEvaluacionesPersona));
                            break;
                        case fi:
                            //startActivity(new Intent(getApplicationContext(), ResultadoActivity.class));
                            //showMessage("VER RESULTADO");
                            mEvaluacionesPresenter.getResultado(mEvaluacionesPersona);
                            break;

                    }
                }
            });
        }
    };
    private static final String pe = "PENDIENTE";
    private static final String en = "EN PROCESO";
    private static final String fi = "FINALIZADO";

    @Override
    public void Onfailed(String mensaje) {
        mTextViewEmpty.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        this.empyRecycler();

    }

    @Override
    public void Onfailed2(String mensaje) {
        showMessage(mensaje);
    }

    @Override
    public void OnLoading(boolean isLoading) {
        if (isLoading) {
            showOnProgressDialog("Cargando...");
        } else
            dismissProgressDialog();
    }
}
