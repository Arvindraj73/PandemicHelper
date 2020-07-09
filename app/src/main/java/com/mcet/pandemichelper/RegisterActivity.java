package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mAuth;

    private MaterialButton mReg;

    private TextInputLayout mName, mEmail, mPhone, mPass, mConfirmPass, mSpeciality;

    private TextView errorView;

    private String name, email, pass, cpass, phone, deviceToken;

    private ProgressDialog progressDialog;

    private FirebaseDatabase mData;
    private DatabaseReference mRef;

    private static final int MY_REQUEST = 100;
    private static final int PS_REQUEST = 200;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;

    private Spinner spinner;

    private GoogleApiClient client;
    private LocationRequest request;
    private Location mLastlocation;

    private double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mName = findViewById(R.id.nameText);
        mPhone = findViewById(R.id.phoneText);
        mPass = findViewById(R.id.passwordText);
        mConfirmPass = findViewById(R.id.passText);
        mReg = findViewById(R.id.regBtn);
        errorView = findViewById(R.id.errorView);
        mEmail = findViewById(R.id.emailText);
        spinner = findViewById(R.id.spinner);
        mSpeciality = findViewById(R.id.specialityText);

        progressDialog = new ProgressDialog(this);

        mData = FirebaseDatabase.getInstance();
        mRef = mData.getReference();

        setUpLocation();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) {
                    Log.d("DocI", "inside");
                    mSpeciality.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (spinner.getSelectedItem().toString().equals("Doctor")) {
            Log.d("Doc", "inside");
            mSpeciality.setVisibility(View.VISIBLE);
        }

        mReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean noErrors = true;
                phone = mPhone.getEditText().getText().toString();
                name = mName.getEditText().getText().toString();
                email = mEmail.getEditText().getText().toString();
                pass = mPass.getEditText().getText().toString();
                cpass = mConfirmPass.getEditText().getText().toString();

                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            deviceToken = task.getResult().getToken();
                            Log.d("token", deviceToken);
                        }
                    }
                });

                //Log.d("df",String.valueOf(phoneStr.length()));
                if (name.isEmpty()) {
                    mName.setError("Field Must Not Be Empty");
                    noErrors = false;
                }
//                else if(name.length()!=10){
//                    mName.setError("Enter a valid Phone Number");
//                    noErrors = false;
//                }
                else {
                    mName.setError(null);
                }

                if (phone.isEmpty()) {
                    mPhone.setError("Field Must Not Be Empty");
                    noErrors = false;
                } else if (phone.length() != 10) {
                    mPhone.setError("Enter a valid Phone Number");
                    noErrors = false;
                } else {
                    mPhone.setError(null);
                }

                if (email.isEmpty()) {
                    mEmail.setError("Field Must Not Be Empty");
                    noErrors = false;
                }
//                else if(email.length()!=10){
//                    mEmail.setError("Enter a valid Phone Number");
//                    noErrors = false;
//                }
                else {
                    mEmail.setError(null);
                }

                if (pass.isEmpty()) {
                    mPass.setError("Field Must Not Be Empty");
                    noErrors = false;
                }
//                else if(pass.length()!=10){
//                    mPass.setError("Enter a valid Phone Number");
//                    noErrors = false;
//                }
                else {
                    mPass.setError(null);
                }

                if (cpass.isEmpty()) {
                    mConfirmPass.setError("Field Must Not Be Empty");
                    noErrors = false;
                }
//                else if(cpass.length()!=10){
//                    mConfirmPass.setError("Enter a valid Phone Number");
//                    noErrors = false;
//                }
                else {
                    mConfirmPass.setError(null);
                }

                if (spinner.getSelectedItem().equals("Select Your Role")) {
                    errorView.setText("Please select a valid role");
                    noErrors = false;
                } else {
                    noErrors = true;
                }

                if (!pass.equals(cpass)) {
                    mConfirmPass.setError("Passwords does not match");
                    mPass.setError("Passwords does not match");
                    mConfirmPass.getEditText().setText("");
                    mPass.getEditText().setText("");
                    noErrors = false;
                }

                if(noErrors){

                    progressDialog.setMessage("Registering your details...");
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

                                UserModel mUser = new UserModel(name, phone, email, pass, mFirebaseUser.getUid(), spinner.getSelectedItem().toString(), String.valueOf(lat), String.valueOf(lon), deviceToken);
                                mRef.child("UserInfo/"+mFirebaseUser.getUid()).setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            if (spinner.getSelectedItem().toString().equals("Doctor")) {
                                                mRef.child("UserInfo/" + mFirebaseUser.getUid()).child("/status").setValue("0");
                                                mRef.child("Doctors").push().setValue(mUser.getUid());
                                                mRef.child("UserInfo/" + mFirebaseUser.getUid() + "/DocDetails/Speciality").setValue(mSpeciality.getEditText().getText().toString());
                                            }

                                            progressDialog.dismiss();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                                        }

                                    }
                                });

                            }

                        }
                    });

                }

            }
        });

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

    private void createLocationRequest() {

        Log.d("DATA", "Req");
        request = new LocationRequest();
        request.setInterval(UPDATE_INTERVAL);
        request.setFastestInterval(FASTEST_INTERVAL);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setSmallestDisplacement(DISPLACEMENT);
    }

    private void displayLocation() {

        Log.d("DATA", "Dis");
        if (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastlocation = LocationServices.FusedLocationApi.getLastLocation(client);

        Log.d("DATA", String.valueOf(mLastlocation));
        try {
            lat = mLastlocation.getLatitude();
            lon = mLastlocation.getLongitude();
            Log.d("DATA", lat + " " + lon);

        } catch (Exception e) {
            Log.d("DATA", e.toString());
        }
    }

    private void buildGoogleApiClient() {

        Log.d("DATA", "Client");
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
    }

    private boolean checkServices() {

        Log.d("DATA", "Check");
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
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
