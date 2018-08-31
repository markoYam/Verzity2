package com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw;

import android.util.Patterns;

public class Dw_webSite extends RuleDw_base {
    public Dw_webSite(String error) {
        super(error);
    }

    @Override
    public boolean isValid(String var1) {
        if (var1.startsWith("http") || var1.startsWith("https")) {
            return Patterns.WEB_URL.matcher(var1).matches();
        } else {
            return false;
        }
    }
}
