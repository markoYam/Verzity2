package com.dwmedios.uniconekt.view.activity.Universitario;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
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
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.UniversidadAdapter;
import com.dwmedios.uniconekt.view.viewmodel.UniversidadViewController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario.DetalleUniversidadActivity.KEY_DETALLE_UNIVERSIDAD;
import static com.dwmedios.uniconekt.view.util.Utils.ParseString;

public class ViewUniversidadesActivity extends BaseActivity implements UniversidadViewController {
    public static String UNIVERSIDAD_KEY = "UNIVERSIDAD";
    public static int SEARCH_KEY = 1;
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextViewEmpy;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private UniversidadAdapter mUniversidadAdapter;
    private UniversidadPresenter mUniversidadPresenter;

    SearchUniversidades uni;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_universidades);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        //  getSupportActionBar().setTitle("Buscar universidades");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUniversidadPresenter = new UniversidadPresenter(this, getApplicationContext());
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                loadUniversidades();

            }
        });
        if (SEARCH_KEY != 3)
            loadUniversidades();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(SEARCH_KEY==3)loadUniversidades();

    }

    SearchView mSearchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.busqueda, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (uni != null) {
                    uni.nombreUniversidad = query;
                    if (query != null || !query.isEmpty()) {
                        mUniversidadPresenter.searchUniversidades(mUniversidadList, query);
                    } else
                        loadUniversidades();
                    mSearchView.clearFocus();
                } else {
                    loadUniversidades();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (uni != null) {
                    uni.nombreUniversidad = newText;
                    if (newText != null || !newText.trim().isEmpty()) {
                        mUniversidadPresenter.searchUniversidades(mUniversidadList, newText);
                    } else
                        loadUniversidades();
                } else
                    loadUniversidades();
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
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (mSearchView.isIconified()) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    //configureSearch();
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    mSearchView.onActionViewCollapsed();
                    //configureSearch();
                }
                return true;
            }
        });

        mSearchView.requestFocus();
        return super.onCreateOptionsMenu(menu);
    }

    private void searFavoritos() {
        if (uni != null && mUniversidadList != null && mUniversidadList.size() > 0) {
            if (!uni.nombreUniversidad.isEmpty()) {
                List<Universidad> temp = new ArrayList<>();
                for (Universidad mUniversidad : mUniversidadList) {
                    if (ParseString(mUniversidad.nombre.toLowerCase()).contains(ParseString(uni.nombreUniversidad.toLowerCase())))
                        temp.add(mUniversidad);
                }
                configureRecyclerView(temp);
            } else {
                loadUniversidades();
            }
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


    public void loadUniversidades() {
        try {
            if (SEARCH_KEY == 1) {
                getSupportActionBar().setTitle("Universidades");
                if (uni == null) uni = new SearchUniversidades();
                //  if(uni.nombreUniversidad!=null || uni.nombreUniversidad.isEmpty())
                mUniversidadPresenter.Search(uni);
            } else if (SEARCH_KEY == 2) {
                getSupportActionBar().setTitle("Programas académicos");
                if (uni == null)
                    uni = getIntent().getExtras().getParcelable(UNIVERSIDAD_KEY);
                mUniversidadPresenter.Search(uni);
            } else if (SEARCH_KEY == 3) {
                mUniversidadPresenter.getFavoritos();
                uni = new SearchUniversidades();
                getSupportActionBar().setTitle("Favoritos");
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.EmpyRecycler();
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

    @Override
    public void Onloading(boolean loading) {
        if (loading) {
            showOnProgressDialog("Cargando...");
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void EmpyRecycler() {
        mTextViewEmpy.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setAdapter(null);
    }

    private List<Universidad> mUniversidadList;

    @Override
    public void OnSuccess(List<Universidad> mUniversidadList) {
        mSwipeRefreshLayout.setRefreshing(false);
        mTextViewEmpy.setVisibility(View.GONE);
        this.mUniversidadList = mUniversidadList;
        configureRecyclerView(this.mUniversidadList);
    }
//private List<Universidad> mUniversidadList;
    @Override
    public void OnSuccessSeach(List<Universidad> mUniversidadList, int type) {
        mSwipeRefreshLayout.setRefreshing(false);
        mTextViewEmpy.setVisibility(View.GONE);
        if (type == 1)
            this.mUniversidadList = mUniversidadList;
        configureRecyclerView(mUniversidadList);
    }

    @Override
    public void Onfailed(String mensaje) {
        mTextViewEmpy.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        EmpyRecycler();
    }


    public void configureRecyclerView(List<Universidad> mUniversidadList) {
        if (mUniversidadList != null && mUniversidadList.size() > 0) {
            mTextViewEmpy.setVisibility(View.GONE);
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

}
