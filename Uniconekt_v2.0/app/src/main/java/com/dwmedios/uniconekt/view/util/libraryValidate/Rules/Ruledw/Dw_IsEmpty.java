package com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw;

public class Dw_IsEmpty extends RuleDw_base {

    public Dw_IsEmpty(String error) {
        super(error);
    }

    @Override
    public boolean isValid(String var1) {
        return var1.length() != 0;
    }
}
