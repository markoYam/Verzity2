package com.dwmedios.uniconekt.view.activity.Universidad;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.presenter.SearcUbicacionUniPresenter;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.viewmodel.SearchUbicacionViewController;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchUbicacionUniActivity extends BaseActivity implements OnMapReadyCallback, SearchUbicacionViewController, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String LAT = "LAT";
    public static final String LONG = "LONG";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.floatingMap)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.buttonOk)
    FloatingActionButton mButtonOk;
    @BindView(R.id.textViewLocalizacion)
    TextView mTextViewDetalle;
    @BindView(R.id.cardOk)
    CardView mCardViewOk;
    @BindView(R.id.buttonSearch)
    ImageButton mImageButton;
    @BindView(R.id.buttonLocation)
    FloatingActionButton mFloatingActionButtonLocation;
    GoogleMap mMap;
    private SearcUbicacionUniPresenter searcUbicacionUniPresenter;
    private LatLng mLatLng;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ubicacion_uni);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Buscar Ubicación");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        searcUbicacionUniPresenter = new SearcUbicacionUniPresenter(this);
        mFloatingActionButton.setOnClickListener(mOnClickListener);
        mButtonOk.setOnClickListener(mOnClickListener);
        mCardViewOk.setOnClickListener(mOnClickListener);
        mImageButton.setOnClickListener(mOnClickListener);
        mFloatingActionButtonLocation.setOnClickListener(mOnClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mGoogleApiClient!=null)mGoogleApiClient.disconnect();
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        if(mGoogleApiClient!=null)mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    private void handleLoad() {
        try {
            String lon = getIntent().getExtras().getString(LONG);
            String lat = getIntent().getExtras().getString(LAT);
            Double lo = Double.parseDouble(lon);
            Double la = Double.parseDouble(lat);
            mLatLng = new LatLng(la, lo);
            searcUbicacionUniPresenter.animateCamera(mLatLng);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mLatLng = mMap.getCameraPosition().target;

            }
        });
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                setPreview();
            }
        });
        handleLoad();
        handlePermison();
    }

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    List<String> mStringsPermison = new ArrayList<>();

    private void handlePermison() {
        mStringsPermison.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        mStringsPermison.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (validatePermison(mStringsPermison, this, 500)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                if (mLastLocation != null) {
                    getMyLocation();
                } else {
                    conectLocation();
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 500) {
            if (checkPermison(mStringsPermison, this, 500)) {
                conectLocation();
            } else {
                showMessage("Es necesario otorgar los permisos para continuar");
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void search() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(SearchUbicacionUniActivity.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("W", "Place: " + place.getName());
                searcUbicacionUniPresenter.animateCamera(place.getLatLng());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                showMessage(status.getStatusMessage());
                Log.i("W", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonSearch:
                    searcUbicacionUniPresenter.search();
                    break;
                case R.id.buttonOk:
                    if (mLatLng == null || mLatLng.latitude == 0.0 || mLatLng.longitude == 0.0) {
                        showMessage("Seleccione la ubicación de la universidad");
                        break;
                    }
                    Intent mIntent = getIntent();
                    mIntent.putExtra(LAT, mLatLng.latitude);
                    mIntent.putExtra(LONG, mLatLng.longitude);
                    setResult(Activity.RESULT_OK, mIntent);
                    finish();
                    break;
                case R.id.cardOk:
                    searcUbicacionUniPresenter.search();
                    break;
                case R.id.buttonLocation:
                    handlePermison();
                    break;
            }

        }
    };

    @Override
    public void updateCameraLocation(LatLng mLatLng) {
        if (mLatLng != null) {
            CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(mLatLng, 15.5f);
            mMap.animateCamera(miUbicacion);
            this.mLatLng = mLatLng;
        }
    }

    private void setPreview() {
        if (mLatLng != null) {
            String p = new String();
            p += "Longitud: " + mLatLng.longitude;
            p += "Latitud: " + mLatLng.latitude;
            // mTextView.setText(mTextView.getText()+" - "+p);
            new taskDetalle(mLatLng).execute();
            //mTextViewDetalle.setText(getDetalle(mLatLng.latitude, mLatLng.longitude));
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
public class taskDetalle extends AsyncTask<Void,Void,Void> {
    LatLng mLatLng = null;
    String detalle = null;

    public taskDetalle(LatLng mLatLng) {
        this.mLatLng = mLatLng;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mTextViewDetalle.setText("Buscando dirección…");
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
            mTextViewDetalle.setText(detalle);

        } else {
            mTextViewDetalle.setText("No se encontró la dirección.");
        }
    }
}

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                mFloatingActionButtonLocation.show();
            } else {
                miUbicacion();
            }
        } else {
            miUbicacion();
        }
    }

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                mLastLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                mLastLocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            }
            return;
        }

    }

    private void getMyLocation() {
        if (mLastLocation != null) {
            LatLng mLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            searcUbicacionUniPresenter.animateCamera(mLatLng);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mFloatingActionButtonLocation.hide();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mFloatingActionButtonLocation.hide();
    }

    void conectLocation() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(SearchUbicacionUniActivity.this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

}
