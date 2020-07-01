package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

public class CreateWorkActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private MaterialButton mSubmit,select;

    private TextInputLayout mWork,mDesc,mPhone,mNum;

    private TextView errorView;

    private String work,desc,numWorkers,phone;

    private ProgressDialog progressDialog;

    private FirebaseDatabase mData;
    private DatabaseReference mRef;

    private String id,lat,lon;

    private int REQUEST_CODE = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            AddressData addressData = data.getParcelableExtra("ADDRESS_INTENT");
            lat = String.valueOf(addressData.getLatitude());
            lon = String.valueOf(addressData.getLongitude());
            Toast.makeText(CreateWorkActivity.this, addressData.toString(), Toast.LENGTH_SHORT).show();
        }
    }
//
//    private void goToPickerActivity() {
//        startActivityForResult(
//                new PlacePicker.IntentBuilder()
//                        .accessToken(getString(R.string.access_token))
//                        .placeOptions(PlacePickerOptions.builder()
//                                .statingCameraPosition(new CameraPosition.Builder()
//                                        .target(new LatLng(40.7544, -73.9862)).zoom(16).build())
//                                .build())
//                        .build(this), REQUEST_CODE);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Mapbox.getInstance(this,"pk.eyJ1IjoiZG03MyIsImEiOiJja2F0eTBlcmQwNjBrMnlvOWZsNndrMDBlIn0.C4OyEpuKG1Ov4TYQeU0RNQ");
        setContentView(R.layout.activity_create_work);

        mAuth = FirebaseAuth.getInstance();

        mWork = findViewById(R.id.workText);
        mPhone = findViewById(R.id.phoneText);
        mNum = findViewById(R.id.noOfText);
        mDesc = findViewById(R.id.descText);
        
        mSubmit = findViewById(R.id.submitBtn);
        select = findViewById(R.id.selectLoc);

        errorView = findViewById(R.id.errorView);

        progressDialog = new ProgressDialog(this);

        mData = FirebaseDatabase.getInstance();
        mRef = mData.getReference();

        id = getIntent().getStringExtra("id");

        if (id.equals("toll")){
            mWork.setHint("Department Name");
            mNum.setVisibility(View.GONE);
            mDesc.setVisibility(View.GONE);
            select.setVisibility(View.GONE);
        }
        else if (id.equals("ph")){
            mWork.setHint("Name");
            mNum.setVisibility(View.GONE);
            mDesc.setVisibility(View.GONE);
        }
        else if (id.equals("labs")){
            mNum.setVisibility(View.GONE);
            mDesc.setVisibility(View.GONE);
            mWork.setHint("Lab Name");
        }
        else if (id.equals("geo")){
            mNum.setVisibility(View.GONE);
            mDesc.setVisibility(View.GONE);
            mWork.setHint("Radius");
            mPhone.setVisibility(View.GONE);
            mWork.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else if (id.equals("sm")){
            mNum.setVisibility(View.GONE);
            mDesc.setVisibility(View.GONE);
            mWork.setHint("Shelter Name");
        }


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new PlacePicker.IntentBuilder()
                        .setLatLong(25.4670, 91.3662)
                        .showLatLong(true)
                        .setMapType(MapType.NORMAL)
                        .build(CreateWorkActivity.this);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean noErrors = true;
                phone = mPhone.getEditText().getText().toString();
                work = mWork.getEditText().getText().toString();
                desc = mDesc.getEditText().getText().toString();
                numWorkers = mNum.getEditText().getText().toString();

//                //Log.d("df",String.valueOf(phoneStr.length()));
//                if (work.isEmpty()) {
//                    mWork.setError("Field Must Not Be Empty");
//                    noErrors = false;
//                }
////                else if(work.length()!=10){
////                    mWork.setError("Enter a valid Phone Number");
////                    noErrors = false;
////                }
//                else {
//                    mWork.setError(null);
//                }
//
//                if (phone.isEmpty()) {
//                    mPhone.setError("Field Must Not Be Empty");
//                    noErrors = false;
//                }
//                else if(phone.length()!=10){
//                    mPhone.setError("Enter a valid Phone Number");
//                    noErrors = false;
//                }
//                else {
//                    mPhone.setError(null);
//                }
//
//                if (desc.isEmpty()) {
//                    mDesc.setError("Field Must Not Be Empty");
//                    noErrors = false;
//                }
////                else if(desc.length()!=10){
////                    mDesc.setError("Enter a valid Phone Number");
////                    noErrors = false;
////                }
//                else {
//                    mDesc.setError(null);
//                }
//
//                if (numWorkers.isEmpty()) {
//                    mNum.setError("Field Must Not Be Empty");
//                    noErrors = false;
//                }
////                else if(numWorkers.length()!=10){
////                    mNum.setError("Enter a valid Phone Number");
////                    noErrors = false;
////                }
//                else {
//                    mNum.setError(null);
//                }
//
//                if(noErrors){

                Toast.makeText(CreateWorkActivity.this,id,Toast.LENGTH_SHORT).show();

                    progressDialog.setMessage("Submitting your details...");
                    progressDialog.show();

                    if (id.equals("labs")){
                        WorkDetailsModel workDetailsModel = new WorkDetailsModel(work,phone,lat,lon);
                        mRef.child("Labs/").push().setValue(workDetailsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    progressDialog.dismiss();
                                    startActivity(new Intent(CreateWorkActivity.this,AdminHomeActivity.class));

                                }

                            }
                        });
                    }
                    else if (id.equals("ph")){
                        WorkDetailsModel workDetailsModel = new WorkDetailsModel(work,phone,lat,lon);
                        mRef.child("PublicHealthCare/").push().setValue(workDetailsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    progressDialog.dismiss();
                                    startActivity(new Intent(CreateWorkActivity.this,AdminHomeActivity.class));

                                }

                            }
                        });
                    }
                    else if (id.equals("toll")){
                        CallModel callModel = new CallModel(work,phone);
                        mRef.child("HelplineNumbers/").push().setValue(callModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    progressDialog.dismiss();
                                    startActivity(new Intent(CreateWorkActivity.this,AdminHomeActivity.class));

                                }

                            }
                        });
                    }
                    else if (id.equals("geo")){
                        WorkDetailsModel workDetailsModel = new WorkDetailsModel(work,lat,lon);
                        mRef.child("GeoFences/").push().setValue(workDetailsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    progressDialog.dismiss();
                                    startActivity(new Intent(CreateWorkActivity.this,AdminHomeActivity.class));

                                }

                            }
                        });
                    }
                    else if (id.equals("vol")){
                        WorkDetailsModel workDetailsModel = new WorkDetailsModel(work,desc,phone,numWorkers,"Not Assigned",lat,lon);
                        mRef.child("VolunteerWorks/").push().setValue(workDetailsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    progressDialog.dismiss();
                                    startActivity(new Intent(CreateWorkActivity.this,AdminVolunteerActivity.class));

                                }

                            }
                        });
                    }
                    else if (id.equals("sm")){
                        WorkDetailsModel workDetailsModel = new WorkDetailsModel(work,phone,lat,lon);
                        mRef.child("Shelter/").push().setValue(workDetailsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    progressDialog.dismiss();
                                    startActivity(new Intent(CreateWorkActivity.this,AdminHomeActivity.class));

                                }

                            }
                        });
                    }
                }

            //}
        });

    }
}
