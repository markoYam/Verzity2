package com.dwmedios.uniconekt.view.util.libraryValidate.Rules;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Rules_Dw;

import java.util.ArrayList;
import java.util.List;

public class ValidateField {
    public static final String FIELD_REQUIRED = "Campo requerido";
    public static final String EMAIL_EQUIRED = "Ingrese un correo electrónico valido.";
    public static final String PHONE_EQUIRED = "Ingrese un número de teléfono valido.";
    public static final String WEB_EQUIRED = "Ingrese un sitio web valido.";
    public List<Rules_Dw> mRuleDwList;

    public ValidateField(List<Rules_Dw> mRuleDwList) {
        this.mRuleDwList = mRuleDwList;
    }

    public ValidateField(List<Rules_Dw> mRuleDwList, errorItem mErrorItem) {
        this.mRuleDwList = mRuleDwList;
        this.mErrorItem = mErrorItem;
    }

    private static void toggleTextInputLayoutError(@NonNull TextInputLayout textInputLayout,
                                                   String msg) {
        textInputLayout.setError(msg);
        if (msg == null) {
            textInputLayout.setErrorEnabled(false);
        } else {
            textInputLayout.setErrorEnabled(true);
        }
    }

    public static void setFocusableView(Rules_Dw mRules_dw) {
        if (mRules_dw.mView != null) {
            if (mRules_dw.mView instanceof AppCompatAutoCompleteTextView) {
                mRules_dw.mView.setFocusable(true);
                mRules_dw.mView.requestFocus();
            }
            if (mRules_dw.mView instanceof TextInputEditText) {
                mRules_dw.mView.setFocusable(true);
                mRules_dw.mView.requestFocus();
            }

        }
    }

    public boolean submit() {

        List<Rules_Dw> list = listErrorRetunrn();
        for (Rules_Dw mCrudObject : list) {
            if (mCrudObject.mView != null) {
                if (mCrudObject.mView instanceof CheckBox) {
                    CheckBox mCheckBox = (CheckBox) mCrudObject.mView;
                    setErrorCheckBox(mCheckBox, mCrudObject.mRuleDw.error);
                }
                if (mCrudObject.mView instanceof Spinner) {
                    Spinner mSpinner = (Spinner) mCrudObject.mView;
                    // if (mErrorItem != null)
                    // mErrorItem.submitResult(mCrudObject.mView);
                }
                if (mCrudObject.mView instanceof AppCompatAutoCompleteTextView) {
                    setError(mCrudObject.mView, mCrudObject.mRuleDw.error);
                }
                if (mCrudObject.mView instanceof TextInputEditText) {
                    setError(mCrudObject.mView, mCrudObject.mRuleDw.error);
                }
            } else {
                //  setError(mCrudObject.mTextInputEditText, mCrudObject.mRuleDw.error);
            }
        }

        if (list != null && list.size() > 0) {
            if (mErrorItem != null) mErrorItem.submitResult(list);
            return false;
        } else {
            return true;
        }
        //  return (numError > 0 ? false : true);
    }

    private List<Rules_Dw> listErrorRetunrn() {
        List<Rules_Dw> lisError = new ArrayList<>();
        for (Rules_Dw mCrudObject : mRuleDwList) {
            if (mCrudObject.mView != null) {

                /**
                 * CheckBox
                 */

                if (mCrudObject.mView instanceof CheckBox) {
                    CheckBox mCheckBox = (CheckBox) mCrudObject.mView;
                    if (mCrudObject.mRuleDw.isValid("")) {
                        setErrorCheckBox(mCheckBox, null);
                    } else {
                        if (mCheckBox.getVisibility() == View.VISIBLE)
                            lisError.add(mCrudObject);
                    }
                }

                /**
                 * Spinner
                 */

                if (mCrudObject.mView instanceof Spinner) {
                    Spinner mSpinner = (Spinner) mCrudObject.mView;
                    if (mCrudObject.mRuleDw.isValid("")) {
                    } else {
                        if (mSpinner.getVisibility() == View.VISIBLE)
                            lisError.add(mCrudObject);
                    }
                }

                /**
                 * AppCompatAutoCompleteTextView
                 */

                if (mCrudObject.mView instanceof AppCompatAutoCompleteTextView) {
                    AppCompatAutoCompleteTextView mAppCompatAutoCompleteTextView = (AppCompatAutoCompleteTextView) mCrudObject.mView;
                    if (mCrudObject.mRuleDw.isValid(mAppCompatAutoCompleteTextView.getText().toString())) {
                        setError(mAppCompatAutoCompleteTextView, null);
                    } else {
                        if (mAppCompatAutoCompleteTextView.getVisibility() == View.VISIBLE) {
                            lisError.add(mCrudObject);
                        }
                    }
                }

                /**
                 * TextInputEditText
                 */

                if (mCrudObject.mView instanceof TextInputEditText) {
                    TextInputEditText mTextInputEditText = (TextInputEditText) mCrudObject.mView;
                    if (mCrudObject.mRuleDw.isValid(mTextInputEditText.getText().toString())) {
                        setError(mTextInputEditText, null);
                    } else {
                        if (mTextInputEditText.getVisibility() == View.VISIBLE) {
                            lisError.add(mCrudObject);
                        }
                    }
                }

            } else {

            }
        }
        return lisError;
    }

    public void setError(View mView, String error) {
        //  TextInputLayout textInputLayout = (TextInputLayout) findViewById(v.getId()).getParent().getParent();
        if (mView instanceof TextInputEditText || mView instanceof AppCompatAutoCompleteTextView) {

            try {
                TextInputLayout layout = (TextInputLayout) mView.getParent().getParent();
                layout.setError(error);
                if (error == null) {
                    layout.setError(null);
                    layout.setErrorEnabled(false);
                } else {
                    layout.setErrorEnabled(true);
                }

            } catch (Exception e) {
                e.printStackTrace();
                if (error == null) {
                    if (mView instanceof TextInputEditText)
                        ((TextInputEditText) mView).setError(null);
                    else if (mView instanceof AppCompatAutoCompleteTextView) ;
                    ((AppCompatAutoCompleteTextView) mView).setError(null);

                } else {
                    if (mView instanceof TextInputEditText)
                        ((TextInputEditText) mView).setError(error);
                    else if (mView instanceof AppCompatAutoCompleteTextView) ;
                    ((AppCompatAutoCompleteTextView) mView).setError(error);

                }
            }
        }
    }

    public void setErrorCheckBox(CheckBox mErrorCheckBox, String error) {
        if (error == null) {
            mErrorCheckBox.setError(null);
        } else {
            mErrorCheckBox.setError(error);

        }
    }

    public errorItem mErrorItem;

    public interface errorItem {
        void submitResult(List<Rules_Dw> mRules_dws);
    }
}
