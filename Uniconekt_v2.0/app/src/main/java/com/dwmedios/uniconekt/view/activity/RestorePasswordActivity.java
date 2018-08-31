package com.dwmedios.uniconekt.view.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.presenter.ResetPasswordPresenter;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.viewmodel.ResetPasswordViewController;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestorePasswordActivity extends BaseActivity implements Validator.ValidationListener,ResetPasswordViewController {

    @BindView(R.id.buttonCancelar)
    Button mButtonCancelar;

    @BindView(R.id.buttonContinuar)
    Button mButtonContinuar;

    @BindView(R.id.textViewCorreo)
    @Email(message = "Ingrese un correo electrónico valido.")
    TextInputEditText mEditTextCorreo;

    private Validator mValidator;
    private ResetPasswordPresenter mResetPasswordPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_restore_password);
        this.setFinishOnTouchOutside(false);
        // TODO: 18/04/2018 para la inyecccion de vistas
        ButterKnife.bind(this);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mResetPasswordPresenter = new ResetPasswordPresenter(getApplicationContext(), this);
        mButtonCancelar.setOnClickListener(mOnClickListener);
        mButtonContinuar.setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.buttonCancelar:
                    finish();
                    break;
                case R.id.buttonContinuar:
                    mValidator.validate();
                    break;
            }
        }
    };

    @Override
    public void onValidationSucceeded() {
        Usuario user = userPassword();
        mResetPasswordPresenter.RessetPassword(user);
    }

    private Usuario userPassword() {
        Usuario user = new Usuario();
        user.nombre = mEditTextCorreo.getText().toString();
        return user;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void Succes(Usuario mUsuario) {
        finish();
    }

    @Override
    public void Failed(String mensaje) {
        showToastMessage(mensaje);
    }

    @Override
    public void Loading(boolean loading) {
        if (loading) {
            showOnProgressDialog("Cargando...");
        } else {
            dismissProgressDialog();
        }
    }
}
