package com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw;

import android.support.design.widget.TextInputEditText;
import android.view.View;

public class Rules_Dw {
    //public TextInputEditText mTextInputEditText;
    public RuleDw_base mRuleDw;
    public View mView;

 /*   public Rules_Dw(referencia/TextInputEditText mTextInputEditText, RuleDw_base mRuleDw) {
        this.mTextInputEditText = mTextInputEditText;
        this.mRuleDw = mRuleDw;
    }
    */

    public Rules_Dw( View mView, RuleDw_base mRuleDw) {
        this.mRuleDw = mRuleDw;
        this.mView = mView;
    }
}
