package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.NivelAcademico;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.presenter.AsesorPresenter;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.CustomAdapter;
import com.dwmedios.uniconekt.view.viewmodel.AsesoresViewController;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderUser;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class AsesoresActivity extends BaseActivity implements AsesoresViewController {
    public static int typeViewAsesor = 0;
    public static final String KEY_RETUNR_DATA = "KEY5475674DFDFDFDFWFW";
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextViewEmpty;
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    private CustomAdapter mCustomAdapter;
    private AsesorPresenter mAsesorPresenter;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asesores);
        ButterKnife.bind(this);
        mToolbar.setTitle("Demoo Asesores");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //falta acabar la contrucciopn
        mAsesorPresenter = new AsesorPresenter(this, getApplicationContext());
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                configureLoad();
            }
        });
        mFloatingActionButton.setOnClickListener(mOnClickListener);
        configureLoad();
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), PaquetesAsesoresActivity.class));
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (typeViewAsesor == 1) {
            Intent mIntent = getIntent();
            setResult(RESULT_CANCELED, mIntent);
            finish();
        } else
            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (typeViewAsesor == 1) {
                    Intent mIntent = getIntent();
                    setResult(RESULT_CANCELED, mIntent);
                    finish();
                } else
                    finish();
                break;
        }
        return true;
    }

    private void configureLoad() {
        switch (typeViewAsesor) {
            case 0:
                getSupportActionBar().setTitle("Mis asesores");
                mFloatingActionButton.show();
                mAsesorPresenter.getMisAsesores();
                break;
            case 1:
                getSupportActionBar().setTitle("Seleccionar asesor ");
                mFloatingActionButton.hide();
                mAsesorPresenter.getAsesores();
                break;
        }
    }

    @Override
    public void Onsucces(List<Persona> mPersonaList) {
        mSwipeRefreshLayout.setRefreshing(false);
        configureRecyclerView(mPersonaList);
    }

    public void configureRecyclerView(List<Persona> mPersonaList) {
        if (mPersonaList != null && mPersonaList.size() > 0) {
            mTextViewEmpty.setVisibility(View.GONE);
            mCustomAdapter = new CustomAdapter(mPersonaList, R.layout.row_asesores, mConfigureHolder);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mCustomAdapter);
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

    CustomAdapter.ConfigureHolder mConfigureHolder = new CustomAdapter.ConfigureHolder() {
        ImageView mImageView;
        TextView mTextView;
        ImageButton mImageButton;
        CardView mCardView;

        @Override
        public void Configure(View itemView, Object mObject) {
            Persona mPersona = (Persona) mObject;
            mImageView = itemView.findViewById(R.id.imageAsesor);
            mTextView = itemView.findViewById(R.id.texviewNombreAsesor);
            mImageButton = itemView.findViewById(R.id.imagebuttom);
            mCardView = itemView.findViewById(R.id.cardAsesor);
            mTextView.setText(mPersona.nombre);
            if (!isNullOrEmpty(mPersona.foto))
                ImageLoader.getInstance().displayImage(getUrlImage(mPersona.foto, getApplicationContext()), mImageView, OptionsImageLoaderUser)
                        ;
            else
                mImageView.setImageResource(R.drawable.profile);
        }

        @Override
        public void Onclick(Object mObject) {
            final Persona mPersona = (Persona) mObject;
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showMessage(mPersona.nombre);
                    switch (typeViewAsesor) {
                        case 0: /*detalle*/
                            showMessage("Detalle en construccion");
                            break;
                        case 1: /*finalizar*/
                            Intent mIntent = getIntent();
                            mIntent.putExtra(KEY_RETUNR_DATA, mPersona);
                            setResult(RESULT_OK, mIntent);
                            finish();
                            break;
                    }
                }
            });
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // showMessage(mPersona.nombre);
                    switch (typeViewAsesor) {
                        case 0: /*detalle*/
                            showMessage("Detalle en construccion");
                            break;
                        case 1: /*finalizar*/
                            Intent mIntent = getIntent();
                            mIntent.putExtra(KEY_RETUNR_DATA, mPersona);
                            setResult(RESULT_OK, mIntent);
                            finish();
                            break;
                    }
                }
            });
        }
    };

    @Override
    public void Onfailed(String mensaje) {
        showMessage(mensaje);
        this.EmpyRecycler();
    }

    @Override
    public void OnLoading(boolean isLoading) {
        if (isLoading)
            showOnProgressDialog("Cargando...");
        else
            dismissProgressDialog();
    }
}
