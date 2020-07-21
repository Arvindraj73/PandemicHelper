package com.mcet.pandemichelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

public class RequestHelpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout name, symptoms, disease, persons;
    private TextView nameText, locationText;
    private ImageButton locationSelect;
    private MaterialButton request;

    private String id;


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

    }

    private void openLocation() {

        Intent intent = new PlacePicker.IntentBuilder()
                .setLatLong(25.4670, 91.3662)
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