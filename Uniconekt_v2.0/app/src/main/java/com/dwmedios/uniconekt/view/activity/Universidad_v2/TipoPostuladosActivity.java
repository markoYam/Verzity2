package com.dwmedios.uniconekt.view.activity.Universidad_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.TipoPostulacion;
import com.dwmedios.uniconekt.presenter.TipoPostulacionPresenter;
import com.dwmedios.uniconekt.view.activity.Universidad.PostuladosActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.CustomAdapter;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.TipoPostulacionViewController;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TipoPostuladosActivity extends BaseActivity implements TipoPostulacionViewController {
    public static final String KEY_TIPO_POSTU = "KEY_DFKOUHNJSDGYSDLF";
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextView;
    private TipoPostulacionPresenter mTipoPostulacionPresenter;
    private CustomAdapter mCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_postulados);
        ButterKnife.bind(this);
        mToolbar.setTitle("Seleccionar tipo postulación");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTipoPostulacionPresenter = new TipoPostulacionPresenter(this,getApplicationContext());
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mTipoPostulacionPresenter.getTipoPostulacion();
            }
        });
        mTipoPostulacionPresenter.getTipoPostulacion();
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
    public void Onsuccess(List<TipoPostulacion> mPostulacionList) {
        mSwipeRefreshLayout.setRefreshing(false);
        configureRecyclerView(mPostulacionList);
    }

    public void configureRecyclerView(List<TipoPostulacion> mPostulacionList) {
        if (mPostulacionList != null && mPostulacionList.size() > 0) {
            mTextView.setVisibility(View.GONE);
            mCustomAdapter = new CustomAdapter(mPostulacionList, R.layout.row_niveles, mHolder);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mCustomAdapter);
            Utils.setAnimRecyclerView(getApplicationContext(),R.anim.layout_animation,mRecyclerView);

        } else {
            mRecyclerView.setAdapter(null);

            this.EmpyRecycler();
        }
    }

    private void EmpyRecycler() {
        mTextView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setAdapter(null);
    }

    CustomAdapter.ConfigureHolder mHolder = new CustomAdapter.ConfigureHolder() {
        TextView mTextView;
        CardView mCardView;
        ImageButton mImageButton;
        ;

        @Override
        public void Configure(View itemView, Object mObject) {
            mTextView = itemView.findViewById(R.id.texviewNombreItem);
            mCardView = itemView.findViewById(R.id.cardview);
            mImageButton = itemView.findViewById(R.id.imagebuttom);
            TipoPostulacion mTipoPostulacion = (TipoPostulacion) mObject;
            mTextView.setText(mTipoPostulacion.nombre);
        }

        @Override
        public void Onclick(Object mObject) {
            final TipoPostulacion mTipoPostulacion = (TipoPostulacion) mObject;
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), PostuladosActivity.class).putExtra(KEY_TIPO_POSTU, mTipoPostulacion));

                }
            });
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), PostuladosActivity.class).putExtra(KEY_TIPO_POSTU, mTipoPostulacion));

                }
            });

        }
    };

    @Override
    public void Onfailed(String mensaje) {

    }

    @Override
    public void OnLoading(boolean isloading) {

    }
}
