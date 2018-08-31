package com.dwmedios.uniconekt.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Dispositivo;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.presenter.LoginPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario.MainUniversitarioActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.activity.demo_upload_image.UploadActivity;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.viewmodel.LoginViewController;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.RegistroActivity.OBJECT;

public class LoginActivity extends BaseActivity implements Validator.ValidationListener,LoginViewController {
    @BindView(R.id.textViewUser)
    @Email(message = "Ingrese un correo electrónico valido.")
    EditText mEditTextUser;

    @BindView(R.id.textViewPassword)
    @NotEmpty(message = "Campo requerido")
    EditText mEditTextPassword;

    @BindView(R.id.textViewRegistro)
    TextView mTextViewRegistro;

    @BindView(R.id.textViewResetPassword)
    TextView mTextViewRecoveryPassword;

    @BindView(R.id.textViewIngresaUniversitario)
    TextView mTextViewIngresaUniversitario;

    @BindView(R.id.buttonIncio)
    Button mButtonIncio;

    private Validator mValidator;

    private LoginPresenter mLoginPresenter;
    private String IMEI = "";
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private Usuario userFacebook = new Usuario();
    private boolean facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        mTextViewRegistro.setOnClickListener(mOnClickListener);
        mButtonIncio.setOnClickListener(mOnClickListener);
        mTextViewRecoveryPassword.setOnClickListener(mOnClickListener);
        mTextViewIngresaUniversitario.setOnClickListener(mOnClickListener);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mLoginPresenter = new LoginPresenter(this, LoginActivity.this);

        callbackManager = CallbackManager.Factory.create();


        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        getSupportActionBar().hide();
        // Callback registration
        loginButton.registerCallback(callbackManager, mFacebookCallback);

        SpannableString mitextoU = new SpannableString("Ingresa para conocer las opciones.");
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        mTextViewIngresaUniversitario.setText(mitextoU);

        SpannableString mitextoU2 = new SpannableString("Regístrate aquí");
        mitextoU2.setSpan(new UnderlineSpan(), 0, mitextoU2.length(), 0);
        mTextViewRegistro.setText(mitextoU2);

        SpannableString mitextoU3 = new SpannableString("¿Olvidó su contraseña?");
        mitextoU3.setSpan(new UnderlineSpan(), 0, mitextoU3.length(), 0);
        mTextViewRecoveryPassword.setText(mitextoU3);

        if (validatePermison(Manifest.permission.READ_PHONE_STATE, LoginActivity.this, 3)) {
            SharePrefManager.getInstance(getApplicationContext()).saveImei(getImei(getApplicationContext()));
        }
    }

    FacebookCallback mFacebookCallback = new FacebookCallback() {

        @Override
        public void onSuccess(Object o) {
            LoginResult result = (LoginResult) o;
            //   showMessage("Login exitoso");
            //  GraphRequest mGraphRequest= new GraphRequest()

            GraphRequest request = GraphRequest.newMeRequest(result.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            try {
                                facebook = true;
                                userFacebook.nombre = object.getString("email");
                                userFacebook.cv_facebook = object.getString("id");
                                Persona per = new Persona();
                                per.nombre = object.getString("name");
                                userFacebook.persona = per;
                                if (validatePermison(Manifest.permission.READ_PHONE_STATE, LoginActivity.this, 2)) {
                                    List<Dispositivo> dispositivos = new ArrayList<>();
                                    dispositivos.add(infoDispositivo());
                                    per.dispositivosList = dispositivos;
                                    //verificar si el usuario ya se encuentra registrado....
                                    mLoginPresenter.LoginUser(userFacebook);
                                }
                            } catch (JSONException e) {
                                showMessage("Ocurrió un error");
                                e.printStackTrace();

                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            showMessage("Operación cancelada.");
        }

        @Override
        public void onError(FacebookException error) {
            showMessage(error.getMessage());
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.textViewRegistro:
                    startActivity(new Intent(getApplicationContext(), RegistroActivity.class));
                    finish();
                    break;
                case R.id.buttonIncio:
                    mValidator.validate();
                   // startActivity(new Intent(getApplicationContext(), UploadActivity.class));
                    break;

                case R.id.textViewResetPassword:
                    startActivity(new Intent(getApplicationContext(), RestorePasswordActivity.class));
                    break;
                case R.id.textViewIngresaUniversitario:
                    getImei();
                    break;
            }

        }
    };

    private void getImei() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            LoginUniversitario();
        } else {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoginUniversitario();

                    return;
                } else {

                    showInfoDialogListener("Atención", "Es nesesario otorgar los permisos para continuar", "OK");
                    getImei();
                }
                return;
            }

            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    List<Dispositivo> dispositivos = new ArrayList<>();
                    dispositivos.add(infoDispositivo());
                    userFacebook.persona.dispositivosList = dispositivos;
                    mLoginPresenter.LoginUser(userFacebook);
                    return;
                } else {
                    LoginManager.getInstance().logOut();
                    showInfoDialogListener("Atención", "Es nesesario otorgar los permisos para continuar", "OK");
                }

                break;

            case 3:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SharePrefManager.getInstance(getApplicationContext()).saveImei(getImei(getApplicationContext()));
                    return;
                } else {

                    showInfoDialogListener("Atención", "Es nesesario otorgar los permisos para continuar", "OK");
                }
                break;
        }
    }

    @Override
    public void onValidationSucceeded() {
        Usuario user = infouser();
        Persona per = new Persona();
        if (validatePermison(Manifest.permission.READ_PHONE_STATE, LoginActivity.this, 2)) {
            List<Dispositivo> dispositivos = new ArrayList<>();
            dispositivos.add(infoDispositivo());
            per.dispositivosList = dispositivos;
            user.persona=per;
            mLoginPresenter.LoginUser(user);
        }
       // mLoginPresenter.LoginUser(user);
    }

    private Usuario infouser() {
        Usuario user = new Usuario();
        user.nombre = mEditTextUser.getText().toString();
        user.contrasenia = mEditTextPassword.getText().toString();
        return user;
    }

    private Dispositivo infoDispositivo() {
        Dispositivo dis = new Dispositivo();
        dis.clave_dispositivo = getImei(getApplicationContext());
        if (SharePrefManager.getInstance(getApplicationContext()).getToken() != null) {
            dis.clave_firebase = SharePrefManager.getInstance(getApplicationContext()).getToken();
        } else {
            return null;
        }
        return dis;
    }

    private void LoginUniversitario() {
        Dispositivo dis = infoDispositivo();
       // mLoginPresenter.LoginUniversitario(dis);
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
    public void LoginSucces(Usuario mUsuario) {
        //guardar usuario
        mLoginPresenter.SaveAllInfoUser(mUsuario);
        SharePrefManager.getInstance(getApplicationContext()).saveTypeUser(2);
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
        finish();
    }

    @Override
    public void LoginFailed(String mensaje) {
        if (facebook) {
            Intent mIntent = new Intent(getApplicationContext(), RegistroActivity.class);
            mIntent.putExtra(OBJECT, userFacebook);
            startActivity(mIntent);
            finish();
        } else {
            showMessage(mensaje);
        }
    }


    public void LoginSuccesUniversitario() {
        SharePrefManager.getInstance(getApplicationContext()).saveTypeUser(1);
        startActivity(new Intent(getApplicationContext(), MainUniversitarioActivity.class));
        finish();
    }

    @Override
    public void Loading(boolean loading) {
        if (loading) {
            showOnProgressDialog("Cargando...");
        } else {
            dismissProgressDialog();
        }
    }


    public void Messaje(String mesaje) {
        showMessage(mesaje);
    }
}
