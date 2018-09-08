package com.dwmedios.uniconekt.view.activity.Universitario;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Financiamientos;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.presenter.FinanciamientoPresenter;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.FinanciamientoAdapter;
import com.dwmedios.uniconekt.view.viewmodel.FinanciamientoViewController;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario.DetalleFinanciamientoActivity.KEY_FINANCIAMIENTO_DETALLE;
import static com.dwmedios.uniconekt.view.util.Transitions.Transisciones.createTransitions;
import static com.dwmedios.uniconekt.view.util.Utils.changeColorToolbar;
import static com.dwmedios.uniconekt.view.util.Utils.setStatusBarGradiant;

public class FinanciamientoActivity extends BaseActivity implements FinanciamientoViewController {
    public static int TYPE_VIEW_F = 1;
    public static final String KEY_FINANCIAMIENTOS = "KEY_FINANCIAMIENTOS";
    public static final String KEY_TRANSICIONES_1 = "KEY_TRANSICIONES_1";
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private FinanciamientoAdapter financiamientoAdapter;
    private FinanciamientoPresenter mFinanciamientoPresenter;
    private Universidad mUniversidad;
    private List<Financiamientos> mFinanciamientosList;
    private String querySearch = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financiamiento);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Cambiar el color del toolbar y status bar
        setStatusBarGradiant(this, R.drawable.status_financiamiento);
        changeColorToolbar(getSupportActionBar(), R.color.Color_financiamientos, FinanciamientoActivity.this);

        mFinanciamientoPresenter = new FinanciamientoPresenter(this, getApplicationContext());
        this.ConfigureLoad();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                searchViewEvent(querySearch);
            }
        });
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

    private SearchView mSearchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.busqueda, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mUniversidad.nombre = query;
                querySearch = query;
                searchViewEvent(query);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mUniversidad != null) {
                    mUniversidad.nombre = newText;
                    querySearch = newText;
                    searchViewEvent(newText);
                }
                return true;
            }
        });
        if (TYPE_VIEW_F == 2) {
            mSearchView.setQueryHint("Buscar financiamiento");
        } else {
            mSearchView.setQueryHint("Buscar por universidad");
        }

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

    private void searchViewEvent(String criterio) {
        if (TYPE_VIEW_F == 2) {
            if (criterio != null && !criterio.isEmpty()) {
                mFinanciamientoPresenter.SearchFinanciamientos(mFinanciamientosList, criterio, false);
            } else {
                mFinanciamientoPresenter.SearchFinanciamientos(mUniversidad);
            }

        } else if (TYPE_VIEW_F == 1) {
            //   mFinanciamientoPresenter.SearchFinanciamientos(mUniversidad);
            // mFinanciamientoPresenter.SearchFinanciamientos(mFinanciamientosList, criterio, true);
            if (criterio != null && !criterio.isEmpty()) {
                mFinanciamientoPresenter.SearchFinanciamientos(mFinanciamientosList, criterio, true);
            } else {
                mFinanciamientoPresenter.SearchFinanciamientos(mUniversidad);
            }
        }
    }

    public void ConfigureLoad() {
        try {
            Log.e("Financiamientos", "Type view " + TYPE_VIEW_F);
            if (TYPE_VIEW_F == 2) {

                if (mUniversidad == null || mUniversidad.nombre == null) {
                    mUniversidad = getIntent().getExtras().getParcelable(KEY_FINANCIAMIENTOS);
                    mFinanciamientoPresenter.SearchFinanciamientos(mUniversidad);
                    getSupportActionBar().setTitle("Financiamientos " + mUniversidad.nombre);
                } else {
                    getSupportActionBar().setTitle("Financiamientos ");
                }

            }
            if (TYPE_VIEW_F == 1) {
                getSupportActionBar().setTitle("Financiamientos");
                mUniversidad = new Universidad();
                mFinanciamientoPresenter.SearchFinanciamientos(mUniversidad);
            }
        } catch (Exception e) {
            if (TYPE_VIEW_F == 1) {
                getSupportActionBar().setTitle("Financiamientos");
                mUniversidad = new Universidad();
                mFinanciamientoPresenter.SearchFinanciamientos(mUniversidad);
            }
            e.printStackTrace();
        }
    }

    @Override
    public void OnSucces(List<Financiamientos> mFinanciamientosList, int first) {
        if (first == 1)
            this.mFinanciamientosList = mFinanciamientosList;
        mTextView.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        configureRecyclerView(mFinanciamientosList);
    }

    @Override
    public void OnFailed(String mensaje) {
        //showMessage(mensaje);
        EmptyRecyclerView();
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
    public void onBackPressed() {
        if (!mSearchView.isIconified()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mSearchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void EmptyRecyclerView() {
        mRecyclerView.setAdapter(null);
        mTextView.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }

    public void configureRecyclerView(List<Financiamientos> mFinanciamientos) {
        if (mFinanciamientos != null && mFinanciamientos.size() > 0) {

            boolean tabletSize = getResources().getBoolean(R.bool.is_tablet);
            if (tabletSize) {
                financiamientoAdapter = new FinanciamientoAdapter(mFinanciamientos, mOnclick);
                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                mRecyclerView.setLayoutManager(layoutManager);
            } else {
                financiamientoAdapter = new FinanciamientoAdapter(mFinanciamientos, mOnclick);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(layoutManager);

            }
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(financiamientoAdapter);
        } else {
            mRecyclerView.setAdapter(null);
            this.EmptyRecyclerView();
        }
    }

    FinanciamientoAdapter.onclick mOnclick = new FinanciamientoAdapter.onclick() {
        @Override
        public void onclickButton(Financiamientos financiamientos, ImageView mImageView) {
            try {
                Intent mIntent = new Intent(getApplicationContext(), DetalleFinanciamientoActivity.class);
                mIntent.putExtra(KEY_FINANCIAMIENTO_DETALLE, financiamientos);
                startActivity(mIntent, createTransitions(FinanciamientoActivity.this, FinanciamientoActivity.KEY_TRANSICIONES_1, mImageView).toBundle());
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(getApplicationContext(), "No es posible visualizar el detalle", Toast.LENGTH_SHORT).show();
            }

        }
    };
}
