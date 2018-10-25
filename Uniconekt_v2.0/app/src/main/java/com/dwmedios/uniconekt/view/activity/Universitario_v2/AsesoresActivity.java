package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.presenter.AsesorPresenter;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.CustomAdapter;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.AsesoresViewController;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario_v2.DetalleAsesorActivity.KEY_USUARIO;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class AsesoresActivity extends BaseActivity implements AsesoresViewController {
    public static int typeViewAsesor = 1;
    public static final String KEY_RETUNR_DATA = "KEY5475674DFDFDFDFWFW";
    public static final String KEY_TRANSITIONS_ASESOR = "KEY1_ASESOR85251";
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
    com.getbase.floatingactionbutton.FloatingActionButton mFloatingActionButton;
    @BindView(R.id.menu_fab)
    FloatingActionsMenu menu;

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

    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), PaquetesAsesoresActivity.class));
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        configureLoad();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (typeViewAsesor == 1) {
            Intent mIntent = getIntent();
            setResult(RESULT_CANCELED, mIntent);
            typeViewAsesor = 0;
            finish();
        } else
            finish();
    }

    @Override
    protected void onDestroy() {
        typeViewAsesor = 0;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (typeViewAsesor == 1) {
                    Intent mIntent = getIntent();
                    setResult(RESULT_CANCELED, mIntent);
                    typeViewAsesor = 0;
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
                menu.setVisibility(View.VISIBLE);
                mAsesorPresenter.getMisAsesores();
                break;
            case 1:
                getSupportActionBar().setTitle("Seleccionar asesor ");
                menu.setVisibility(View.GONE);
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

    CustomAdapter.ConfigureHolder mConfigureHolder = new CustomAdapter.ConfigureHolder() {
        ImageView mImageView;
        TextView mTextView;
        TextView mTextViewDescripcion;
        Button mImageButton;
        CardView mCardView;
        LinearLayout mLinearLayout;

        @Override
        public void Configure(View itemView, Object mObject) {
            Persona mPersona = (Persona) mObject;
            mImageView = itemView.findViewById(R.id.imageAsesor);
            mTextView = itemView.findViewById(R.id.texviewNombreAsesor);
            mTextViewDescripcion = itemView.findViewById(R.id.textViewDescripcion);
            mImageButton = itemView.findViewById(R.id.imagebuttom);
            mLinearLayout = itemView.findViewById(R.id.contenedorDescripcion);
            mCardView = itemView.findViewById(R.id.cardAsesor);
            mTextView.setText(mPersona.nombre);

            if (!isNullOrEmpty(mPersona.desPersona)) {
                mLinearLayout.setVisibility(View.VISIBLE);
                mTextViewDescripcion.setText(mPersona.desPersona);
            } else {
                mLinearLayout.setVisibility(View.GONE);
                mTextViewDescripcion.setText("");
            }

            if (!isNullOrEmpty(mPersona.foto)) {
                Glide
                        .with(AsesoresActivity.this)
                        .load(getUrlImage(mPersona.foto, getApplicationContext()))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                mImageView.setImageResource(R.drawable.profile);
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(mImageView);
            }
            else {
                mImageView.setImageResource(R.drawable.profile);
            }
        }

        @Override
        public void Onclick(Object mObject) {
            final Persona mPersona = (Persona) mObject;
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showMessage(mPersona.nombre);
                  /*  switch (typeViewAsesor) {
                        case 0:
                            startActivity(new Intent(getApplicationContext(), DetalleAsesorActivity.class).putExtra(KEY_USUARIO, mPersona));

                            break;
                        case 1:
                            Intent mIntent = getIntent();
                            mIntent.putExtra(KEY_RETUNR_DATA, mPersona);
                            setResult(RESULT_OK, mIntent);
                            finish();
                            break;
                    }*/
                }
            });
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // showMessage(mPersona.nombre);
                    switch (typeViewAsesor) {
                        case 0: /*detalle*/
                            startActivity(new Intent(getApplicationContext(), DetalleAsesorActivity.class).putExtra(KEY_USUARIO, mPersona));
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
        //showMessage(mensaje);
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

