package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CONTROL_LOCATION_UPDATES;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;

    private GoogleApiClient client;
    private LocationRequest request;
    private Location mLastlocation;

    private static final int MY_REQUEST = 100;
    private static final int PS_REQUEST = 200;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    private DatabaseReference reference,geoRef;
    private GeoFire geoFire;

    private Marker marker;

    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("UserInfo");
        geoRef = FirebaseDatabase.getInstance().getReference();
        geoFire = new GeoFire(reference.child(mUser.getUid()));


        setUpLocation();
    }

    private void setUpLocation() {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{
                    ACCESS_COARSE_LOCATION,ACCESS_FINE_LOCATION
            },MY_REQUEST);

        }
        else{

            if (checkServices()){
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (checkServices()){
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }
                break;
        }
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        mLastlocation = LocationServices.FusedLocationApi.getLastLocation(client);
        if (mLastlocation != null){
            double lat = mLastlocation.getLatitude();
            double lon = mLastlocation.getLongitude();
            Log.d("DATA",String.valueOf(lat)+" "+String.valueOf(lon));

            geoFire.setLocation("Location", new GeoLocation(lat, lon),
                    new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            if (marker != null)
                                marker.remove();
                            marker = mMap.addMarker(new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                        .position(new LatLng(lat,lon))
                                        .title("You"));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lon),12.0f));
                        }
                    });

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
        if (resultcode != ConnectionResult.SUCCESS){
            if (GooglePlayServicesUtil.isUserRecoverableError(resultcode))
                GooglePlayServicesUtil.getErrorDialog(resultcode,this,PS_REQUEST).show();
            else {
                Toast.makeText(this,"Device Not Supported",Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        geoRef.child("GeoFences").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    Log.d("Children", s.toString());
                    WorkDetailsModel mModel = s.getValue(WorkDetailsModel.class);
                    Log.d("in", mModel.getName());
                    Log.d("in", "in");
                    LatLng area = new LatLng(Double.parseDouble(mModel.getLat()), Double.parseDouble(mModel.getLon()));
                    mMap.addCircle(new CircleOptions()
                            .center(area)
                            .radius(Double.parseDouble(mModel.getName()))
                            .strokeColor(Color.BLUE)
                            .fillColor(R.color.colorPrimaryLight)
                            .strokeWidth(5.0f));
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(mModel.getLat()), Double.parseDouble(mModel.getLon())))
                            .title("Fence"));

                    GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(area.latitude,area.longitude),0.5f);
                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                        @Override
                        public void onKeyEntered(String key, GeoLocation location) {
                            Log.d("gyg","inside");
                            sendNotification("PandemicHelper","You have entered a danger zone");
                        }

                        @Override
                        public void onKeyExited(String key) {
                            Log.d("gyg","outside");
                            sendNotification("PandemicHelper","You have exited a danger zone");
                        }

                        @Override
                        public void onKeyMoved(String key, GeoLocation location) {

                        }

                        @Override
                        public void onGeoQueryReady() {

                        }

                        @Override
                        public void onGeoQueryError(DatabaseError error) {
                            Log.d("ED",error.toString());
                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//
//
//        LatLng area2 = new LatLng(11.100,76.92);
//        mMap.addCircle(new CircleOptions()
//                .center(area2)
//                .radius(500)
//                .strokeColor(Color.BLUE)
//                .fillColor(R.color.colorPrimaryLight)
//                .strokeWidth(5.0f));
//
//        GeoQuery geoQuery2 = geoFire.queryAtLocation(new GeoLocation(area2.latitude,area2.longitude),0.5f);
//        geoQuery2.addGeoQueryEventListener(new GeoQueryEventListener() {
//            @Override
//            public void onKeyEntered(String key, GeoLocation location) {
//                Log.d("gyg","inside1");
//                sendNotification("PandemicHelper",key+ " have entered a danger zone");
//            }
//
//            @Override
//            public void onKeyExited(String key) {
//                Log.d("gyg","outside1");
//                sendNotification("PandemicHelper",key+ " have exited a danger zone");
//            }
//
//            @Override
//            public void onKeyMoved(String key, GeoLocation location) {
//
//            }
//
//            @Override
//            public void onGeoQueryReady() {
//
//            }
//
//            @Override
//            public void onGeoQueryError(DatabaseError error) {
//                Log.d("ED",error.toString());
//            }
//        });


    }

    private void sendNotification(String pandemicHelper, String s) {
        Log.d("NO","notify");
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_earth)
                .setContentTitle(pandemicHelper)
                .setContentText(s);
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this,MapsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "geo";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Geofence",
                    NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }
        manager.notify(0,builder.build());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdate();
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client,request, this);
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
