package com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw;

import android.util.Patterns;

public class Dw_Phone_valid extends RuleDw_base {
    public Dw_Phone_valid(String error) {
        super(error);
    }

    @Override
    public boolean isValid(String var1) {
        return Patterns.PHONE.matcher(var1).matches();
    }
}
