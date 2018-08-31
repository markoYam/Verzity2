package com.dwmedios.uniconekt.view.adapter.holder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Licenciaturas;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LicenciaturasHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textViewProgramaAcademico)
    protected TextView mTextViewPrograma;
    @BindView(R.id.checkboxPrograaAcademico)
    public CheckBox mCheckBox;

   /* @Nullable
    @BindView(R.id.headerPreguntas)
    public TextView itemView;*/

    public LicenciaturasHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.setIsRecyclable(false);
    }
    public LicenciaturasHolder(View itemView,boolean load) {
        super(itemView);
        if (load)
            ButterKnife.bind(this, itemView);
        this.setIsRecyclable(false);
    }

    public void configureLiecnciaturas(Licenciaturas mLicenciaturas) {
        mTextViewPrograma.setText(mLicenciaturas.nombre);
        mCheckBox.setChecked(mLicenciaturas.ischeCked);

    }
}
