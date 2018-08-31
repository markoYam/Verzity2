package com.dwmedios.uniconekt.view.activity.Universitario.ActivityMaps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.SearchUniversidades;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.presenter.UniversidadPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario.DetalleUniversidadActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
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
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.dwmedios.uniconekt.view.activity.Universitario.DetalleUniversidadActivity.KEY_DETALLE_UNIVERSIDAD;
import static com.dwmedios.uniconekt.view.activity.Universitario.SearchUniversidadActivity.TYPE_VIEW_MAPS;
import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderLight;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.dwmedios.uniconekt.view.util.Utils.ERROR_CONECTION;

public class MapsUniversityActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,UniversidadViewController,LocationListener {

    private GoogleMap mMap;
    private UniversidadPresenter mUniversidadPresenter;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.linearmapa)
    LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_university);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (TYPE_VIEW_MAPS == 1) {
            getSupportActionBar().setTitle("Universidades cercanas");
        } else if (TYPE_VIEW_MAPS == 2) {
            getSupportActionBar().setTitle("Universidades en el extranjero");
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mUniversidadPresenter = new UniversidadPresenter(this, getApplicationContext());
        getLocation();
        mMap.setOnMarkerClickListener(markerClickListener);
        mMap.setOnInfoWindowClickListener(mOnInfoWindowClickListener);
        buscarUniversidades();
    }

    GoogleMap.OnInfoWindowClickListener mOnInfoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            onclickMaker(marker);
        }
    };
    adaper.onclick mOnclick = new adaper.onclick() {
        @Override
        public void onclick(Universidad mUniversidad) {
            showToastLongMessage(mUniversidad.nombre);
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
            marker.showInfoWindow();
            Snackbar.make(mLinearLayout, "Ver detalle de la universidad.", Snackbar.LENGTH_LONG)
                    .setAction("Ir", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (Universidad mUniversidad : mUniversidadList) {
                                if (mUniversidad.id == position) {
                                    Intent mIntent = new Intent(getApplicationContext(), DetalleUniversidadActivity.class);
                                    mIntent.putExtra(KEY_DETALLE_UNIVERSIDAD, mUniversidad);
                                    startActivity(mIntent);
                                    break;
                                }
                            }

                        }
                    }).show();
        } catch (Exception e) {
            marker.hideInfoWindow();
        }
        CameraUpdate maker = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 13.5f);
        mMap.animateCamera(maker);
    }

    @Override
    protected void onDestroy() {
        if(mGoogleApiClient!=null)mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public static class adaper implements GoogleMap.InfoWindowAdapter {
        View master;
        Context mContext;
        List<Universidad> mUniversidadList;
        onclick mOnclick;

        public adaper(Context mContext, List<Universidad> mUniversidadList, onclick mOnclick) {
            this.mContext = mContext;
            this.mUniversidadList = mUniversidadList;
            this.mOnclick = mOnclick;
            master = LayoutInflater.from(mContext).inflate(R.layout.row_marker_google, null);
        }

        public interface onclick {
            void onclick(Universidad mUniversidad);
        }

        public void configure(Marker marker, View master) {
            Universidad mUniversidadTemp;
            try {
                int id = Integer.parseInt(marker.getSnippet().toString());
                TextView mTextViewNombre = master.findViewById(R.id.nombreuniversidadMaker);
                ImageView mImageView = master.findViewById(R.id.imageUniversidad);
                for (Universidad mUniversidad : mUniversidadList) {
                    if (mUniversidad.id == id) {
                        mTextViewNombre.setText(mUniversidad.nombre);
                        if (mUniversidad.logo != null)
                            ImageLoader.getInstance().displayImage(getUrlImage(mUniversidad.logo, mContext), mImageView, OptionsImageLoaderLight);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public View getInfoWindow(Marker marker) {
            configure(marker, master);
            return master;
        }

        @Override
        public View getInfoContents(Marker marker) {
            configure(marker, master);
            return master;
        }
    }

    GoogleApiClient mGoogleApiClient;

    public void getLocation() {
        if (mGoogleApiClient == null)

        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(MapsUniversityActivity.this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    Location mLastLocation;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                //  showMessage(String.valueOf(mLastLocation.getLatitude()) + String.valueOf(mLastLocation.getLongitude()) + "-> serivicio gogle");
                //SearchUniversidades();
            } else {
                miUbicacion();
            }
        }

    }

    public void SearchUniversidades() {
        SearchUniversidades mUniversidades = new SearchUniversidades();
        if (mLastLocation != null) {
            LatLng coordenadas = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            if (TYPE_VIEW_MAPS == 1) {
                CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 12.5f);
                mMap.animateCamera(miUbicacion);
            }
            mMap.addMarker(new MarkerOptions()
                    .position(coordenadas)
                    .title("Mi ubicación")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_university_student)));

        }
        if (TYPE_VIEW_MAPS == 1) {
            //cerca de mi
            String estado = getCiudad(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            if (!estado.isEmpty()) {
                mUniversidades.estado = estado;
                mUniversidadPresenter.Search(mUniversidades);
            } else {
                this.Onfailed(ERROR_CONECTION);
            }
        } else if (TYPE_VIEW_MAPS == 2) {
            //extranjero
            mUniversidades.extranjero = true;
            mUniversidadPresenter.Search(mUniversidades);
        }
    }
public void buscarUniversidades()
{
    mUniversidadPresenter.Search(new SearchUniversidades());
}
    public String getCiudad(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String ciudad = "";
        try {
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                ciudad = addressList.get(0).getAdminArea();
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

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            mLastLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            if (mLastLocation != null) {
                //   showMessage(String.valueOf(mLastLocation.getLatitude()) + String.valueOf(mLastLocation.getLongitude()) + "-> manual");
            }
            return;
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

    public List<Universidad> mUniversidadList;

    @Override
    public void OnSuccess(List<Universidad> mUniversidadList) {
        this.mUniversidadList = mUniversidadList;
        adaper mAdaper = new adaper(getApplicationContext(), mUniversidadList, mOnclick);
        mMap.setInfoWindowAdapter(mAdaper);
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
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_verde)));

                        if (mUniversidadList.size() == 1) {
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
    public void OnSuccessSeach(List<Universidad> mUniversidadList, int type) {
        this.mUniversidadList = mUniversidadList;
        adaper mAdaper = new adaper(getApplicationContext(), mUniversidadList, mOnclick);
        mMap.setInfoWindowAdapter(mAdaper);
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
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_verde)));

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
        if (mLastLocation != null) {
            LatLng coordenadas = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 12.5f);
            mMap.animateCamera(miUbicacion);

        }
    }
}

