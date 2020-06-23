package com.mcet.pandemichelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;

public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        findViewById(R.id.card_add_lab).setOnClickListener(this);
        findViewById(R.id.card_add_health).setOnClickListener(this);
        findViewById(R.id.card_add_toll).setOnClickListener(this);
        findViewById(R.id.card_add_volunteer).setOnClickListener(this);
        findViewById(R.id.card_v_labour).setOnClickListener(this);
        findViewById(R.id.card_geo).setOnClickListener(this);
        findViewById(R.id.card_sm).setOnClickListener(this);
        findViewById(R.id.card_relif).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.card_add_lab:
                Intent i = new Intent(AdminHomeActivity.this, CreateWorkActivity.class);
                i.putExtra("id", "labs");
                startActivity(i);
                break;
            case R.id.card_add_health:
                Intent iPh = new Intent(AdminHomeActivity.this, CreateWorkActivity.class);
                iPh.putExtra("id", "ph");
                startActivity(iPh);
                break;
            case R.id.card_add_toll:
                Intent iToll = new Intent(AdminHomeActivity.this, CreateWorkActivity.class);
                iToll.putExtra("id", "toll");
                startActivity(iToll);
                break;
            case R.id.card_add_volunteer:
                Intent iVol = new Intent(AdminHomeActivity.this, AdminVolunteerActivity.class);
                iVol.putExtra("id", "vol");
                startActivity(iVol);
                break;

            case R.id.card_v_labour:
                Intent iLab = new Intent(AdminHomeActivity.this, AdminLabourActivity.class);
                iLab.putExtra("id", "lab");
                startActivity(iLab);
                break;

            case R.id.card_geo:
                Intent iGeo = new Intent(AdminHomeActivity.this, CreateWorkActivity.class);
                iGeo.putExtra("id", "geo");
                startActivity(iGeo);
                break;

            case R.id.card_sm:
                Intent iS = new Intent(AdminHomeActivity.this, CreateWorkActivity.class);
                iS.putExtra("id", "sm");
                startActivity(iS);
                break;

            case R.id.card_relif:
                Intent iR = new Intent(AdminHomeActivity.this, AdminVolunteerActivity.class);
                iR.putExtra("id", "rm");
                startActivity(iR);
                break;
        }

    }
}