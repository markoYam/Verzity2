package com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw;

import android.support.design.widget.TextInputEditText;

public class Dw_ConfirmPassword extends RuleDw_base {
    public TextInputEditText mTextInputEditText;

    public Dw_ConfirmPassword(TextInputEditText mTextInputEditText,String error) {
        super(error);
        this.mTextInputEditText = mTextInputEditText;
    }

    @Override
    public boolean isValid(String var1) {
        return (var1.equals(mTextInputEditText.getText().toString()));
    }
}
