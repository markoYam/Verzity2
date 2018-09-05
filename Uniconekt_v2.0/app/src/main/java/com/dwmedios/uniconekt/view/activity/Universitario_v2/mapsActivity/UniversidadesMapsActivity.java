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
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.SearchUniversidades;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.presenter.UniversidadPresenter;
import com.dwmedios.uniconekt.view.activity.Universitario.DetalleUniversidadActivity;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.viewmodel.UniversidadViewController;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.activity.Universitario.DetalleUniversidadActivity.KEY_DETALLE_UNIVERSIDAD;
import static com.dwmedios.uniconekt.view.activity.Universitario.SearchUniversidadActivity.TYPE_VIEW_MAPS;
import static com.dwmedios.uniconekt.view.activity.Universitario_v2.mapsActivity.DialogPreviewMapsActivity.KEY_RESULT;
import static com.dwmedios.uniconekt.view.activity.Universitario_v2.mapsActivity.DialogPreviewMapsActivity.KEY_UNIVERSIDAD_DIALOGO;
import static com.dwmedios.uniconekt.view.util.ImageUtils.OptionsImageLoaderLight;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class UniversidadesMapsActivity extends BaseActivity implements OnMapReadyCallback, UniversidadViewController, LocationListener {

    private GoogleMap mMap;
    private UniversidadPresenter mUniversidadPresenter;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.linearmapa)
    LinearLayout mLinearLayout;
    LocationManager locationManager;
    String locationProvider;
    private List<Universidad> mUniversidadList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universidades_maps);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boolean extranjero = SharePrefManager.getInstance(getApplicationContext()).isSeachExtranjero();
        if (extranjero)
            getSupportActionBar().setTitle("Universidades en el extranjero");
        else
            getSupportActionBar().setTitle("Universidades cercanas");
        mUniversidadPresenter = new UniversidadPresenter(this, getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initializeLocationManager();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

    public void buscarUniversidades() {
        mUniversidadPresenter.Search(new SearchUniversidades(), 0);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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
    }

    GoogleMap.OnInfoWindowClickListener mOnInfoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            onclickMaker(marker);
        }
    };
  /*  MapsUniversityActivity.adaper.onclick mOnclick = new MapsUniversityActivity.adaper.onclick() {
        @Override
        public void onclick(Universidad mUniversidad) {
            showToastLongMessage(mUniversidad.nombre);
        }
    };*/
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
            //marker.showInfoWindow();
            marker.hideInfoWindow();
          /*  Snackbar.make(mLinearLayout, "Ver detalle de la universidad.", Snackbar.LENGTH_LONG)
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
                    }).show();*/
            Universidad mUniversidadEnviar = null;
            for (Universidad mUniversidad : mUniversidadList) {
                if (mUniversidad.id == position) {
                    /*  */
                    mUniversidadEnviar = mUniversidad;
                    break;
                }
            }

            mIntent.putExtra(KEY_UNIVERSIDAD_DIALOGO, mUniversidadEnviar);
            isViewDialog = true;
        } catch (Exception e) {
            marker.hideInfoWindow();
        }

        CameraUpdate maker = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 13.5f);
        mMap.animateCamera(maker);
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
                        Intent mIntent2 = new Intent(getApplicationContext(), DetalleUniversidadActivity.class);
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
        this.locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {

        super.onResume();
        List<String> mStrings = new ArrayList<String>();
        mStrings.add(Manifest.permission.ACCESS_FINE_LOCATION);
        mStrings.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        validatePermison(mStrings, this, 101);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.locationManager.requestLocationUpdates(this.locationProvider, 400, 1, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    this.locationManager.requestLocationUpdates(this.locationProvider, 400, 1, this);
                } else {
                    showMessage("Permisos no otorgados");
                }
                break;
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

    @Override
    public void onLocationChanged(Location location) {
        marcarMiUbicacion(location);
    }

    Marker markerListLocation = null;

    private void marcarMiUbicacion(Location location) {
        if (location != null && mMap != null) {
            if (mLocationLast != null) {
                if (mLocationLast.getLongitude() != location.getLongitude() && mLocationLast.getLatitude() != location.getLatitude()) {
                    mLocationLast = location;

                    LatLng coordenadas = new LatLng(location.getLatitude(), location.getLongitude());
                    if (markerListLocation != null) markerListLocation.remove();
                    markerListLocation = mMap.addMarker(new MarkerOptions()
                            .position(coordenadas)
                            .title("Mi ubicación")
                            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.profile, R.layout.marker_usuario, true))));
                    CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 4f);
                    mMap.animateCamera(miUbicacion);
                }
            } else {
                mLocationLast = location;
                LatLng coordenadas = new LatLng(location.getLatitude(), location.getLongitude());
                if (markerListLocation != null) markerListLocation.remove();
                markerListLocation = mMap.addMarker(new MarkerOptions()
                        .position(coordenadas)
                        .title("Mi ubicación")
                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.profile, R.layout.marker_usuario, true))));

                CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 4f);
                mMap.animateCamera(miUbicacion);

            }
        }
    }

    Location mLocationLast = null;

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
        //MapsUniversityActivity.adaper.onclick mOnclick;

     /*   public adaper(Context mContext, List<Universidad> mUniversidadList, MapsUniversityActivity.adaper.onclick mOnclick) {
            this.mContext = mContext;
            this.mUniversidadList = mUniversidadList;
            this.mOnclick = mOnclick;
            master = LayoutInflater.from(mContext).inflate(R.layout.row_marker_google, null);
        }
*/
        public interface onclick {
            void onclick(Universidad mUniversidad);
        }

        public void configure(Marker marker, final View master) {
            Universidad mUniversidadTemp;
            try {
                int id = Integer.parseInt(marker.getSnippet().toString());
                TextView mTextViewNombre = master.findViewById(R.id.nombreuniversidadMaker);
                TextView mTextViewDes = master.findViewById(R.id.desuniversidadMaker);
                ImageView mImageView = master.findViewById(R.id.imageUniversidad);
                Button mButton = master.findViewById(R.id.buttonVisto);
                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(master.getContext(), "Vas a dar los chupos", Toast.LENGTH_SHORT).show();
                    }
                });
                for (Universidad mUniversidad : mUniversidadList) {
                    if (mUniversidad.id == id) {
                        mTextViewNombre.setText(mUniversidad.nombre);
                        mTextViewDes.setText(mUniversidad.descripcion);
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

    @Override
    public void OnSuccessSeach(List<Universidad> mUniversidadList, int type) {
        this.mUniversidadList = mUniversidadList;
        loadCurrentLocation();
        // UniversidadesMapsActivity.adaper mAdaper = new UniversidadesMapsActivity.adaper(getApplicationContext(), mUniversidadList, mOnclick);
        // mMap.setInfoWindowAdapter(mAdaper);
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

    private void loadCurrentLocation() {
        Location location = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location = locationManager.getLastKnownLocation(locationProvider);
        }
        if (location != null) {

            onLocationChanged(location);
        }
    }

    @Override
    public void Onfailed(String mensaje) {
        showMessage(mensaje);
        loadCurrentLocation();
    }

    private Bitmap getMarkerBitmapFromView(int resId, int layout, boolean isUser) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        if (!isUser)
            markerImageView.setImageResource(resId);
        else
            configureCabeceras(markerImageView);
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
        loadCurrentLocation();
    }

    private void configureCabeceras(ImageView mImageView) {
        Persona mPersona = mUniversidadPresenter.getDatosPersona();
        if (mPersona != null) {
            if (!isNullOrEmpty(mPersona.foto))
                new taskImageP(getUrlImage(mPersona.foto, getApplicationContext()), mImageView).execute();
            else {
                Usuario mUsuario = mUniversidadPresenter.getUsuario();
                if (mUsuario != null) {
                    if (!isNullOrEmpty(mUsuario.cv_facebook))
                        new taskImageFacebook(mUsuario.cv_facebook, mImageView).execute();
                }
            }
        } else {
            Usuario mUsuario = mUniversidadPresenter.getUsuario();
            if (mUsuario != null) {
                if (!isNullOrEmpty(mUsuario.cv_facebook))
                    new taskImageFacebook(mUsuario.cv_facebook, mImageView).execute();
            }
        }
    }

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
