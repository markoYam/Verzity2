package com.dwmedios.uniconekt.view.activity.base;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.Utils_app_Running.BaseApp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Luis Cabanas on 19/03/2018.
 */

public class BaseActivity extends BaseApp {
    public interface OnFinishInputDialog {
        void onFinish(String text);
    }

    protected void onHomePressed() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (upIntent == null || !NavUtils.shouldUpRecreateTask(this, upIntent)) {
            finish();
        } else {
            TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
        }
    }
    public boolean validatePermison(String permison, Activity onresult, int CodeResult) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), permison) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(onresult, new String[]{permison}, CodeResult);
        }
        return false;
    }

    public boolean validatePermison(List<String> permison, Activity onresult, int CodeResult) {
        List<String> temp = new ArrayList<>();
        for (String per : permison) {
            if (ActivityCompat.checkSelfPermission(onresult.getApplicationContext(), per) != PackageManager.PERMISSION_GRANTED) {
                temp.add(per);
            }
        }
        if (temp.size() > 0) {
            String[] persmisos = new String[temp.size()];
            for (int i = 0; i < temp.size(); i++) {
                persmisos[i] = temp.get(i);
            }
            ActivityCompat.requestPermissions(onresult, persmisos, CodeResult);
        } else {
            return true;
        }

        return false;
    }

    public boolean checkPermison(List<String> permison, Activity onresult, int CodeResult) {
        List<String> temp = new ArrayList<>();
        for (String per : permison) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), per) != PackageManager.PERMISSION_GRANTED) {
                temp.add(per);
            }
        }
        if (temp.size() > 0) {
            String[] persmisos = new String[temp.size()];
            for (int i = 0; i < temp.size(); i++) {
                persmisos[i] = temp.get(i);
            }
            //ActivityCompat.requestPermissions(onresult, persmisos, CodeResult);
        } else {
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideKeyboard();
    }

    /*public void showdialogInputMaterial(String titulo, String mensaje, int icono, LovelyTextInputDialog.OnTextInputConfirmListener mOnTextInputConfirmListener) {
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setIcon(icono)
                .setInputFilter("El siguiente campo no puede estar vacío.", new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("\\w+");
                    }
                })
                .setConfirmButton(android.R.string.ok, mOnTextInputConfirmListener)
                .setNegativeButton(android.R.string.no, null)
                .show();
    }*/

    /*public void showdialogInputMaterial(String titulo, String mensaje, int icono, String buttonOk, LovelyTextInputDialog.OnTextInputConfirmListener mOnTextInputConfirmListener) {
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setIcon(icono)
                .setInputFilter("El siguiente campo no puede estar vacío.", new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        if (text.isEmpty()) {
                            return false;
                        } else
                            return true;
                    }
                })
                .setConfirmButton(buttonOk, mOnTextInputConfirmListener)
                .setNegativeButton(android.R.string.no, null)
                .show();
    }*/

  /*  public void showdialogMaterial(String titulo, String mensaje, int icono, View.OnClickListener mOnClickListener) {
        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorPrimary)
                .setButtonsColorRes(R.color.colorPrimaryDark)
                .setIcon(icono)
                .setTitle(titulo)
                .setCancelable(false)
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, mOnClickListener)
                .setNegativeButton(android.R.string.no, null)
                .show();
    }*/

  /*  public void showdialogMaterial(String titulo, String mensaje, int icono, View.OnClickListener yes, View.OnClickListener no) {
        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorPrimary)
                .setButtonsColorRes(R.color.colorPrimaryDark)
                .setIcon(icono)
                .setTitle(titulo)
                .setCancelable(false)
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, yes)
                .setNegativeButton(android.R.string.no, no)
                .show();
    }*/

  /*  public void showdialogMaterialConfig(String titulo, String mensaje, int icono, View.OnClickListener mOnClickListener) {
        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorPrimary)
                .setButtonsColorRes(R.color.colorPrimaryDark)
                .setIcon(icono)
                .setTitle(titulo)
                .setCancelable(false)
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, mOnClickListener)
                .setNegativeButton(android.R.string.no, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                })
                .show();
    }*/

   /* public void showdialogMaterial2(String titulo, String mensaje, int icono, View.OnClickListener mOnClickListener) {
        new LovelyStandardDialog(this, LovelyStandardDialog.ButtonLayout.HORIZONTAL)
                .setTopColorRes(R.color.colorPrimary)
                .setButtonsColorRes(R.color.colorPrimaryDark)
                .setIcon(icono)
                .setCancelable(false)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, mOnClickListener)
                .show();
    }*/

  /*  public void ShowDialogMaterialChoice(String titulo, String mensaje, int icono, List<String> List, LovelyChoiceDialog.OnItemSelectedListener mOnItemSelectedListener) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_choice_licenciaturas, List);
        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(titulo)
                .setIcon(icono)
                .setMessage(mensaje)
                .setItems(adapter, mOnItemSelectedListener)
                .show();
    }*/

  /*  public void ShowDialogMaterialChoice2(String titulo,int icono,List<String> List ,LovelyChoiceDialog.OnItemSelectedListener mOnItemSelectedListener) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_choice_licenciaturas, List);
        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.colorPrimary)
                .setTitle(titulo)
                .setIcon(icono)
                .setItems(adapter, mOnItemSelectedListener)
                .show();
    }*/
// TODO: 06/09/2018 verificar este metodo
   /* public void showmesaajeDesactivateUniversity() {
        showdialogMaterial2("Atención", "La información de la universidad se esta validando por el administrador, se le notificara por correo cuando este proceso concluya.", R.drawable.ic_action_information, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }*/

   /* public void showdialogMaterial(String titulo, String mensaje, int icono) {
        new LovelyInfoDialog(this)
                .setTopColorRes(R.color.colorPrimary)
                .setIcon(icono)
                .setNotShowAgainOptionEnabled(0)
                .setNotShowAgainOptionChecked(true)
                .setTitle(titulo)
                .setMessage(mensaje)
                .show();
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        hideKeyboard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyboard();
    }

    AlertDialog alert = null;

    public void AlertNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Habilidar servicios de ubicación")
                .setCancelable(false)
                .setPositiveButton("Habilitar", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }).setCancelable(false);

        alert = builder.create();
        alert.show();
    }

    public void showConfirmDialog(String title, String msj, DialogInterface.OnClickListener mOnClickListenerYes) {
        AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(BaseActivity.this);
        dialogConfirm.setTitle(title);
        dialogConfirm.setMessage(msj);
        dialogConfirm.setPositiveButton("Si", mOnClickListenerYes);
        dialogConfirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.cancel();
            }
        });
        dialogConfirm.show();
    }

    public void showConfirmDialog1(String title, String msj, DialogInterface.OnClickListener mOnClickListenerYes) {
        AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(BaseActivity.this);
        dialogConfirm.setTitle(title);
        dialogConfirm.setMessage(msj);
        dialogConfirm.setCancelable(false);
        dialogConfirm.setPositiveButton("OK", mOnClickListenerYes);
        dialogConfirm.show();
    }

    public void showConfirmDialog(String title, String msj, DialogInterface.OnClickListener mOnClickListenerYes, DialogInterface.OnClickListener mOnClickListenerNo) {
        AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(BaseActivity.this);
        dialogConfirm.setTitle(title);
        dialogConfirm.setMessage(msj);
        dialogConfirm.setCancelable(false);
        dialogConfirm.setPositiveButton("Si", mOnClickListenerYes);
        dialogConfirm.setNegativeButton("No", mOnClickListenerNo);
        dialogConfirm.show();
    }

    public void showConfirmDialog(String title, String msj, String button1, String button2, DialogInterface.OnClickListener mOnClickListenerYes, DialogInterface.OnClickListener mOnClickListenerNo) {
        AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(BaseActivity.this);
        dialogConfirm.setTitle(title);
        dialogConfirm.setMessage(msj);
        dialogConfirm.setCancelable(false);
        dialogConfirm.setPositiveButton(button1, mOnClickListenerYes);
        dialogConfirm.setNegativeButton(button2, mOnClickListenerNo);
        dialogConfirm.show();
    }

    public void showInfoDialog(String title, String msj, String button) {
        AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(BaseActivity.this);
        dialogConfirm.setTitle(title);
        dialogConfirm.setMessage(msj);
        dialogConfirm.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        dialogConfirm.show();
    }

    public void showInfoDialog(String title, String msj, String button, DialogInterface.OnClickListener mOnClickListener) {
        AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(BaseActivity.this);
        dialogConfirm.setTitle(title);
        dialogConfirm.setMessage(msj);
        dialogConfirm.setPositiveButton(button, mOnClickListener);
        dialogConfirm.show();
    }

    public void showInfoDialogListener(String title, String msj, String button) {
        AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(BaseActivity.this);
        dialogConfirm.setTitle(title);
        dialogConfirm.setMessage(msj);
        dialogConfirm.setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        dialogConfirm.show();
    }

    public void showInputDialog(String title, String textHint, String textButton, String textEdit, final OnFinishInputDialog mOnFinishInputDialog) {

        AlertDialog.Builder dialogInput = new AlertDialog.Builder(BaseActivity.this);
        dialogInput.setTitle(title);
        final EditText input = new EditText(BaseActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint(textHint);
        input.setFocusableInTouchMode(true);
        input.setTextColor(getResources().getColor(R.color.colorPrimary));
        input.setText(textEdit);
        dialogInput.setView(input);

        dialogInput.setPositiveButton(textButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mOnFinishInputDialog != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    mOnFinishInputDialog.onFinish(input.getText().toString());
                }
            }
        });

        dialogInput.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                dialog.cancel();
            }
        });

        dialogInput.create();
        dialogInput.show();
    }

    public void showInputDialog(String title, String textHint, String textButton, String textEdit, final OnFinishInputDialog mOnFinishInputDialog, boolean showCancel) {
        try {
            AlertDialog.Builder dialogInput = new AlertDialog.Builder(BaseActivity.this);
            dialogInput.setTitle(title);
            dialogInput.setCancelable(false);
            final EditText input = new EditText(BaseActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setHint(textHint);
            input.setFocusableInTouchMode(true);
            input.setTextColor(getResources().getColor(R.color.colorPrimary));
            input.setText(textEdit);
            input.setMaxLines(4);

            int maxLength = 140;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            input.setFilters(fArray);

            input.setSingleLine(false);
            input.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(250);
            input.setFilters(FilterArray);

            dialogInput.setView(input);

            dialogInput.setPositiveButton(textButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mOnFinishInputDialog != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        mOnFinishInputDialog.onFinish(input.getText().toString());
                    }
                }
            });
            if (showCancel) {
                dialogInput.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                        dialog.cancel();
                    }
                });
            }
            dialogInput.create();
            dialogInput.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showInputDialogNumber(String title, String textHint, String textButton, String textEdit, final OnFinishInputDialog mOnFinishInputDialog, boolean showCancel) {

        AlertDialog.Builder dialogInput = new AlertDialog.Builder(BaseActivity.this);
        dialogInput.setTitle(title);
        dialogInput.setCancelable(false);
        final EditText input = new EditText(BaseActivity.this);
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint(textHint);
        input.setFocusableInTouchMode(true);
        input.setTextColor(getResources().getColor(R.color.colorPrimary));
        input.setText(textEdit);
        input.setWidth(50);
        input.setSingleLine(true);
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(250);
        input.setFilters(FilterArray);

        int maxLength = 20;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        input.setFilters(fArray);

        input.setSelection(input.getText().length());
        dialogInput.setView(input);

        dialogInput.setPositiveButton(textButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mOnFinishInputDialog != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    mOnFinishInputDialog.onFinish(input.getText().toString());
                }
            }
        });
        if (showCancel) {
            dialogInput.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                    dialog.cancel();
                }
            });
        }
        dialogInput.create();
        dialogInput.show();
    }

    public void showItemsDialog(String title, CharSequence[] items, DialogInterface.OnClickListener mOnClickListener) {
        AlertDialog.Builder dialogInput = new AlertDialog.Builder(BaseActivity.this);
        dialogInput.setTitle(title);
        dialogInput.setItems(items, mOnClickListener);
        dialogInput.create();
        dialogInput.show();
    }

    public void showOnProgressDialog(String message) {
        try {
            mProgressDialog = new ProgressDialog(BaseActivity.this);
            mProgressDialog.setMessage(message);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgress(0);
            mProgressDialog.setMax(100);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showOnProgressDialog(String message, Context mContext) {
        try {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage(message);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgress(0);
            mProgressDialog.setMax(100);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissProgressDialog() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showToastMessage(String mensaje) {
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    public void showToastLongMessage(String mensaje) {
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
    }

    public void showDialog(String title, String msj, String buttonClose) {
        AlertDialog.Builder dialogConfirm = new AlertDialog.Builder(BaseActivity.this);
        dialogConfirm.setTitle(title);
        dialogConfirm.setMessage(msj);
        dialogConfirm.setNegativeButton(buttonClose, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.cancel();
            }
        });
        dialogConfirm.show();
    }

    public void showMessage(String title, String message, String button) {
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public void showMessage(String message) {
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public Boolean checkWifi() {
        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager != null) {
            NetworkInfo info = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean checkDatasMovil() {
        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager != null) {
            NetworkInfo info = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public <T> boolean isListEmpty(List<T> list) {
        if (list == null || list.size() == 0)
            return true;
        else
            return false;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setFontButton(Button button, String fontPath) {
        Typeface mTypeface = Typeface.createFromAsset(getAssets(), fontPath);
        button.setTypeface(mTypeface);
    }

    public void setFontEditText(EditText editText, String fontPath) {
        Typeface mTypeface = Typeface.createFromAsset(getAssets(), fontPath);
        editText.setTypeface(mTypeface);
    }


    //Validaciones
    public boolean isEmptyEditText(EditText mEditText) {

        boolean empty = false;
        String text = mEditText.getText().toString();
        if (TextUtils.isEmpty(text)) {
            empty = true;
        }

        return empty;
    }

    private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean isValidEmail(String email) {

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public static String getImei(Context c) {
        String result = null;
        if (ActivityCompat.checkSelfPermission(c, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager = (TelephonyManager) c
                    .getSystemService(Context.TELEPHONY_SERVICE);
            result = telephonyManager.getDeviceId();
        }
        if (result == null) {
            result = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.e("Secure id", result + "");
        }
        return result;
    }

    public void animateViewpager(final ViewPager mViewPager) {

        final int numPages = mViewPager.getAdapter().getCount();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
                if (position < numPages) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(position + 1);
                        }
                    }, 2000);
                }
                if (position == numPages) {

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private ProgressDialog mProgressDialog;
    private ConnectivityManager mConnectivityManager;
}
