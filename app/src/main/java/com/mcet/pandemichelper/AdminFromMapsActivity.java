package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class AdminFromMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    Float flat,flog,tlat,tlog;
    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest request;
    private Location mLastlocation;

    private LinearLayout legend;
    private FloatingActionButton history;

    private static final int MY_REQUEST = 100;
    private static final int PS_REQUEST = 200;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    private int PROXIMITY_RADIUS = 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_from_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        if(getIntent().getStringExtra("id").equals("hw"))
        {
            flat=Float.parseFloat(getIntent().getStringExtra("lat"));
            flog=Float.parseFloat(getIntent().getStringExtra("lon"));

            LatLng from = new LatLng(flat, flog);
            mMap.addMarker(new MarkerOptions().position(from).title(getIntent().getStringExtra("address")));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(from));

        }
        else if(getIntent().getStringExtra("id").equals("ri"))
        {
            flat=Float.parseFloat(getIntent().getStringExtra("lat"));
            flog=Float.parseFloat(getIntent().getStringExtra("lon"));

            LatLng from = new LatLng(flat, flog);
            mMap.addMarker(new MarkerOptions().position(from).title(getIntent().getStringExtra("address")));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(from));

        }
        else if(getIntent().getStringExtra("id").equals("tp"))
        {
            flat=Float.parseFloat(getIntent().getStringExtra("lat"));
            flog=Float.parseFloat(getIntent().getStringExtra("lon"));

            LatLng from = new LatLng(flat, flog);
            mMap.addMarker(new MarkerOptions().position(from).title(getIntent().getStringExtra("Here ")));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(from));

        }
        else if(getIntent().getStringExtra("id").equals("ew"))
        {
            flat=Float.parseFloat(getIntent().getStringExtra("lat"));
            flog=Float.parseFloat(getIntent().getStringExtra("lon"));

            LatLng from = new LatLng(flat, flog);
            mMap.addMarker(new MarkerOptions().position(from).title(getIntent().getStringExtra("job")));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(from));

        }

        else{
        flat=Float.parseFloat(getIntent().getStringExtra("flat"));
        flog=Float.parseFloat(getIntent().getStringExtra("flog"));
        tlat=Float.parseFloat(getIntent().getStringExtra("tlat"));
        tlog=Float.parseFloat(getIntent().getStringExtra("tlog"));


        // Add a marker in Sydney and move the camera
        LatLng from = new LatLng(flat, flog);
        mMap.addMarker(new MarkerOptions().position(from).title("From Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(from));
        LatLng to = new LatLng(tlat, tlog);
        mMap.addMarker(new MarkerOptions().position(to).title("To Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(to));
    }}

    private void setUpLocation() {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION
            }, MY_REQUEST);

        } else {

            if (checkServices()) {
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkServices()) {
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }
                break;
        }
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastlocation = LocationServices.FusedLocationApi.getLastLocation(client);
        if (mLastlocation != null) {
            double lat = mLastlocation.getLatitude();
            double lon = mLastlocation.getLongitude();
            Log.d("DATA", lat + " " + lon);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 12.0f));

        }
    }

    private void createLocationRequest() {
        request = new LocationRequest();
        request.setInterval(UPDATE_INTERVAL);
        request.setFastestInterval(FASTEST_INTERVAL);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }

    private boolean checkServices() {

        int resultcode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultcode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultcode))
                GooglePlayServicesUtil.getErrorDialog(resultcode, this, PS_REQUEST).show();
            else {
                Toast.makeText(this, "Device Not Supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdate();
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        client.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastlocation = location;
        displayLocation();
    }
}