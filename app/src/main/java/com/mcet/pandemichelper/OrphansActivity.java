package com.mcet.pandemichelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OrphansActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orphans);

        findViewById(R.id.nearbyOr).setOnClickListener(this);
        findViewById(R.id.vul_shel).setOnClickListener(this);
        findViewById(R.id.adopt_orphans).setOnClickListener(this);
        findViewById(R.id.indian_orphanage).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.nearbyOr:
                Intent i = new Intent(OrphansActivity.this, DoctorActivity.class);
                i.putExtra("id", "orph");
                startActivity(i);
                break;

            case R.id.vul_shel:
                Intent j = new Intent(OrphansActivity.this, DoctorActivity.class);
                j.putExtra("id", "vul");
                startActivity(j);
                break;

            case R.id.adopt_orphans:
                Intent k = new Intent(OrphansActivity.this, CoursesActivity.class);
                k.putExtra("id", "ao");
                startActivity(k);
                break;

            case R.id.indian_orphanage:
                Intent l = new Intent(OrphansActivity.this, CoursesActivity.class);
                l.putExtra("id", "io");
                startActivity(l);
                break;
        }
    }


}