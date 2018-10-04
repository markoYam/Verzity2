package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Dispositivo;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.presenter.LoginPresenter;
import com.dwmedios.uniconekt.view.activity.RegistroActivity;
import com.dwmedios.uniconekt.view.activity.RestorePasswordActivity;
import com.dwmedios.uniconekt.view.activity.SplashActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.util.ImageUtils.setTintView;

public class LoginActivity2 extends BaseActivity implements LoginViewController {
    public static final String REGISTRO_FACEBOOK = "key_facebook2erdsdwsd";
    @BindView(R.id.descontraseña)
    TextInputEditText mTextInputEditTextContraseña;
    @BindView(R.id.desCorreo)
    TextInputEditText mTextInputEditTextCorreo;
    @BindView(R.id.buttonRegistrar)
    Button mButtonRegistrar;
    @BindView(R.id.buttonIniciarSesion)
    Button mButtonIniciar;
    @BindView(R.id.textViewResetPassword)
    TextView mTextViewPassword;
    @BindView(R.id.buttonregistrarUniversidad)
    Button mButtonRegistrarUniversidad;
    @BindView(R.id.imageViewLogo)
    ImageView mImageViewLogo;
    private LoginPresenter mLoginPresenter;
    private CallbackManager callbackManager;
    //private boolean loginFacebook = false;
    private Usuario mUsuarioLogin = null;
    private LoginButton mLoginButton;
    private int action = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login2);
        ButterKnife.bind(this);
        mLoginPresenter = new LoginPresenter(this, getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
            in.setDuration(500);
            mImageViewLogo.startAnimation(in);
            mImageViewLogo.setVisibility(View.VISIBLE);
        }
        onclickControls();
        if (validatePermison(Manifest.permission.READ_PHONE_STATE, LoginActivity2.this, 2))
            SharePrefManager.getInstance(getApplicationContext()).saveImei(getImei(getApplicationContext()));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    FacebookCallback mFacebookCallback = new FacebookCallback() {

        @Override
        public void onSuccess(Object o) {
            LoginResult result = (LoginResult) o;
            GraphRequest request = GraphRequest.newMeRequest(result.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            try {
                               // loginFacebook = true;
                                action = 1;
                                mUsuarioLogin = new Usuario();
                                mUsuarioLogin.nombre = object.getString("email");
                                mUsuarioLogin.cv_facebook = object.getString("id");
                                Persona per = new Persona();
                                per.nombre = object.getString("name");
                                mUsuarioLogin.persona = per;
                                if (validatePermison(Manifest.permission.READ_PHONE_STATE, LoginActivity2.this, 2)) {
                                    List<Dispositivo> dispositivos = new ArrayList<>();
                                    String imei = getImei(getApplicationContext());
                                    if (imei != null)
                                        SharePrefManager.getInstance(getApplicationContext()).saveImei(imei);
                                    Dispositivo mDispositivo = new Dispositivo();
                                    mDispositivo.clave_dispositivo = imei;
                                    mDispositivo.clave_firebase = SharePrefManager.getInstance(getApplicationContext()).getToken();
                                    dispositivos.add(mDispositivo);
                                    per.dispositivosList = dispositivos;
                                    //verificar si el usuario ya se encuentra registrado....
                                    mLoginPresenter.LoginUserFacebook(mUsuarioLogin);
                                }
                            } catch (JSONException e) {
                                showMessage("Ocurrió un error al iniciar sesión con Facebook");
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
           // loginFacebook = false;
        }

        @Override
        public void onError(FacebookException error) {
            showMessage(error.getMessage());
            //loginFacebook = false;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoginManager.getInstance().logOut();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTintView(getApplicationContext(), mTextInputEditTextCorreo, R.color.colorPrimaryDark, R.drawable.ic_action_ic_user);
            setTintView(getApplicationContext(), mTextInputEditTextContraseña, R.color.colorPrimaryDark, R.drawable.ic_action_ic_contrasea);

        }

    }

    public void onclickControls() {
        mButtonIniciar.setOnClickListener(mOnClickListener);
        mButtonRegistrar.setOnClickListener(mOnClickListener);
        mTextViewPassword.setOnClickListener(mOnClickListener);
        mButtonRegistrarUniversidad.setOnClickListener(mOnClickListener);
        mLoginButton = (LoginButton) findViewById(R.id.login_button);
        mLoginButton.setReadPermissions("email", "public_profile");
        mLoginButton.registerCallback(callbackManager, mFacebookCallback);
        SpannableString mitextoU3 = new SpannableString("¿Olvidó su contraseña?");
        mitextoU3.setSpan(new UnderlineSpan(), 0, mitextoU3.length(), 0);
        mTextViewPassword.setText(mitextoU3);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_button: /*facebook*/
                    break;
                case R.id.buttonIniciarSesion:
                    if (validateFields()) {
                        action = 2;
                        if (validatePermison(Manifest.permission.READ_PHONE_STATE, LoginActivity2.this, 2))
                            mLoginPresenter.LoginUser(getInfoFromView());
                    }
                    break;
                case R.id.buttonRegistrar:
                    startActivity(new Intent(getApplicationContext(), RegistroUniversitarioActivity.class));
                    break;
                case R.id.textViewResetPassword:
                    startActivity(new Intent(getApplicationContext(), RestorePasswordActivity.class));
                    break;
                case R.id.buttonregistrarUniversidad:
                    startActivity(new Intent(getApplicationContext(), RegistroActivity.class));
                    break;
            }
        }
    };

    public Usuario getInfoFromView() {
        Usuario mUsuario = new Usuario();
        mUsuario.nombre = mTextInputEditTextCorreo.getText().toString();
        mUsuario.contrasenia = mTextInputEditTextContraseña.getText().toString();
        List<Dispositivo> mDispositivos = new ArrayList<>();
        Dispositivo mDispositivo = new Dispositivo();
        mDispositivo.clave_dispositivo = SharePrefManager.getInstance(getApplicationContext()).getImei();
        mDispositivo.clave_firebase = SharePrefManager.getInstance(getApplicationContext()).getToken();
        mDispositivos.add(mDispositivo);
        Persona mPersona = new Persona();
        mPersona.dispositivosList = mDispositivos;
        mUsuario.persona = mPersona;
        return mUsuario;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SharePrefManager.getInstance(getApplicationContext()).saveImei(getImei(getApplicationContext()));
                    switch (action) {
                        case 1:
                            //facebook
                            List<Dispositivo> mDispositivos = new ArrayList<>();
                            Dispositivo mDispositivo = new Dispositivo();
                            mDispositivo.clave_dispositivo = SharePrefManager.getInstance(getApplicationContext()).getImei();
                            mDispositivo.clave_firebase = SharePrefManager.getInstance(getApplicationContext()).getToken();
                            mDispositivos.add(mDispositivo);
                            mUsuarioLogin.persona.dispositivosList = mDispositivos;
                            mLoginPresenter.LoginUserFacebook(mUsuarioLogin);
                            break;

                        case 2:
                            mLoginPresenter.LoginUser(getInfoFromView());
                            break;
                    }
                 /*   if (continueRegister) {
                        if (mValidateField.submit()) {
                            Usuario mUsuario = getInfoFromView();
                            mRegistroUniversitarioPresenter.CrearCuentaAcceso(mUsuario);
                        }
                    }*/
                    return;
                } else {

                    showInfoDialogListener("Atención", "Es necesario otorgar los permisos para continuar", "OK");
                    // getImei();
                    LoginManager.getInstance().logOut();
                }
                return;
            }
        }
    }

    public boolean validateFields() {
        String correo = mTextInputEditTextCorreo.getText().toString().trim();
        String contraseña = mTextInputEditTextContraseña.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches() || correo == null || correo.length() == 0) {
            mTextInputEditTextCorreo.setError("Ingrese un correo electrónico valido");
            mTextInputEditTextCorreo.requestFocus();
            return false;
        } else {
            mTextInputEditTextCorreo.setError(null);
        }
        if (contraseña == null || contraseña.length() == 0) {
            mTextInputEditTextContraseña.setError("La contraseña es requerida");
            mTextInputEditTextContraseña.requestFocus();
            return false;
        } else {
            mTextInputEditTextContraseña.setError(null);
            return true;
        }
    }

    @Override
    public void LoginSucces(Usuario mUsuario) {
        /** validar el tipo de usuario*/
        mLoginPresenter.SaveAllInfoUser(mUsuario);
        if (mUsuario.persona.mTipoPersonas.nombre.equals("UNIVERSITARIO"))
            SharePrefManager.getInstance(getApplicationContext()).saveTypeUser(1);
        else if (mUsuario.persona.mTipoPersonas.nombre.equals("UNIVERSIDAD"))
            SharePrefManager.getInstance(getApplicationContext()).saveTypeUser(2);
        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
        finish();
    }

    @Override
    public void LoginFailed(String mensaje) {
        showMessage(mensaje);
        LoginManager.getInstance().logOut();

    }

    @Override
    public void Loading(boolean loading) {
        if (loading) {
            showOnProgressDialog("Cargando...");
        } else
            dismissProgressDialog();
    }

    @Override
    public void RegistrarFacebook(Usuario mUsuario) {
        Intent mIntent = new Intent(getApplicationContext(), RegistroUniversitarioActivity.class);
        mIntent.putExtra(REGISTRO_FACEBOOK, mUsuarioLogin);
        startActivity(mIntent);
    }

    @Override
    public void MensajeFacebook(String mensaje) {
        showMessage(mensaje);
        LoginManager.getInstance().logOut();
    }
}
