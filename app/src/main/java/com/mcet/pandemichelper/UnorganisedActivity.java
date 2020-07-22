package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

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

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.util.ArrayList;
import java.util.List;

public class UnorganisedActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView location;
    private TextInputLayout name, work, worker_count, phoneNumber;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unorganised);
        findViewById(R.id.rBtn).setOnClickListener(this);
        findViewById(R.id.selectLocationW).setOnClickListener(this);

        name = findViewById(R.id.nameW);
        work = findViewById(R.id.workName);
        worker_count = findViewById(R.id.worker_count);
        phoneNumber = findViewById(R.id.phnW);

        preferences = getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);

        worker_count.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNumberDialog();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rBtn:
                submitDetails();
                break;

            case R.id.selectLocationW:
                openLocation();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 15 && resultCode == RESULT_OK && data != null) {
            AddressData addressData = data.getParcelableExtra("ADDRESS_INTENT");
//            lat = String.valueOf(addressData.getLatitude());
//            lon = String.valueOf(addressData.getLongitude());
            Log.d("to", addressData.toString());
            location.setText(addressData.toString());
        }

    }

    private void submitDetails() {

    }

    private void openLocation() {

        Intent intent = new PlacePicker.IntentBuilder()
                .setLatLong(Double.parseDouble(preferences.getString("lat", "25.4670")), Double.parseDouble(preferences.getString("lon", "91.3662")))
                .showLatLong(true)
                .setMapType(MapType.NORMAL)
                .setPlaceSearchBar(true, String.valueOf(R.string.google_maps_key))
                .build(this);
        startActivityForResult(intent, 15);

    }

    private void openNumberDialog() {

        View view = LayoutInflater.from(this).inflate(R.layout.number_picker_dialog_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        NumberPicker picker = view.findViewById(R.id.number_picker);
        builder.setTitle("Select No of Workers Required")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("persons", String.valueOf(picker.getValue()));
                        worker_count.getEditText().setText(String.valueOf(picker.getValue()));
                    }
                });

        builder.show();
    }

}