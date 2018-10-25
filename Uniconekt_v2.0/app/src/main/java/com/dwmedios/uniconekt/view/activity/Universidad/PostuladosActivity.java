package com.dwmedios.uniconekt.view.activity.Universidad;

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
import com.dwmedios.uniconekt.model.PostuladosGeneral;
import com.dwmedios.uniconekt.model.TipoPostulacion;
import com.dwmedios.uniconekt.presenter.PostuladosGeneralesPresenter;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.PostuladosAdapter;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.PostuladosGeneralesViewController;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universidad.DetalleNotificacionActivity.KEY_POSTULADO_DETALLE;
import static com.dwmedios.uniconekt.view.activity.Universidad.DetalleNotificacionActivity.VIEW_POSTULADO_DETALLE;
import static com.dwmedios.uniconekt.view.activity.Universidad_v2.TipoPostuladosActivity.KEY_TIPO_POSTU;

public class PostuladosActivity extends BaseActivity implements PostuladosGeneralesViewController {
    private PostuladosAdapter mPostuladosAdapter;
    private StickyRecyclerHeadersDecoration mStickyRecyclerHeadersDecoration;
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private PostuladosGeneralesPresenter mPostuladosGeneralesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postulados);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("Postulados");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPostuladosGeneralesPresenter = new PostuladosGeneralesPresenter(this, getApplicationContext());
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                reload();
            }
        });
        getPostulados();
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

    TipoPostulacion mTipoPostulacion;

    public void getPostulados() {
        if (mTipoPostulacion == null)
            mTipoPostulacion = getIntent().getExtras().getParcelable(KEY_TIPO_POSTU);
        if (mTipoPostulacion != null) {
            mPostuladosGeneralesPresenter.Getpostulados(mTipoPostulacion.tipo);
        }
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    public void Onsuccess(List<PostuladosGeneral> mPostuladosGenerals) {
        configureRecyclerView(mPostuladosGenerals);
    }

    public void configureRecyclerView(List<PostuladosGeneral> mPostuladosGenerals) {
        mTextView.setVisibility(View.GONE);
        if (mPostuladosGenerals != null && mPostuladosGenerals.size() > 0) {

            mPostuladosAdapter = new PostuladosAdapter(mPostuladosGenerals, mOnclick);
            mStickyRecyclerHeadersDecoration = new StickyRecyclerHeadersDecoration(mPostuladosAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            mPostuladosAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    mStickyRecyclerHeadersDecoration.invalidateHeaders();

                }
            });
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mPostuladosAdapter);
            mRecyclerView.addItemDecoration(mStickyRecyclerHeadersDecoration);
            Utils.setAnimRecyclerView(getApplicationContext(),R.anim.layout_animation,mRecyclerView);
        } else {
            mRecyclerView.setAdapter(null);
            this.EmptyRecyclerView();
        }
    }

    PostuladosAdapter.onclick mOnclick = new PostuladosAdapter.onclick() {
        @Override
        public void onclickListener(PostuladosGeneral mPostuladosGeneral) {
            try {
                VIEW_POSTULADO_DETALLE = 1;
                Intent mIntent = new Intent(getApplicationContext(), DetalleNotificacionActivity.class);
                mIntent.putExtra(KEY_POSTULADO_DETALLE, mPostuladosGeneral);
                startActivity(mIntent);
            } catch (Exception ex) {
                ex.printStackTrace();
                /**
                 * poner validacion de descripcion o mensaje
                 s
                 */
                showMessage("No es posible visualizar el detalle.");
            }

        }
    };

    @Override
    public void OnFailed(String mensaje) {
        showMessage(mensaje);
        mTextView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void Onloading(boolean loading) {
        if (loading) {
            showOnProgressDialog("Cargando..");
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void EmptyRecyclerView() {
        mTextView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
