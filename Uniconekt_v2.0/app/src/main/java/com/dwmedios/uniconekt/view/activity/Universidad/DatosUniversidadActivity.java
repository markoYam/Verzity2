package com.dwmedios.uniconekt.view.activity.Universidad;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.CodigoPostal;
import com.dwmedios.uniconekt.model.Direccion;
import com.dwmedios.uniconekt.model.Paises;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.presenter.DatosUniversidadPresenter;
import com.dwmedios.uniconekt.presenter.DatosUsuarioPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario.MainUniversitarioActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.UtilsFtp.ftpClient;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_Email_Valid;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_IsEmpty;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_Phone_valid;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_Spinner_Valid;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Dw_webSite;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.RuleDw_base;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.Ruledw.Rules_Dw;
import com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField;
import com.dwmedios.uniconekt.view.viewmodel.DatosUniversidadViewController;
import com.dwmedios.uniconekt.view.viewmodel.DatosUsuarioViewController;
import com.google.android.gms.maps.model.LatLng;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.SplashActivity.TYPE_LOGIN_PAQUETES;
import static com.dwmedios.uniconekt.view.activity.Universidad.SearchUbicacionUniActivity.LAT;
import static com.dwmedios.uniconekt.view.activity.Universidad.SearchUbicacionUniActivity.LONG;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField.EMAIL_EQUIRED;
import static com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField.FIELD_REQUIRED;
import static com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField.PHONE_EQUIRED;
import static com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField.WEB_EQUIRED;
import static com.dwmedios.uniconekt.view.util.libraryValidate.Rules.ValidateField.setFocusableView;

public class DatosUniversidadActivity extends BaseActivity implements DatosUniversidadViewController,DatosUsuarioViewController {
    public static int TYPE_VIEW_UNIVERSITY = 0;
    @BindView(R.id.fabPerfilUniversidad)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.profile_Universidad)
    ImageView mImageViewProfile;
    @BindView(R.id.editTextNombreuniversidad)
    TextInputEditText mTextInputEditTextNombre;
    @BindView(R.id.edittextReprecentanteUniversidad)
    TextInputEditText nombreReprecentante;
    @BindView(R.id.edittextDescripcionUniversidad)
    TextInputEditText mTextInputEditTextDescripcion;
    @BindView(R.id.edittextsSitioWebUniversidad)
    AppCompatAutoCompleteTextView mTextInputEditTextSitio;
    @BindView(R.id.desTelefonoUniversidad)
    TextInputEditText mTextInputEditTextTelefono;
    @BindView(R.id.EdittextdesCorreoUniversidad)
    TextInputEditText mTextInputEditTextCorreo;
    @BindView(R.id.buttonSiguienteUniversidad)
    Button mButtonSiguiente;
    @BindView(R.id.buttonAnteriorUniversidad)
    Button mButtonAnterior;
    //------------------------------------------------
    // Direccion.
    @BindView(R.id.spinnerPaises)
    Spinner mSpinnerPais;
    @BindView(R.id.editTexCodigo)
    TextInputEditText mTextInputEditTextCodigo;
    @BindView(R.id.editTextEstado)
    TextInputEditText mTextInputEditTextEstado;
    @BindView(R.id.editTextMunicipio)
    TextInputEditText mTextInputEditTextMunicipio;
    @BindView(R.id.editTextCiudad)
    TextInputEditText mTextInputEditTextCiudad;
    @BindView(R.id.edittextCalle)
    TextInputEditText mTextInputEditTextCalle;
    @BindView(R.id.layoutGeneral)
    LinearLayout mLinearLayoutGeneral;
    @BindView(R.id.layoutDireccion)
    LinearLayout mLinearLayoutDireccion;
    @BindView(R.id.seccionVistaUniversidad)
    TextView mTextViewTitulo;
    public int LAYOUT_ACTIVE = 0;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.layoutButtons)
    LinearLayout mLinearLayoutButtons;
    @BindView(R.id.ImageViewverUbicacion)
    ImageView mImageButton;
    @BindView(R.id.textViewUbicacion)
    TextView mTextViewUbicacion;
    ValidateField mValidateField;
    private List<Rules_Dw> mRules_dwList;
    private DatosUniversidadPresenter mDatosUniversidadPresenter;
    private DatosUsuarioPresenter mDatosUsuarioPresenter;
    private List<Paises> mPaisesList;
    public boolean paisLocal = false;
    private String pais_seleccionado;
    private boolean FIRST_ACCESS = true;
    private String latitude = null;
    private String longitude = null;
    private boolean isFIRST_ACCESS = false;
    private ftpClient mFtpClient;
    private String patchImage = null;
    private List<CodigoPostal> mCodigoPostals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_universidad);
        ButterKnife.bind(this);
        mDatosUsuarioPresenter = new DatosUsuarioPresenter(this, getApplicationContext());
        mDatosUniversidadPresenter = new DatosUniversidadPresenter(getApplicationContext(), this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Registro");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFloatingActionButton.setOnClickListener(mOnClickListener);
        mButtonSiguiente.setOnClickListener(mOnClickListener);
        mTextInputEditTextCodigo.addTextChangedListener(mTextWatcher);
        mSpinnerPais.setOnItemSelectedListener(mOnItemSelectedListener);
        mButtonAnterior.setOnClickListener(mOnClickListener);
        mTextInputEditTextCodigo.setOnEditorActionListener(mOnEditorActionListener);
        mImageButton.setOnClickListener(mOnClickListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isFIRST_ACCESS) {
            this.ConfigureLoad();
            this.LoadInfofromData();
            isFIRST_ACCESS = true;
        }
        if (GO_TO_MAP) abrirMapa();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i > 0) {
                if (mPaisesList != null) {
                    if (mPaisesList.get((i - 1)).nombre.equals("México")) {
                        OnchageVisivility(true);
                        paisLocal = true;
                        pais_seleccionado = mPaisesList.get((i - 1)).nombre;
                        mTextInputEditTextCodigo.requestFocus();
                    } else {
                        OnchageVisivility(false);
                        paisLocal = false;
                        pais_seleccionado = mPaisesList.get((i - 1)).nombre;
                        mTextInputEditTextDescripcion.requestFocus();
                    }
                } else {
                    OnchageVisivility(false);
                    paisLocal = false;
                    mTextInputEditTextDescripcion.requestFocus();
                }
            } else {
                OnchageVisivility(false);
                paisLocal = false;
                mTextInputEditTextDescripcion.requestFocus();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            OnchageVisivility(true);
        }
    };

    @Override
    public void ConfigureLoad() {
        RuleDw_base campo_requerido = new Dw_IsEmpty(FIELD_REQUIRED);
        RuleDw_base Email = new Dw_Email_Valid(EMAIL_EQUIRED);
        RuleDw_base telefono = new Dw_Phone_valid(PHONE_EQUIRED);
        RuleDw_base sitio_web = new Dw_webSite(WEB_EQUIRED + " Ejemplo https://www.ejemplo.com");
        RuleDw_base spinner_valid = new Dw_Spinner_Valid(mSpinnerPais, "Seleccione su país de origen");
        mRules_dwList = new ArrayList<>();
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextNombre, campo_requerido));
        mRules_dwList.add(new Rules_Dw(nombreReprecentante, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextDescripcion, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextSitio, sitio_web));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextTelefono, telefono));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextCorreo, Email));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextCodigo, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextEstado, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextMunicipio, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mTextInputEditTextCiudad, campo_requerido));
        mRules_dwList.add(new Rules_Dw(mSpinnerPais, spinner_valid));

        mValidateField = new ValidateField(mRules_dwList, mErrorItem);

        ArrayList<String> mStrings= new ArrayList<>();
        mStrings.add("https://");
        mStrings.add("http://");
        ArrayAdapter<String> mStringArrayAdapter= new ArrayAdapter<String>(getApplicationContext(),R.layout.row_spinner_paises,mStrings);
        mTextInputEditTextSitio.setThreshold(1);
        mTextInputEditTextSitio.setAdapter(mStringArrayAdapter);

        slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down_linear);

        slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up_linear);

        KeyboardVisibilityEvent.setEventListener(DatosUniversidadActivity.this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen) {
                            mLinearLayoutButtons.startAnimation(slide_down);
                            mLinearLayoutButtons.setVisibility(View.GONE);

                        } else {
                            mLinearLayoutButtons.startAnimation(slide_up);
                            mLinearLayoutButtons.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    Animation slide_down;
    Animation slide_up;
    private Direccion mDireccion;
    private Universidad mUniversidad;

    @Override
    public void LoadInfofromData() {

        mUniversidad = mDatosUniversidadPresenter.GetInfoUniversity();
        if (mUniversidad != null) {
            getSupportActionBar().setTitle("Perfil Universidad");
            mTextInputEditTextNombre.setText(mUniversidad.nombre);
            nombreReprecentante.setText(mUniversidad.reprecentante);
            if (mUniversidad.descripcion != null) {
                mTextInputEditTextDescripcion.setText(mUniversidad.descripcion);
            }
            if (mUniversidad.sitio != null)
                mTextInputEditTextSitio.setText(mUniversidad.sitio);
            if (mUniversidad.correo != null)
                mTextInputEditTextCorreo.setText(mUniversidad.correo);
            if (mUniversidad.telefono != null)
                mTextInputEditTextTelefono.setText(mUniversidad.telefono);
            if (mUniversidad.mDireccion != null) {
                mDireccion = mUniversidad.mDireccion;
                latitude = mUniversidad.mDireccion.latitud;
                longitude = mUniversidad.mDireccion.longitud;
                try {
                    if (!latitude.isEmpty() && !longitude.isEmpty()) {
                        Double la = Double.parseDouble(latitude.toString());
                        Double lo = Double.parseDouble(longitude.toString());
                        LatLng mLatLng = new LatLng(la, lo);
                        new taskDetalle(mLatLng).execute();
                    }
                } catch (Exception ex) {
                    mTextViewUbicacion.setText("No se encontró ubicación. ");
                    ex.printStackTrace();
                }

            } else {
                mTextViewUbicacion.setText("No se encontró ubicación. ");
            }
            if (mUniversidad.logo != null) {
                new taskImageP(getUrlImage(mUniversidad.logo, this)).execute();
                //ImageLoader.getInstance().displayImage(getUrlImage(mUniversidad.logo, this), mImageViewProfile, OptionsImageLoaderDark);
                this.patchImage = mUniversidad.logo;
            }  // TODO: 14/05/2018  hace falta la direccion

        }
    }

    public class taskImageP extends AsyncTask<Void, Void, Void> {

        Bitmap bmp;
        public String key;

        public taskImageP(String key) {
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(key);
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
    public Universidad getInfoFromview() {
        Universidad mUniversidad = mDatosUniversidadPresenter.GetInfoUniversity();
        if (mUniversidad != null) {
            mUniversidad.nombre = mTextInputEditTextNombre.getText().toString();
            mUniversidad.reprecentante = nombreReprecentante.getText().toString();
            mUniversidad.descripcion = mTextInputEditTextDescripcion.getText().toString();
            mUniversidad.sitio = mTextInputEditTextSitio.getText().toString();
            mUniversidad.correo = mTextInputEditTextCorreo.getText().toString();
            mUniversidad.telefono = mTextInputEditTextTelefono.getText().toString();
            // TODO: 21/06/2018  aqui poner el codigo de las fotos del usuario
            mUniversidad.logo = patchImage;
            // TODO: 14/05/2018  hace falta la direccion
            Direccion temp = getDireccion();
            if (temp != null) {
                mUniversidad.mDireccion = temp;
            } else {
                showMessage("Hace falta datos de direccion");
            }

        }
        return mUniversidad;
    }

    @Override
    public void Onsucces(Universidad mUniversidad) {

        showMessage("Operación realizada con éxito");
     /*   if (mDatosUniversidadPresenter.updateUniversidad(mUniversidad)) {
            this.LoadInfofromData();
        }*/
        mDatosUniversidadPresenter.updateUniversidad(mUniversidad);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, 1000);

       /* if (TYPE_LOGIN_PAQUETES == 1) {
            TYPE_LOGIN_PAQUETES = 0;
            showdialogMaterial("Atención", "Ir al menu principal", R.drawable.ic_action_information, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }*/

    }

    TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_NEXT) {
                mTextInputEditTextCalle.requestFocus();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onDestroy() {
        TYPE_LOGIN_PAQUETES = 0;
        super.onDestroy();
    }

    @Override
    public void Onfailed(String mensaje) {
        showMessage(mensaje);
    }
private boolean isCodigo=false;
    @Override
    public void OnFailed(String mensaje) {

        this.mPaisesList = null;
        List<String> mStringListTemnp = new ArrayList<>();
        mStringListTemnp.add("No se encontraron elementos.");
        ArrayAdapter<String> mStringArrayAdapter = new ArrayAdapter<String>(this, R.layout.row_spinner_paises, mStringListTemnp);
        mSpinnerPais.setAdapter(mStringArrayAdapter);
        mSpinnerPais.setSelection(0);

    }

    @Override
    public void Onloading(boolean loading) {
        if (loading) {
            showOnProgressDialog("Cargando...");
        } else {
            dismissProgressDialog();
        }
    }

    @Override
    public void EmptyRecyclerView() {

    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.fabPerfilUniversidad:
                    mFtpClient = new ftpClient(getApplicationContext(), DatosUniversidadActivity.this);
                    mFtpClient.pickImage(new ftpClient.patchImageInterface() {
                        @Override
                        public void patch(String patch) {
                            // showMessage(patch);
                            patchImage = patch;
                        }
                    }, mImageViewProfile);
                    break;

                case R.id.buttonSiguienteUniversidad:
                    if (LAYOUT_ACTIVE == 0) {
                        mTextViewTitulo.setText("Dirección");
                        mLinearLayoutDireccion.startAnimation(slide_up);
                        mLinearLayoutGeneral.setVisibility(View.GONE);
                        mLinearLayoutDireccion.setVisibility(View.VISIBLE);
                        mButtonAnterior.setVisibility(View.VISIBLE);
                        LAYOUT_ACTIVE = 1;
                        if (FIRST_ACCESS) {
                            FIRST_ACCESS = false;
                            mDatosUsuarioPresenter.Getpaises();
                        }
                        mButtonSiguiente.setText("Guardar Cambios");
                    } else {
                        mButtonSiguiente.setText("Guardar Cambios");
                        if (mValidateField.submit()) {
                            if (latitude == null || longitude == null) {
                                showMessage("Seleccione la ubicación de la universidad");
                                break;
                            }
                            mDatosUniversidadPresenter.RegistrarUniversidad(getInfoFromview());
                        }
                    }


                    break;
                case R.id.buttonAnteriorUniversidad:
                    if (LAYOUT_ACTIVE == 1) {
                        mTextViewTitulo.setText("Datos generales");
                        mLinearLayoutGeneral.startAnimation(slide_up);
                        mLinearLayoutGeneral.setVisibility(View.VISIBLE);
                        mLinearLayoutDireccion.setVisibility(View.GONE);
                        LAYOUT_ACTIVE = 0;
                        mButtonSiguiente.setText("Siguiente");
                        mButtonAnterior.setVisibility(View.INVISIBLE);
                    }
                    break;
                case R.id.ImageViewverUbicacion:
                    abrirMapa();
                    break;
            }
        }
    };
    ValidateField.errorItem mErrorItem= new ValidateField.errorItem() {
        @Override
        public void submitResult(List<Rules_Dw> mRules_dws) {
            if (mRules_dws.get(0).mView != null) {
                changeVisivility(mRules_dws.get(0).mView);
                if (mRules_dws.get(0).mView instanceof Spinner) {
                   showMessage(mRules_dws.get(0).mRuleDw.error);
                }else
                {
                    setFocusableView(mRules_dws.get(0));
                }
            }
        }
    };
    private void changeVisivility(View mView) {
        try {
            LinearLayout layout = (LinearLayout) mView.getParent().getParent().getParent().getParent();
            if (layout != null) {
                if (layout.getId() == R.id.layoutGeneral) {
                    mTextViewTitulo.setText("Datos generales");
                    mLinearLayoutGeneral.startAnimation(slide_up);
                    mLinearLayoutGeneral.setVisibility(View.VISIBLE);
                    mLinearLayoutDireccion.setVisibility(View.GONE);
                    LAYOUT_ACTIVE = 0;
                    mButtonSiguiente.setText("Siguiente");
                    mButtonAnterior.setVisibility(View.INVISIBLE);

                } else if (layout.getId() == R.id.layoutDireccion) {
                    mTextViewTitulo.setText("Dirección");
                    mLinearLayoutDireccion.startAnimation(slide_up);
                    mLinearLayoutGeneral.setVisibility(View.GONE);
                    mLinearLayoutDireccion.setVisibility(View.VISIBLE);
                    mButtonAnterior.setVisibility(View.VISIBLE);
                    LAYOUT_ACTIVE = 1;
                    if (FIRST_ACCESS) {
                        FIRST_ACCESS = false;
                        mDatosUsuarioPresenter.Getpaises();
                    }
                    mButtonSiguiente.setText("Guardar Cambios");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private int requestCode = 12;

    public void abrirMapa() {
        List<String> per = new ArrayList<>();
        per.add(Manifest.permission.ACCESS_FINE_LOCATION);
        per.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (validatePermison(per, this, 1)) {//falta el estatus

            LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            boolean gps = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean red = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!gps || !red) {
                GO_TO_MAP = true;
                AlertNoGps();
            } else {
                GO_TO_MAP = false;
                Intent mIntent = new Intent(getApplicationContext(), SearchUbicacionUniActivity.class);
                if (mUniversidad != null) {
                    if (mUniversidad.mDireccion != null) {
                        if (mUniversidad.mDireccion.latitud != null && mUniversidad.mDireccion.longitud != null)
                            if (!mUniversidad.mDireccion.latitud.isEmpty() && !mUniversidad.mDireccion.longitud.isEmpty()) {
                                mIntent.putExtra(LAT, mUniversidad.mDireccion.latitud);
                                mIntent.putExtra(LONG, mUniversidad.mDireccion.longitud);
                            }
                    }
                }
                startActivityForResult(mIntent, requestCode);
            }

        }
    }

    private boolean GO_TO_MAP = false;

    @Override
    public void onBackPressed() {
        if (LAYOUT_ACTIVE == 1) {
            mTextViewTitulo.setText("Datos generales");
            mLinearLayoutGeneral.startAnimation(slide_up);
            mLinearLayoutGeneral.setVisibility(View.VISIBLE);
            mLinearLayoutDireccion.setVisibility(View.GONE);
            LAYOUT_ACTIVE = 0;
            mButtonSiguiente.setText("Siguiente");
            mButtonAnterior.setVisibility(View.INVISIBLE);
        } else {
            TYPE_LOGIN_PAQUETES = 0;
            Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.requestCode) {
            if (resultCode == Activity.RESULT_OK) {

                try {
                    Double la = data.getDoubleExtra(LAT, 0.0);
                    Double lo = data.getDoubleExtra(LONG, 0.0);
                    latitude = la.toString();
                    longitude = lo.toString();
                    LatLng mLatLng = new LatLng(la, lo);
                    new taskDetalle(mLatLng).execute();

                } catch (Exception ex) {
                    mTextViewUbicacion.setText("No se encontró ubicación. ");
                    ex.printStackTrace();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //    latitude=null;
                // longitude=null;
            }
        }
        if (mFtpClient != null) mFtpClient.activityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mFtpClient != null)
            mFtpClient.permisonResult(requestCode, permissions, grantResults);
    }

    @Override
    public void OnsuccesCodigo(List<CodigoPostal> mCodigoPostalList) {
         this.mCodigoPostals = mCodigoPostalList;
        CodigoPostal mCodigoPostal = mCodigoPostalList.get(0);
        mTextInputEditTextCiudad.setText(mCodigoPostal.ciudad);
        mTextInputEditTextEstado.setText(mCodigoPostal.estado);
        mTextInputEditTextMunicipio.setText(mCodigoPostal.municipio);
    }

    @Override
    public void onfailesCodigo() {
        mCodigoPostals = null;
        mTextInputEditTextCiudad.setText("");
        mTextInputEditTextEstado.setText("");
        mTextInputEditTextMunicipio.setText("");

    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (mTextInputEditTextCodigo.length() > 4) {
                CodigoPostal mCodigoPostal = new CodigoPostal();
                mCodigoPostal.codigo_postal = mTextInputEditTextCodigo.getText().toString();
                mDatosUsuarioPresenter.SearCodigoPostal(mCodigoPostal);
            }
        }
    };

    @Override
    public void OnsuccesPais(List<Paises> mPaisesList) {
        this.mPaisesList = mPaisesList;
        List<String> mStringListTemnp = new ArrayList<>();
        mStringListTemnp.add("--Seleccionar país de origen.--");
        int position = 0;
        for (int i = 0; i < mPaisesList.size(); i++) {
            if (mDireccion != null) {
                if (mPaisesList.get(i).nombre.equals(mDireccion.pais)) position = (i + 1);
            }
            mStringListTemnp.add(mPaisesList.get(i).nombre);
        }
        ArrayAdapter<String> mStringArrayAdapter = new ArrayAdapter<String>(this, R.layout.row_spinner_paises, mStringListTemnp);
        mSpinnerPais.setAdapter(mStringArrayAdapter);
        mSpinnerPais.setSelection(0);
        if (position != 0) {
            mSpinnerPais.setSelection(position);
            mTextInputEditTextCodigo.setText(mDireccion.codigo_postal);
            mTextInputEditTextCalle.setText(mDireccion.descripcion);
        }
    }

    @Override
    public void OnchageVisivility(boolean visibility) {
        try {
            LinearLayout codigo = (LinearLayout) mTextInputEditTextCodigo.getParent().getParent().getParent();
            LinearLayout municipio = (LinearLayout) mTextInputEditTextMunicipio.getParent().getParent().getParent();
            LinearLayout ciudad = (LinearLayout) mTextInputEditTextCiudad.getParent().getParent().getParent();
            LinearLayout estado = (LinearLayout) mTextInputEditTextEstado.getParent().getParent().getParent();
            if (visibility) {
                codigo.setVisibility(View.VISIBLE);
                municipio.setVisibility(View.VISIBLE);
                ciudad.setVisibility(View.VISIBLE);
                estado.setVisibility(View.VISIBLE);
                mTextInputEditTextCodigo.setVisibility(View.VISIBLE);
                mTextInputEditTextMunicipio.setVisibility(View.VISIBLE);
                mTextInputEditTextCiudad.setVisibility(View.VISIBLE);
                mTextInputEditTextEstado.setVisibility(View.VISIBLE);
            } else {
                codigo.setVisibility(View.GONE);
                municipio.setVisibility(View.GONE);
                ciudad.setVisibility(View.GONE);
                estado.setVisibility(View.GONE);
                mTextInputEditTextCodigo.setVisibility(View.GONE);
                mTextInputEditTextMunicipio.setVisibility(View.GONE);
                mTextInputEditTextCiudad.setVisibility(View.GONE);
                mTextInputEditTextEstado.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Direccion getDireccion() {
        Direccion temp = new Direccion();
        Universidad uni = mDatosUniversidadPresenter.GetInfoUniversity();
        if (uni != null) {
            if (uni.mDireccion != null) {
                temp = uni.mDireccion;
                if (paisLocal) {
                    temp.pais = pais_seleccionado;
                    temp.descripcion = mTextInputEditTextCalle.getText().toString();
                    temp.municipio = mTextInputEditTextMunicipio.getText().toString();
                    temp.estado = mTextInputEditTextEstado.getText().toString();
                    temp.codigo_postal = mTextInputEditTextCodigo.getText().toString();
                    temp.ciudad = mTextInputEditTextCiudad.getText().toString();
                    temp.latitud = this.latitude;
                    temp.longitud = this.longitude;
                } else {
                    temp.pais = pais_seleccionado;
                    temp.descripcion = mTextInputEditTextCalle.getText().toString();
                    temp.municipio = "";
                    temp.estado = "";
                    temp.codigo_postal = "";
                    temp.ciudad = "";
                    temp.latitud = this.latitude;
                    temp.longitud = this.longitude;
                }
            } else {
                if (paisLocal) {
                    temp.pais = pais_seleccionado;
                    temp.descripcion = mTextInputEditTextCalle.getText().toString();
                    temp.municipio = mTextInputEditTextMunicipio.getText().toString();
                    temp.estado = mTextInputEditTextEstado.getText().toString();
                    temp.codigo_postal = mTextInputEditTextCodigo.getText().toString();
                    temp.ciudad = mTextInputEditTextCiudad.getText().toString();
                    temp.latitud = this.latitude;
                    temp.longitud = this.longitude;
                } else {
                    temp.pais = pais_seleccionado;
                    temp.descripcion = mTextInputEditTextCalle.getText().toString();
                    temp.municipio = "";
                    temp.estado = "";
                    temp.codigo_postal = "";
                    temp.ciudad = "";
                    temp.latitud = this.latitude;
                    temp.longitud = this.longitude;
                }
            }
        } else {
            if (paisLocal) {
                temp.pais = pais_seleccionado;
                temp.descripcion = mTextInputEditTextCalle.getText().toString();
                temp.municipio = mTextInputEditTextMunicipio.getText().toString();
                temp.estado = mTextInputEditTextEstado.getText().toString();
                temp.codigo_postal = mTextInputEditTextCodigo.getText().toString();
                temp.ciudad = mTextInputEditTextCiudad.getText().toString();
                temp.latitud = this.latitude;
                temp.longitud = this.longitude;
            } else {
                temp.pais = pais_seleccionado;
                temp.descripcion = mTextInputEditTextCalle.getText().toString();
                temp.municipio = "";
                temp.estado = "";
                temp.codigo_postal = "";
                temp.ciudad = "";
                temp.latitud = this.latitude;
                temp.longitud = this.longitude;
            }
        }
        return temp;
    }

    @Override
    public void returnData(Persona mPersona) {
        Persona temp = new Persona();
    }

    @Override
    public void OnsuccesEditar(Persona mPersona) {

    }

    @Override
    public void OnsuccesActualizar(Persona mPersona) {

    }

    @Override
    public void OnsuccesVerificar(Persona mPersona) {

    }

    @Override
    public void onfailedVerficar(String mensaje) {

    }

    @Override
    public void onfailedActualizar(String mensaje) {

    }

    public class taskDetalle extends AsyncTask<Void, Void, Void> {
        LatLng mLatLng = null;
        String detalle = null;

        public taskDetalle(LatLng mLatLng) {
            this.mLatLng = mLatLng;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mTextViewUbicacion.setText("Buscando dirección…");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (mLatLng != null)
                detalle = getDetalle(mLatLng.latitude, mLatLng.longitude);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (detalle != null && !detalle.isEmpty()) {
                mTextViewUbicacion.setText(detalle);

            } else {
                mTextViewUbicacion.setText("No se encontró la dirección.");
            }
        }
    }

    public String getDetalle(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String ciudad = "";
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                ciudad = addressList.get(0).getAddressLine(0);
            }
            Log.e("estado:", ciudad);
            Log.e("lat:", "" + latitude);
            Log.e("long:", "" + longitude);

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("CiudadE:", e.getMessage());
        }
        return ciudad;
    }
}
