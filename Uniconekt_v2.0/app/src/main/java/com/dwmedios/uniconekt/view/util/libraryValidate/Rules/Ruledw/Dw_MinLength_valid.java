package com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw;

public class Dw_MinLength_valid extends RuleDw_base {
    private int length = 0;

    public Dw_MinLength_valid(String error, int length) {
        super(error);
        this.length = length;
    }

    public Dw_MinLength_valid(String error) {
        super(error);
    }


    @Override
    public boolean isValid(String var1) {
        return (var1.length() >= length);
    }
}
