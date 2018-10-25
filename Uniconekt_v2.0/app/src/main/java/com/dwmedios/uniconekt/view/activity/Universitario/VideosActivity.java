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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.Videos;
import com.dwmedios.uniconekt.presenter.VideosPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.ReproductorUrlActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.VideoAdapter;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.viewmodel.VideoViewController;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario.VideoViewActivity.KEY_VIDEO_VIEWER;
import static com.dwmedios.uniconekt.view.util.Transitions.Transisciones.createTransitions;
import static com.dwmedios.uniconekt.view.util.Utils.changeColorToolbar;
import static com.dwmedios.uniconekt.view.util.Utils.setStatusBarGradiant;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class VideosActivity extends BaseActivity implements VideoViewController {
    public static String KEY_VIDEO = "KEY_VIDEO";
    public static final String KEY_VIDEO_TRANSITION = "KEY_47575278575";
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private VideosPresenter mVideosPresenter;
    private VideoAdapter videoAdapter;
    private List<Videos> mVideosList;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Videos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boolean extranjero = SharePrefManager.getInstance(getApplicationContext()).isSeachExtranjero();
        if (extranjero) {
            setStatusBarGradiant(VideosActivity.this, R.drawable.status_uni_extra);
            changeColorToolbar(getSupportActionBar(), R.color.Color_extranjero, VideosActivity.this);
        }
        mVideosPresenter = new VideosPresenter(this, getApplicationContext());
        this.configureView();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                configureView();
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
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    mVideosPresenter.GetVideos(searchUniversity);
                } else {
                    mVideosPresenter.searcVideos(mVideosList, query);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    mVideosPresenter.GetVideos(searchUniversity);
                } else {
                    mVideosPresenter.searcVideos(mVideosList, newText);
                }
                return true;
            }
        });
        mSearchView.setQueryHint("Buscar videos");
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
    public void Onsucces(List<Videos> videosList, int i) {
        if (i == 0)
            this.mVideosList = videosList;
        mTextView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        configureRecyclerView(videosList);
    }

    View mView, mView2;

    public void configureRecyclerView(List<Videos> mVideosList) {
        if (mVideosList != null && mVideosList.size() > 0) {
            boolean tabletSize = getResources().getBoolean(R.bool.is_tablet);
            if (tabletSize) {
                videoAdapter = new VideoAdapter(mVideosList, mOnclick);
                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                mRecyclerView.setLayoutManager(layoutManager);
            } else {
                videoAdapter = new VideoAdapter(mVideosList, mOnclick);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(layoutManager);

            }

            //LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            //  mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(videoAdapter);
        } else {
            mRecyclerView.setAdapter(null);
            this.EmptyRecyclerView();

        }
    }

    VideoAdapter.onclick mOnclick = new VideoAdapter.onclick() {
        @Override
        public void onclick(Videos mVideos, ImageView mImageView) {

            if (!isNullOrEmpty(mVideos.url)) {
                Intent mIntent = new Intent(getApplicationContext(), VideoViewActivity.class);
                mIntent.putExtra(KEY_VIDEO_VIEWER, mVideos);
                startActivity(mIntent, createTransitions(VideosActivity.this, KEY_VIDEO_TRANSITION, mImageView).toBundle());
            } else if (!isNullOrEmpty(mVideos.ruta)) {
                Intent mIntent = new Intent(getApplicationContext(), ReproductorUrlActivity.class);
                mIntent.putExtra(ReproductorUrlActivity.KEY_VIDEO_URL, mVideos);
                startActivity(mIntent);
            }

        }
    };

    @Override
    public void Onfailed(String mensaje) {
        showToastMessage(mensaje);
        mTextView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
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
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setAdapter(null);
        mTextView.setVisibility(View.VISIBLE);
    }

    private Universidad searchUniversity;

    @Override
    public void configureView() {
        try {
            if (searchUniversity == null) {
                searchUniversity = getIntent().getExtras().getParcelable(KEY_VIDEO);
                mVideosPresenter.GetVideos(searchUniversity);
            } else {
                mVideosPresenter.GetVideos(searchUniversity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
