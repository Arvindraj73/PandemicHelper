package com.mcet.pandemichelper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
//import com.razorpay.Checkout;
//import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class DonateActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        bottomNavigationView = findViewById(R.id.donate_bnv);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);

        openFragment(new FundFragment());
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.money:
                    openFragment(new FundFragment());
                    return true;
                case R.id.materials:
                    openFragment(new MaterialsFragment());
                    return true;
                case R.id.history:
                    Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                    intent.putExtra("id", "rm");
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.d_container, fragment);
        fragmentTransaction.commit();
    }
}
