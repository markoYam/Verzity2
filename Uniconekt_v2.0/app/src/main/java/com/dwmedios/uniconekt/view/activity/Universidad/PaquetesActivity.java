package com.dwmedios.uniconekt.view.activity.Universidad;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.model.Becas;
import com.dwmedios.uniconekt.model.Paquetes;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.VentasPaquetes;
import com.dwmedios.uniconekt.presenter.PaquetePresenter;
import com.dwmedios.uniconekt.view.activity.SplashActivity;
import com.dwmedios.uniconekt.view.activity.Universitario.MainUniversitarioActivity;
import com.dwmedios.uniconekt.view.activity.View_Utils.ConfirmBuyActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.BecasAdapter;
import com.dwmedios.uniconekt.view.adapter.PaquetesAdapter;
import com.dwmedios.uniconekt.view.adapter.VideoAdapter;
import com.dwmedios.uniconekt.view.viewmodel.PaquetesViewController;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.SplashActivity.TYPE_LOGIN_PAQUETES;
import static com.dwmedios.uniconekt.view.activity.View_Utils.ConfirmBuyActivity.KEY_DETALLLE_COMPRA;
import static com.dwmedios.uniconekt.view.activity.View_Utils.ConfirmBuyActivity.KEY_SOLO_VER;
import static com.dwmedios.uniconekt.view.util.Paypal.getTokenPaypal;

public class PaquetesActivity extends BaseActivity implements PaquetesViewController {
    public static boolean KEY_FIRST_ACCESS = false;
    @BindView(R.id.recyclerview_utils)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_empyRecycler)
    TextView mTextView;
    @BindView(R.id.swiperefresh)

    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;
    private PaquetePresenter mPaquetePresenter;
    private PaquetesAdapter mPaquetesAdapter;
    private AllController mAllController;

    public PayPalConfiguration mPayPalConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paquetes);
        ButterKnife.bind(this);
        if (TYPE_LOGIN_PAQUETES == 1) {
            mToolbar.setTitle("Comprar paquete");
        } else {
            mToolbar.setTitle("Paquetes");
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPaquetePresenter = new PaquetePresenter(this, getApplicationContext());
        mPaquetePresenter.GetPaquetes();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mPaquetePresenter.GetPaquetes();
            }
        });
        Intent mIntent = new Intent(this, PayPalService.class);
        mPayPalConfiguration = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(getTokenPaypal(getApplicationContext()));
        mIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, mPayPalConfiguration);
        startService(mIntent);
        mAllController = new AllController(getApplicationContext());

    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        //---- testearlo..........
        TYPE_LOGIN_PAQUETES = 0;
        KEY_FIRST_ACCESS = false;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                cancelBuy();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (cancelBuy()) {
            super.onBackPressed();
        }

    }

    public boolean cancelBuy() {
        final boolean[] isCondition = {false};
        if (TYPE_LOGIN_PAQUETES == 1) {
            showdialogMaterial("¿Desea cancelar la compra?", "No podrá gozar los beneficios de VERZITY hasta que realice la adquisición de un paquete.", R.drawable.ic_action_information, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//yes
                    if (mPaquetePresenter.borrarTodo()) {
                        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                        finish();

                    }
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//nop
                    isCondition[0] = false;
                }
            });
            return isCondition[0];
        } else {
            finish();
            return true;
        }
    }

    @Override
    public void OnSucces(List<Paquetes> mPaquetesList) {
        mTextView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        configureRecyclerView(ConfigureView(mPaquetesList));
    }

    public List<Paquetes> ConfigureView(List<Paquetes> mPaquetesList) {
        VentasPaquetes mVentasPaquetes = mAllController.getVentaPaquete();
        int position = 0;
        if (mVentasPaquetes != null) {
            for (int i = 0; i < mPaquetesList.size(); i++) {
                position = i;
                if (mPaquetesList.get(i).id == mVentasPaquetes.id_paquete) {
                    mPaquetesList.get(i).actual = true;
                    break;
                }
            }

            Paquetes mPaquetes = mPaquetesList.get(position);
            if (mPaquetes != null) {
                mPaquetesList.remove(position);
                mPaquetesList.add(0, mPaquetes);
            }

        }
        return mPaquetesList;
    }

    @Override
    public void OnSuccesPaquete(VentasPaquetes mVentasPaquetes) {
        mPaquetePresenter.GuardarPaquete(mVentasPaquetes);
        mostrarventa(mVentasPaquetes,false);
    }

    public  void mostrarventa(VentasPaquetes mVentasPaquetes,boolean solover) {
        KEY_SOLO_VER = solover;
        Intent mIntent = new Intent(getApplicationContext(), ConfirmBuyActivity.class);
        mIntent.putExtra(KEY_DETALLLE_COMPRA, mVentasPaquetes);
        startActivity(mIntent);
    }

    public void configureRecyclerView(List<Paquetes> mPaquetesList) {
        if (mPaquetesList != null && mPaquetesList.size() > 0) {

         /*   boolean tabletSize = getResources().getBoolean(R.bool.is_tablet);
            if (tabletSize) {
                mPaquetesAdapter = new PaquetesAdapter(mPaquetesList, mOnclick);
                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                mRecyclerView.setLayoutManager(layoutManager);
            } else {
                mPaquetesAdapter = new PaquetesAdapter(mPaquetesList, mOnclick);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(layoutManager);

            }*/
            mPaquetesAdapter = new PaquetesAdapter(mPaquetesList, mOnclick);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mPaquetesAdapter);
        } else {
            mRecyclerView.setAdapter(null);
            this.EmptyRecyclerView();
        }
    }

    PaquetesAdapter.onclick mOnclick = new PaquetesAdapter.onclick() {
        @Override
        public void onclickButton(final Paquetes mPaquetes) {
            VentasPaquetes temp = mAllController.getVentaPaquete();
            if (!mPaquetes.actual) {
                if (temp != null) {
                    showdialogMaterial("Atención", "Ya cuenta con un paquete activo. ¿Desea actualizarlo?", R.drawable.ic_action_information, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            paypayPayament(mPaquetes);
                        }
                    });
                } else {
                    paypayPayament(mPaquetes);
                }
            } else {
                if (temp != null) {
                    temp.mPaquetes = mPaquetes;
                    mostrarventa(temp,true);
                }
                else
                    showMessage("No es posible visualizar el resumen de la compra.");
            }
        }
    };
    private Paquetes mPaquetes;

    public void paypayPayament(Paquetes mPaquetes) {
        this.mPaquetes = mPaquetes;
        PayPalPayment mPayPalPayment = new PayPalPayment(new BigDecimal(mPaquetes.costo), "MXN", mPaquetes.nombre, PayPalPayment.PAYMENT_INTENT_ORDER);
        Intent mIntent = new Intent(this, PaymentActivity.class);
        mIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, mPayPalConfiguration);
        mIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, mPayPalPayment);
        startActivityForResult(mIntent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data
                    .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {

                    //informacion extra del pedido
                    System.out.println(confirm.toJSONObject().toString(4));
                    String detail = confirm.toJSONObject().toString(4);
                    String state = confirm.getProofOfPayment().getState();
                    if (state != null) {
                        if (state.equals("approved")) {
                            VentasPaquetes mVentasPaquetes = new VentasPaquetes();
                            Universidad mUniversidad = mPaquetePresenter.getUniversidad();
                            mVentasPaquetes.id_universidad = mUniversidad.id;
                            mVentasPaquetes.id_paquete = mPaquetes.id;
                            mPaquetePresenter.SaveVentapaquete(mVentasPaquetes);                           //llama al formulario de los detalles de la universidad.........
                        } else {
                            showMessage("Ocurrió un error al tratar de generar la compra");
                        }
                    } else {
                        showMessage("Ocurrió un error al tratar de generar la compra");
                    }
                    System.out.println(confirm.getPayment().toJSONObject()
                            .toString(4));
                    Toast.makeText(getApplicationContext(), "Orden procesada",
                            Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            showMessage("El usuario cancelo la compra del paquete.");
        }
    }

    @Override
    public void OnFailed(String mensaje) {
        showMessage(mensaje);
    }

    @Override
    public void Onloading(boolean loading) {
        if (loading) {
            showOnProgressDialog("Procesando...");
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void EmptyRecyclerView() {
        mRecyclerView.setAdapter(null);
        mSwipeRefreshLayout.setRefreshing(false);
        mTextView.setVisibility(View.VISIBLE);
    }
}
