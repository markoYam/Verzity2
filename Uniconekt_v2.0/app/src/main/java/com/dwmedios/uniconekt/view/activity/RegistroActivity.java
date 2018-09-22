package com.dwmedios.uniconekt.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Dispositivo;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.presenter.RegistroPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.LoginActivity2;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.util.UtilsFtp.ftpClient;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_ConfirmPassword;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_Email_Valid;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_IsEmpty;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_MinLength_valid;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_Phone_valid;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_required_field;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.RuleDw_base;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Rules_Dw;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField;
import com.dwmedios.uniconekt.view.viewmodel.RegistroViewController;
import com.facebook.login.widget.ProfilePictureView;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistroActivity extends BaseActivity implements Validator.ValidationListener, RegistroViewController {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    // @BindView(R.id.nbUniversidad)
    //TextInputLayout mEditTextUniversidad;

    @NotEmpty(message = "Campo requerido")
    @BindView(R.id.nbUniversidad)
    TextInputEditText mTextInputEditTextNombre;
    @BindView(R.id.nbReprecentante)
    @NotEmpty(message = "Campo requerido")
    TextInputEditText mEditTextReprecentante;

    @BindView(R.id.desTelefono)
    @NotEmpty(message = "Campo requerido")
    TextInputEditText mEditTextTelefono;

    @BindView(R.id.desCorreo)
    @NotEmpty(message = "Campo requerido")
    @Email(message = "Email invalido")
    TextInputEditText mEditTextCorreo;

    @BindView(R.id.contrasenia)
    @NotEmpty(message = "Campo requerido")
    @Password(min = 8, message = "Debe contener 8 caracteres como minimo")
    TextInputEditText mEditTextContrasenia;

    @BindView(R.id.contrasenia_confirmar)
    @NotEmpty(message = "Campo requerido")
    @ConfirmPassword(message = "No coinciden las contraseñas.")
    TextInputEditText mEditTextContraseniaConfirmar;

    @BindView(R.id.checkbox_terminos)
    @Checked(message = "Debe estar de acuerdo con los términos.")
    CheckBox mCheckBoxTerminos;
    @BindView(R.id.layoutContraseña)
    TextInputLayout mTextInputLayoutContraseña;

    @BindView(R.id.layoutConfirmarContraseña)
    TextInputLayout mTextInputLayoutConfirmarContraseña;
    @BindView(R.id.buttonRegistrar)
    Button mButtonRegistrar;

    @BindView(R.id.profile_image)
    ImageView mImageViewProfile;

    @BindView(R.id.friendProfilePicture)
    ProfilePictureView mProfilePictureView;

    @BindView(R.id.fabPerfil)
    FloatingActionButton mFloatingActionButton;

    private RegistroViewController mRegistroViewController;
    private RegistroPresenter mRegistroPresenter;
    private Validator mValidator;
    private String IMEI = "";
    private String CVFACEBOOK = "";
    private boolean FACEBOOK = false;
    private String imageProfilepatch = null;
    public static String OBJECT = "OBJET_PERSON";
    ValidateField mValidateField;
    List<Rules_Dw> mRules_dws = new ArrayList<>();
    View mView;
    private ftpClient mFtpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //agregar siempre para que aga los vinculos
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Registro - Universidad");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mView = findViewById(R.id.cardviewSlide);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mButtonRegistrar.setOnClickListener(mOnClickListener);
        mFloatingActionButton.setOnClickListener(mOnClickListener);
        mRegistroPresenter = new RegistroPresenter(this, getApplicationContext());

        // getImei();
        configureActivity();
        rules();
    }

    private void configureActivity() {
        try {
            Usuario temp = getIntent().getExtras().getParcelable(OBJECT);
            CVFACEBOOK = temp.cv_facebook;
            mEditTextReprecentante.setText(temp.persona.nombre);
            mEditTextCorreo.setText(temp.nombre);
            mEditTextCorreo.setEnabled(false);
            FACEBOOK = true;
            LinearLayout mLinearLayout = (LinearLayout) mTextInputLayoutContraseña.getParent();
            mLinearLayout.setVisibility(View.GONE);
            LinearLayout mLinearLayout2 = (LinearLayout) mTextInputLayoutConfirmarContraseña.getParent();
            mLinearLayout2.setVisibility(View.GONE);
            mEditTextContrasenia.setVisibility(View.GONE);
            mEditTextContraseniaConfirmar.setVisibility(View.GONE);
            //mImageViewProfile.setVisibility(View.GONE);
            //  ImageLoader.getInstance().displayImage("https://graph.facebook.com/"+temp.cv_facebook+"/picture?type=large",mImageViewProfile,OptionsImageLoaderUser);
            // mProfilePictureView.setVisibility(View.VISIBLE);
            //  mProfilePictureView.setProfileId(temp.cv_facebook);

            new taskImage().execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class taskImage extends AsyncTask<Void, Void, Void> {

        Bitmap bmp;

        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL("https://graph.facebook.com/" + CVFACEBOOK + "/picture?type=large");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (bmp != null)
                mImageViewProfile.setImageBitmap(bmp);
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), LoginActivity2.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), LoginActivity2.class));
        finish();
        super.onBackPressed();
    }

    private void getImei() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            IMEI = getImei(getApplicationContext());
        } else {
            ActivityCompat.requestPermissions(RegistroActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
    }

    public void rules() {

        RuleDw_base email = new Dw_Email_Valid("Ingrese un correo electrónico valido. ");
        RuleDw_base noEmpty = new Dw_IsEmpty("Campo requerido");
        RuleDw_base phone = new Dw_Phone_valid("Ingrese un número de teléfono valido");
        RuleDw_base check = new Dw_required_field(mCheckBoxTerminos, "Debe estar de acuerdo con los términos.");
        RuleDw_base password = new Dw_ConfirmPassword(mEditTextContrasenia, "La contraseña y la confirmación no coinciden.");
       // RuleDw_base longitudCaracteres = new Dw_Length_valid("Ingrese un número de teléfono valido", 14);
        RuleDw_base longitudCaracteresPass = new Dw_MinLength_valid("La contraseña debe tener como mínimo 8 caracteres de longitud.", 8);

        mRules_dws.add(new Rules_Dw(mEditTextCorreo, email));
        mRules_dws.add(new Rules_Dw(mEditTextTelefono, phone));
        //mRules_dws.add(new Rules_Dw(mEditTextTelefono, longitudCaracteres));
        mRules_dws.add(new Rules_Dw(mEditTextCorreo, email));
        mRules_dws.add(new Rules_Dw(mTextInputEditTextNombre, noEmpty));
        mRules_dws.add(new Rules_Dw(mEditTextReprecentante, noEmpty));
        mRules_dws.add(new Rules_Dw(mEditTextContrasenia, longitudCaracteresPass));
        mRules_dws.add(new Rules_Dw(mEditTextContraseniaConfirmar, longitudCaracteresPass));
        mRules_dws.add(new Rules_Dw(mEditTextContrasenia, noEmpty));
        mRules_dws.add(new Rules_Dw(mEditTextContraseniaConfirmar, password));
        mRules_dws.add(new Rules_Dw(mCheckBoxTerminos, check));
        mValidateField = new ValidateField(mRules_dws);
        mCheckBoxTerminos.setOnCheckedChangeListener(mOnCheckedChangeListener);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImei();
                    return;
                } else {

                    showInfoDialogListener("Atención", "Es nesesario otorgar los permisos para continuar", "OK");
                }
                return;
            }
        }
        if (mFtpClient != null)
            mFtpClient.permisonResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onValidationSucceeded() {
        //  showMessage("Informacion Correcta");
        Usuario temp = user();
        if (!IMEI.isEmpty()) {
            mRegistroPresenter.registerUser(temp);
        }

    }

    CheckBox.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (mCheckBoxTerminos.isChecked()) {
                mCheckBoxTerminos.setError(null);
            }
        }
    };

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

    private static void toggleTextInputLayoutError(@NonNull TextInputLayout textInputLayout,
                                                   String msg) {
        textInputLayout.setError(msg);
        if (msg == null) {
            textInputLayout.setErrorEnabled(false);
        } else {
            textInputLayout.setErrorEnabled(true);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.buttonRegistrar:
                    if (mValidateField.submit()) {
                        Usuario temp = user();
                        if (!IMEI.isEmpty()) {
                            mRegistroPresenter.registerUser(temp);
                        }
                    }
                    break;
                case R.id.fabPerfil:
                    mFtpClient = new ftpClient(getApplicationContext(), RegistroActivity.this);
                    mFtpClient.pickImage(new ftpClient.patchImageInterface() {
                        @Override
                        public void patch(String patch) {
                            imageProfilepatch = patch;
                        }
                    }, mImageViewProfile);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mFtpClient != null)
            mFtpClient.activityResult(requestCode, resultCode, data);
    }

    public boolean activateSlide;

    public void slideUp() {
        mView.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                mView.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        mView.startAnimation(animate);
    }

    public void slideDown() {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                mView.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        mView.startAnimation(animate);
    }

    @Override
    public void successRegister(Usuario data) {
        //guardar en la base de datosxccs
        boolean save = mRegistroPresenter.SaveAllInfoUser(data);
        if (save) {
            SharePrefManager.getInstance(getApplicationContext()).saveTypeUser(2);
            startActivity(new Intent(getApplicationContext(), SplashActivity.class));
            finish();
        }
    }

    private Usuario user() {
        getImei(); // TODO: 18/04/2018 obtener imei
        Usuario user = new Usuario();
        user.nombre = mEditTextCorreo.getText().toString();
        if (!FACEBOOK) user.contrasenia = mEditTextContrasenia.getText().toString();
        if (FACEBOOK) user.cv_facebook = CVFACEBOOK;
        Persona persona = new Persona();
        persona.correo = mEditTextCorreo.getText().toString();
        persona.nombre = mEditTextReprecentante.getText().toString();
        persona.telefono = mEditTextTelefono.getText().toString();

        Dispositivo dispositivo = new Dispositivo();
        dispositivo.clave_dispositivo = IMEI;
        dispositivo.clave_firebase = SharePrefManager.getInstance(getApplicationContext()).getToken();
        List<Dispositivo> dis = new ArrayList<>();
        dis.add(dispositivo);
        persona.dispositivosList = dis;

        Universidad universidad = new Universidad();
        universidad.logo = imageProfilepatch;
        universidad.nombre = mTextInputEditTextNombre.getText().toString();
        universidad.reprecentante = mEditTextReprecentante.getText().toString();

        List<Universidad> uni = new ArrayList<>();
        uni.add(universidad);

        persona.universidad = uni;
        user.persona = persona;
        // persona.telefono=mEditTextTelefono.getText().toString();
        return user;

    }

    @Override
    public void failureRegister(String message) {
        showMessage(message);
    }

    @Override
    public void showProgress(boolean loading) {
        //pendiente probar
        if (loading) {
            showOnProgressDialog("Cargando...");
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void UserReturn(Usuario usuario) {

    }
}
