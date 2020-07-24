package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class WorkerWorkMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String job;
    private Marker marker;
    private DatabaseReference mReference;
    private UserModel model;
    private HelpModel modelJob;
    private MaterialModel materialModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_work_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.worker_map);
        mapFragment.getMapAsync(this);

        mReference = FirebaseDatabase.getInstance().getReference();
        job=getIntent().getStringExtra("job");
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
        LatLng work = new LatLng(Double.parseDouble(getIntent().getStringExtra("lat")), Double.parseDouble(getIntent().getStringExtra("lon")));
        mMap.addMarker(new MarkerOptions().position(work).title(getIntent().getStringExtra("address"))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(work, 12.0f));
        if (getIntent().getStringExtra("id").equals("hw")) {
            mReference.child("UserInfo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        Log.d("Children", s.toString());
                        UserModel mModel = s.getValue(UserModel.class);
                        Log.d("in", mModel.getRole());
                        if (mModel.getRole().equals("Health Worker")) {
                                marker = googleMap.addMarker(new MarkerOptions()
                                        .title(mModel.getName())
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
//                    Toast.makeText(WorkerWorkMapsActivity.this, Objects.requireNonNull(marker.getTag()).toString(), Toast.LENGTH_SHORT).show();
//                    Intent ew = new Intent(WorkerWorkMapsActivity.this, WorksViewActivity.class);
//                    ew.putExtra("id", "hw");
//                    ew.putExtra("uid", marker.getTag().toString());
//                    startActivity(ew);
//                    finish();
                    openWorkerDialog(marker.getTag().toString());
                }
            });
        }
        else if (getIntent().getStringExtra("id").equals("ri")) {
            mReference.child("UserInfo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        Log.d("Children", s.toString());
                        UserModel mModel = s.getValue(UserModel.class);
                        Log.d("in", mModel.getRole());
                        if (mModel.getRole().equals("Volunteer")) {
                            marker = googleMap.addMarker(new MarkerOptions()
                                    .title(mModel.getName())
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
                    openWorkerDialog(marker.getTag().toString());
                }
            });
        }
    }

    private void openWorkerDialog(String uid) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.worker_view_dialog, null);
        builder.setView(view);
        mReference.child("UserInfo").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TextView name = view.findViewById(R.id.worker_name);
                TextView num = view.findViewById(R.id.worker_num);
                model = dataSnapshot.getValue(UserModel.class);
                name.setText(model.getName());
                num.setText(model.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        builder.setPositiveButton(R.string.assign, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getIntent().getStringExtra("id").equals("hw")) {
                    mReference.child("HealthWorks").child(job).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            modelJob = dataSnapshot.getValue(HelpModel.class);
                            Log.d("da", dataSnapshot.toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    mReference.child("UserInfo").child(uid).child("Works").child(job).setValue(modelJob).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mReference.child("HealthWorks").child(job).child("assignedTo").setValue(uid);
                                Toast.makeText(WorkerWorkMapsActivity.this, "Assigned", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(WorkerWorkMapsActivity.this, AdminHealthWorkerActivity.class));
                                finish();
                            }
                        }
                    });
                }
                else if (getIntent().getStringExtra("id").equals("ri")) {
                    Log.d("job",job);
                    WorkDetailsModel workDetailsModel = new WorkDetailsModel("Collection of Relief Materials", job);
                    String workey = mReference.child("UserInfo").child(uid).child("Works").push().getKey();
                    mReference.child("UserInfo").child(uid).child("Works").child(workey).setValue(workDetailsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mReference.child("ReliefMaterials").child(job).child("assignedTo").setValue(uid);
                                mReference.child("UserInfo").child(job).child("ReliefMaterials").child(getIntent().getStringExtra("key")).child("assignedTo").setValue(uid);
                                Toast.makeText(WorkerWorkMapsActivity.this, "Assigned", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(WorkerWorkMapsActivity.this, AdminHomeActivity.class));
                                finish();
                            }
                        }
                    });
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }
}