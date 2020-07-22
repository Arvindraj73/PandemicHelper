package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

public class RequestHelpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout name, symptoms, disease, persons;
    private TextView nameText, locationText;
    private ImageButton locationSelect;
    private MaterialButton request;

    private String id;

    private SharedPreferences preferences;
    private DatabaseReference reference, ref;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request_help);

        name = findViewById(R.id.nameReq);
        symptoms = findViewById(R.id.symptoms);
        disease = findViewById(R.id.cause);
        persons = findViewById(R.id.person_count);
        nameText = findViewById(R.id.reqName);
        locationText = findViewById(R.id.myLocation);

        ref = FirebaseDatabase.getInstance().getReference();
        preferences = getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("UserInfo/" + user.getUid());

        persons.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNumberDialog();
            }
        });

        findViewById(R.id.selectLocation).setOnClickListener(this);
        findViewById(R.id.requestBtn).setOnClickListener(this);

        id = getIntent().getStringExtra("id");

        assert id != null;
        if (id.equals("med")) {
            disease.setVisibility(View.VISIBLE);
            symptoms.setVisibility(View.VISIBLE);
            persons.setVisibility(View.GONE);
            nameText.setText(R.string.reqMed);
        } else if (id.equals("food")) {
            disease.setVisibility(View.GONE);
            symptoms.setVisibility(View.GONE);
            persons.setVisibility(View.VISIBLE);
            nameText.setText(R.string.reqfood);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 14 && resultCode == RESULT_OK && data != null) {
            AddressData addressData = data.getParcelableExtra("ADDRESS_INTENT");
//            lat = String.valueOf(addressData.getLatitude());
//            lon = String.valueOf(addressData.getLongitude());
            Log.d("to", addressData.toString());
            locationText.setText(addressData.toString());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.selectLocation:
                openLocation();
                break;

            case R.id.requestBtn:
                submitDetails();
                break;

        }
    }

    private void submitDetails() {
        if (id.equals("med")) {

            HelpModel model = new HelpModel(name.getEditText().getText().toString(), symptoms.getEditText().getText().toString()
                    , disease.getEditText().getText().toString(), locationText.getText().toString());
            reference.child("MedRequests").push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        ref.child("MedRequests").child(user.getUid()).setValue(model);
                        Toast.makeText(RequestHelpActivity.this, "Request Send", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else if (id.equals("food")) {

            HelpModel model = new HelpModel(name.getEditText().getText().toString()
                    , persons.getEditText().getText().toString()
                    , locationText.getText().toString());
            reference.child("FoodRequests").push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        ref.child("FoodRequests").child(user.getUid()).setValue(model);
                        Toast.makeText(RequestHelpActivity.this, "Request Send", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private void openLocation() {

        Intent intent = new PlacePicker.IntentBuilder()
                .setLatLong(Double.parseDouble(preferences.getString("lat", "25.4670")), Double.parseDouble(preferences.getString("lon", "91.3662")))
                .showLatLong(true)
                .setMapType(MapType.NORMAL)
                .setPlaceSearchBar(true, String.valueOf(R.string.google_maps_key))
                .build(this);
        startActivityForResult(intent, 14);

    }

    private void openNumberDialog() {

        View view = LayoutInflater.from(this).inflate(R.layout.number_picker_dialog_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        NumberPicker picker = view.findViewById(R.id.number_picker);
        builder.setTitle("Select No of Persons")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("persons", String.valueOf(picker.getValue()));
                        persons.getEditText().setText(String.valueOf(picker.getValue()));
                    }
                });

        builder.show();
    }

}