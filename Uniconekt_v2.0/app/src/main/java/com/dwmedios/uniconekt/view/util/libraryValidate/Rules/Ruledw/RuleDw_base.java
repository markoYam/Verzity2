package com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw;

import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

public  abstract class RuleDw_base {
    public final String error;

    public RuleDw_base(String error) {
        this.error = error;
    }
    public abstract boolean isValid(String var1);
}
