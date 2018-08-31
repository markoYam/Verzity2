package com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

public class Dw_required_field extends RuleDw_base {
    public View mView;

    public Dw_required_field(View mView,String error) {
        super(error);
        this.mView = mView;
    }

    @Override
    public boolean isValid(String var1) {
        if (mView instanceof CheckBox) {
            CheckBox mCheckBox = (CheckBox) mView;
            return mCheckBox.isChecked();

        }
        if (mView instanceof EditText) {
            EditText mEditText = (EditText) mView;
            return mEditText.length() != 0;
        }
        if (mView instanceof RadioButton) {
            RadioButton mRadioButton = (RadioButton) mView;
            return mRadioButton.isChecked();
        }
        return false;
    }
}
