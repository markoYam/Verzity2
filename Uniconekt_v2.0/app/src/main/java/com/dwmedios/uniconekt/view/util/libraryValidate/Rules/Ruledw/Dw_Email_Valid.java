package com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw;

import android.util.Patterns;

public class Dw_Email_Valid extends RuleDw_base {
    public Dw_Email_Valid(String error) {
        super(error);
    }

    @Override
    public boolean isValid(String var1) {
        return Patterns.EMAIL_ADDRESS.matcher(var1).matches();
    }
}
