package com.dwmedios.uniconekt.view.activity.Universidad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.data.service.response.PaypalResponse;
import com.dwmedios.uniconekt.model.Paquetes;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.VentasPaquetes;
import com.dwmedios.uniconekt.presenter.PaquetePresenter;
import com.dwmedios.uniconekt.view.activity.SplashActivity;
import com.dwmedios.uniconekt.view.activity.View_Utils.ConfirmBuyActivity;
import com.dwmedios.uniconekt.view.activity.View_Utils.DialogActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.adapter.PaquetesAdapter;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.PaquetesViewController;
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

import static com.dwmedios.uniconekt.view.activity.SplashActivity.TYPE_LOGIN_PAQUETES;
import static com.dwmedios.uniconekt.view.activity.View_Utils.ConfirmBuyActivity.KEY_DETALLLE_COMPRA;
import static com.dwmedios.uniconekt.view.activity.View_Utils.DialogActivity.KEY_DIALOG;
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
                .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
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
                if (TYPE_LOGIN_PAQUETES == 1)
                    cancelBuy();
                else
                    finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (TYPE_LOGIN_PAQUETES == 1)
            cancelBuy();
        else
            super.onBackPressed();
    }

    public void cancelBuy() {
        DialogActivity.handleDialog mHandleDialog = new DialogActivity.handleDialog();
        mHandleDialog.logo = R.drawable.ic_action_information;
        mHandleDialog.titulo = "¿Desea cancelar la compra?";
        mHandleDialog.buttonCancel = true;
        mHandleDialog.touchOutSide = false;
        mHandleDialog.contenido = "No podrá gozar los beneficios de VERZITY hasta que realice la adquisición de un paquete.";
        startActivityForResult(new Intent(getApplicationContext(), DialogActivity.class).putExtra(KEY_DIALOG, mHandleDialog), 201);
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
        mostrarventa(mVentasPaquetes, false);
    }

    public void mostrarventa(VentasPaquetes mVentasPaquetes, boolean solover) {
        ConfirmBuyActivity.tipoVista = 0;
        ConfirmBuyActivity.KEY_SOLO_VER = solover;
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
            Utils.setAnimRecyclerView(getApplicationContext(),R.anim.layout_animation,mRecyclerView);
        } else {
            mRecyclerView.setAdapter(null);
            this.EmptyRecyclerView();
        }
    }

    PaquetesAdapter.onclick mOnclick = new PaquetesAdapter.onclick() {
        @Override
        public void onclickButton(final Paquetes mPaquetes) {
            comprarNuevo = mPaquetes;
            VentasPaquetes temp = mAllController.getVentaPaquete();
            if (!mPaquetes.actual) {
                if (temp != null) {
                    DialogActivity.handleDialog mHandleDialog = new DialogActivity.handleDialog();
                    mHandleDialog.logo = R.drawable.ic_action_information;
                    mHandleDialog.titulo = "Atención";
                    mHandleDialog.touchOutSide = false;
                    mHandleDialog.buttonCancel = true;
                    mHandleDialog.contenido = "Ya cuenta con un paquete activo. ¿Desea actualizarlo?";
                    startActivityForResult(new Intent(getApplicationContext(), DialogActivity.class).putExtra(KEY_DIALOG, mHandleDialog), 202);
                } else {
                    paypayPayament(mPaquetes);
                }
            } else {
                if (temp != null) {
                    temp.mPaquetes = mPaquetes;
                    mostrarventa(temp, true);
                } else
                    showMessage("No es posible visualizar el resumen de la compra.");
            }
        }
    };

    public void retryBuy() {
        DialogActivity.handleDialog mHandleDialog = new DialogActivity.handleDialog();
        mHandleDialog.logo = R.drawable.ic_action_information;
        mHandleDialog.titulo = "Atención";
        mHandleDialog.buttonCancel = true;
        mHandleDialog.touchOutSide = false;
        mHandleDialog.contenido = "No fue posible realizar la compra. ¿Reintentar?";
        startActivityForResult(new Intent(getApplicationContext(), DialogActivity.class).putExtra(KEY_DIALOG, mHandleDialog), 202);
    }

    private Paquetes mPaquetes;
    private Paquetes comprarNuevo;

    public void paypayPayament(Paquetes mPaquetes) {
        this.mPaquetes = mPaquetes;
        PayPalPayment mPayPalPayment = new PayPalPayment(new BigDecimal(mPaquetes.costo), "MXN", mPaquetes.nombre, PayPalPayment.PAYMENT_INTENT_SALE);
        Intent mIntent = new Intent(this, PaymentActivity.class);
        mIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, mPayPalConfiguration);
        mIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, mPayPalPayment);
        startActivityForResult(mIntent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {

            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String detail = confirm.toJSONObject().toString(4);
                        PaypalResponse mPaypalResponse = new Gson().fromJson(detail, PaypalResponse.class);
                        if (mPaypalResponse.response.validPay()) {
                            VentasPaquetes mVentasPaquetes = new VentasPaquetes();
                            Universidad mUniversidad = mPaquetePresenter.getUniversidad();
                            mVentasPaquetes.id_universidad = mUniversidad.id;
                            mVentasPaquetes.id_paquete = mPaquetes.id;
                            mVentasPaquetes.referencia = mPaypalResponse.response.referencia;
                            mPaquetePresenter.SaveVentapaquete(mVentasPaquetes);
                        } else {
                            /*Toast.makeText(getApplicationContext(), mPaypalResponse.response.status,
                                    Toast.LENGTH_LONG).show();*/
                           // retryBuy(mPaypalResponse.response);
                            retryBuy();
                        }

                    } catch (JSONException e) {
                        /*Toast.makeText(getApplicationContext(), "Ocurrió un error al procesar el pago",
                                Toast.LENGTH_LONG).show();*/
                        e.printStackTrace();
                        retryBuy();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                showMessage("El usuario cancelo la compra del paquete.");
            }
        }
        if (requestCode == 201) {
            if (resultCode == RESULT_OK) {
                if (mPaquetePresenter.borrarTodo()) {
                    startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                    finish();

                }
            }
            if (resultCode == RESULT_CANCELED) {
                //finish();
            }
        }
        if (requestCode == 202) {
            if (resultCode == RESULT_OK) {
                paypayPayament(comprarNuevo);
            }
            if (resultCode == RESULT_CANCELED) {

            }
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
