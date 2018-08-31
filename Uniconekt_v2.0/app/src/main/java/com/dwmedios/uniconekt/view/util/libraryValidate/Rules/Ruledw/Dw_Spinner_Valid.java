package com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw;

import android.widget.Spinner;

public class Dw_Spinner_Valid extends RuleDw_base {
    private Spinner mSpinner;

    public Dw_Spinner_Valid(Spinner mSpinner, String error) {
        super(error);
        this.mSpinner = mSpinner;
    }

    @Override
    public boolean isValid(String var1) {
        if (mSpinner != null && mSpinner.getSelectedItem() != null) {
            if (mSpinner.getSelectedItem().toString().contains("Seleccione")
                    || mSpinner.getSelectedItem().toString().contains("-")
                    || mSpinner.getSelectedItem().toString().contains("Seleccionar")
                    ) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }
}
