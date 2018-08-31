package com.dwmedios.uniconekt.view.activity.Universitario;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Becas;
import com.dwmedios.uniconekt.model.Cupones;
import com.dwmedios.uniconekt.presenter.CuponesPresenter;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.BecasAdapter;
import com.dwmedios.uniconekt.view.adapter.CuponAdapter;
import com.dwmedios.uniconekt.view.viewmodel.CuponesViewController;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario.DetalleCuponActivity.KEY_DETALLE_CUPON;
import static com.dwmedios.uniconekt.view.util.Utils.changeColorToolbar;
import static com.dwmedios.uniconekt.view.util.Utils.setStatusBarGradiant;

public class CuponesViewActivity extends BaseActivity implements CuponesViewController {

    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextView;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private CuponesPresenter mCuponesPresenter;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private CuponAdapter mCuponAdapter;
    private SearchView mSearchView;
    private List<Cupones> mCuponesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupones_view);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Cupones");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Cambiar el color del toolbar y status bar
        setStatusBarGradiant(this,R.drawable.status_cupon);
        changeColorToolbar(getSupportActionBar(),R.color.Color_cupones,CuponesViewActivity.this);
        mCuponesPresenter = new CuponesPresenter(this, getApplicationContext());
        mCuponesPresenter.GetCuponesVigentes();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mCuponesPresenter.GetCuponesVigentes();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.busqueda, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mCuponesPresenter.searchCupones(mCuponesList, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCuponesPresenter.searchCupones(mCuponesList, newText);

                return true;
            }
        });
        mSearchView.setQueryHint("Buscar cupones");
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

    @Override
    public void OnSucces(List<Cupones> mCuponesList, int i) {
        if (i == 1) this.mCuponesList = mCuponesList;
        mTextView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        configureRecyclerView(mCuponesList);
    }

    public void configureRecyclerView(List<Cupones> mCuponesList) {
        if (mCuponesList != null && mCuponesList.size() > 0) {
            boolean tabletSize = getResources().getBoolean(R.bool.is_tablet);
            if (tabletSize) {
                mCuponAdapter = new CuponAdapter(mCuponesList, mOnclick);
                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                mRecyclerView.setLayoutManager(layoutManager);
            } else {
                mCuponAdapter = new CuponAdapter(mCuponesList, mOnclick);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(layoutManager);

            }
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mCuponAdapter);
        } else {
            mRecyclerView.setAdapter(null);
            this.EmptyRecyclerView();
        }
    }

    CuponAdapter.onclick mOnclick = new CuponAdapter.onclick() {
        @Override
        public void onclick(Cupones mCupones) {
            startActivity(new Intent(getApplicationContext(), DetalleCuponActivity.class).putExtra(KEY_DETALLE_CUPON, mCupones));
            //  showMessage(mCupones.nombre);
        }
    };

    @Override
    public void OnFailed(String mensaje) {
        //showMessage(mensaje);
        this.EmptyRecyclerView();
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
    public void EmptyRecyclerView() {
        mRecyclerView.setAdapter(null);
        mSwipeRefreshLayout.setRefreshing(false);
        mTextView.setVisibility(View.VISIBLE);
    }
}
