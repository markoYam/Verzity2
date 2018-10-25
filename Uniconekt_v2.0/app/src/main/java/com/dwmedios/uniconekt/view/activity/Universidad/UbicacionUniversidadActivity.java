package com.dwmedios.uniconekt.view.activity.Universidad;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.dwmedios.uniconekt.R;
import com.dwmedios.uniconekt.data.controller.AllController;
import com.dwmedios.uniconekt.model.Persona;
import com.dwmedios.uniconekt.model.Universidad;
import com.dwmedios.uniconekt.model.Usuario;
import com.dwmedios.uniconekt.view.activity.base.BaseActivity;
import com.dwmedios.uniconekt.view.util.SharePrefManager;
import com.dwmedios.uniconekt.view.util.Utils;
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
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlFacebook;
import static com.dwmedios.uniconekt.view.util.ImageUtils.getUrlImage;
import static com.dwmedios.uniconekt.view.util.Utils.changeColorToolbar;
import static com.dwmedios.uniconekt.view.util.Utils.setStatusBarGradiant;
import static com.facebook.internal.Utility.isNullOrEmpty;

public class UbicacionUniversidadActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String KEY_UBICACION_UNIVERSIDAD = "key_universidad_";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private Universidad mUniversidad;
    AllController mAllController;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion_universidad);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Ubicación");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boolean extranjero = SharePrefManager.getInstance(getApplicationContext()).isSeachExtranjero();
        if (extranjero) {
            setStatusBarGradiant(UbicacionUniversidadActivity.this, R.drawable.status_uni_extra);
            changeColorToolbar(getSupportActionBar(), R.color.Color_extranjero, UbicacionUniversidadActivity.this);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mAllController = new AllController(getApplicationContext());
    }


    GoogleApiClient mGoogleApiClient;

    public void configureLoad() {
        try {
            mUniversidad = getIntent().getExtras().getParcelable(KEY_UBICACION_UNIVERSIDAD);
            getSupportActionBar().setTitle("Ubicación " + mUniversidad.nombre);
            conectLocation();
        } catch (Exception e) {
            showMessage("No se encontró la dirección de la universidad.");
            finish();
        }
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
    protected void onDestroy() {
        if (mGoogleApiClient != null) mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    void conectLocation() {
        if (mGoogleApiClient == null)

        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(UbicacionUniversidadActivity.this)
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
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                if (markerListLocation == null)
                    marcarMiUbicacion(mLastLocation, crearVistausuario(null));
                LoadImageUser();
                mostrarDireccion();
            } else {
                if (markerListLocation == null)
                    marcarMiUbicacion(mLastLocation, crearVistausuario(null));
                LoadImageUser();
            }
        } else {
            mostrarDireccion();
        }
    }

    public void mostrarDireccion() {
        //mMap.clear();
        List<String> per = new ArrayList<>();
        per.add(Manifest.permission.ACCESS_FINE_LOCATION);
        per.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (validatePermison(per, UbicacionUniversidadActivity.this, 1)) {//falta el estatus
            if (mLastLocation != null) {
                if (mUniversidad != null) {
                    if (mUniversidad.mDireccion != null) {
                        if (mUniversidad.mDireccion.latitud != null && mUniversidad.mDireccion.longitud != null) {
                            // TODO: 18/05/2018 poner codigo aquiiii.............

                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                mMap.setMyLocationEnabled(true);

                                //    marcarMiUbicacion(mLastLocation, crearVistausuario(null));
                                //   LoadImageUser();


                                Float lat = Float.parseFloat(mUniversidad.mDireccion.latitud);
                                Float lon = Float.parseFloat(mUniversidad.mDireccion.longitud);
                                LatLng uni = new LatLng(lat, lon);
                                mMap.addMarker(new MarkerOptions()
                                        .position(uni)
                                        .title(mUniversidad.nombre)
                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.border_maker1, R.layout.marker_demo, false))));
                                LatLng miUbicacionVer = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                                String url = getUrl(miUbicacionVer, uni);
                                Log.d("onMapClick", url.toString());
                                FetchUrl FetchUrl = new FetchUrl();

                                // Start downloading json data from Google Directions API
                                FetchUrl.execute(url);
                                return;
                            }
                        } else {
                            showMessage("No se encontró la dirección de la universidad.");
                            // marcarMiUbicacion(mLastLocation, crearVistausuario(null));
                            // LoadImageUser();
                        }
                    } else {
                        showMessage("No se encontró la dirección de la universidad.");
                        // marcarMiUbicacion(mLastLocation, crearVistausuario(null));
                        //  LoadImageUser();
                    }
                } else {
                    mMap.setMyLocationEnabled(true);
                    //  marcarMiUbicacion(mLastLocation, crearVistausuario(null));
                    // LoadImageUser();
                }
            } else {
                showMessage("Ocurrió un error al obtener su ubicación");
                //  finish();

                //  marcarMiUbicacion(mLastLocation, crearVistausuario(null));
                // conectLocation();
            }
        }
    }

    public class uri {
        public String uri;
        public String nombre;
    }

    private uri getUri() {
        Persona mPersona = mAllController.getDatosPersona();
        uri mUri = new uri();
        if (mPersona != null) {
            if (!isNullOrEmpty(mPersona.foto)) {
                String[] string = mPersona.foto.split("/");
                mUri.nombre = string[string.length - 1];
                mUri.uri = getUrlImage(mPersona.foto, getApplicationContext());
            } else {
                Usuario mUsuario = mAllController.getusuarioPersona();
                if (mUsuario != null) {
                    mUri.uri = getUrlFacebook(mUsuario.cv_facebook);
                    mUri.nombre = "PefilFacebook.jpg";
                }
            }
        } else {
            Usuario mUsuario = mAllController.getusuarioPersona();
            if (mUsuario != null) {
                if (!isNullOrEmpty(mUsuario.cv_facebook)) {
                    mUri.uri = getUrlFacebook(mUsuario.cv_facebook);
                    mUri.nombre = "PefilFacebook.jpg";
                }
            }
        }
        return mUri;
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

    private void LoadImageUser() {
        uri uri = getUri();
        new Utils.DownloadImage(uri.uri, mDownloadImageInterface, uri.nombre).execute();
    }

    private String mpatch;

    Utils.DownloadImage.downloadImageInterface mDownloadImageInterface = new Utils.DownloadImage.downloadImageInterface() {
        @Override
        public void Onsucces(String patch) {
            mpatch = patch;
            if (mLastLocation != null) {
                //if (mBitmap == null)
                Bitmap mBitmap = crearVistausuario(patch);
                marcarMiUbicacion(mLastLocation, mBitmap);
            }
        }
    };
    Marker markerListLocation = null;

    private void marcarMiUbicacion(Location location, Bitmap mBitmap) {
        if (location != null && mMap != null) {
            if (mLastLocation != null) {
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
            mLastLocation = location;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            mostrarDireccion();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        configureLoad();

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    mLastLocation = location;
                    mostrarDireccion();
                    if (markerListLocation == null)
                        marcarMiUbicacion(mLastLocation, crearVistausuario(null));
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

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            mLastLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            if (mLastLocation != null) {
                //   showMessage(String.valueOf(mLastLocation.getLatitude()) + String.valueOf(mLastLocation.getLongitude()) + "-> manual");
                //mostrarDireccion();
                LoadImageUser();
            }
            return;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // metodos para los mapas

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                Parser parser = new Parser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyAdoRAAgr9jLrPubtOtncrL9OH9Y8pJtcE";
        //String url2 = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters + "&key=" + MY_API_KEY


        return url;
    }
}
