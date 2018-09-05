package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.service.response.PaypalResponse;
import com.dwmedios.uniconekt.model.NivelAcademico;
import com.dwmedios.uniconekt.model.PaqueteAsesor;
import com.dwmedios.uniconekt.model.Paquetes;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.VentaPaqueteAsesor;
import com.dwmedios.uniconekt.model.VentasPaquetes;
import com.dwmedios.uniconekt.presenter.PaquetesAsesorPresenter;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.CustomAdapter;
import com.dwmedios.uniconekt.view.viewmodel.PaquetesAsesorViewController;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario_v2.AsesoresActivity.KEY_RETUNR_DATA;
import static com.dwmedios.uniconekt.view.activity.Universitario_v2.AsesoresActivity.typeViewAsesor;
import static com.dwmedios.uniconekt.view.util.Paypal.getTokenPaypal;

public class PaquetesAsesoresActivity extends BaseActivity implements PaquetesAsesorViewController {
    public final int CODE_RESULT_ASESOR = 57936;
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private PaquetesAsesorPresenter mPaquetesAsesorPresenter;
    private CustomAdapter mCustomAdapter;
    public PayPalConfiguration mPayPalConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paquetes_asesores);
        ButterKnife.bind(this);
        mToolbar.setTitle("Paquetes asesores");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPaquetesAsesorPresenter = new PaquetesAsesorPresenter(this, getApplicationContext());
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mPaquetesAsesorPresenter.getPaquetes();
            }
        });
        mPaquetesAsesorPresenter.getPaquetes();
        iniciarServicioPaypal();
    }

    private void iniciarServicioPaypal() {
        Intent mIntent = new Intent(this, PayPalService.class);
        mPayPalConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(getTokenPaypal(getApplicationContext()));
        mIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, mPayPalConfiguration);
        startService(mIntent);
    }

    private void DetenerServicioPaypal() {
        stopService(new Intent(this, PayPalService.class));
    }

    public void PagarPaypal(PaqueteAsesor mPaqueteAsesor) {

        PayPalPayment mPayPalPayment = new PayPalPayment(new BigDecimal(mPaqueteAsesor.costo), "MXN", mPaqueteAsesor.nombre, PayPalPayment.PAYMENT_INTENT_ORDER);
        Intent mIntent = new Intent(this, PaymentActivity.class);
        mIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, mPayPalConfiguration);
        mIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, mPayPalPayment);
        startActivityForResult(mIntent, 200);
    }

    private void HandleResultPaypal(int resultCode, Intent mIntent) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = mIntent
                    .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {

                    String detail = confirm.toJSONObject().toString(4);
                    PaypalResponse mPaypalResponse = new Gson().fromJson(detail, PaypalResponse.class);
                    if (mPaypalResponse.response.validPay()) {
                        Toast.makeText(getApplicationContext(), "Orden procesada",
                                Toast.LENGTH_LONG).show();

                        VentaPaqueteAsesor mVentaPaqueteAsesor = new VentaPaqueteAsesor();
                        mVentaPaqueteAsesor.idAsesor = asesorSeleccionado.id;
                        mVentaPaqueteAsesor.idPaquete = mPaqueteAsesor.id;
                        mVentaPaqueteAsesor.idPersona = mPaquetesAsesorPresenter.getDatosPersona().id;
                        mVentaPaqueteAsesor.referencia = mPaypalResponse.response.referencia;
                        mPaquetesAsesorPresenter.saveVenta(mVentaPaqueteAsesor);
                    } else {
                        Toast.makeText(getApplicationContext(), mPaypalResponse.response.status,
                                Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Ocurrió un error al procesar el pago",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            showMessage("El usuario cancelo la compra del paquete.");
        }
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
    protected void onDestroy() {
        DetenerServicioPaypal();
        super.onDestroy();
    }

    @Override
    public void Onsucces(List<PaqueteAsesor> mPaqueteAsesorList) {
        mTextView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        configureRecyclerView(ConfigureView(mPaqueteAsesorList));
    }

    public List<PaqueteAsesor> ConfigureView(List<PaqueteAsesor> mPaquetesList) {
        VentaPaqueteAsesor mVentaPaqueteAsesor = mPaquetesAsesorPresenter.getPaqueteAsesor();
        int position = 0;
        if (mVentaPaqueteAsesor != null) {
            for (int i = 0; i < mPaquetesList.size(); i++) {
                position = i;
                if (mPaquetesList.get(i).id == mVentaPaqueteAsesor.idPaquete) {
                    mPaquetesList.get(i).isPaqueteActual = true;
                    break;
                }
            }

            PaqueteAsesor mPaquetes = mPaquetesList.get(position);
            if (mPaquetes != null) {
                mPaquetesList.remove(position);
                mPaquetesList.add(0, mPaquetes);
            }

        }
        return mPaquetesList;
    }

    public void configureRecyclerView(List<PaqueteAsesor> mPaqueteAsesorList) {
        if (mPaqueteAsesorList != null && mPaqueteAsesorList.size() > 0) {
            mCustomAdapter = new CustomAdapter(mPaqueteAsesorList, R.layout.row_paquetes_asesores, mConfigureHolder);
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
        mTextView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setAdapter(null);
    }

    CustomAdapter.ConfigureHolder mConfigureHolder = new CustomAdapter.ConfigureHolder() {
        TextView mTextViewnumCuestionarios;
        TextView mTextViewDescripcion;
        TextView mtTextViewDias;
        TextView mTextViewCosto;
        TextView mTextViewNombre;
        Button mButtonComprar;
        Button mButtonComprar2;

        @Override
        public void Configure(View itemView, Object mObject) {
            PaqueteAsesor mPaqueteAsesor = (PaqueteAsesor) mObject;
            mTextViewnumCuestionarios = itemView.findViewById(R.id.textviewNumCuestionarios);
            mTextViewDescripcion = itemView.findViewById(R.id.textViewDescripcionPaquete);
            mtTextViewDias = itemView.findViewById(R.id.VigenciaPaquete);
            mTextViewCosto = itemView.findViewById(R.id.textViewCostoPaquete);
            mTextViewNombre = itemView.findViewById(R.id.textViewNombrePaquete);
            mButtonComprar = itemView.findViewById(R.id.buttonVerMas);
            mButtonComprar2 = itemView.findViewById(R.id.buttonVerMas2);

            mTextViewNombre.setText(mPaqueteAsesor.nombre);
            mTextViewDescripcion.setText(mPaqueteAsesor.descripcion);

            java.util.Currency moneda = java.util.Currency.getInstance("MXN");
            java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance();
            format.setCurrency(moneda);
            String costo = format.format(mPaqueteAsesor.costo).toString();
            costo = costo.replace("MXN", "");
            costo = costo.replace("MX", "");
            costo = costo.replace("$", "");
            mTextViewCosto.setText("$" + costo + " MXN");
            mTextViewnumCuestionarios.setText("Cuestionarios a liberar: " + mPaqueteAsesor.numCuestionarios);
            mtTextViewDias.setText(mPaqueteAsesor.vigencia + " días de vigencia");

            if (mPaqueteAsesor.isPaqueteActual) {
                mButtonComprar.setVisibility(View.GONE);
                mButtonComprar2.setVisibility(View.VISIBLE);
            } else {
                mButtonComprar2.setVisibility(View.GONE);
                mButtonComprar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void Onclick(Object mObject) {
            final PaqueteAsesor mPaqueteAsesor2 = (PaqueteAsesor) mObject;
            mButtonComprar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typeViewAsesor = 1;
                    mPaqueteAsesor = mPaqueteAsesor2;
                    Intent mIntent = new Intent(getApplicationContext(), AsesoresActivity.class);
                    startActivityForResult(mIntent, CODE_RESULT_ASESOR);
                }
            });
            mButtonComprar2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typeViewAsesor = 1;
                    mPaqueteAsesor = mPaqueteAsesor2;
                    Intent mIntent = new Intent(getApplicationContext(), AsesoresActivity.class);
                    startActivityForResult(mIntent, CODE_RESULT_ASESOR);
                }
            });
        }
    };
    private PaqueteAsesor mPaqueteAsesor = null;
    private Persona asesorSeleccionado = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_RESULT_ASESOR:
                if (resultCode == RESULT_OK) {
                    asesorSeleccionado = data.getExtras().getParcelable(KEY_RETUNR_DATA);
                    if (mPaqueteAsesor != null) {
                        PagarPaypal(mPaqueteAsesor);
                    }
                    //startActivity(new Intent(getApplicationContext(), PreviewPaqueteAsesorActivity.class));
                } else {
                    mPaqueteAsesor = null;
                    showMessage("Operacion cancelada");
                }
                break;

            case 200:
                HandleResultPaypal(resultCode, data);

                break;
        }
    }

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

    @Override
    public void onfailedVenta(String mensaje) {
        showMessage(mensaje);
    }

    @Override
    public void OnsuccesVenta(VentaPaqueteAsesor mVentaPaqueteAsesor) {
        showMessage("Guardado correctamente en la base de datos");
        if (mPaquetesAsesorPresenter.saveVentaPaquete(mVentaPaqueteAsesor))
            mPaquetesAsesorPresenter.getPaquetes();
    }
}
