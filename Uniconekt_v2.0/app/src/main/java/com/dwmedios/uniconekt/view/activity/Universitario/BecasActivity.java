package com.dwmedios.uniconekt.view.activity.Universitario;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Becas;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.Videos;
import com.dwmedios.uniconekt.presenter.BecasPresenter;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.BecasAdapter;
import com.dwmedios.uniconekt.view.adapter.VideoAdapter;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.BecasViewController;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario.DetalleBecasActivity.KEY_BECA_DETALLE;
import static com.dwmedios.uniconekt.view.util.Transitions.Transisciones.Dw_CreateTransactions;
import static com.dwmedios.uniconekt.view.util.Utils.changeColorToolbar;
import static com.dwmedios.uniconekt.view.util.Utils.setStatusBarGradiant;

public class BecasActivity extends BaseActivity implements BecasViewController {
    public static final String KEY_BECAS = "key-becas";
    public static final String KEY_BECAS_COLOR = "key-becas_COLOR";
    public static int TYPE_VIEW = 1;
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private BecasAdapter mBecasAdapter;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private BecasPresenter mBecasPresenter;
    private List<Becas> mBecasList;
    private Universidad mUniversidad = new Universidad();
    private String querySearch = new String();
    // TODO: 02/05/2018  1=todos->2=beca universidad

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_becas);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Cambiar el color del toolbar y status bar
        setStatusBarGradiant(this,R.drawable.status_beca);
        changeColorToolbar(getSupportActionBar(), R.color.Color_becas, BecasActivity.this);

        mBecasPresenter = new BecasPresenter(this, getApplicationContext());
        this.ConfigureLoad();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                searchViewMetod(querySearch);
            }
        });

    }

    @Override
    public void Onsucces(List<Becas> mBecasList,int first) {
        Log.e("Becas", "Exito");
        if (first == 1)
            this.mBecasList = mBecasList;
        mTextView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        configureRecyclerView(mBecasList);
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
                searchViewMetod(query);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mUniversidad != null) {
                    mUniversidad.nombre = newText;
                    querySearch = newText;
                    searchViewMetod(newText);
                    return true;
                }
                return false;
            }
        });
        if (TYPE_VIEW == 2) {
            mSearchView.setQueryHint("Buscar becas");
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

    public void searchViewMetod(String criterio) {
        if (TYPE_VIEW == 2) {
            if (criterio!=null && !criterio.isEmpty()) {
                mBecasPresenter.SearchBecas(mBecasList, criterio,false);
            } else {
                mBecasPresenter.GetBecasUniversidad(mUniversidad);
            }

        } else if (TYPE_VIEW == 1) {           // mBecasPresenter.GetBecasUniversidad(mUniversidad);
            if (criterio!=null && !criterio.isEmpty()) {
                mBecasPresenter.SearchBecas(mBecasList, criterio,true);
            } else {
                mBecasPresenter.GetBecasUniversidad(mUniversidad);
            }
           // mBecasPresenter.SearchBecas(mBecasList, criterio,true);
        }
    }

    @Override
    public void ConfigureLoad() {
        try {
            Log.e("BecasActivity", "Type_View" + TYPE_VIEW);
            if (TYPE_VIEW == 2) {
                if (mUniversidad.nombre == null) {
                    mUniversidad = getIntent().getExtras().getParcelable(KEY_BECAS);
                    mBecasPresenter.GetBecasUniversidad(mUniversidad);
                }
                getSupportActionBar().setTitle("Becas " + mUniversidad.nombre);
            }
            if (TYPE_VIEW == 1) {
                getSupportActionBar().setTitle("Becas");
                mUniversidad = new Universidad();
                mBecasPresenter.GetBecasUniversidad(mUniversidad);
            }
        } catch (Exception e) {
            if (TYPE_VIEW == 1) {
                getSupportActionBar().setTitle("Becas");
                mUniversidad = new Universidad();
                mBecasPresenter.GetBecasUniversidad(mUniversidad);
            }
            e.printStackTrace();
        }
    }

    @Override
    public void OnFailed(String mensaje) {
        Log.e("Becas", mensaje);
        mRecyclerView.setAdapter(null);
        mTextView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void configureRecyclerView(List<Becas> mBecasList) {
        if (mBecasList != null && mBecasList.size() > 0) {
            boolean tabletSize = getResources().getBoolean(R.bool.is_tablet);
            if (tabletSize) {
                mBecasAdapter = new BecasAdapter(mBecasList, mOnclick);
                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                mRecyclerView.setLayoutManager(layoutManager);
            } else {
                mBecasAdapter = new BecasAdapter(mBecasList, mOnclick);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(layoutManager);

            }
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mBecasAdapter);
        } else {
            mRecyclerView.setAdapter(null);
            this.EmptyRecyclerView();
        }
    }
public static final String KEY_TRANSACTIONS="KEY_TRANSACTIONS";
    BecasAdapter.onclick mOnclick = new BecasAdapter.onclick() {
        @Override
        public void onclickButton(Becas mBecas, ImageView mImageView) {
            Intent mIntent = new Intent(getApplicationContext(), DetalleBecasActivity.class);
            mIntent.putExtra(KEY_BECA_DETALLE, mBecas);
            Dw_CreateTransactions(mIntent, KEY_TRANSACTIONS, mImageView, BecasActivity.this);
        }
    };

    @Override
    public void Onloading(boolean loading) {
        if (loading) {
            showOnProgressDialog("Cargado...");
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
        mTextView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setAdapter(null);
    }
}
