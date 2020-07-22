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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.util.ArrayList;
import java.util.List;

public class UnorganisedActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mRef;

    private FirebaseUser mUser;

    private TextView location, nameText, email, phone;
    private TextInputLayout name, work, phoneNumber;
    private SharedPreferences preferences;

    private DatabaseReference reference;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unorganised);
        findViewById(R.id.rBtn).setOnClickListener(this);
        findViewById(R.id.selectLocationW).setOnClickListener(this);

        work = findViewById(R.id.workName);

        nameText = findViewById(R.id.unorgName);
        phone = findViewById(R.id.unorgPhone);
        email = findViewById(R.id.unorgEmail);
        location = findViewById(R.id.myLocationW);

        mRef = FirebaseDatabase.getInstance().getReference("UserInfo/");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("UserInfo/" + mUser.getUid());

        preferences = getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);
        uid = getIntent().getStringExtra("uid");
        mRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel mModel = dataSnapshot.getValue(UserModel.class);
                nameText.setText(mModel.getName());
                email.setText(mModel.getEmail());
                phone.setText(mModel.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        CallModel model = new CallModel(
                nameText.getText().toString(),
                location.getText().toString(),
                phone.getText().toString(),
                work.getEditText().getText().toString()
                );
        reference.child("UnorganisedWorkRequests").push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mRef.child(uid).child("Works").child(mUser.getUid()).setValue(model);
                    Toast.makeText(UnorganisedActivity.this, "Request Send", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UnorganisedActivity.this, HomeActivity.class));
                    finish();
                }
            }
        });
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

}