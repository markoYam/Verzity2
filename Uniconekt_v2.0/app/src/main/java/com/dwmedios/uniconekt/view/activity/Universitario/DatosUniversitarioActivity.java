package com.dwmedios.uniconekt.view.activity.Universitario;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.CodigoPostal;
import com.dwmedios.uniconekt.model.Direccion;
import com.dwmedios.uniconekt.model.Dispositivo;
import com.dwmedios.uniconekt.model.Paises;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.presenter.DatosUsuarioPresenter;
import com.dwmedios.uniconekt.view.activity.View_Utils.Dialog_user;
import com.dwmedios.uniconekt.view.activity.View_Utils.UploadImage;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_Email_Valid;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_IsEmpty;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_Phone_valid;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_Spinner_Valid;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.RuleDw_base;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Rules_Dw;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField;
import com.dwmedios.uniconekt.view.viewmodel.DatosUsuarioViewController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.View_Utils.Dialog_user.KEY_CUENTA;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlFacebook;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField.EMAIL_EQUIRED;
import static com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField.FIELD_REQUIRED;
import static com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField.PHONE_EQUIRED;
import static com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField.setFocusableView;

public class DatosUniversitarioActivity extends BaseActivity implements DatosUsuarioViewController {
    public static final String KEY_REGISTRO_UNIVERSITARIO = "KEY_UNIVERSITARIO";
    public static final String REQUERIDO = "Campo requerido.";
    public static final String IS_UNIVERSIDAD = "UNIVERSIDAD_POSTULADO";
    public static final String LICE_SELECT = "Licenciatura_Seleccionada";
    public static final String KEY_RESTAURAR = "KEY_RESTAURAR";

    public static boolean KEY_VER_PERFIL_UNIVERSITARIO = false;
    public static boolean KEY_VER_PERFIL_REPRESENTANTE = false;
    @BindView(R.id.spinnerPaises)
    Spinner mSpinner;
    @BindView(R.id.editTexCodigo)
    TextInputEditText mEditTextCodigo;
    @BindView(R.id.editTextMunicipio)
    TextInputEditText mEditTextMunicipio;
    @BindView(R.id.editTextEstado)
    TextInputEditText mEditTextEstado;
    @BindView(R.id.edittextCalle)
    TextInputEditText mEditTextCalle;
    @BindView(R.id.editTextCiudad)
    TextInputEditText mEditTextCiudad;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.editTextNombre)
    TextInputEditText mEditTextNombre;
    @BindView(R.id.editTextTelefono)
    TextInputEditText mEditTextTelefono;
    @BindView(R.id.editTextCorreo)
    TextInputEditText mEditTextCorreo;
    @BindView(R.id.buttonRegistrousuario)
    Button mButtonContinuar;
    @BindView(R.id.profile_Usuario)
    ImageView mImageViewFoto;
    @BindView(R.id.fabPerfilUniverstario)
    FloatingActionButton mFloatingActionButton;
    private DatosUsuarioPresenter mDatosUsuarioPresenter;
    private List<Paises> mPaisesList;
    private List<CodigoPostal> mCodigoPostals;
    private boolean PAIS_LOCAL = false;
    private String pais_seleccionado = null;
    private ValidateField mValidateField;
    private List<Rules_Dw> mRules_dwList;
    private String patchFoto = null;
    private boolean isRestaurar = false;
    private boolean isCodigo = false;
    private UploadImage mUploadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_universitario);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Registro");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDatosUsuarioPresenter = new DatosUsuarioPresenter(this, getApplicationContext());
        mEditTextCodigo.addTextChangedListener(mTextWatcher);
        mEditTextCorreo.addTextChangedListener(mTextWatcherCorreo);
        mEditTextCodigo.setOnEditorActionListener(mOnEditorActionListener);
        mDatosUsuarioPresenter.Getpaises();
        mSpinner.setOnItemSelectedListener(mOnItemSelectedListener);

        mButtonContinuar.setOnClickListener(mOnClickListener);
        mUploadImage = new UploadImage(DatosUniversitarioActivity.this, mResultInfo);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //metodo para seleccionar imagen
                mUploadImage.pickImage();
            }
        });
        this.OnchageVisivility(false);
        ConfigureLoad();
    }

    UploadImage.resultInfo mResultInfo = new UploadImage.resultInfo() {
        @Override
        public void Onsucces(String patch,String mensaje) {
            patchFoto = patch;
            showMessage(mensaje);

        }

        @Override
        public void Onfailed(String mensaje) {
            showMessage(mensaje);
        }

        @Override
        public void Onloading(boolean isLoading) {
            if (isLoading)
                showOnProgressDialog("Subiendo fotografía...", DatosUniversitarioActivity.this);
            else
                dismissProgressDialog();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mUploadImage.onRequestPermison(requestCode);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mUploadImage.OnActivityResult(requestCode, resultCode, data, mImageViewFoto);
        if (requestCode == 120) {
            if (resultCode == Activity.RESULT_OK) {
                //gargar el perfil....
                String contenido = mEditTextCorreo.getText().toString();
                if (!contenido.isEmpty()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(contenido).matches()) {
                        Persona mPersona = new Persona();
                        mPersona.correo = contenido;
                        Dispositivo mDispositivo = infoDispositivo();
                        List<Dispositivo> mDispositivos = new ArrayList<>();
                        mDispositivos.add(mDispositivo);
                        mPersona.dispositivosList = mDispositivos;
                        isRestaurar = true;
                        mDatosUsuarioPresenter.ActualizarPerfil(mPersona);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void ConfigureLoad() {
        RuleDw_base campo_requerido = new Dw_IsEmpty(FIELD_REQUIRED);
        RuleDw_base Email = new Dw_Email_Valid(EMAIL_EQUIRED);
        RuleDw_base telefono = new Dw_Phone_valid(PHONE_EQUIRED);
        RuleDw_base spinner_valid = new Dw_Spinner_Valid(mSpinner, "Seleccione su país de origen");
        RuleDw_base spinner_valid2 = new Dw_Spinner_Valid(mSpinner, "  Seleccione licenciatura de interés");
        mRules_dwList = new ArrayList<>();
        mRules_dwList.add(new Rules_Dw(mEditTextNombre, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mEditTextCodigo, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mEditTextEstado, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mEditTextMunicipio, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mEditTextCiudad, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mEditTextTelefono, telefono));
        mRules_dwList.add(new Rules_Dw(mEditTextCorreo, Email));
        mRules_dwList.add(new Rules_Dw(mSpinner, spinner_valid));
        mValidateField = new ValidateField(mRules_dwList, mErrorItem);

        loadPerfilUser();


    }


    private Persona mPersonaPerfil;

    public void loadPerfilUser() {
        if (KEY_VER_PERFIL_UNIVERSITARIO) {
            getSupportActionBar().setTitle("Perfil universitario");
            mButtonContinuar.setText("Guardar Cambios");
            mPersonaPerfil = mDatosUsuarioPresenter.getInfo();
            Usuario mUsuario = mDatosUsuarioPresenter.getUsuario();
            if (!mPersonaPerfil.nombre.equals("temp")) {
                mEditTextNombre.setText(mPersonaPerfil.nombre);
                mEditTextCorreo.setText(mPersonaPerfil.correo);
                mEditTextTelefono.setText(mPersonaPerfil.telefono);
                if (mPersonaPerfil.foto != null) {
                    Glide
                            .with(DatosUniversitarioActivity.this)
                            .load(getUrlImage(mPersonaPerfil.foto, getApplicationContext()))
                            .listener(mRequestListener)
                            .into(mImageViewFoto);
                    patchFoto = mPersonaPerfil.foto;
                } else if (mUsuario != null && mUsuario.cv_facebook != null) {
                    Glide
                            .with(DatosUniversitarioActivity.this)
                            .load(getUrlFacebook(mUsuario.cv_facebook))
                            .listener(mRequestListener)
                            .into(mImageViewFoto);
                }

            }
        } else if (KEY_VER_PERFIL_REPRESENTANTE) {
            getSupportActionBar().setTitle("Perfil representante");
            mButtonContinuar.setText("Guardar Cambios");
            mPersonaPerfil = mDatosUsuarioPresenter.getInfo();
            Usuario mUsuario = mDatosUsuarioPresenter.getUsuario();
            OnchageVisivility(false);
            LinearLayout spines = (LinearLayout) mSpinner.getParent();
            LinearLayout direccion = (LinearLayout) mEditTextCalle.getParent().getParent().getParent();
            spines.setVisibility(View.GONE);
            direccion.setVisibility(View.GONE);
            mSpinner.setVisibility(View.GONE);
            mEditTextCalle.setVisibility(View.GONE);
            if (mPersonaPerfil != null) {
                if (mPersonaPerfil.foto != null) {
                    Glide
                            .with(DatosUniversitarioActivity.this)
                            .load(getUrlImage(mPersonaPerfil.foto, getApplicationContext()))
                            .listener(mRequestListener)
                            .into(mImageViewFoto);
                    patchFoto = mPersonaPerfil.foto;
                }
                if (mUsuario != null && mUsuario.cv_facebook != null) {
                    Glide
                            .with(DatosUniversitarioActivity.this)
                            .load(getUrlFacebook(mUsuario.cv_facebook))
                            .listener(mRequestListener)
                            .into(mImageViewFoto);
                }

                if (!mPersonaPerfil.nombre.equals("temp")) {
                    mEditTextNombre.setText(mPersonaPerfil.nombre);
                    mEditTextNombre.setEnabled(true);
                    mEditTextCorreo.setText(mPersonaPerfil.correo);
                    mEditTextTelefono.setText(mPersonaPerfil.telefono);
                }
            }
        } else {
            //  getLicenciaturas();
        }
    }

    RequestListener mRequestListener = new RequestListener() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
            mImageViewFoto.setImageResource(R.drawable.profile);
            return true;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
            BitmapDrawable mBitmapDrawable = (BitmapDrawable) resource;
            mImageViewFoto.setImageBitmap(mBitmapDrawable.getBitmap());
            return true;
        }
    };
    ValidateField.errorItem mErrorItem = new ValidateField.errorItem() {
        @Override
        public void submitResult(List<Rules_Dw> mRules_dws) {
            if (mRules_dws.get(0).mView != null) {
                if (mRules_dws.get(0).mView instanceof Spinner) {
                    showMessage(mRules_dws.get(0).mRuleDw.error);
                } else {
                    setFocusableView(mRules_dws.get(0));
                }
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                if (!KEY_VER_PERFIL_REPRESENTANTE && !KEY_VER_PERFIL_UNIVERSITARIO) {
                    showMessage("Operación cancelada.");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_NEXT) {
                mEditTextCalle.requestFocus();
                return true;
            }
            return false;
        }
    };
    AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i > 0) {
                if (mPaisesList != null) {
                    if (mPaisesList.get((i - 1)).nombre.equals("México")) {
                        PAIS_LOCAL = true;
                        OnchageVisivility(true);
                        mEditTextCodigo.requestFocus();
                        pais_seleccionado = mPaisesList.get((i - 1)).nombre;
                    } else {
                        PAIS_LOCAL = false;
                        OnchageVisivility(false);
                        pais_seleccionado = mPaisesList.get((i - 1)).nombre;
                        mEditTextCalle.requestFocus();
                    }
                } else {
                    pais_seleccionado = null;
                    PAIS_LOCAL = false;
                    OnchageVisivility(false);
                    mEditTextCalle.requestFocus();
                }
            } else {
                PAIS_LOCAL = false;
                pais_seleccionado = null;
                OnchageVisivility(false);
                mEditTextNombre.requestFocus();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            OnchageVisivility(false);
        }
    };


    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (mEditTextCodigo.length() > 4) {
                CodigoPostal mCodigoPostal = new CodigoPostal();
                mCodigoPostal.codigo_postal = mEditTextCodigo.getText().toString();
                isCodigo = true;
                mDatosUsuarioPresenter.SearCodigoPostal(mCodigoPostal);
            }
        }
    };
    /**
     * agregar para validar el correo.....
     */
    TextWatcher mTextWatcherCorreo = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (KEY_VER_PERFIL_UNIVERSITARIO || KEY_VER_PERFIL_REPRESENTANTE) {

            } else {
                String contenido = mEditTextCorreo.getText().toString();
                if (!contenido.isEmpty()) {
                    if (Patterns.EMAIL_ADDRESS.matcher(contenido).matches()) {
                        Persona mPersona = new Persona();
                        mPersona.correo = contenido;
                        Dispositivo mDispositivo = infoDispositivo();
                        List<Dispositivo> mDispositivos = new ArrayList<>();
                        mDispositivos.add(mDispositivo);
                        mPersona.dispositivosList = mDispositivos;
                        mDatosUsuarioPresenter.verificarPerfil(mPersona);
                    }
                }
            }
        }
    };

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

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mValidateField.submit()) {
                if (KEY_VER_PERFIL_UNIVERSITARIO || KEY_VER_PERFIL_REPRESENTANTE) {
                    //regresar al menu principal
                    mDatosUsuarioPresenter.EditarPerfil(returnData());
                } else {
                    mDatosUsuarioPresenter.ReturnInfo();
                }
            }
        }
    };

    @Override
    public void OnsuccesCodigo(List<CodigoPostal> mCodigoPostalList) {
        this.mCodigoPostals = mCodigoPostalList;
        CodigoPostal mCodigoPostal = mCodigoPostalList.get(0);
        mEditTextCiudad.setText(mCodigoPostal.ciudad);
        mEditTextEstado.setText(mCodigoPostal.estado);
        mEditTextMunicipio.setText(mCodigoPostal.municipio);
    }

    @Override
    public void onfailesCodigo() {
        mCodigoPostals = null;
        mEditTextCiudad.setText("");
        mEditTextEstado.setText("");
        mEditTextMunicipio.setText("");

    }

    @Override
    public void OnsuccesPais(List<Paises> mPaisesList) {
        this.mPaisesList = mPaisesList;
        List<String> mStringListTemnp = new ArrayList<>();
        mStringListTemnp.add("--Seleccionar país de origen.--");
        for (Paises mPaises : mPaisesList) {
            mStringListTemnp.add(mPaises.nombre);
        }
        ArrayAdapter<String> mStringArrayAdapter = new ArrayAdapter<String>(this, R.layout.row_spinner_paises, mStringListTemnp);
        mStringArrayAdapter.setDropDownViewResource(R.layout.row_spinner_paises);
        mSpinner.setAdapter(mStringArrayAdapter);
        int postionPais_selected = 0;
        if (KEY_VER_PERFIL_UNIVERSITARIO || KEY_VER_PERFIL_REPRESENTANTE) {
            if (mPersonaPerfil != null) {
                if (mPersonaPerfil.direccion != null) {
                    for (int i = 0; i < mPaisesList.size(); i++) {
                        if (mPersonaPerfil.direccion.pais != null)
                            if (mPersonaPerfil.direccion.pais.equals(mPaisesList.get(i).nombre)) {
                                postionPais_selected = i;
                                break;
                            }
                    }
                    mSpinner.setSelection((postionPais_selected + 1));
                    if (mPersonaPerfil.direccion.codigo_postal != null) {
                        mEditTextCodigo.setText(mPersonaPerfil.direccion.codigo_postal);
                    }
                    if (mPersonaPerfil.direccion.descripcion != null) {
                        mEditTextCalle.setText(mPersonaPerfil.direccion.descripcion);
                    }
                }
            }
        } else {
            mSpinner.setSelection(0);
        }
    }

    @Override
    public void OnchageVisivility(boolean visibility) {
        LinearLayout codigo = (LinearLayout) mEditTextCodigo.getParent().getParent().getParent();
        LinearLayout municipio = (LinearLayout) mEditTextMunicipio.getParent().getParent().getParent();
        LinearLayout ciudad = (LinearLayout) mEditTextCiudad.getParent().getParent().getParent();
        LinearLayout estado = (LinearLayout) mEditTextEstado.getParent().getParent().getParent();
        if (visibility) {
            codigo.setVisibility(View.VISIBLE);
            municipio.setVisibility(View.VISIBLE);
            ciudad.setVisibility(View.VISIBLE);
            estado.setVisibility(View.VISIBLE);
            mEditTextCodigo.setVisibility(View.VISIBLE);
            mEditTextMunicipio.setVisibility(View.VISIBLE);
            mEditTextCiudad.setVisibility(View.VISIBLE);
            mEditTextEstado.setVisibility(View.VISIBLE);
        } else {
            codigo.setVisibility(View.GONE);
            municipio.setVisibility(View.GONE);
            ciudad.setVisibility(View.GONE);
            estado.setVisibility(View.GONE);
            mEditTextCodigo.setVisibility(View.GONE);
            mEditTextMunicipio.setVisibility(View.GONE);
            mEditTextCiudad.setVisibility(View.GONE);
            mEditTextEstado.setVisibility(View.GONE);
        }
    }

    @Override
    public void returnData(Persona mPersona) {
        mPersona.nombre = mEditTextNombre.getText().toString();
        mPersona.correo = mEditTextCorreo.getText().toString();
        mPersona.telefono = mEditTextTelefono.getText().toString();
        mPersona.foto = patchFoto;
        Direccion mDireccion = new Direccion();
        if (PAIS_LOCAL) {
            mDireccion.ciudad = mEditTextCiudad.getText().toString();
            mDireccion.codigo_postal = mEditTextCodigo.getText().toString();
            mDireccion.estado = mEditTextEstado.getText().toString();
            mDireccion.municipio = mEditTextMunicipio.getText().toString();
            mDireccion.descripcion = mEditTextCalle.getText().toString();
        }
        mDireccion.pais = (PAIS_LOCAL ? "México" : (PAIS_LOCAL && pais_seleccionado != null ? "México" : pais_seleccionado));
        mPersona.direccion = mDireccion;

        Intent mIntent = getIntent();
        mIntent.putExtra(KEY_REGISTRO_UNIVERSITARIO, mPersona);
        mIntent.putExtra(KEY_RESTAURAR, (isRestaurar ? 1 : 0));
        //mIntent.putExtra(LICE_SELECT, id_licenciaturaInteres);
        setResult(RESULT_OK, mIntent);
        finish();
    }

    public Persona returnData() {
        mPersonaPerfil = mDatosUsuarioPresenter.getInfo();
        if (mPersonaPerfil != null) {
            mPersonaPerfil.nombre = mEditTextNombre.getText().toString();
            mPersonaPerfil.correo = mEditTextCorreo.getText().toString();
            mPersonaPerfil.telefono = mEditTextTelefono.getText().toString();
            mPersonaPerfil.foto = patchFoto;
            Direccion mDireccion;
            if (mPersonaPerfil.direccion != null)
                mDireccion = mPersonaPerfil.direccion;
            else mDireccion = new Direccion();

            if (PAIS_LOCAL) {
                mDireccion.ciudad = mEditTextCiudad.getText().toString();
                mDireccion.codigo_postal = mEditTextCodigo.getText().toString();
                mDireccion.estado = mEditTextEstado.getText().toString();
                mDireccion.municipio = mEditTextMunicipio.getText().toString();
                mDireccion.descripcion = mEditTextCalle.getText().toString();
            } else {
                mDireccion.ciudad = "";
                mDireccion.codigo_postal = "";
                mDireccion.estado = "";
                mDireccion.municipio = "";
                mDireccion.descripcion = mEditTextCalle.getText().toString();
            }
            mDireccion.pais = (PAIS_LOCAL ? "México" : (PAIS_LOCAL && pais_seleccionado != null ? "México" : pais_seleccionado));
            mPersonaPerfil.direccion = mDireccion;
            return mPersonaPerfil;
        } else
            return null;
    }

    @Override
    public void OnsuccesEditar(Persona mPersona) {
        mDatosUsuarioPresenter.updateInfouser(mPersona);
        showMessage("Operación realizada con éxito");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, 1000);

    }

    @Override
    public void OnsuccesActualizar(Persona mPersona) {
        mDatosUsuarioPresenter.updateInfouser(mPersona);
        mDatosUsuarioPresenter.ReturnInfo();
        showMessage("Operación realiza con éxito.");
    }

    @Override
    public void OnsuccesVerificar(Persona mPersona) {
    /*    showMessage("tiene una cuenta activa");
        SimpleCustomDialog mSimpleCustomDialog= new SimpleCustomDialog(DatosUniversitarioActivity.this,getApplicationContext());
        mSimpleCustomDialog.setTitulo("Desea recuperar su cuenta..");
        mSimpleCustomDialog.setmImageViewProfile(mPersona.foto);
        mSimpleCustomDialog.setNombreusuario(mPersona.nombre);
        mSimpleCustomDialog.show();*/
        Intent mIntent = new Intent(getApplicationContext(), Dialog_user.class);
        mIntent.putExtra(KEY_CUENTA, mPersona);
        startActivityForResult(mIntent, 120);
    }

    @Override
    public void onfailedVerficar(String mensaje) {
        //  showMessage("no tiene una cuenta activa");
    }

    @Override
    public void onfailedActualizar(String mensaje) {
        showMessage("ocurrio un error al actualizar");
    }


    @Override
    public void OnFailed(String mensaje) {
        if (pais_seleccionado == null) {
            List<String> mStringListTemnp = new ArrayList<>();
            mStringListTemnp.add("No se encontraron elementos.");
            ArrayAdapter<String> mStringArrayAdapter = new ArrayAdapter<String>(this, R.layout.row_spinner_paises, mStringListTemnp);
            mSpinner.setAdapter(mStringArrayAdapter);
            mSpinner.setSelection(0);
        }
        showMessage(mensaje);
    }

    @Override
    public void Onloading(boolean loading) {
        if (loading)
            showOnProgressDialog("Procesando...");
        else
            dismissProgressDialog();
    }

    @Override
    public void EmptyRecyclerView() {

    }

    @Override
    public void onBackPressed() {
        if (!KEY_VER_PERFIL_REPRESENTANTE && !KEY_VER_PERFIL_UNIVERSITARIO) {
            showMessage("Operación cancelada.");
        }
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        KEY_VER_PERFIL_REPRESENTANTE = false;
        KEY_VER_PERFIL_UNIVERSITARIO = false;
        super.onDestroy();
    }
}
