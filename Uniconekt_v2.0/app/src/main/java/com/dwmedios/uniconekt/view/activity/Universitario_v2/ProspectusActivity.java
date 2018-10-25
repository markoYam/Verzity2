package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.content.Intent;
import android.net.Uri;
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
import com.dwmedios.uniconekt.model.ClasViewModel;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.presenter.ProspectusPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario.VideosActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.MenuAdapter;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.viewmodel.ProspectusViewController;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario.VideosActivity.KEY_VIDEO;
import static com.dwmedios.uniconekt.view.util.Utils.changeColorToolbar;
import static com.dwmedios.uniconekt.view.util.Utils.setStatusBarGradiant;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class ProspectusActivity extends BaseActivity implements ProspectusViewController {
    public static final String KEY_PROSPECTUS = "LIRBGV02GVBNOR3G7B3QIÑ";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextView;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ProspectusPresenter mProspectusPresenter;
    private MenuAdapter menuAdapter;
    private Universidad mUniversidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prospectus);
        ButterKnife.bind(this);
        mToolbar.setTitle("Prospectus");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boolean extranjero = SharePrefManager.getInstance(getApplicationContext()).isSeachExtranjero();
        if (extranjero) {
            setStatusBarGradiant(ProspectusActivity.this, R.drawable.status_uni_extra);
            changeColorToolbar(getSupportActionBar(), R.color.Color_extranjero, ProspectusActivity.this);
        }
        mProspectusPresenter = new ProspectusPresenter(this, getApplicationContext());
        mProspectusPresenter.crearMenu();
        loadInfo();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mProspectusPresenter.crearMenu();
                loadInfo();
            }
        });
    }

    private void loadInfo() {
        if (mUniversidad == null)
            mUniversidad = getIntent().getExtras().getParcelable(KEY_PROSPECTUS);
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
        super.onBackPressed();
        finish();
    }

    @Override
    public void Onsucces(List<ClasViewModel.menu> menuList) {
        mTextView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        configureRecyclerView(menuList);
    }

    public void configureRecyclerView(List<ClasViewModel.menu> menus) {
        if (menus != null && menus.size() > 0) {
            menuAdapter = new MenuAdapter(menus, mOnclick, 2);
            LinearLayoutManager layoutManager = new LinearLayoutManager(ProspectusActivity.this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(menuAdapter);
        } else {
            mRecyclerView.setAdapter(null);
            this.EmpyRecycler();
        }
    }

    private void EmpyRecycler() {
        mTextView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    MenuAdapter.onclick mOnclick = new MenuAdapter.onclick() {
        @Override
        public void onclick(ClasViewModel.menu menu) {
            ClasViewModel.tipoMenu validate = ClasViewModel.tipoMenu.valueOf(menu.tipo.toString());
            switch (validate) {
                case videos:
                    Intent mIntent = new Intent(getApplicationContext(), VideosActivity.class);
                    mIntent.putExtra(KEY_VIDEO, mUniversidad);
                    startActivity(mIntent);
                    break;
                case folletos:
                    if (!isNullOrEmpty(mUniversidad.folleto)) {
                        if (mUniversidad.folleto.startsWith("http://") || mUniversidad.folleto.startsWith("https://")) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                            browserIntent.setData(Uri.parse(mUniversidad.folleto));
                            startActivity(browserIntent);
                        } else {
                            showMessage("La universidad no cuenta con folletos digitales");
                        }

                    } else
                        showMessage("La universidad no cuenta con folletos digitales");
                    break;
            }

        }
    };

    @Override
    public void OnFailed(String mensaje) {

    }
}
