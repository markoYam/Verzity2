package com.dwmedios.uniconekt.view.activity.View_Utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.VentaPaqueteAsesor;
import com.dwmedios.uniconekt.model.VentasPaquetes;
import com.dwmedios.uniconekt.presenter.ConfirmBuyPresenter;
import com.dwmedios.uniconekt.view.activity.Universidad.DatosUniversidadActivity;
import com.dwmedios.uniconekt.view.activity.Universitario.MainUniversitarioActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.SplashActivity.TYPE_LOGIN_PAQUETES;
import static com.dwmedios.uniconekt.view.util.Utils.configurefechaCompleted;

public class ConfirmBuyActivity extends AppCompatActivity {
    public static final String KEY_DETALLLE_COMPRA = "545658245747";
    public static int tipoVista = 0;
    public static Boolean KEY_SOLO_VER = false;
    @BindView(R.id.buttonOk)
    Button mButton;
    @BindView(R.id.textViewCostoCompra)
    TextView mTextViewCosto;
    @BindView(R.id.textViewFechaCompra)
    TextView mTextViewFechaCompra;
    @BindView(R.id.textViewNombreCompra)
    TextView mTextViewNombrePaquete;
    @BindView(R.id.textViewVigenciaCompra)
    TextView mTextViewCompra;
    private ConfirmBuyPresenter mConfirmBuyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_confirm_buy);
        ButterKnife.bind(this);
        mConfirmBuyPresenter = new ConfirmBuyPresenter(getApplicationContext());
        this.setFinishOnTouchOutside(false);
        ConfigureView();
        mButton.setOnClickListener(mOnClickListener);
    }

    private void ConfigureView() {
        try {
            if (tipoVista == 0) {
                VentasPaquetes mVentasPaquetes = getIntent().getExtras().getParcelable(KEY_DETALLLE_COMPRA);
                if (mVentasPaquetes != null) {
                    if (mVentasPaquetes.mPaquetes != null) {
                        if (!mVentasPaquetes.mPaquetes.nombre.isEmpty()) {
                            mTextViewNombrePaquete.setText(mVentasPaquetes.mPaquetes.nombre);
                        }
                        if (mVentasPaquetes.mPaquetes.costo != null) {


                            java.util.Currency moneda = java.util.Currency.getInstance("MXN");
                            java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance();
                            format.setCurrency(moneda);
                            String costo = format.format(mVentasPaquetes.mPaquetes.costo).toString();
                            costo = costo.replace("MXN", "");
                            costo = costo.replace("MX", "");
                            costo = costo.replace("$", "");
                            mTextViewCosto.setText("$" + costo + " MXN");

                            //  mTextViewCosto.setText(format.format(mVentasPaquetes.mPaquetes.costo).toString());
                        }

                    }
                    //  Date currentTime = Calendar.getInstance().getTime();
                    if (mVentasPaquetes.fechaVenta != null) {
                        mTextViewFechaCompra.setText(configurefechaCompleted(mVentasPaquetes.fechaVenta));
                    }
                    if (mVentasPaquetes.fechaVigencia != null) {
                        mTextViewCompra.setText(configurefechaCompleted(mVentasPaquetes.fechaVigencia));
                    }
                }
            } else if (tipoVista == 1) {
                VentaPaqueteAsesor mVentasPaquetes = getIntent().getExtras().getParcelable(KEY_DETALLLE_COMPRA);
                if (mVentasPaquetes != null) {
                    if (mVentasPaquetes.mPaqueteAsesor != null) {
                        if (!mVentasPaquetes.mPaqueteAsesor.nombre.isEmpty()) {
                            mTextViewNombrePaquete.setText(mVentasPaquetes.mPaqueteAsesor.nombre);
                        }
                        if (mVentasPaquetes.mPaqueteAsesor.costo != null) {


                            java.util.Currency moneda = java.util.Currency.getInstance("MXN");
                            java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance();
                            format.setCurrency(moneda);
                            String costo = format.format(mVentasPaquetes.mPaqueteAsesor.costo).toString();
                            costo = costo.replace("MXN", "");
                            costo = costo.replace("MX", "");
                            costo = costo.replace("$", "");
                            mTextViewCosto.setText("$" + costo + " MXN");
                        }

                    }
                    if (mVentasPaquetes.fecha_Compra != null) {
                        mTextViewFechaCompra.setText(configurefechaCompleted(mVentasPaquetes.fecha_Compra));
                    }
                    if (mVentasPaquetes.fecha_vigencia != null) {
                        mTextViewCompra.setText(configurefechaCompleted(mVentasPaquetes.fecha_vigencia));
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            validarUniversidad();
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            validarUniversidad();
        }
    };

    private void validarUniversidad() {
        if (!KEY_SOLO_VER) {
            Universidad mUniversidad = mConfirmBuyPresenter.getUniversidad();
            if (mUniversidad != null && mUniversidad.mDireccion != null) {
                Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                startActivity(new Intent(getApplicationContext(), DatosUniversidadActivity.class));
                finish();
            }
        } else finish();
    }

    @Override
    protected void onDestroy() {
        TYPE_LOGIN_PAQUETES = 0;
        KEY_SOLO_VER = false;
        super.onDestroy();
    }
}
