package com.dwmedios.uniconekt.view.adapter.holder;

import android.graphics.Color;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.model.Paquetes;
import com.dwmedios.uniconekt.model.VentasPaquetes;
import com.dwmedios.uniconekt.view.adapter.PaquetesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaquetesHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textViewNombrePaquete)
    TextView mTextViewNombre;
    @BindView(R.id.textViewCostoPaquete)
    TextView mTextViewCosto;
    @BindView(R.id.textViewDescripcionPaquete)
    TextView mTextViewDescripcion;
    @BindView(R.id.VigenciaPaquete)
    TextView mTextViewVigencia;
    @BindView(R.id.buttonVerMas)
    Button mButton;
    @BindView(R.id.checkboxBeca)
    CheckBox mCheckBoxBeca;
    @BindView(R.id.checkboxFinanciamiento)
    CheckBox mCheckBoxFinanciamiento;
    @BindView(R.id.checkboxPostulacion)
    CheckBox mCheckBoxPostulacion;
    @BindView(R.id.buttonVerMas2)
    Button mButton2;

    public PaquetesHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
       // this.setIsRecyclable(false);
    }

    public void Configure(final Paquetes mPaquetes, final PaquetesAdapter.onclick mOnclick) {
        if (mPaquetes.nombre != null) mTextViewNombre.setText(mPaquetes.nombre);
        java.util.Currency moneda = java.util.Currency.getInstance("MXN");
        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance();


        if (mPaquetes.costo != null)
        {
            format.setCurrency(moneda);
            String costo=format.format(mPaquetes.costo).toString();
            costo= costo.replace("MXN","");
            costo= costo.replace("MX","");
            costo= costo.replace("$","");
            mTextViewCosto.setText("$"+costo +" MXN");

          //  mTextViewCosto.setText(mPaquetes.costo+"");
            System.out.println(format.format(23));
        }
        if (mPaquetes.descripcion != null) mTextViewDescripcion.setText(mPaquetes.descripcion);
        if (mPaquetes.dias_vigencia != 0)
            mTextViewVigencia.setText(mPaquetes.dias_vigencia + " días de vigencia");
        if (mPaquetes.aplica_Postulacion) {
            mCheckBoxPostulacion.setChecked(true);
        } else {
            mCheckBoxPostulacion.setChecked(false);
        }
        if (mPaquetes.aplica_Beca) {
            mCheckBoxBeca.setChecked(true);
        } else {
            mCheckBoxBeca.setChecked(false);
        }
        if (mPaquetes.aplica_Financiamiento) {
            mCheckBoxFinanciamiento.setChecked(true);
        } else {
            mCheckBoxFinanciamiento.setChecked(false);
        }
        if (mPaquetes.actual) {
            mButton.setVisibility(View.GONE);
            mButton2.setVisibility(View.VISIBLE);
            //mButton.setBackgroundColor(Color.parseColor("#F12D24"));
        }else
        {
            mButton.setVisibility(View.VISIBLE);
            mButton2.setVisibility(View.GONE);
        }
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (mOnclick != null) mOnclick.onclickButton(mPaquetes);
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclick != null) mOnclick.onclickButton(mPaquetes);
            }
        });

    }
}
