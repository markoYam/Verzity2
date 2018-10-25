package com.dwmedios.uniconekt.view.activity.Universitario_v2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.CodigoPostal;
import com.dwmedios.uniconekt.model.Direccion;
import com.dwmedios.uniconekt.model.Dispositivo;
import com.dwmedios.uniconekt.model.Estados;
import com.dwmedios.uniconekt.model.Paises;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.presenter.GetPaisesPresenter;
import com.dwmedios.uniconekt.presenter.RegistroUniversitarioPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario.MainUniversitarioActivity;
import com.dwmedios.uniconekt.view.activity.View_Utils.UploadImage;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_ConfirmPassword;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_Email_Valid;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_IsEmpty;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_MinLength_valid;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_Phone_valid;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_Spinner_Valid;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_required_field;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.RuleDw_base;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Rules_Dw;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField;
import com.dwmedios.uniconekt.view.viewmodel.GetPaisesViewController;
import com.dwmedios.uniconekt.view.viewmodel.RegistroUniversitarioViewController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario_v2.LoginActivity2.REGISTRO_FACEBOOK;
import static com.dwmedios.uniconekt.view.util.ImageUtils.setTintView;
import static com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField.EMAIL_EQUIRED;
import static com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField.FIELD_REQUIRED;
import static com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField.PHONE_EQUIRED;
import static com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField.setFocusableView;

public class RegistroUniversitarioActivity extends BaseActivity implements GetPaisesViewController, RegistroUniversitarioViewController {
    private GetPaisesPresenter mGetPaisesPresenter;
    private RegistroUniversitarioPresenter mRegistroUniversitarioPresenter;
    @BindView(R.id.edittextNombreUniversitario)
    public TextInputEditText mTextInputEditTextNombre;
    @BindView(R.id.editTextTelefonoUniversitario)
    public TextInputEditText mTextInputEditTextTelefono;
    @BindView(R.id.spinnerPaises)
    public Spinner mSpinner;
    @BindView(R.id.editTextCodigoUniversitario)
    public TextInputEditText mTextInputEditTextCodigo;
    @BindView(R.id.edittextEstadoUniversitario)
    public TextInputEditText mTextInputEditTextEstado;
    @BindView(R.id.editTextMunicipioUniversitario)
    public TextInputEditText mTextInputEditTextMunicipio;
    @BindView(R.id.editTextCiudadUniversitario)
    public TextInputEditText mTextInputEditTextCiudad;
    @BindView(R.id.editTextDireccionUniversitario)
    public TextInputEditText mTextInputEditTextDireccion;
    @BindView(R.id.edittextCorreoUniversitario)
    public TextInputEditText mTextInputEditTextCorreo;
    @BindView(R.id.edittextContraseñaUniversitario)
    public TextInputEditText mTextInputEditTextContraseña;
    @BindView(R.id.edittextConfirmacionUniversitario)
    public TextInputEditText mTextInputEditTextConfirmacion;
    @BindView(R.id.buttonRegistrousuario)
    public Button mButton;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.profile_Usuario)
    ImageView mImageView;
    @BindView(R.id.fabPerfilUniverstario)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.checkbox_terminos)
    CheckBox mCheckBox;
    @BindView(R.id.texviewTerminos)
    TextView mTextViewTerminos;
    private ValidateField mValidateField;
    private List<Rules_Dw> mRules_dwList;
    private String patchPhoto = null;
    //private String paisSeleccionado = null;
    private String cv_Facebook = null;
    private boolean continueRegister = false;
    private UploadImage mUploadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_universitario);
        ButterKnife.bind(this);
        mToolbar.setTitle("Registro - Estudiante");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mGetPaisesPresenter = new GetPaisesPresenter(this, getApplicationContext());
        mRegistroUniversitarioPresenter = new RegistroUniversitarioPresenter(getApplicationContext(), this);
        loadValidations();
        mUploadImage = new UploadImage(RegistroUniversitarioActivity.this, mResultInfo);

        mGetPaisesPresenter.getPaises();
        mRegistroUniversitarioPresenter.getTerminos();
    }

    UploadImage.resultInfo mResultInfo = new UploadImage.resultInfo() {
        @Override
        public void Onsucces(String patch, String mensaje) {
            patchPhoto = patch;
            showMessage(mensaje);

        }

        @Override
        public void Onfailed(String mensaje) {
            mImageView.setImageResource(R.drawable.profile);
            showMessage(mensaje);
        }

        @Override
        public void Onloading(boolean isLoading) {
            if (isLoading)
                showOnProgressDialog("Subiendo fotografía...", RegistroUniversitarioActivity.this);
            else
                dismissProgressDialog();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onSucces(List<Paises> mPaisesList) {
        List<String> mStringListTemnp = new ArrayList<>();
        mStringListTemnp.add("--Seleccionar país de origen.--");
        int position = 0;
        for (int i = 0; i < mPaisesList.size(); i++) {
            /*if (mDireccion != null) {
                if (mPaisesList.get(i).nombre.equals(mDireccion.pais)) position = (i + 1);
            }*/
            mStringListTemnp.add(mPaisesList.get(i).nombre);
        }
        ArrayAdapter<String> mStringArrayAdapter = new ArrayAdapter<String>(this, R.layout.row_spinner_paises, mStringListTemnp);
        mSpinner.setAdapter(mStringArrayAdapter);
        mSpinner.setSelection(0);
      /*  if (position != 0) {
            mSpinnerPais.setSelection(position);
            mTextInputEditTextCodigo.setText(mDireccion.codigo_postal);
            mTextInputEditTextCalle.setText(mDireccion.descripcion);
        }*/
    }

    public void RegistroFacebook() {
        try {
            Usuario mUsuario = getIntent().getExtras().getParcelable(REGISTRO_FACEBOOK);
            if (mUsuario != null) {
                cv_Facebook = mUsuario.cv_facebook;
                new taskImage().execute();
                if (mUsuario.nombre != null) {
                    mTextInputEditTextCorreo.setText(mUsuario.nombre);
                    mTextInputEditTextCorreo.setEnabled(false);

                }
                if (mUsuario.persona != null) {
                    if (mUsuario.persona.nombre != null) {
                        mTextInputEditTextNombre.setText(mUsuario.persona.nombre);
                        //mTextInputEditTextNombre.setEnabled(false);
                    }
                    if (mUsuario.persona.telefono != null) {
                        mTextInputEditTextTelefono.setText(mUsuario.persona.telefono);
                        mTextInputEditTextTelefono.setEnabled(false);
                    }
                }
                View mView = (View) mTextInputEditTextContraseña.getParent();
                mView.setVisibility(View.GONE);
                mTextInputEditTextContraseña.setVisibility(View.GONE);
                mTextInputEditTextConfirmacion.setVisibility(View.GONE);
                View mView2 = (View) mTextInputEditTextConfirmacion.getParent();
                mView2.setVisibility(View.GONE);
            }
        } catch (Exception ex) {

        }
    }

    public void loadValidations() {
        /**
         * Acciones de los botones
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTintView(getApplicationContext(), mTextInputEditTextNombre, R.color.colorGris, R.drawable.ic_action_ic_user);
            setTintView(getApplicationContext(), mTextInputEditTextTelefono, R.color.colorGris, R.drawable.ic_telefono);
            setTintView(getApplicationContext(), mTextInputEditTextDireccion, R.color.colorGris, R.drawable.ic_action_ciudad);
            setTintView(getApplicationContext(), mTextInputEditTextCorreo, R.color.colorGris, R.drawable.ic_action_mail);
            setTintView(getApplicationContext(), mTextInputEditTextContraseña, R.color.colorGris, R.drawable.ic_action_ic_contrasea);
            setTintView(getApplicationContext(), mTextInputEditTextConfirmacion, R.color.colorGris, R.drawable.ic_action_ic_contrasea);
            setTintView(getApplicationContext(), mTextInputEditTextCodigo, R.color.colorGris, R.drawable.ic_action_ic_school);
            setTintView(getApplicationContext(), mTextInputEditTextEstado, R.color.colorGris, R.drawable.ic_action_estado);
            setTintView(getApplicationContext(), mTextInputEditTextMunicipio, R.color.colorGris, R.drawable.ic_action_ciudad);
            setTintView(getApplicationContext(), mTextInputEditTextCiudad, R.color.colorGris, R.drawable.ic_action_ciudad);
        }
        mButton.setOnClickListener(mOnClickListener);
        mFloatingActionButton.setOnClickListener(mOnClickListener);
        mSpinner.setOnItemSelectedListener(mOnItemSelectedListener);
        mTextInputEditTextCodigo.addTextChangedListener(mTextWatcher);
        mTextInputEditTextCodigo.setOnEditorActionListener(mOnEditorActionListener);
        mCheckBox.setOnCheckedChangeListener(mOnCheckedChangeListener);
        RegistroFacebook();
        changeVisivility(false);
        /**
         * Obtener el imei del dispositivo
         */
        getImei();
        /**
         * Creacion de criterios de validaciones
         */
        RuleDw_base campo_requerido = new Dw_IsEmpty(FIELD_REQUIRED);
        RuleDw_base Email = new Dw_Email_Valid(EMAIL_EQUIRED);
        RuleDw_base telefono = new Dw_Phone_valid(PHONE_EQUIRED);
        RuleDw_base spinner_valid = new Dw_Spinner_Valid(mSpinner, "Seleccione su país de origen");
        RuleDw_base longitudCaracteresPass = new Dw_MinLength_valid("La contraseña debe tener como mínimo 8 caracteres de longitud.", 8);
        RuleDw_base password = new Dw_ConfirmPassword(mTextInputEditTextContraseña, "La contraseña y la confirmación no coinciden.");
        RuleDw_base check = new Dw_required_field(mCheckBox, "Debe estar de acuerdo con los términos.");

        /**
         * Agregadon controles a validar
         */
        mRules_dwList = new ArrayList<>();
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextNombre, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextTelefono, telefono));
        mRules_dwList.add(new Rules_Dw(mSpinner, spinner_valid));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextCodigo, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextEstado, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextMunicipio, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextCiudad, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextCorreo, Email));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextContraseña, longitudCaracteresPass));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextContraseña, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextConfirmacion, longitudCaracteresPass));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextConfirmacion, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextConfirmacion, password));
        mRules_dwList.add(new Rules_Dw(mCheckBox, check));
        mValidateField = new ValidateField(mRules_dwList, mErrorItem);

    }

    CheckBox.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (mCheckBox.isChecked()) {
                mCheckBox.setError(null);
            }
        }
    };
    ValidateField.errorItem mErrorItem = new ValidateField.errorItem() {
        @Override
        public void submitResult(List<Rules_Dw> mRules_dws) {
            if (mRules_dws.get(0).mView != null) {
                if (mRules_dws.get(0).mView instanceof Spinner) {
                    showMessage(mRules_dws.get(0).mRuleDw.error);
                    setFocusableView(mRules_dws.get(0));
                } else {
                    setFocusableView(mRules_dws.get(0));
                }
            }
        }
    };
    TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                mTextInputEditTextDireccion.requestFocus();
                return true;
            }
            return false;
        }
    };

    public Usuario getInfoFromView() {
        Usuario mUsuario = new Usuario();
        mUsuario.nombre = mTextInputEditTextCorreo.getText().toString();
        mUsuario.contrasenia = mTextInputEditTextContraseña.getText().toString();
        mUsuario.cv_facebook = cv_Facebook;

        Persona mPersona = new Persona();
        mPersona.correo = mTextInputEditTextCorreo.getText().toString();
        mPersona.nombre = mTextInputEditTextNombre.getText().toString();
        mPersona.telefono = mTextInputEditTextTelefono.getText().toString();
        mPersona.foto = patchPhoto;

        List<Dispositivo> mDispositivosList = new ArrayList<>();
        Dispositivo mDispositivo = new Dispositivo();
        mDispositivo.clave_firebase = SharePrefManager.getInstance(getApplicationContext()).getToken();
        mDispositivo.clave_dispositivo = SharePrefManager.getInstance(getApplicationContext()).getImei();
        mDispositivosList.add(mDispositivo);


        Direccion mDireccion = new Direccion();
        mDireccion.descripcion = mTextInputEditTextDireccion.getText().toString();
        mDireccion.pais = mSpinner.getSelectedItem().toString();
        //showMessage(mSpinner.getSelectedItem().toString());
        mDireccion.estado = ((mTextInputEditTextEstado.getVisibility() == View.VISIBLE) ? mTextInputEditTextEstado.getText().toString() : null);
        mDireccion.municipio = ((mTextInputEditTextMunicipio.getVisibility() == View.VISIBLE) ? mTextInputEditTextMunicipio.getText().toString() : null);
        mDireccion.codigo_postal = (mTextInputEditTextCodigo.getVisibility() == View.VISIBLE) ? mTextInputEditTextCodigo.getText().toString() : null;
        mDireccion.ciudad = (mTextInputEditTextCiudad.getVisibility() == View.VISIBLE) ? mTextInputEditTextCiudad.getText().toString() : null;

        mPersona.direccion = mDireccion;

        mPersona.dispositivosList = mDispositivosList;

        mUsuario.persona = mPersona;

        return mUsuario;
    }

    AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selected = mSpinner.getSelectedItem().toString();
            if (selected != null) {
                if (selected.equals("México")) {
                    changeVisivility(true);
                } else {
                    changeVisivility(false);
                }
            } else {
                changeVisivility(false);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            changeVisivility(false);
        }
    };
    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mTextInputEditTextCodigo.getText().length() >= 5) {
                CodigoPostal mCodigoPostal = new CodigoPostal();
                mCodigoPostal.codigo_postal = mTextInputEditTextCodigo.getText().toString();
                mGetPaisesPresenter.SearCodigoPostal(mCodigoPostal);
            }
        }
    };

    private void changeVisivility(boolean visible) {
        try {

            int viewType = (visible ? View.VISIBLE : View.GONE);
            View cod = (TextInputLayout) mTextInputEditTextCodigo.getParent().getParent();
            View estado = (TextInputLayout) mTextInputEditTextEstado.getParent().getParent();
            View municipio = (TextInputLayout) mTextInputEditTextMunicipio.getParent().getParent();
            View ciudad = (TextInputLayout) mTextInputEditTextCiudad.getParent().getParent();
            mTextInputEditTextCodigo.setVisibility(viewType);
            mTextInputEditTextEstado.setVisibility(viewType);
            mTextInputEditTextMunicipio.setVisibility(viewType);
            mTextInputEditTextCiudad.setVisibility(viewType);
            cod.setVisibility(viewType);
            estado.setVisibility(viewType);
            municipio.setVisibility(viewType);
            ciudad.setVisibility(viewType);
            if (visible) mTextInputEditTextCodigo.requestFocus();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getImei() {
        if (SharePrefManager.getInstance(getApplicationContext()).getImei() == null) {
            if (validatePermison(Manifest.permission.READ_PHONE_STATE, RegistroUniversitarioActivity.this, 3)) {
                SharePrefManager.getInstance(getApplicationContext()).saveImei(getImei(getApplicationContext()));
            }
        }
    }

    public class taskImage extends AsyncTask<Void, Void, Void> {

        Bitmap bmp;

        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL("https://graph.facebook.com/" + cv_Facebook + "/picture?type=large");
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
                mImageView.setImageBitmap(bmp);
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mUploadImage.onRequestPermison(requestCode);
        switch (requestCode) {
            case 3: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SharePrefManager.getInstance(getApplicationContext()).saveImei(getImei(getApplicationContext()));
                    if (continueRegister) {
                        if (mValidateField.submit()) {
                            Usuario mUsuario = getInfoFromView();
                            mRegistroUniversitarioPresenter.CrearCuentaAcceso(mUsuario);
                        }
                    }
                    return;
                } else {

                    showInfoDialogListener("Atención", "Es necesario otorgar los permisos para continuar", "OK");
                    getImei();
                }
                return;
            }
        }
    }

    @Override
    public void OnsuccesRegistro(Usuario mUsuario) {
        if (mRegistroUniversitarioPresenter.SaveAllInfoUser(mUsuario)) {
            if (mUsuario.persona.mTipoPersonas.nombre.equals("UNIVERSITARIO"))
                SharePrefManager.getInstance(getApplicationContext()).saveTypeUser(1);
            else if (mUsuario.persona.mTipoPersonas.nombre.equals("UNIVERSIDAD"))
                SharePrefManager.getInstance(getApplicationContext()).saveTypeUser(2);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mUploadImage.OnActivityResult(requestCode, resultCode, data, mImageView);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void OnFailedRegistro(String mensaje) {
        showMessage(mensaje);
    }

    @Override
    public void OnLoadingRegistro(boolean isLoading) {
        if (isLoading) {
            showOnProgressDialog("Cargando...");
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void setTerminos(String contenido) {
        try {
            mTextViewTerminos.setText(Html.fromHtml(
                    "<a href='" + contenido + "'>Acepto términos y condiciones.</a>"));
            mTextViewTerminos.setClickable(true);
            mTextViewTerminos.setMovementMethod(LinkMovementMethod.getInstance());

        } catch (Exception ex) {

        }
    }

    @Override
    public void onFailed(String mensaje) {
        showMessage(mensaje);
    }

    @Override
    public void onSuccesEstado(List<Estados> mPaisesList) {

    }

    @Override
    public void onFailedEstado(String mensaje) {

    }

    @Override
    public void OnLoading(boolean isLoading) {
        if (isLoading) {
            showOnProgressDialog("Cargando...");
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void onSuccesCodigo(List<CodigoPostal> mCodigoPostals) {
        CodigoPostal mCodigoPostal = mCodigoPostals.get(0);
        if (mCodigoPostal != null) {
            // mTextInputEditTextCodigo.setText(mCodigoPostal.codigo_postal);
            mTextInputEditTextEstado.setText(mCodigoPostal.estado);
            mTextInputEditTextMunicipio.setText(mCodigoPostal.municipio);
            mTextInputEditTextCiudad.setText(mCodigoPostal.ciudad);
            mTextInputEditTextDireccion.requestFocus();
        } else {
            this.emptyControls();
        }
    }

    @Override
    public void emptyControls() {
        mTextInputEditTextCodigo.setText(null);
        mTextInputEditTextEstado.setText(null);
        mTextInputEditTextMunicipio.setText(null);
        mTextInputEditTextCiudad.setText(null);
    }

    @Override
    public void OnfailedCodigo(String mensaje) {
        showMessage(mensaje);
        this.emptyControls();
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonRegistrousuario:
                    if (mValidateField.submit()) {
                        Usuario mUsuario = getInfoFromView();
                        if (mUsuario.persona.dispositivosList.get(0).clave_dispositivo == null) {
                            continueRegister = true;
                            getImei();
                        } else {
                            mRegistroUniversitarioPresenter.CrearCuentaAcceso(mUsuario);
                        }
                    }
                    break;
                case R.id.fabPerfilUniverstario:
                    if (mUploadImage != null)
                        mUploadImage.pickImage();
                    break;
            }

        }
    };
}
