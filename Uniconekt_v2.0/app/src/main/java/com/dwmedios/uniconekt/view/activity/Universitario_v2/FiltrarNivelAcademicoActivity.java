package com.dwmedios.uniconekt.view.activity.Universitario_v2;

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
import com.dwmedios.uniconekt.model.NivelAcademico;
import com.dwmedios.uniconekt.presenter.NivelAcademicoPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario.SearchLicenciaturasActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.CustomAdapter;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.NivelAcademicoViewController;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario.SearchLicenciaturasActivity.KEY_NIVEL;
import static com.dwmedios.uniconekt.view.util.Utils.changeColorToolbar;
import static com.dwmedios.uniconekt.view.util.Utils.setStatusBarGradiant;

public class FiltrarNivelAcademicoActivity extends BaseActivity implements NivelAcademicoViewController {
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextViewEmpty;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private NivelAcademicoPresenter mNivelAcademicoPresenter;
    private CustomAdapter mCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar_nivel_academico);
        ButterKnife.bind(this);
        mToolbar.setTitle("Seleccionar nivel académico");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boolean extranjero = SharePrefManager.getInstance(getApplicationContext()).isSeachExtranjero();
        if (extranjero) {
            setStatusBarGradiant(FiltrarNivelAcademicoActivity.this, R.drawable.status_uni_extra);
            changeColorToolbar(getSupportActionBar(), R.color.Color_extranjero, FiltrarNivelAcademicoActivity.this);
        }
        mNivelAcademicoPresenter = new NivelAcademicoPresenter(getApplicationContext(), this);
        mNivelAcademicoPresenter.getNivelesAcademicos();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNivelAcademicoPresenter.getNivelesAcademicos();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void Onsucces(List<NivelAcademico> mNivelAcademicoList) {
        mSwipeRefreshLayout.setRefreshing(false);
        mTextViewEmpty.setVisibility(View.GONE);
        configureRecyclerView(mNivelAcademicoList);
    }

    public void configureRecyclerView(List<NivelAcademico> mUniversidadList) {
        if (mUniversidadList != null && mUniversidadList.size() > 0) {
            mTextViewEmpty.setVisibility(View.GONE);
            mCustomAdapter = new CustomAdapter(mUniversidadList, R.layout.row_niveles, mUniversidadAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mCustomAdapter);
            Utils.setAnimRecyclerView(getApplicationContext(), R.anim.layout_animation, mRecyclerView);
        } else {
            mRecyclerView.setAdapter(null);

            this.EmpyRecycler();
        }
    }

    private void EmpyRecycler() {
        mTextViewEmpty.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setAdapter(null);
    }

    CustomAdapter.ConfigureHolder mUniversidadAdapter = new CustomAdapter.ConfigureHolder() {
        // @BindView(R.id.tituloRow)
        TextView mTextView;
        //@BindView(R.id.cardRow)
        CardView mCardView;
        ImageButton mImageButton;

        @Override
        public void Configure(View itemView, Object mObject) {
            //ButterKnife.bind(itemView.getContext(), itemView);
            mTextView = itemView.findViewById(R.id.texviewNombreItem);
            mCardView = itemView.findViewById(R.id.cardview);
            mImageButton = itemView.findViewById(R.id.imagebuttom);
            NivelAcademico mNivelAcademico = (NivelAcademico) mObject;
            mTextView.setText(mNivelAcademico.nombre);
        }

        @Override
        public void Onclick(final Object mObject) {
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NivelAcademico mNivelAcademico = (NivelAcademico) mObject;
                    startActivity(new Intent(getApplicationContext(), SearchLicenciaturasActivity.class).putExtra(KEY_NIVEL, mNivelAcademico));
                }
            });
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NivelAcademico mNivelAcademico = (NivelAcademico) mObject;
                    startActivity(new Intent(getApplicationContext(), SearchLicenciaturasActivity.class).putExtra(KEY_NIVEL, mNivelAcademico));
                }
            });
        }
    };

    @Override
    public void Onloading(boolean isLoading) {
        if (isLoading) {
            showOnProgressDialog("Cargando...");
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void Onfailed(String mensaje) {
        showMessage(mensaje);
        mSwipeRefreshLayout.setRefreshing(false);
        mTextViewEmpty.setVisibility(View.VISIBLE);
    }
}
