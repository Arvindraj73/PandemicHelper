package com.mcet.pandemichelper;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Intent sIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(sIntent);
                        finish();

                    }
                },
                2000);

    }

    @Override
    protected void onStart() {
        super.onStart();


        //finish();
    }
}
