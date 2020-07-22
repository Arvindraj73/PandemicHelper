package com.mcet.pandemichelper;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DoctorActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;

    private GoogleApiClient client;
    private LocationRequest request;
    private Location mLastlocation;

    private FloatingActionButton history;

    private static final int MY_REQUEST = 100;
    private static final int PS_REQUEST = 200;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    private int PROXIMITY_RADIUS = 10000;
    private DatabaseReference mReference;

    private Marker marker;

    private FirebaseUser mUser;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.doc_map);
        mapFragment.getMapAsync(this);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference();

        history = findViewById(R.id.history_floating_action_button);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DoctorActivity.this, HistoryActivity.class);
                i.putExtra("id", "unorg");
                startActivity(i);
            }
        });
        setUpLocation();
        id = getIntent().getStringExtra("id");
        if (id.equals("unorg")) {
            history.setVisibility(View.VISIBLE);
        } else {
            history.setVisibility(View.GONE);
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        Log.d("DDDD", vectorDrawable.getIntrinsicWidth() + " " + vectorDrawable.getIntrinsicHeight());
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (getIntent().getStringExtra("id").equals("doc")) {
            mReference.child("UserInfo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        Log.d("Children", s.toString());
                        UserModel mModel = s.getValue(UserModel.class);
                        Log.d("in", mModel.getRole());
                        if (mModel.getRole().equals("Doctor")) {
                            Log.d("in2", String.valueOf(s.child("/status").getValue()));
                            int status = Integer.parseInt(s.child("/status").getValue().toString());
                            if (status == 0) {
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_cancel))
                                        .title(mModel.getName())
                                        .snippet(mModel.getSpeciality())
                                        .position(new LatLng(Double.parseDouble(mModel.getLat()), Double.parseDouble(mModel.getLon()))));
                                marker.setTag(mModel.getUid());
                            } else if (status == 1) {
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .title(mModel.getName())
                                        .snippet(mModel.getSpeciality())
                                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_location))
                                        .position(new LatLng(Double.parseDouble(mModel.getLat()), Double.parseDouble(mModel.getLon()))));
                                marker.setTag(mModel.getUid());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Toast.makeText(DoctorActivity.this, Objects.requireNonNull(marker.getTag()).toString(), Toast.LENGTH_SHORT).show();
                    // handle the clicked marker object
                    Intent docIntent = new Intent(DoctorActivity.this, DocViewActivity.class);
                    docIntent.putExtra("id", "user");
                    docIntent.putExtra("uid", marker.getTag().toString());
                    startActivity(docIntent);
                    finish();
                }
            });
        } else if (getIntent().getStringExtra("id").equals("psy")) {
            mReference.child("UserInfo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        Log.d("Children", s.toString());
                        UserModel mModel = s.getValue(UserModel.class);
                        Log.d("in", mModel.getRole());
                        if (mModel.getRole().equals("Doctor") && mModel.getSpeciality().equals("Psychiatrist")) {
                            Log.d("in2", String.valueOf(s.child("/status").getValue()));
                            int status = Integer.parseInt(s.child("/status").getValue().toString());
                            if (status == 0) {
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_cancel))
                                        .title(mModel.getName())
                                        .snippet(mModel.getSpeciality())
                                        .position(new LatLng(Double.parseDouble(mModel.getLat()), Double.parseDouble(mModel.getLon()))));
                                marker.setTag(mModel.getUid());
                            } else if (status == 1) {
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .title(mModel.getName())
                                        .snippet(mModel.getSpeciality())
                                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_location))
                                        .position(new LatLng(Double.parseDouble(mModel.getLat()), Double.parseDouble(mModel.getLon()))));
                                marker.setTag(mModel.getUid());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Toast.makeText(DoctorActivity.this, Objects.requireNonNull(marker.getTag()).toString(), Toast.LENGTH_SHORT).show();
                    // handle the clicked marker object
                    Intent docIntent = new Intent(DoctorActivity.this, DocViewActivity.class);
                    docIntent.putExtra("id", "user");
                    docIntent.putExtra("uid", marker.getTag().toString());
                    startActivity(docIntent);
                    finish();
                }
            });
        }
        else if (getIntent().getStringExtra("id").equals("vul")) {
            mReference.child("Vulnerable").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        Log.d("Children", s.toString());
                        WorkDetailsModel mModel = s.getValue(WorkDetailsModel.class);
                        Log.d("in", mModel.getName());
                        Log.d("in", "in");
                        LatLng location = new LatLng(Double.parseDouble(mModel.getLat()), Double.parseDouble(mModel.getLon()));
                        mMap.addMarker(new MarkerOptions().position(location).snippet(mModel.getPhoneNumber()).title(mModel.getName()));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if (getIntent().getStringExtra("id").equals("orph")) {
            mReference.child("orphange Center").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        Log.d("Children", s.toString());
                        WorkDetailsModel mModel = s.getValue(WorkDetailsModel.class);
                        Log.d("in", mModel.getName());
                        Log.d("in", "in");
                        LatLng location = new LatLng(Double.parseDouble(mModel.getLat()), Double.parseDouble(mModel.getLon()));
                        googleMap.addMarker(new MarkerOptions().position(location).snippet(mModel.getPhoneNumber()).title(mModel.getName()));
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if (getIntent().getStringExtra("id").equals("unorg")) {
            mReference.child("UserInfo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        Log.d("Children", s.toString());
                        UserModel mModel = s.getValue(UserModel.class);
                        Log.d("in", mModel.getRole());
                        if (mModel.getRole().equals("Unorganised Worker")) {
                            marker = googleMap.addMarker(new MarkerOptions()
                                    .title(mModel.getName())
                                    .snippet(mModel.getSpeciality())
                                    .position(new LatLng(Double.parseDouble(mModel.getLat()), Double.parseDouble(mModel.getLon()))));
                            marker.setTag(mModel.getUid());

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Toast.makeText(DoctorActivity.this, Objects.requireNonNull(marker.getTag()).toString(), Toast.LENGTH_SHORT).show();
                    // handle the clicked marker object
                    Intent docIntent = new Intent(DoctorActivity.this, UnorganisedActivity.class);
                    docIntent.putExtra("id", "user");
                    docIntent.putExtra("uid", marker.getTag().toString());
                    startActivity(docIntent);
                    finish();
                }
            });
        }
    }

    private String getUrl(double latitude, double longitude) {
        //maps.googleapis.com/maps/api/place/findplacefromtext/json?input=orphanages&inputtype=textquery&key=AIzaSyBzHbDmyGXgM5Sd11V-CIJw38-VqBTdOdk
        StringBuilder googlePlacesUrl = new StringBuilder("maps.googleapis.com/maps/api/place/findplacefromtext/json?input=orphanages&inputtype=textquery");
        googlePlacesUrl.append("locationbaias=circle:" + PROXIMITY_RADIUS + "@" + latitude + "," + longitude);
        googlePlacesUrl.append("&key=" + "AIzaSyBzHbDmyGXgM5Sd11V-CIJw38-VqBTdOdk");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
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
