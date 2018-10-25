package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.CodigoPostal;
import com.dwmedios.uniconekt.model.Estados;
import com.dwmedios.uniconekt.model.Paises;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.SearchUniversidades;
import com.dwmedios.uniconekt.presenter.GetPaisesPresenter;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.GetPaisesViewController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario_v2.VisualizarUniversidadesActivity.KEY_BUSQUEDA;
import static com.dwmedios.uniconekt.view.util.Utils.changeColorToolbar;
import static com.dwmedios.uniconekt.view.util.Utils.setStatusBarGradiant;

public class FiltrarEstadosActivity extends BaseActivity implements GetPaisesViewController {
    GetPaisesPresenter mGetPaisesPresenter;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.spinnerPaises)
    Spinner mSpinner;
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextView;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Paises> mPaisesList;
    private customAdapter mcustomAdapter;
    private Paises mPaisesSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar_estados);
        ButterKnife.bind(this);
        mGetPaisesPresenter = new GetPaisesPresenter(this, getApplicationContext());
        mToolbar.setTitle("Buscar por ubicación");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (SharePrefManager.getInstance(getApplicationContext()).isSeachExtranjero()) {
            setStatusBarGradiant(FiltrarEstadosActivity.this, R.drawable.status_uni_extra);
            changeColorToolbar(getSupportActionBar(), R.color.Color_extranjero, this);
        } else {
            changeColorToolbar(getSupportActionBar(), R.color.Color_buscarUniversidad, this);
            mSpinner.setEnabled(false);
        }

        mTextView.setText("Seleccione un país para visualizar los estados.");
        mGetPaisesPresenter.getPaises();
        mSpinner.setOnItemSelectedListener(mOnItemSelectedListener);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //mSwipeRefreshLayout.setRefreshing(true);
                if (mPaisesSeleccionado != null) {
                    mGetPaisesPresenter.getEstados(mPaisesSeleccionado);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position != 0) {
                String pais = mSpinner.getSelectedItem().toString();
                int pos = 0;
                for (Paises mPaises : mPaisesList) {
                    pos++;
                    if (mPaises.nombre.equals(pais))
                        break;
                }
                Paises mPaises = mPaisesList.get((pos - 1));
                mPaisesSeleccionado = mPaises;
                if (mPaises != null) {
                    mGetPaisesPresenter.getEstados(mPaises);
                }
            } else {
                mRecyclerView.setAdapter(null);
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText("Seleccione un país para visualizar los estados.");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

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

    public void configureRecyclerView(List<Estados> mEstados) {
        if (mEstados != null && mEstados.size() > 0) {
            mcustomAdapter = new customAdapter(mEstados);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mcustomAdapter);
            Utils.setAnimRecyclerView(getApplicationContext(), R.anim.layout_animation, mRecyclerView);
        } else {
            mRecyclerView.setAdapter(null);
            this.empyRecycler();
        }
    }

    public void empyRecycler() {
        mTextView.setVisibility(View.VISIBLE);
        //   mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSucces(List<Paises> mPaisesList) {
        this.mPaisesList = mPaisesList;
        if (!SharePrefManager.getInstance(getApplicationContext()).isSeachExtranjero()) {
            Persona mPersona = mGetPaisesPresenter.getPersona();
            if (mPersona != null) {
                List<String> mStringListTemnp = new ArrayList<>();
                mStringListTemnp.add("--Seleccionar país de interés--");
                int pos = 0;
                for (int i = 0; i < mPaisesList.size(); i++) {
                    if (mPaisesList.get(i).nombre.equals(mPersona.direccion.pais)) {
                        pos = (i + 1);
                    }
                    mStringListTemnp.add(mPaisesList.get(i).nombre);
                }
                ArrayAdapter<String> mStringArrayAdapter = new ArrayAdapter<String>(this, R.layout.row_spinner_paises, mStringListTemnp);
                mSpinner.setAdapter(mStringArrayAdapter);
                mSpinner.setSelection(pos);
            }
        } else {
            List<String> mStringListTemnp = new ArrayList<>();
            mStringListTemnp.add("--Seleccionar país de interés--");
            int position = 0;
            for (int i = 0; i < mPaisesList.size(); i++) {
            /*if (mDireccion != null) {
                if (mPaisesList.get(i).nombre.equals(mDireccion.pais)) position = (i + 1);
            }*/
                mStringListTemnp.add(mPaisesList.get(i).nombre);
            }
            ArrayAdapter<String> mStringArrayAdapter = new ArrayAdapter<String>(this, R.layout.row_spinner_paises, mStringListTemnp);
            mSpinner.setAdapter(mStringArrayAdapter);
            mSpinner.setSelection(0);
        }
    }

    @Override
    public void onFailed(String mensaje) {
        List<String> mStringListTemnp = new ArrayList<>();
        mStringListTemnp.add("No se encontraron elementos.");
        ArrayAdapter<String> mStringArrayAdapter = new ArrayAdapter<String>(this, R.layout.row_spinner_paises, mStringListTemnp);
        mSpinner.setAdapter(mStringArrayAdapter);
        mSpinner.setSelection(0);
    }

    public class holder extends RecyclerView.ViewHolder {
        @BindView(R.id.tituloRow)
        TextView mTextView;
        @BindView(R.id.cardRow)
        CardView mCardView;

        public holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void configureView(final Estados mEstados) {
            mTextView.setText(mEstados.nombre);
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchUniversidades mUniversidades = new SearchUniversidades();
                    mUniversidades.estado = mEstados.nombre;
                    mUniversidades.pais = mSpinner.getSelectedItem().toString();
                    Utils.tipoBusqueda_Universidad = 3;
                    startActivity(new Intent(getApplicationContext(), VisualizarUniversidadesActivity.class).putExtra(KEY_BUSQUEDA, mUniversidades));
                }
            });
        }

    }

    private class customAdapter extends RecyclerView.Adapter<FiltrarEstadosActivity.holder> {
        private List<Estados> mEstadosList;

        public customAdapter(List<Estados> mEstadosList) {
            this.mEstadosList = mEstadosList;
        }

        @Override
        public FiltrarEstadosActivity.holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View master2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_custom_view, parent, false);
            return new holder(master2);
        }

        @Override
        public void onBindViewHolder(FiltrarEstadosActivity.holder holder, int position) {
            holder.configureView(mEstadosList.get(position));
        }

        @Override
        public int getItemCount() {
            return (mEstadosList != null && mEstadosList.size() > 0 ? mEstadosList.size() : 0);
        }
    }

    @Override
    public void onSuccesEstado(List<Estados> mPaisesList) {
        mSwipeRefreshLayout.setRefreshing(false);
        mTextView.setVisibility(View.GONE);
        configureRecyclerView(mPaisesList);
    }

    @Override
    public void onFailedEstado(String mensaje) {
        // showMessage(mensaje);
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setAdapter(null);
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText("No se encontraron elementos");
    }

    /**
     * codigo sin usar
     */
    @Override
    public void OnLoading(boolean isLoading) {
        if (isLoading) {
            showOnProgressDialog("Cargando...");
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void onSuccesCodigo(List<CodigoPostal> mCodigoPostals) {

    }

    @Override
    public void OnfailedCodigo(String mensaje) {

    }

    @Override
    public void emptyControls() {

    }
}
