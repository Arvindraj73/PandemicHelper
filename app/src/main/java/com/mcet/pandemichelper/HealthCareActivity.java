package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HealthCareActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_care);

        bottomNavigationView = findViewById(R.id.hos_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        openFragment(new HospitalsFragment());

    }

    private void openFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.hos_container, fragment);
        transaction.commit();

    }

        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){

                case R.id.page_1 :
                    openFragment(new HospitalsFragment());
                    return true;
                case R.id.page_2:
                    openFragment(new MedicalsFragment());
                    return true;
                case R.id.page_3:
                    openFragment(new LabsFragment());
                    return true;
                case R.id.page_4:
                    openFragment(new HCFragment());
                    return true;
                case R.id.page_5:
                    openFragment(new ShelterFragment());
                    return true;
            }
            return false;
        }
    };

}
