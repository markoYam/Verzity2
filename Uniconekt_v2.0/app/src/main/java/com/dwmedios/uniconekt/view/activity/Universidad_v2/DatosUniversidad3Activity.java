package com.dwmedios.uniconekt.view.activity.Universidad_v2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.FrameLayout;
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
import com.dwmedios.uniconekt.model.Estados;
import com.dwmedios.uniconekt.model.Paises;
import com.dwmedios.uniconekt.model.Paquetes;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.VentasPaquetes;
import com.dwmedios.uniconekt.presenter.DatosUniversidadPresenter;
import com.dwmedios.uniconekt.presenter.GetPaisesPresenter;
import com.dwmedios.uniconekt.view.activity.Universidad.SearchUbicacionUniActivity;
import com.dwmedios.uniconekt.view.activity.Universitario.MainUniversitarioActivity;
import com.dwmedios.uniconekt.view.activity.View_Utils.UploadImage;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.fragments.base.BaseFragment;
import com.dwmedios.uniconekt.view.util.ImageUtils;
import com.dwmedios.uniconekt.view.viewmodel.DatosUniversidadViewController;
import com.dwmedios.uniconekt.view.viewmodel.GetPaisesViewController;
import com.google.android.gms.maps.model.LatLng;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universidad.SearchUbicacionUniActivity.LAT;
import static com.dwmedios.uniconekt.view.activity.Universidad.SearchUbicacionUniActivity.LONG;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class DatosUniversidad3Activity extends BaseActivity implements DatosUniversidadViewController {
    int lastPosition = 0;
    public static Universidad mUniversidad = null;
    public static List<Paises> mPaisesList = null;
    public static Paquetes mPaquetes = null;
    private fragmentDireccion mFragmentDireccion;
    private fragmentGeneral mFragmentGeneral;
    private fragmentOtros mFragmentOtros;
    Animation mAnimation;
    //region configuracion accion navigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (lastPosition != 0) {
                        lastPosition = 0;
                        getSupportActionBar().setTitle("Datos generales");
                        if (mFragmentGeneral == null) mFragmentGeneral = new fragmentGeneral();
                        setFragment(mFragmentGeneral);
                        return true;
                    } else
                        return true;
                case R.id.navigation_direccion:
                    if (lastPosition != 1) {
                        lastPosition = 1;
                        getSupportActionBar().setTitle("Dirección");
                        if (mFragmentDireccion == null)
                            mFragmentDireccion = new fragmentDireccion();
                        setFragment(mFragmentDireccion);
                        return true;
                    } else
                        return true;
                case R.id.navigation_otros:
                    if (lastPosition != 2) {
                        lastPosition = 2;
                        getSupportActionBar().setTitle("Links");
                        if (mFragmentOtros == null) mFragmentOtros = new fragmentOtros();
                        setFragment(mFragmentOtros);
                        return true;
                    } else
                        return true;
            }
            return false;
        }
    };

    private void setFragment(Fragment mFragment) {
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.slide_rigth_to_left_enter, R.anim.slide_rigth_to_left_exit);
        mFragmentTransaction.replace(R.id.contenido, mFragment);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();

        if (lastPosition == 0)
            getSupportActionBar().setTitle("Datos generales");
        if (lastPosition == 1)
            getSupportActionBar().setTitle("Dirección");
        if (lastPosition == 2)
            getSupportActionBar().setTitle("Links");

    }
//endregion

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        DatosUniversidad3Activity.mUniversidad = null;
        DatosUniversidad3Activity.mPaisesList = null;
        DatosUniversidad3Activity.mPaquetes = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_universidad3);
        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mToolbar.setTitle("Perfil Universidad");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        validarTeclado();
        mDatosUniversidadPresenter = new DatosUniversidadPresenter(getApplicationContext(), this);
        mFloatingActionButton.setOnClickListener(mOnClickListener);
        this.ConfigureLoad();


    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (guardarUniversidad()) {
                if (DatosUniversidad3Activity.mUniversidad != null) {
                    DatosUniversidad3Activity.mUniversidad.mVentasPaquetesList = null;
                    mDatosUniversidadPresenter.RegistrarUniversidad(DatosUniversidad3Activity.mUniversidad);
                } else {
                    showMessage("Ocurrió un error al actualizar la información de la universidad.");
                }
            }
        }
    };

    private void validarTeclado() {
        KeyboardVisibilityEvent.setEventListener(DatosUniversidad3Activity.this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        try {
                            if (isOpen) {
                                mFloatingActionButton.setVisibility(View.GONE);
                                navigation.setVisibility(View.GONE);

                            } else {
                                if (mAnimation == null)
                                    mAnimation = AnimationUtils.loadAnimation(DatosUniversidad3Activity.this, android.R.anim.fade_in);
                                navigation.startAnimation(mAnimation);
                                navigation.setVisibility(View.VISIBLE);
                                mFloatingActionButton.startAnimation(mAnimation);
                                mFloatingActionButton.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception ex) {
                            mFloatingActionButton.setVisibility(View.GONE);
                            navigation.setVisibility(View.GONE);
                        }

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), MainUniversitarioActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return true;
    }

    //region metodos interfaz
    @Override
    public void ConfigureLoad() {
        DatosUniversidad3Activity.mUniversidad = mDatosUniversidadPresenter.GetInfoUniversity();
        if (DatosUniversidad3Activity.mUniversidad != null) {
            if (DatosUniversidad3Activity.mUniversidad.mVentasPaquetesList != null && DatosUniversidad3Activity.mUniversidad.mVentasPaquetesList.size() > 0) {
                VentasPaquetes mVentasPaquetes = DatosUniversidad3Activity.mUniversidad.mVentasPaquetesList.get(0);
                if (mVentasPaquetes != null) {
                    if (mVentasPaquetes.mPaquetes != null) {
                        DatosUniversidad3Activity.mPaquetes = mVentasPaquetes.mPaquetes;
                    }
                }
            }
        }
        //Lanzar la primera vista
        if (mFragmentGeneral == null) mFragmentGeneral = new fragmentGeneral();
        setFragment(mFragmentGeneral);
    }

    @Override
    public void LoadInfofromData() {

    }

    @Override
    public Universidad getInfoFromview() {
        return null;
    }

    @Override
    public void Onsucces(Universidad mUniversidad) {
        showMessage("Operación realizada con éxito");
        mDatosUniversidadPresenter.updateUniversidad(mUniversidad);
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
    public void Onfailed(String mensaje) {
        showMessage(mensaje);
    }

    @Override
    public void Onloading(boolean loading) {
        if (loading) showOnProgressDialog("Cargando...");
        else
            dismissProgressDialog();
    }

    public static void setAutocompleteTextWeb(AutoCompleteTextView mAutocompleteTextWeb, Context mContext) {
        try {
            ArrayList<String> mStrings = new ArrayList<>();
            mStrings.add("https://");
            mStrings.add("http://");
            //ApaterAutocomplete mApaterAutocomplete = new ApaterAutocomplete(mContext, R.layout.row_spinner_paises);
            ArrayAdapter<String> mStringArrayAdapter = new ArrayAdapter<String>(mContext, R.layout.row_spinner_paises, mStrings);
            mAutocompleteTextWeb.setThreshold(1);
            mAutocompleteTextWeb.setAdapter(mStringArrayAdapter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //endregion

    public static final int requestCodeMap = 12;
    public static final int requestCodeImage = 13;
    public static boolean GO_TO_MAP = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (lastPosition == 0) {
                mFragmentGeneral.mUploadImage.OnActivityResult(requestCode, resultCode, data, mFragmentGeneral.mImageViewFoto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showMessage("Ocurrió un error durante la carga de la fotografía");
        }

        if (requestCode == requestCodeMap) {
            if (resultCode == Activity.RESULT_OK) {
                if (DatosUniversidad3Activity.mUniversidad != null && DatosUniversidad3Activity.mUniversidad.mDireccion != null) {
                    try {
                        Double la = data.getDoubleExtra(LAT, 0.0);
                        Double lo = data.getDoubleExtra(LONG, 0.0);
                        DatosUniversidad3Activity.mUniversidad.mDireccion.latitud = la.toString();
                        DatosUniversidad3Activity.mUniversidad.mDireccion.longitud = lo.toString();
                        // getSupportActionBar().setTitle("Dirección");
                        //  setFragment(new fragmentDireccion());
                        //  lastPosition = 1;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            if (lastPosition == 0)
                mFragmentGeneral.mUploadImage.onRequestPermison(requestCode);
        } catch (Exception ex) {
            ex.printStackTrace();
            showMessage("Ocurrió un error durante la carga de la fotografía");
        }
        if (requestCode == 1) {
            List<String> per = new ArrayList<>();
            per.add(Manifest.permission.ACCESS_FINE_LOCATION);
            per.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkPermison(per, this, 1)) {
                GO_TO_MAP = true;
                try {
                    if (lastPosition == 1) {
                        if (mFragmentDireccion != null)
                            mFragmentDireccion.abrirMapa();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showMessage("No fue posible visualizar la ubicación de la universidad.");
                }

            } else {
                showMessage("Es necesario otorgar los permisos para continuar");
            }
        }
    }

    //region creacion del fragment datos generales
    @SuppressLint("ValidFragment")
    public static class fragmentGeneral extends BaseFragment {

        public fragmentGeneral() {

        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View master = inflater.inflate(R.layout.layout_datos_universidad_general, container, false);
            ButterKnife.bind(this, master);
            if (DatosUniversidad3Activity.mUniversidad != null) {
                if (DatosUniversidad3Activity.mPaquetes != null) {
                    if (DatosUniversidad3Activity.mPaquetes.aplica_contacto) {
                        mSitio.setEnabled(true);
                        mTextInputEditTextTelefono.setEnabled(true);
                        mTextInputEditTextCorreo.setEnabled(true);
                    } else {
                        mSitio.setEnabled(false);
                        mTextInputEditTextTelefono.setEnabled(false);
                        mTextInputEditTextCorreo.setEnabled(false);
                    }
                    if (DatosUniversidad3Activity.mPaquetes.aplica_descripcion) {
                        mTextInputEditTextDescripcion.setEnabled(true);
                    } else {
                        mTextInputEditTextDescripcion.setEnabled(false);
                    }
                    if (DatosUniversidad3Activity.mPaquetes.aplica_logo) {
                        mFloatingActionButton.setEnabled(true);
                    } else {
                        mFloatingActionButton.setEnabled(false);
                        mFloatingActionButton.setImageResource(R.drawable.ic_prohibido_24);
                    }
                }
                Universidad mUniversidadLocal = DatosUniversidad3Activity.mUniversidad;
                if (!isNullOrEmpty(mUniversidadLocal.logo)) {
                    Glide.with(getActivity())
                            .load(ImageUtils.getUrlImage(mUniversidad.logo, getContext()))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    mImageViewFoto.setImageResource(R.drawable.profile);
                                    return true;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(mImageViewFoto);
                }
                if (!isNullOrEmpty(mUniversidadLocal.nombre)) {
                    mTextInputEditTextNombre.setText(mUniversidadLocal.nombre);
                }
                if (!isNullOrEmpty(mUniversidadLocal.descripcion)) {
                    mTextInputEditTextDescripcion.setText(mUniversidadLocal.descripcion);
                }
                if (!isNullOrEmpty(mUniversidadLocal.sitio)) {
                    mSitio.setText(mUniversidadLocal.sitio);
                }

                if (!isNullOrEmpty(mUniversidadLocal.telefono)) {
                    mTextInputEditTextTelefono.setText(mUniversidadLocal.telefono);
                }
                if (!isNullOrEmpty(mUniversidadLocal.correo)) {
                    mTextInputEditTextCorreo.setText(mUniversidadLocal.correo);
                }
            }
            mTextInputEditTextNombre.addTextChangedListener(mTextWatcher);
            mTextInputEditTextDescripcion.addTextChangedListener(mTextWatcher);
            mSitio.addTextChangedListener(mTextWatcher);
            mTextInputEditTextTelefono.addTextChangedListener(mTextWatcher);
            mTextInputEditTextCorreo.addTextChangedListener(mTextWatcher);
            setAutocompleteTextWeb(mSitio, getContext());
            mImageViewFoto.setOnClickListener(mOnClickListener);
            mUploadImage = new UploadImage(getActivity(), mResultInfo);
            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            return master;
        }


        @Override
        public void onStart() {
            super.onStart();
        }

        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DatosUniversidad3Activity.mPaquetes != null) {
                    if (DatosUniversidad3Activity.mPaquetes.aplica_logo) {
                        mUploadImage.pickImage();
                    } else {
                        //  mFloatingActionButton.setImageResource(R.drawable.ic_prohibido_24);
                    }

                }

            }
        };

        UploadImage.resultInfo mResultInfo = new UploadImage.resultInfo() {
            @Override
            public void Onsucces(String patch, String mensaje) {
                showMessage(mensaje);
                if (DatosUniversidad3Activity.mUniversidad != null)
                    DatosUniversidad3Activity.mUniversidad.logo = patch;
            }

            @Override
            public void Onfailed(String mensaje) {
                showMessage(mensaje);
            }

            @Override
            public void Onloading(boolean isLoading) {
                if (isLoading)
                    showOnProgressDialog("Subiendo fotografía...");
                else
                    dismissProgressDialog();
            }
        };
        TextWatcher mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (DatosUniversidad3Activity.mUniversidad != null) {
                    if (mTextInputEditTextNombre.isFocused()) {
                        DatosUniversidad3Activity.mUniversidad.nombre = mTextInputEditTextNombre.getText().toString();
                    }
                    if (mTextInputEditTextDescripcion.isFocused()) {
                        DatosUniversidad3Activity.mUniversidad.descripcion = mTextInputEditTextDescripcion.getText().toString();
                    }
                    if (mSitio.isFocused()) {
                        DatosUniversidad3Activity.mUniversidad.sitio = mSitio.getText().toString();
                    }
                    if (mTextInputEditTextTelefono.isFocused()) {
                        DatosUniversidad3Activity.mUniversidad.telefono = mTextInputEditTextTelefono.getText().toString();
                    }
                    if (mTextInputEditTextCorreo.isFocused()) {
                        DatosUniversidad3Activity.mUniversidad.correo = mTextInputEditTextCorreo.getText().toString();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };


        @BindView(R.id.profile_Universidad)
        ImageView mImageViewFoto;
        @BindView(R.id.fabPerfilUniversidad)
        FloatingActionButton mFloatingActionButton;
        @BindView(R.id.editTextNombreuniversidad)
        TextInputEditText mTextInputEditTextNombre;
        @BindView(R.id.edittextDescripcionUniversidad)
        TextInputEditText mTextInputEditTextDescripcion;
        @BindView(R.id.edittextsSitioWebUniversidad)
        AppCompatAutoCompleteTextView mSitio;
        @BindView(R.id.desTelefonoUniversidad)
        TextInputEditText mTextInputEditTextTelefono;
        @BindView(R.id.EdittextdesCorreoUniversidad)
        TextInputEditText mTextInputEditTextCorreo;
        private UploadImage mUploadImage;
    }

    public static class fragmentDireccion extends BaseFragment implements GetPaisesViewController {
        GetPaisesPresenter mGetPaisesPresenter;


        public fragmentDireccion() {
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View master = inflater.inflate(R.layout.layout_datos_universidad_direccion, container, false);
            ButterKnife.bind(this, master);
            mTextInputEditTextCodigo.addTextChangedListener(mTextWatcher);
            mTextInputEditTextCalle.addTextChangedListener(mTextWatcher);
            mSpinnerPais.setOnItemSelectedListener(mOnItemSelectedListener);
            mImageViewVerUbicacion.setOnClickListener(mOnClickListener);
            mTextInputEditTextCodigo.setOnEditorActionListener(mOnEditorActionListener);
            mGetPaisesPresenter = new GetPaisesPresenter(this, getContext());
            if (DatosUniversidad3Activity.mPaquetes != null) {
                if (DatosUniversidad3Activity.mPaquetes.aplica_direccion) {
                    mSpinnerPais.setEnabled(true);
                    mTextInputEditTextCalle.setEnabled(true);
                    mTextInputEditTextCodigo.setEnabled(true);
                } else {
                    mSpinnerPais.setEnabled(false);
                    mTextInputEditTextCalle.setEnabled(false);
                    mTextInputEditTextCodigo.setEnabled(false);
                }
                if (DatosUniversidad3Activity.mPaquetes.aplica_geolocalizacion) {
                    mImageViewVerUbicacion.setEnabled(true);
                } else {
                    mImageViewVerUbicacion.setEnabled(false);
                    mImageViewVerUbicacion.setImageResource(R.drawable.ic_prohibido_24);
                }
            }
            if (DatosUniversidad3Activity.mPaisesList != null && DatosUniversidad3Activity.mPaisesList.size() > 0)
                onSucces(DatosUniversidad3Activity.mPaisesList);
            else
                mGetPaisesPresenter.getPaises();
            return master;
        }

        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirMapa();
            }
        };

        @Override
        public void onStart() {
            super.onStart();
            validarInicio();
            if (GO_TO_MAP) abrirMapa();
        }

        private void validarInicio() {
            try {
                if (DatosUniversidad3Activity.mUniversidad != null && DatosUniversidad3Activity.mUniversidad.mDireccion != null) {
                    if (!DatosUniversidad3Activity.mUniversidad.mDireccion.latitud.isEmpty() && !DatosUniversidad3Activity.mUniversidad.mDireccion.longitud.isEmpty()) {
                        Double la = Double.parseDouble(DatosUniversidad3Activity.mUniversidad.mDireccion.latitud.toString());
                        Double lo = Double.parseDouble(DatosUniversidad3Activity.mUniversidad.mDireccion.longitud.toString());
                        LatLng mLatLng = new LatLng(la, lo);
                        new taskDetalle(mLatLng).execute();
                    } else mTextViewUbicacion.setText("No se encontró ubicación. ");
                } else
                    mTextViewUbicacion.setText("No se encontró ubicación. ");
            } catch (Exception ex) {
                mTextViewUbicacion.setText("No se encontró ubicación. ");
                ex.printStackTrace();
            }
        }

        public void abrirMapa() {
            List<String> per = new ArrayList<>();
            per.add(Manifest.permission.ACCESS_FINE_LOCATION);
            per.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {//falta el estatus

                LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                boolean gps = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean red = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (!gps || !red) {
                    GO_TO_MAP = true;
                    AlertNoGps(getActivity());
                } else {
                    GO_TO_MAP = false;
                    Intent mIntent = new Intent(getActivity(), SearchUbicacionUniActivity.class);
                    if (mUniversidad != null) {
                        if (mUniversidad.mDireccion != null) {
                            if (mUniversidad.mDireccion.latitud != null && mUniversidad.mDireccion.longitud != null)
                                if (!mUniversidad.mDireccion.latitud.isEmpty() && !mUniversidad.mDireccion.longitud.isEmpty()) {
                                    mIntent.putExtra(LAT, mUniversidad.mDireccion.latitud);
                                    mIntent.putExtra(LONG, mUniversidad.mDireccion.longitud);
                                }
                        }
                    }
                    getActivity().startActivityForResult(mIntent, requestCodeMap);
                }

            } else {
                String[] persmisos = new String[per.size()];
                for (int i = 0; i < per.size(); i++) {
                    persmisos[i] = per.get(i);
                }
                ActivityCompat.requestPermissions(getActivity(), persmisos, 1);
            }
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
        TextWatcher mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mTextInputEditTextCodigo.isFocused()) {
                    if (mTextInputEditTextCodigo.length() > 4) {
                        CodigoPostal mCodigoPostal = new CodigoPostal();
                        mCodigoPostal.codigo_postal = mTextInputEditTextCodigo.getText().toString();
                        mGetPaisesPresenter.SearCodigoPostal(mCodigoPostal);
                    }
                }
                if (mTextInputEditTextCalle.isFocused()) {
                    if (DatosUniversidad3Activity.mUniversidad != null) {
                        if (DatosUniversidad3Activity.mUniversidad.mDireccion != null) {
                            DatosUniversidad3Activity.mUniversidad.mDireccion.descripcion = mTextInputEditTextCalle.getText().toString();
                        } else {
                            DatosUniversidad3Activity.mUniversidad.mDireccion = new Direccion();
                            DatosUniversidad3Activity.mUniversidad.mDireccion.descripcion = mTextInputEditTextCalle.getText().toString();
                        }
                    }
                }
            }
        };
        AdapterView.OnItemSelectedListener mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position > 0) {
                        if (DatosUniversidad3Activity.mPaisesList != null && DatosUniversidad3Activity.mPaisesList
                                .size() > 0) {
                            if (mSpinnerPais.getSelectedItem().toString().equals("México")) {
                                changeVisivity(true);
                                mTextInputEditTextCodigo.requestFocus();
                                if (DatosUniversidad3Activity.mUniversidad != null) {
                                    if (DatosUniversidad3Activity.mUniversidad.mDireccion != null) {
                                        DatosUniversidad3Activity.mUniversidad.mDireccion.pais = DatosUniversidad3Activity.mPaisesList.get((position - 1)).nombre;
                                    } else {
                                        DatosUniversidad3Activity.mUniversidad.mDireccion = new Direccion();
                                        DatosUniversidad3Activity.mUniversidad.mDireccion.pais = DatosUniversidad3Activity.mPaisesList.get((position - 1)).nombre;
                                    }
                                }
                            } else {
                                changeVisivity(false);
                                if (DatosUniversidad3Activity.mUniversidad != null) {
                                    if (DatosUniversidad3Activity.mUniversidad.mDireccion != null) {
                                        DatosUniversidad3Activity.mUniversidad.mDireccion.pais = DatosUniversidad3Activity.mPaisesList.get((position - 1)).nombre;
                                        DatosUniversidad3Activity.mUniversidad.mDireccion.codigo_postal = null;
                                        DatosUniversidad3Activity.mUniversidad.mDireccion.estado = null;
                                        DatosUniversidad3Activity.mUniversidad.mDireccion.municipio = null;
                                        DatosUniversidad3Activity.mUniversidad.mDireccion.ciudad = null;
                                    } else {
                                        DatosUniversidad3Activity.mUniversidad.mDireccion = new Direccion();
                                        DatosUniversidad3Activity.mUniversidad.mDireccion.pais = DatosUniversidad3Activity.mPaisesList.get((position - 1)).nombre;

                                    }
                                }
                            }
                        } else
                            changeVisivity(false);
                    } else
                        changeVisivity(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                changeVisivity(false);
            }
        };

        private void changeVisivity(boolean visilble) {
            int vi = visilble ? View.VISIBLE : View.GONE;
            LinearLayout codigo = (LinearLayout) mTextInputEditTextCodigo.getParent().getParent().getParent();
            if (codigo != null) {
                codigo.setVisibility(vi);
            }
            LinearLayout estado = (LinearLayout) mTextInputEditTextEstado.getParent().getParent().getParent();
            if (estado != null) {
                estado.setVisibility(vi);
            }
            LinearLayout municipio = (LinearLayout) mTextInputEditTextMunicipio.getParent().getParent().getParent();
            if (municipio != null) {
                municipio.setVisibility(vi);
            }
            LinearLayout ciudad = (LinearLayout) mTextInputEditTextCiudad.getParent().getParent().getParent();
            if (ciudad != null) {
                ciudad.setVisibility(vi);
            }
        }


        //region metodos interfaz
        @Override
        public void onSucces(List<Paises> mPaisesList) {
            DatosUniversidad3Activity.mPaisesList = mPaisesList;
            List<String> mStringListTemnp = new ArrayList<>();
            mStringListTemnp.add("--Seleccionar país de origen.--");
            int position = 0;
            for (int i = 0; i < mPaisesList.size(); i++) {
                if (DatosUniversidad3Activity.mUniversidad != null) {
                    if (DatosUniversidad3Activity.mUniversidad.mDireccion != null) {
                        if (mPaisesList.get(i).nombre.equals(DatosUniversidad3Activity.mUniversidad.mDireccion.pais))
                            position = (i + 1);
                    }
                }
                mStringListTemnp.add(mPaisesList.get(i).nombre);
            }
            ArrayAdapter<String> mStringArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.row_spinner_paises, mStringListTemnp);
            mSpinnerPais.setAdapter(mStringArrayAdapter);
            mSpinnerPais.setSelection(0);
            if (position != 0) {
                mSpinnerPais.setSelection(position);
                if (DatosUniversidad3Activity.mUniversidad != null) {
                    if (DatosUniversidad3Activity.mUniversidad.mDireccion != null) {
                        if (DatosUniversidad3Activity.mUniversidad.mDireccion.pais.equals("México")) {
                            mTextInputEditTextCodigo.setText(DatosUniversidad3Activity.mUniversidad.mDireccion.codigo_postal);
                            LinearLayout codigo = (LinearLayout) mTextInputEditTextCodigo.getParent().getParent().getParent();
                            LinearLayout estado = (LinearLayout) mTextInputEditTextEstado.getParent().getParent().getParent();
                            LinearLayout municipio = (LinearLayout) mTextInputEditTextMunicipio.getParent().getParent().getParent();
                            LinearLayout ciudad = (LinearLayout) mTextInputEditTextCiudad.getParent().getParent().getParent();
                            codigo.setVisibility(View.VISIBLE);
                            municipio.setVisibility(View.VISIBLE);
                            estado.setVisibility(View.VISIBLE);
                            ciudad.setVisibility(View.VISIBLE);
                        }
                    }
                }


            }
            if (DatosUniversidad3Activity.mUniversidad != null) {
                if (DatosUniversidad3Activity.mUniversidad.mDireccion != null) {
                    mTextInputEditTextCalle.setText(DatosUniversidad3Activity.mUniversidad.mDireccion.descripcion);
                }
            }
        }

        @Override
        public void onFailed(String mensaje) {
            // showMessaje(mensaje);
            DatosUniversidad3Activity.mPaisesList = null;
            List<String> mStringListTemnp = new ArrayList<>();
            mStringListTemnp.add("No se encontraron elementos.");
            ArrayAdapter<String> mStringArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.row_spinner_paises, mStringListTemnp);
            mSpinnerPais.setAdapter(mStringArrayAdapter);
            mSpinnerPais.setSelection(0);
        }

        @Override
        public void onSuccesEstado(List<Estados> mPaisesList) {

        }

        @Override
        public void onFailedEstado(String mensaje) {
            showMessaje(mensaje);
        }

        @Override
        public void OnLoading(boolean isLoading) {
            if (isLoading) showOnProgressDialog("Cargando...");
            else dismissProgressDialog();
        }

        @Override
        public void onSuccesCodigo(List<CodigoPostal> mCodigoPostals) {
            CodigoPostal mCodigoPostal = mCodigoPostals.get(0);
            // TODO: 08/11/2018 Mostrar codigo postal en caso que tenga
            LinearLayout codigo = (LinearLayout) mTextInputEditTextCodigo.getParent().getParent().getParent();
            if (codigo != null) {
                //mTextInputEditTextCodigo.setText(mCodigoPostal.codigo_postal);
                codigo.setVisibility(View.VISIBLE);
            }

            // TODO: 08/11/2018 Mostrar Estado en caso que tenga
            if (!isNullOrEmpty(mCodigoPostal.estado)) {
                LinearLayout estado = (LinearLayout) mTextInputEditTextEstado.getParent().getParent().getParent();
                if (estado != null) {
                    mTextInputEditTextEstado.setText(mCodigoPostal.estado);
                    estado.setVisibility(View.VISIBLE);
                }
            }

            // TODO: 08/11/2018 Mostrar municipio en caso que tenga
            if (!isNullOrEmpty(mCodigoPostal.municipio)) {
                LinearLayout municipio = (LinearLayout) mTextInputEditTextMunicipio.getParent().getParent().getParent();
                if (municipio != null) {
                    mTextInputEditTextMunicipio.setText(mCodigoPostal.municipio);
                    municipio.setVisibility(View.VISIBLE);
                }
            }

            // TODO: 08/11/2018 Mostrar la ciudad seleccionada
            if (!isNullOrEmpty(mCodigoPostal.ciudad)) {
                LinearLayout ciudad = (LinearLayout) mTextInputEditTextCiudad.getParent().getParent().getParent();
                if (ciudad != null) {
                    mTextInputEditTextCiudad.setText(mCodigoPostal.ciudad);
                    ciudad.setVisibility(View.VISIBLE);
                }
            }

            if (DatosUniversidad3Activity.mUniversidad != null) {
                if (DatosUniversidad3Activity.mUniversidad.mDireccion == null)
                    DatosUniversidad3Activity.mUniversidad.mDireccion = new Direccion();
                DatosUniversidad3Activity.mUniversidad.mDireccion.codigo_postal = mCodigoPostal.codigo_postal;
                DatosUniversidad3Activity.mUniversidad.mDireccion.estado = mCodigoPostal.estado;
                DatosUniversidad3Activity.mUniversidad.mDireccion.municipio = mCodigoPostal.municipio;
                DatosUniversidad3Activity.mUniversidad.mDireccion.ciudad = mCodigoPostal.ciudad;
            }
        }

        @Override
        public void OnfailedCodigo(String mensaje) {
            mTextInputEditTextEstado.setText(null);
            mTextInputEditTextMunicipio.setText(null);
            mTextInputEditTextCiudad.setText(null);

            if (DatosUniversidad3Activity.mUniversidad != null) {
                if (DatosUniversidad3Activity.mUniversidad.mDireccion == null)
                    DatosUniversidad3Activity.mUniversidad.mDireccion = new Direccion();
                DatosUniversidad3Activity.mUniversidad.mDireccion.codigo_postal = null;
                DatosUniversidad3Activity.mUniversidad.mDireccion.estado = null;
                DatosUniversidad3Activity.mUniversidad.mDireccion.municipio = null;
                DatosUniversidad3Activity.mUniversidad.mDireccion.ciudad = null;
            }

        }

        @Override
        public void emptyControls() {

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
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
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
        //endregion


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
        @BindView(R.id.textViewUbicacion)
        TextView mTextViewUbicacion;
        @BindView(R.id.ImageViewverUbicacion)
        ImageView mImageViewVerUbicacion;
    }


    public static class fragmentOtros extends BaseFragment {
        @BindView(R.id.editTextfolleto)
        AutoCompleteTextView mTextInputEditTextFolleto;
        @BindView(R.id.editTextTwitter)
        AutoCompleteTextView mTextInputEditTextTwitter;
        @BindView(R.id.editTextInstagram)
        AutoCompleteTextView mTextInputEditTextInstagram;
        @BindView(R.id.editTextFacebook)
        AutoCompleteTextView mTextInputEditTextFacebook;

        public fragmentOtros() {
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View master = inflater.inflate(R.layout.layout_datos_universidad_otros, container, false);
            ButterKnife.bind(this, master);
            if (DatosUniversidad3Activity.mUniversidad != null) {
                if (DatosUniversidad3Activity.mPaquetes != null) {
                    if (DatosUniversidad3Activity.mPaquetes.aplica_Prospectus) {
                        mTextInputEditTextFolleto.setEnabled(true);
                    } else
                        mTextInputEditTextFolleto.setEnabled(false);
                    if (DatosUniversidad3Activity.mPaquetes.aplica_redes) {
                        mTextInputEditTextFacebook.setEnabled(true);
                        mTextInputEditTextInstagram.setEnabled(true);
                        mTextInputEditTextTwitter.setEnabled(true);
                    } else {
                        mTextInputEditTextFacebook.setEnabled(false);
                        mTextInputEditTextInstagram.setEnabled(false);
                        mTextInputEditTextTwitter.setEnabled(false);
                    }
                }

                mTextInputEditTextFolleto.setText(DatosUniversidad3Activity.mUniversidad.folleto);
                mTextInputEditTextFacebook.setText(DatosUniversidad3Activity.mUniversidad.facebook);
                mTextInputEditTextInstagram.setText(DatosUniversidad3Activity.mUniversidad.instagram);
                mTextInputEditTextTwitter.setText(DatosUniversidad3Activity.mUniversidad.twitter);

                mTextInputEditTextFolleto.addTextChangedListener(mTextWatcher);
                mTextInputEditTextFacebook.addTextChangedListener(mTextWatcher);
                mTextInputEditTextInstagram.addTextChangedListener(mTextWatcher);
                mTextInputEditTextTwitter.addTextChangedListener(mTextWatcher);

                setAutocompleteTextWeb(mTextInputEditTextFolleto, getContext());
                setAutocompleteTextWeb(mTextInputEditTextFacebook, getContext());
                setAutocompleteTextWeb(mTextInputEditTextInstagram, getContext());
                setAutocompleteTextWeb(mTextInputEditTextTwitter, getContext());
                //mTextInputEditTextInstagram.on
            }

            return master;
        }

        TextWatcher mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (DatosUniversidad3Activity.mUniversidad != null) {
                    if (mTextInputEditTextFolleto.isFocused()) {
                        DatosUniversidad3Activity.mUniversidad.folleto = mTextInputEditTextFolleto.getText().toString();
                    }
                    if (mTextInputEditTextFacebook.isFocused()) {
                        mTextInputEditTextFacebook.showDropDown();

                        DatosUniversidad3Activity.mUniversidad.facebook = mTextInputEditTextFacebook.getText().toString();
                    }
                    if (mTextInputEditTextInstagram.isFocused()) {
                        DatosUniversidad3Activity.mUniversidad.instagram = mTextInputEditTextInstagram.getText().toString();
                    }
                    if (mTextInputEditTextTwitter.isFocused()) {
                        DatosUniversidad3Activity.mUniversidad.twitter = mTextInputEditTextTwitter.getText().toString();
                    }
                }
            }
        };
    }

    //endregion
    //region Guardar universidad
    public boolean guardarUniversidad() {

        if (mFragmentGeneral == null) mFragmentGeneral = new fragmentGeneral();
        if (mFragmentDireccion == null) mFragmentDireccion = new fragmentDireccion();
        if (mFragmentOtros == null) mFragmentOtros = new fragmentOtros();
        if (DatosUniversidad3Activity.mUniversidad != null && DatosUniversidad3Activity.mPaquetes != null) {
            // TODO: 08/11/2018 datos generales

            //region nombre
            if (isNullOrEmpty(DatosUniversidad3Activity.mUniversidad.nombre)) {
                try {
                    if (lastPosition == 0) {
                        TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentGeneral.mTextInputEditTextNombre.getParent().getParent();
                        mTextInputLayout.setErrorEnabled(true);
                        mTextInputLayout.setError("Campo requerido");
                        mTextInputLayout.requestFocus();
                        return false;
                    } else {
                        lastPosition = 0;
                        setFragment(mFragmentGeneral);
                        navigation.setSelectedItemId(R.id.navigation_home);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (mFragmentGeneral != null) {
                                        TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentGeneral.mTextInputEditTextNombre.getParent().getParent();
                                        mTextInputLayout.setErrorEnabled(true);
                                        mTextInputLayout.setError("Campo requerido");
                                        mTextInputLayout.requestFocus();
                                        return;
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    return;
                                }

                            }
                        }, 550);

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            } else {
                //quitar errores
                try {
                    if (lastPosition == 0) {
                        TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentGeneral.mTextInputEditTextNombre.getParent().getParent();
                        mTextInputLayout.setErrorEnabled(false);
                        mTextInputLayout.setError(null);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
//endregion

            //region Validar contacto
            if (DatosUniversidad3Activity.mPaquetes != null && DatosUniversidad3Activity.mPaquetes.aplica_contacto) {

                //region validar sitio
                if (!isNullOrEmpty(DatosUniversidad3Activity.mUniversidad.sitio) && !isValidWeb(DatosUniversidad3Activity.mUniversidad.sitio)) {
                    try {
                        if (lastPosition == 0) {
                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentGeneral.mSitio.getParent().getParent();
                            mTextInputLayout.setErrorEnabled(true);
                            mTextInputLayout.setError("Ingrese un sitio web valido. Ejemplo https://www.ejemplo.com");
                            mTextInputLayout.requestFocus();
                            return false;
                        } else {
                            lastPosition = 0;
                            setFragment(mFragmentGeneral);
                            navigation.setSelectedItemId(R.id.navigation_home);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (mFragmentGeneral != null) {
                                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentGeneral.mSitio.getParent().getParent();
                                            mTextInputLayout.setErrorEnabled(true);
                                            mTextInputLayout.setError("Ingrese un sitio web valido. Ejemplo https://www.ejemplo.com");
                                            mTextInputLayout.requestFocus();
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }
                            }, 550);
                            return false;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return false;
                    }
                } else {
                    //quitar errores
                    try {
                        if (lastPosition == 0) {
                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentGeneral.mSitio.getParent().getParent();
                            mTextInputLayout.setErrorEnabled(false);
                            mTextInputLayout.setError(null);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                //endregion

                //region validar telefono
                if (!isNullOrEmpty(DatosUniversidad3Activity.mUniversidad.telefono) && DatosUniversidad3Activity.mUniversidad.telefono.length() < 10) {
                    try {
                        if (lastPosition == 0) {
                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentGeneral.mTextInputEditTextTelefono.getParent().getParent();
                            mTextInputLayout.setErrorEnabled(true);
                            mTextInputLayout.setError("Ingrese un número de teléfono valido.");
                            mTextInputLayout.requestFocus();
                            return false;
                        } else {
                            lastPosition = 0;
                            setFragment(mFragmentGeneral);
                            navigation.setSelectedItemId(R.id.navigation_home);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (mFragmentGeneral != null) {
                                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentGeneral.mTextInputEditTextTelefono.getParent().getParent();
                                            mTextInputLayout.setErrorEnabled(true);
                                            mTextInputLayout.setError("Ingrese un número de teléfono valido.");
                                            mTextInputLayout.requestFocus();
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }
                            }, 550);
                            return false;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return false;
                    }
                } else {
                    //quitar errores
                    try {
                        if (lastPosition == 0) {
                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentGeneral.mTextInputEditTextTelefono.getParent().getParent();
                            mTextInputLayout.setErrorEnabled(false);
                            mTextInputLayout.setError(null);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                //endregion

                //region validar correo
                if (!isNullOrEmpty(DatosUniversidad3Activity.mUniversidad.correo) && !isValidMail(DatosUniversidad3Activity.mUniversidad.correo)) {
                    try {
                        if (lastPosition == 0) {
                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentGeneral.mTextInputEditTextCorreo.getParent().getParent();
                            mTextInputLayout.setErrorEnabled(true);
                            mTextInputLayout.setError("Ingrese un correo electrónico valido.");
                            mTextInputLayout.requestFocus();
                            return false;
                        } else {
                            lastPosition = 0;
                            setFragment(mFragmentGeneral);
                            navigation.setSelectedItemId(R.id.navigation_home);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (mFragmentGeneral != null) {
                                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentGeneral.mTextInputEditTextCorreo.getParent().getParent();
                                            mTextInputLayout.setErrorEnabled(true);
                                            mTextInputLayout.setError("Ingrese un correo electrónico valido.");
                                            mTextInputLayout.requestFocus();
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }
                            }, 550);
                            return false;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return false;
                    }
                } else {
                    //quitar errores
                    try {
                        if (lastPosition == 0) {
                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentGeneral.mTextInputEditTextCorreo.getParent().getParent();
                            mTextInputLayout.setErrorEnabled(false);
                            mTextInputLayout.setError(null);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                //endregion
            }
            if (DatosUniversidad3Activity.mPaquetes != null && DatosUniversidad3Activity.mPaquetes.aplica_direccion) {
                if (DatosUniversidad3Activity.mUniversidad.mDireccion != null) {
                    if (!isNullOrEmpty(DatosUniversidad3Activity.mUniversidad.mDireccion.pais)) {
                        if (DatosUniversidad3Activity.mUniversidad.mDireccion.pais.equals("México")) {
                            if (isNullOrEmpty(DatosUniversidad3Activity.mUniversidad.mDireccion.codigo_postal)) {
                                try {
                                    if (lastPosition == 1) {
                                        TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentDireccion.mTextInputEditTextCodigo.getParent().getParent();
                                        mTextInputLayout.setErrorEnabled(true);
                                        mTextInputLayout.setError("Ingrese un código postal valido.");
                                        mTextInputLayout.requestFocus();
                                        return false;
                                    } else {
                                        lastPosition = 1;
                                        setFragment(mFragmentDireccion);
                                        navigation.setSelectedItemId(R.id.navigation_direccion);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    if (mFragmentDireccion != null) {
                                                        TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentDireccion.mTextInputEditTextCodigo.getParent().getParent();
                                                        mTextInputLayout.setErrorEnabled(true);
                                                        mTextInputLayout.setError("Ingrese un código postal valido.");
                                                        mTextInputLayout.requestFocus();
                                                    }
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }

                                            }
                                        }, 550);
                                        return false;
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    return false;
                                }
                            } else {
                                //quitar errores
                                try {
                                    if (lastPosition == 1) {
                                        TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentDireccion.mTextInputEditTextCodigo.getParent().getParent();
                                        mTextInputLayout.setErrorEnabled(false);
                                        mTextInputLayout.setError(null);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
            if (DatosUniversidad3Activity.mPaquetes != null && DatosUniversidad3Activity.mPaquetes.aplica_Prospectus) {
                //region validar sitio folleto
                if (!isNullOrEmpty(DatosUniversidad3Activity.mUniversidad.folleto) && !isValidWeb(DatosUniversidad3Activity.mUniversidad.folleto)) {
                    try {
                        if (lastPosition == 2) {
                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentOtros.mTextInputEditTextFolleto.getParent().getParent();
                            mTextInputLayout.setErrorEnabled(true);
                            mTextInputLayout.setError("Ingrese un sitio web valido. Ejemplo https://www.ejemplo.com");
                            mTextInputLayout.requestFocus();
                            return false;
                        } else {
                            lastPosition = 2;
                            setFragment(mFragmentOtros);
                            navigation.setSelectedItemId(R.id.navigation_otros);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (mFragmentOtros != null) {
                                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentOtros.mTextInputEditTextFolleto.getParent().getParent();
                                            mTextInputLayout.setErrorEnabled(true);
                                            mTextInputLayout.setError("Ingrese un sitio web valido. Ejemplo https://www.ejemplo.com");
                                            mTextInputLayout.requestFocus();
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }
                            }, 550);
                            return false;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return false;
                    }
                } else {
                    //quitar errores
                    try {
                        if (lastPosition == 2) {
                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentOtros.mTextInputEditTextFolleto.getParent().getParent();
                            mTextInputLayout.setErrorEnabled(false);
                            mTextInputLayout.setError(null);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                //endregion
            }

            if (DatosUniversidad3Activity.mPaquetes != null && DatosUniversidad3Activity.mPaquetes.aplica_redes) {
                // region validar sitio facebook
                if (!isNullOrEmpty(DatosUniversidad3Activity.mUniversidad.facebook) && !isValidWeb(DatosUniversidad3Activity.mUniversidad.facebook)) {
                    try {
                        if (lastPosition == 2) {
                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentOtros.mTextInputEditTextFacebook.getParent().getParent();
                            mTextInputLayout.setErrorEnabled(true);
                            mTextInputLayout.setError("Ingrese un sitio web valido. Ejemplo https://www.ejemplo.com");
                            mTextInputLayout.requestFocus();
                            return false;
                        } else {
                            lastPosition = 2;
                            setFragment(mFragmentOtros);
                            navigation.setSelectedItemId(R.id.navigation_otros);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (mFragmentOtros != null) {
                                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentOtros.mTextInputEditTextFacebook.getParent().getParent();
                                            mTextInputLayout.setErrorEnabled(true);
                                            mTextInputLayout.setError("Ingrese un sitio web valido. Ejemplo https://www.ejemplo.com");
                                            mTextInputLayout.requestFocus();
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }
                            }, 550);
                            return false;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return false;
                    }
                } else {
                    //quitar errores
                    try {
                        if (lastPosition == 2) {
                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentOtros.mTextInputEditTextFacebook.getParent().getParent();
                            mTextInputLayout.setErrorEnabled(false);
                            mTextInputLayout.setError(null);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                //endregion

                // region validar sitio instagram
                if (!isNullOrEmpty(DatosUniversidad3Activity.mUniversidad.instagram) && !isValidWeb(DatosUniversidad3Activity.mUniversidad.instagram)) {
                    try {
                        if (lastPosition == 2) {
                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentOtros.mTextInputEditTextInstagram.getParent().getParent();
                            mTextInputLayout.setErrorEnabled(true);
                            mTextInputLayout.setError("Ingrese un sitio web valido. Ejemplo https://www.ejemplo.com");
                            mTextInputLayout.requestFocus();
                            return false;
                        } else {
                            lastPosition = 2;
                            setFragment(mFragmentOtros);
                            navigation.setSelectedItemId(R.id.navigation_otros);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (mFragmentOtros != null) {
                                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentOtros.mTextInputEditTextInstagram.getParent().getParent();
                                            mTextInputLayout.setErrorEnabled(true);
                                            mTextInputLayout.setError("Ingrese un sitio web valido. Ejemplo https://www.ejemplo.com");
                                            mTextInputLayout.requestFocus();
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }
                            }, 550);
                            return false;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return false;
                    }
                } else {
                    //quitar errores
                    try {
                        if (lastPosition == 2) {
                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentOtros.mTextInputEditTextInstagram.getParent().getParent();
                            mTextInputLayout.setErrorEnabled(false);
                            mTextInputLayout.setError(null);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                //endregion

                // region validar sitio twitter
                if (!isNullOrEmpty(DatosUniversidad3Activity.mUniversidad.twitter) && !isValidWeb(DatosUniversidad3Activity.mUniversidad.twitter)) {
                    try {
                        if (lastPosition == 2) {
                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentOtros.mTextInputEditTextTwitter.getParent().getParent();
                            mTextInputLayout.setErrorEnabled(true);
                            mTextInputLayout.setError("Ingrese un sitio web valido. Ejemplo https://www.ejemplo.com");
                            mTextInputLayout.requestFocus();
                            return false;
                        } else {
                            lastPosition = 2;
                            setFragment(mFragmentOtros);
                            navigation.setSelectedItemId(R.id.navigation_otros);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (mFragmentOtros != null) {
                                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentOtros.mTextInputEditTextTwitter.getParent().getParent();
                                            mTextInputLayout.setErrorEnabled(true);
                                            mTextInputLayout.setError("Ingrese un sitio web valido. Ejemplo https://www.ejemplo.com");
                                            mTextInputLayout.requestFocus();
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                }
                            }, 550);
                            return false;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return false;
                    }
                } else {
                    //quitar errores
                    try {
                        if (lastPosition == 2) {
                            TextInputLayout mTextInputLayout = (TextInputLayout) mFragmentOtros.mTextInputEditTextTwitter.getParent().getParent();
                            mTextInputLayout.setErrorEnabled(false);
                            mTextInputLayout.setError(null);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                //endregion
            }
            //endregion
        } else return false;

        return true;
    }
    //endregion
    //region validacionesGenerales

    public boolean isValidPhone(String var1) {
        return Patterns.PHONE.matcher(var1).matches();

    }

    public boolean isValidMail(String var1) {
        return Patterns.EMAIL_ADDRESS.matcher(var1).matches();
    }

    public boolean isValidWeb(String var1) {
        try {
            if (var1.startsWith("http") || var1.startsWith("https")) {
                return Patterns.WEB_URL.matcher(var1).matches();
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    //endregion
    //region controles
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.contenido)
    FrameLayout mFrameLayoutContenido;
    DatosUniversidadPresenter mDatosUniversidadPresenter;
    @BindView(R.id.floatingSubmit)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    //endregion

    public static class ApaterAutocomplete extends ArrayAdapter<String> {
        List<String> listTipos = new ArrayList<>();
        List<String> listTiposOriginal = new ArrayList<>();

        public ApaterAutocomplete(@NonNull Context context, int resource) {
            super(context, resource);
            listTipos.add("http://");
            listTipos.add("https://");
            listTiposOriginal.add("http://");
            listTiposOriginal.add("https://");
        }

        @Override
        public int getCount() {
            return listTipos.size();
        }

        @Nullable
        @Override
        public String getItem(int position) {
            return listTipos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    List<String> Suggestion = new ArrayList<>();
                    if (constraint != null) {

                        for (String item : listTipos) {

                        }
                        if (constraint.toString().toLowerCase().startsWith("http://") || constraint.toString().toLowerCase().startsWith("https://"))
                            Suggestion.add(constraint.toString().toLowerCase());
                        else {

                        }
                    }

                    filterResults.values = Suggestion;
                    filterResults.count = Suggestion.size();

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    listTipos.clear();
                    notifyDataSetInvalidated();
                    if (results != null && results.count > 0) {
                        // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                        for (Object object : (List<?>) results.values) {
                            listTipos.add(object.toString());
                        }
                        notifyDataSetChanged();
                    } else if (constraint == null) {
                        listTipos.addAll(listTiposOriginal);
                        notifyDataSetInvalidated();
                    }
                }
            }

                    ;
        }
    }
}
