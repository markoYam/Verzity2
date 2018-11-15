package com.dwmedios.uniconekt.view.activity.Universitario_v2.mapsActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.SearchUniversidades;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.presenter.UniversidadPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario_v2.UniversidadDetalleActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.util.Utils;
import com.dwmedios.uniconekt.view.viewmodel.UniversidadViewController;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario.DetalleUniversidadActivity.KEY_DETALLE_UNIVERSIDAD;
import static com.dwmedios.uniconekt.view.activity.Universitario.SearchUniversidadActivity.TYPE_VIEW_MAPS;
import static com.dwmedios.uniconekt.view.activity.Universitario_v2.mapsActivity.DialogPreviewMapsActivity.KEY_RESULT;
import static com.dwmedios.uniconekt.view.activity.Universitario_v2.mapsActivity.DialogPreviewMapsActivity.KEY_UNIVERSIDAD_DIALOGO;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlFacebook;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.dwmedios.uniconekt.view.util.Utils.changeColorToolbar;
import static com.dwmedios.uniconekt.view.util.Utils.setStatusBarGradiant;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class UniversidadesMapsActivity extends BaseActivity implements OnMapReadyCallback, UniversidadViewController, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private UniversidadPresenter mUniversidadPresenter;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.linearmapa)
    RelativeLayout mLinearLayout;
    @BindView(R.id.floatingMap)
    FloatingActionButton mFloatingActionButton;
    LocationManager locationManager;
    String locationProvider;
    private List<Universidad> mUniversidadList;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universidades_maps);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boolean extranjero = SharePrefManager.getInstance(getApplicationContext()).isSeachExtranjero();
        if (extranjero) {
            getSupportActionBar().setTitle("Universidades en el extranjero");
            setStatusBarGradiant(UniversidadesMapsActivity.this, R.drawable.status_uni_extra);
            changeColorToolbar(getSupportActionBar(), R.color.Color_extranjero, UniversidadesMapsActivity.this);
        } else
            getSupportActionBar().setTitle("Universidades cercanas");
        mUniversidadPresenter = new UniversidadPresenter(this, getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initializeLocationManager();
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocationLast != null) {
                    LatLng mLatLng = new LatLng(mLocationLast.getLatitude(), mLocationLast.getLongitude());
                    CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(mLatLng, 4f);
                    mMap.animateCamera(miUbicacion);
                    mFloatingActionButton.setVisibility(View.GONE);
                }
            }
        });
      //  LoadFirt();
    }

    TextToSpeech mTextToSpeech;
    int result;

    private void LoadFirt() {
        mTextToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Locale spanish = new Locale("es", "ES");
                    result = mTextToSpeech.setLanguage(spanish);
                } else {
                    showMessage("No soportado...");
                }
            }
        });
    }

    public void speak(String text) {
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            showMessage("Not support languaje");
        } else {
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }

    Bitmap mBitmap;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

    public void buscarUniversidades() {
        mUniversidadPresenter.Search(new SearchUniversidades(), 0);
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
                mGoogleApiClient.unregisterConnectionCallbacks(this);
                mGoogleApiClient.unregisterConnectionFailedListener(this);
                mGoogleApiClient = null;
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
       // if (mGoogleApiClient != null) mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    void conectLocation() {
        if (mGoogleApiClient == null)

        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(UniversidadesMapsActivity.this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<String> mStrings = new ArrayList<String>();
        mStrings.add(Manifest.permission.ACCESS_FINE_LOCATION);
        mStrings.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        validatePermison(mStrings, this, 100);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        this.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMarkerClickListener(markerClickListener);
        mMap.setOnInfoWindowClickListener(mOnInfoWindowClickListener);
        buscarUniversidades();
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (isViewDialog)
                    if (mIntent != null)
                        startActivityForResult(mIntent, 200);
            }
        });
        this.conectLocation();
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    mLocationLast = location;
                    if (markerListLocation == null)
                        marcarMiUbicacion(mLocationLast, crearVistausuario(null));
                    LoadImageUser();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
            return;
        }
    }

    GoogleMap.OnInfoWindowClickListener mOnInfoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            onclickMaker(marker);
        }
    };
    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            onclickMaker(marker);
            return true;
        }
    };

    private void onclickMaker(Marker marker) {

        try {
            final int position = Integer.parseInt(marker.getSnippet());
            mIntent = new Intent(getApplicationContext(), DialogPreviewMapsActivity.class);
            marker.hideInfoWindow();
            Universidad mUniversidadEnviar = null;
            for (Universidad mUniversidad : mUniversidadList) {
                if (mUniversidad.id == position) {
                    /*  */
                    mUniversidadEnviar = mUniversidad;
                    break;
                }
            }
            //speak(mUniversidadEnviar.nombre);
            mIntent.putExtra(KEY_UNIVERSIDAD_DIALOGO, mUniversidadEnviar);
            isViewDialog = true;
        } catch (Exception e) {
            marker.hideInfoWindow();
        }

        CameraUpdate maker = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 13.5f);
        mMap.animateCamera(maker);
        mFloatingActionButton.setVisibility(View.VISIBLE);
    }

    private boolean isViewDialog = false;
    Intent mIntent = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 200:
                if (resultCode == RESULT_OK) {
                    Universidad mUniversidad = data.getExtras().getParcelable(KEY_RESULT);
                    if (mUniversidad != null) {
                        Intent mIntent2 = new Intent(getApplicationContext(), UniversidadDetalleActivity.class);
                        mIntent2.putExtra(KEY_DETALLE_UNIVERSIDAD, mUniversidad);
                        startActivity(mIntent2);
                        isViewDialog = false;
                        mIntent = null;
                    }

                } else {
                    isViewDialog = false;
                    mIntent = null;
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    showMessage("Permisos no otorgados");
                }
                break;
        }
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
    public void EmpyRecycler() {

    }

    @Override
    public void OnSuccess(List<Universidad> mUniversidadList) {

    }


    Marker markerListLocation = null;

    private void marcarMiUbicacion(Location location, Bitmap mBitmap) {
        if (location != null && mMap != null) {
            if (mLocationLast != null) {
                LatLng coordenadas = new LatLng(location.getLatitude(), location.getLongitude());
                if (markerListLocation != null) {
                    markerListLocation.setPosition(coordenadas);
                    markerListLocation.setIcon(BitmapDescriptorFactory.fromBitmap(mBitmap));
                } else {
                    markerListLocation = mMap.addMarker(new MarkerOptions()
                            .position(coordenadas)
                            .title("Mi ubicación")
                            .icon(BitmapDescriptorFactory.fromBitmap(mBitmap)));
                    CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 4f);
                    mMap.animateCamera(miUbicacion);
                }
            }
        } else {
            mLocationLast = location;
            LatLng coordenadas = new LatLng(location.getLatitude(), location.getLongitude());
            if (markerListLocation != null) {
                markerListLocation.setPosition(coordenadas);
            } else {
                markerListLocation = mMap.addMarker(new MarkerOptions()
                        .position(coordenadas)
                        .title("Mi ubicación")
                        .icon(BitmapDescriptorFactory.fromBitmap(mBitmap)));
                CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 4f);
                mMap.animateCamera(miUbicacion);

            }
        }
    }


    Location mLocationLast = null;

    @Override
    public void OnSuccessSeach(List<Universidad> mUniversidadList, int type) {
        this.mUniversidadList = mUniversidadList;
        try {
            for (int i = 0; i < mUniversidadList.size(); i++) {

                try {
                    float lat = Float.parseFloat(mUniversidadList.get(i).mDireccion.latitud);
                    float lon = Float.parseFloat(mUniversidadList.get(i).mDireccion.longitud);
                    String title = mUniversidadList.get(i).nombre;
                    if (lat != 0 && lon != 0) {
                        LatLng mLatLng = new LatLng(lat, lon);
                        mMap.addMarker(new MarkerOptions()
                                .position(mLatLng)
                                .title(title)
                                .snippet(mUniversidadList.get(i).id + "")
                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.border_maker1, R.layout.marker_demo, false))));

                        if (mUniversidadList.size() == (i + 1)) {
                            if (TYPE_VIEW_MAPS == 2) {
                                CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(mLatLng, 12.5f);
                                mMap.animateCamera(miUbicacion);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Onfailed(String mensaje) {
        showMessage(mensaje);
        conectLocation();
    }

    private Bitmap getMarkerBitmapFromView(int resId, int layout, boolean isUser) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        if (!isUser)
            markerImageView.setImageResource(resId);
        //configureCabeceras(markerImageView);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private Bitmap crearVistausuario(String uriImage) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_usuario, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(R.drawable.ic_action_user_profile);

        if (!isNullOrEmpty(uriImage)) {
            File mFile = new File(uriImage);
            Bitmap myBitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath());
            markerImageView.setImageBitmap(myBitmap);
        } else {
            markerImageView.setImageResource(R.drawable.profile);
        }
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private void initializeLocationManager() {
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        this.locationProvider = locationManager.getBestProvider(criteria, false);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationLast = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLocationLast != null) {
                if (markerListLocation == null)
                    marcarMiUbicacion(mLocationLast, crearVistausuario(null));
                LoadImageUser();
            } else {
                if (markerListLocation == null)
                    marcarMiUbicacion(mLocationLast, crearVistausuario(null));
                LoadImageUser();
            }
        } else {
            //mostrarDireccion();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class uri {
        public String uri;
        public String nombre;
    }

    private uri getUri() {
        Persona mPersona = mUniversidadPresenter.getDatosPersona();
        uri mUri = new uri();
        if (mPersona != null) {
            if (!isNullOrEmpty(mPersona.foto)) {
                String[] string = mPersona.foto.split("/");
                mUri.nombre = string[string.length - 1];
                mUri.uri = getUrlImage(mPersona.foto, getApplicationContext());
            } else {
                Usuario mUsuario = mUniversidadPresenter.getUsuario();
                if (mUsuario != null) {
                    mUri.uri = getUrlFacebook(mUsuario.cv_facebook);
                    mUri.nombre = "PefilFacebook.jpg";
                }
            }
        } else {
            Usuario mUsuario = mUniversidadPresenter.getUsuario();
            if (mUsuario != null) {
                if (!isNullOrEmpty(mUsuario.cv_facebook)) {
                    mUri.uri = getUrlFacebook(mUsuario.cv_facebook);
                    mUri.nombre = "PefilFacebook.jpg";
                }
            }
        }
        return mUri;
    }

    private void LoadImageUser() {
        uri uri = getUri();
        new Utils.DownloadImage(uri.uri, mDownloadImageInterface, uri.nombre).execute();
    }

    private String mpatch;

    Utils.DownloadImage.downloadImageInterface mDownloadImageInterface = new Utils.DownloadImage.downloadImageInterface() {
        @Override
        public void Onsucces(String patch) {
            mpatch = patch;
            if (mLocationLast != null) {
                if (mBitmap == null)
                    mBitmap = crearVistausuario(patch);
                marcarMiUbicacion(mLocationLast, mBitmap);
            }
        }
    };

    public class taskImageFacebook extends AsyncTask<Void, Void, Void> {

        Bitmap bmp;
        public String key;
        ImageView mImageView;

        public taskImageFacebook(String key, ImageView mImageView) {
            this.key = key;
            this.mImageView = mImageView;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL("https://graph.facebook.com/" + key + "/picture?type=large");
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

    public class taskImageP extends AsyncTask<Void, Void, Void> {

        Bitmap bmp;
        public String key;
        ImageView mImageView;

        public taskImageP(String key, ImageView mImageView) {

            this.key = key;
            this.mImageView = mImageView;
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
                mImageView.setImageBitmap(bmp);
            super.onPostExecute(aVoid);
        }
    }
}
