package com.mcet.pandemichelper;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AdminFromMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    Float flat,flog,tlat,tlog;
    private GoogleMap mMap;

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
}