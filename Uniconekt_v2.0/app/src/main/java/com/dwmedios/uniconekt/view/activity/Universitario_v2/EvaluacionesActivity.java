package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.cuestionarios.EvaluacionesPersona;
import com.dwmedios.uniconekt.model.cuestionarios.Resultados;
import com.dwmedios.uniconekt.presenter.EvaluacionesPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario.MainUniversitarioActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.CustomAdapter;
import com.dwmedios.uniconekt.view.adapter.CustomAdapterStiky;
import com.dwmedios.uniconekt.view.viewmodel.CustomViewController;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario_v2.PresentarCuestionarioActivity.KEY_PRESENTAR;
import static com.dwmedios.uniconekt.view.activity.Universitario_v2.ResultadoActivity.KEY_RESULTADO;
import static com.dwmedios.uniconekt.view.util.Utils.changeColorToolbar;
import static com.dwmedios.uniconekt.view.util.Utils.setStatusBarGradiant;

public class EvaluacionesActivity extends BaseActivity implements CustomViewController {

    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextViewEmpty;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;
    private CustomAdapter mCustomAdapter;
    public EvaluacionesPresenter mEvaluacionesPresenter;
    private SearchView mSearchView;
    private List<EvaluacionesPersona> mEvaluacionesPersonas;

    //Pruebassssssss
    private CustomAdapterStiky mCustomAdapterStiky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluaciones);
        ButterKnife.bind(this);
        mToolbar.setTitle("Cuestionarios");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Cambiar el color del toolbar y status bar
        setStatusBarGradiant(this, R.drawable.status_examen);
        changeColorToolbar(getSupportActionBar(), R.color.Color_examen, EvaluacionesActivity.this);

        mEvaluacionesPresenter = new EvaluacionesPresenter(this, getApplicationContext());
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mEvaluacionesPresenter.getEvaluaciones();
            }
        });
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PaquetesAsesoresActivity.class));
            }
        });

    }

    CustomAdapterStiky.ConfigureHolder mConfigureHolderStyky = new CustomAdapterStiky.ConfigureHolder() {
        TextView mTextViewNombre;
        TextView mTextVieHeader;
        //TextView mTextViewVigencia;
        TextView mTextViewResultado;
        Button mButton0; //res
        Button mButton1; //continuar
        Button mButton2;//presentar
        ImageView mImageView;

        @Override
        public void Configure(View itemView, Object mObject, boolean isHeader) {
            EvaluacionesPersona mEvaluacionesPersona = (EvaluacionesPersona) mObject;
            if (isHeader) {
                mTextVieHeader = itemView.findViewById(R.id.textoCabecera);
                mTextVieHeader.setText(mEvaluacionesPersona.mEvaluaciones.defaultValue);
            } else {
                //Inflar vista normal
                mButton0 = itemView.findViewById(R.id.buttonEpresentar0);
                mButton1 = itemView.findViewById(R.id.buttonEpresentar1);
                mButton2 = itemView.findViewById(R.id.buttonEpresentar2);
                mImageView = itemView.findViewById(R.id.imageCuestionario);

                    Glide.with(EvaluacionesActivity.this)
                            .load(R.raw.imageeva)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(mImageView);

                mTextViewNombre = itemView.findViewById(R.id.textViewEnombre);
                mTextViewResultado = itemView.findViewById(R.id.textViewEestatus);
             //   mTextViewVigencia = itemView.findViewById(R.id.textViewVigencia);
              //  mTextViewVigencia.setText("Vigencia " + getDays(mEvaluacionesPersona.mEvaluaciones.fechaFin) + " " + configurefechaCompleted(mEvaluacionesPersona.mEvaluaciones.fechaFin));
                mTextViewNombre.setText(mEvaluacionesPersona.mEvaluaciones.nombre);

                switch (mEvaluacionesPersona.mEstatus.estatusNot) {
                    case pe:
                        mButton2.setVisibility(View.VISIBLE);
                        mButton0.setVisibility(View.GONE);
                        mButton1.setVisibility(View.GONE);
                        break;
                    case en:
                        mButton1.setVisibility(View.VISIBLE);
                        mButton0.setVisibility(View.GONE);
                        mButton2.setVisibility(View.GONE);
                        break;
                    case fi:
                        mButton0.setVisibility(View.VISIBLE);
                        mButton1.setVisibility(View.GONE);
                        mButton2.setVisibility(View.GONE);
                        break;

                }
                mTextViewResultado.setText(mEvaluacionesPersona.mEstatus.estatusNot);
            }
        }

        @Override
        public void Onclick(Object mObject) {
            final EvaluacionesPersona mEvaluacionesPersona = (EvaluacionesPersona) mObject;
            mButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (mEvaluacionesPersona.mEstatus.estatusNot) {
                        case pe:
                            startActivity(new Intent(getApplicationContext(), PresentarCuestionarioActivity.class).putExtra(KEY_PRESENTAR, mEvaluacionesPersona));
                            break;
                        case en:
                            startActivity(new Intent(getApplicationContext(), PresentarCuestionarioActivity.class).putExtra(KEY_PRESENTAR, mEvaluacionesPersona));
                            break;
                        case fi:
                            //startActivity(new Intent(getApplicationContext(), ResultadoActivity.class));
                            //showMessage("VER RESULTADO");
                            mEvaluacionesPresenter.getResultado(mEvaluacionesPersona);
                            break;

                    }
                }
            });
            mButton0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (mEvaluacionesPersona.mEstatus.estatusNot) {
                        case pe:
                            startActivity(new Intent(getApplicationContext(), PresentarCuestionarioActivity.class).putExtra(KEY_PRESENTAR, mEvaluacionesPersona));
                            break;
                        case en:
                            startActivity(new Intent(getApplicationContext(), PresentarCuestionarioActivity.class).putExtra(KEY_PRESENTAR, mEvaluacionesPersona));
                            break;
                        case fi:
                            //startActivity(new Intent(getApplicationContext(), ResultadoActivity.class));
                            //showMessage("VER RESULTADO");
                            mEvaluacionesPresenter.getResultado(mEvaluacionesPersona);
                            break;

                    }
                }
            });
            mButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (mEvaluacionesPersona.mEstatus.estatusNot) {
                        case pe:
                            startActivity(new Intent(getApplicationContext(), PresentarCuestionarioActivity.class).putExtra(KEY_PRESENTAR, mEvaluacionesPersona));
                            break;
                        case en:
                            startActivity(new Intent(getApplicationContext(), PresentarCuestionarioActivity.class).putExtra(KEY_PRESENTAR, mEvaluacionesPersona));
                            break;
                        case fi:
                            //startActivity(new Intent(getApplicationContext(), ResultadoActivity.class));
                            //showMessage("VER RESULTADO");
                            mEvaluacionesPresenter.getResultado(mEvaluacionesPersona);
                            break;

                    }
                }
            });
        }

        @Override
        public long getHeaderId(Object mObject) {
            EvaluacionesPersona mEvaluacionesPersona = (EvaluacionesPersona) mObject;
            return mEvaluacionesPersona.mEvaluaciones.defaultValue.charAt(0);
        }
    };

    @Override
    protected void onStart() {
        mEvaluacionesPresenter.getEvaluaciones();
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!mSearchView.isIconified()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mSearchView.onActionViewCollapsed();
        } else {
            Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
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
                mEvaluacionesPresenter.SearchEvaluaciones(mEvaluacionesPersonas, query);
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mEvaluacionesPresenter.SearchEvaluaciones(mEvaluacionesPersonas, newText);
                return true;
            }
        });

        mSearchView.setQueryHint("Buscar cuestionarios");


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

    @Override
    public void OnSucces(List<?> mObjectsList) {

    }

    @Override
    public void OnSucces(Object mObjectsList) {
        Resultados mResultados = (Resultados) mObjectsList;
        startActivity(new Intent(getApplicationContext(), ResultadoActivity.class).putExtra(KEY_RESULTADO, mResultados));
    }

    @Override
    public void OnSucces(List<?> mObjectsList, int tipo) {
        if (tipo == 0) this.mEvaluacionesPersonas = (List<EvaluacionesPersona>) mObjectsList;
        mTextViewEmpty.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        configureRecyclerView(mObjectsList);
    }

    StickyRecyclerHeadersDecoration headersDecor;

    public void configureRecyclerView(List<?> mEstados) {
        if (mEstados != null && mEstados.size() > 0) {
            // resolver problema de la decoracion que se multiplica y agrega espacio vacio
            if (headersDecor != null) {
                mRecyclerView.removeItemDecoration(headersDecor);
                mRecyclerView.setAdapter(null);
            }
            mCustomAdapterStiky = new CustomAdapterStiky(mEstados, R.layout.row_evaluaciones, R.layout.row_header_custom_adapater, mConfigureHolderStyky);
            headersDecor = new StickyRecyclerHeadersDecoration(mCustomAdapterStiky);
            mCustomAdapterStiky.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    headersDecor.invalidateHeaders();

                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            LayoutAnimationController mLayoutAnimationController = AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mCustomAdapterStiky);
            mRecyclerView.addItemDecoration(headersDecor);

            mRecyclerView.setLayoutAnimation(mLayoutAnimationController);
            mRecyclerView.getAdapter().notifyDataSetChanged();
            mRecyclerView.scheduleLayoutAnimation();

        } else {
            mRecyclerView.setAdapter(null);
            this.empyRecycler();
        }
    }

    public void empyRecycler() {
        mTextViewEmpty.setVisibility(View.VISIBLE);
        mRecyclerView.setAdapter(null);
        //   mSwipeRefreshLayout.setRefreshing(false);
    }

    private static final String pe = "PENDIENTE";
    private static final String en = "EN PROCESO";
    private static final String fi = "FINALIZADO";

    @Override
    public void Onfailed(String mensaje) {
        mTextViewEmpty.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        this.empyRecycler();

    }

    @Override
    public void Onfailed2(String mensaje) {
        showMessage(mensaje);
    }

    @Override
    public void OnLoading(boolean isLoading) {
        if (isLoading) {
            showOnProgressDialog("Cargando...");
        } else
            dismissProgressDialog();
    }
}
