package com.dwmedios.uniconekt.view.adapter.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.PostuladosGeneral;
import com.dwmedios.uniconekt.view.adapter.PostuladosAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.util.Utils.configurefecha;

public class PostuladosHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.texviewNombreUniversitarioPostulado)
    TextView mTextViewNombre;
    @BindView(R.id.buttonVerDetallePostulado)
    ImageButton mImageButtonVerMas;
    @BindView(R.id.cardviewPostulados)
    CardView mCardView;
    @BindView(R.id.teFechaPostulado)
    TextView mTextViewFecha;
    @BindView(R.id.textViewPostuladoA)
    TextView mTextViewPostuladoA;

    public PostuladosHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public PostuladosHolder(View itemView, Boolean mBoolean) {
        super(itemView);
        if (mBoolean)
            ButterKnife.bind(this, itemView);
        setIsRecyclable(false);
    }

    public void configure(final PostuladosGeneral mPostuladosGeneral, final PostuladosAdapter.onclick mOnclick) {
        if (mPostuladosGeneral.persona != null)
            if (mPostuladosGeneral.persona.nombre != null)
                mTextViewNombre.setText(mPostuladosGeneral.persona.nombre);
        if (mPostuladosGeneral.fechaPostulacion != null) {

            String fe = String.valueOf(android.text.format.DateFormat.format("EEEE", mPostuladosGeneral.fechaPostulacion));
            mTextViewFecha.setText(fe + " " + configurefecha(mPostuladosGeneral.fechaPostulacion));
        }
        if (mPostuladosGeneral.financiamiento != null)
            if (mPostuladosGeneral.financiamiento.nombre != null)
                mTextViewPostuladoA.setText(mPostuladosGeneral.financiamiento.nombre);
        if (mPostuladosGeneral.beca != null)
            if (mPostuladosGeneral.beca.nombre != null)
                mTextViewPostuladoA.setText(mPostuladosGeneral.beca.nombre);
        if (mPostuladosGeneral.licenciatura != null)
            if (mPostuladosGeneral.licenciatura.nombre != null)
                mTextViewPostuladoA.setText(mPostuladosGeneral.licenciatura.nombre + " - " + mPostuladosGeneral.licenciatura.mNivelEstudios.nombre);

        mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclick != null) mOnclick.onclickListener(mPostuladosGeneral);
            }
        });
        mImageButtonVerMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnclick != null) mOnclick.onclickListener(mPostuladosGeneral);
            }
        });
    }
}

