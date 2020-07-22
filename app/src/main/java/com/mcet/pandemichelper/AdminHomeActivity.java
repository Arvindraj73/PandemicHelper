package com.mcet.pandemichelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialToolbar mAppBar;
    private FirebaseAuth auth;
    private SharedPreferences preferences;

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

        findViewById(R.id.card_essential).setOnClickListener(this);
        findViewById(R.id.card_healthworker).setOnClickListener(this);
        findViewById(R.id.card_personalpass).setOnClickListener(this);
        findViewById(R.id.card_Tpass).setOnClickListener(this);
        findViewById(R.id.card_orphange_center).setOnClickListener(this);
        findViewById(R.id.card_reliefmaterialcenter).setOnClickListener(this);
        findViewById(R.id.card_vulnerable_center).setOnClickListener(this);
        findViewById(R.id.card_food_Request).setOnClickListener(this);
        mAppBar = findViewById(R.id.appbarAdmin);

        auth = FirebaseAuth.getInstance();
        preferences = getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);
        mAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logoutAdmin:
                        logOut();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void logOut() {
        auth.signOut();
        preferences.getAll().clear();
        finish();
        startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class));
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
                Intent iVol = new Intent(AdminHomeActivity.this,TabActivity.class);
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

            case R.id.card_essential:
                Intent iE=new Intent(AdminHomeActivity.this,AdminEssentialWorkerActivity.class);
                iE.putExtra("id","ew");
                startActivity(iE);
                break;
            case R.id.card_healthworker:
                Intent ih=new Intent(AdminHomeActivity.this, AdminHealthWorkerActivity.class);
                ih.putExtra("id","hw");
                startActivity(ih);
                break;
            case R.id.card_personalpass:
                Intent ip=new Intent(AdminHomeActivity.this,AdminPersonalPassActivity.class);
                ip.putExtra("id","ip");
                startActivity(ip);
                break;
            case R.id.card_Tpass:
                startActivity(new Intent(AdminHomeActivity.this,AdminTransportPassActivity.class));
                break;
            case R.id.card_reliefmaterialcenter:
                Intent rmc = new Intent(AdminHomeActivity.this, CreateWorkActivity.class);
                rmc.putExtra("id", "rmc");
                startActivity(rmc);

                break;
            case R.id.card_orphange_center:
                Intent oc = new Intent(AdminHomeActivity.this, CreateWorkActivity.class);
                oc.putExtra("id", "oc");
                startActivity(oc);

                break;
            case R.id.card_vulnerable_center:
                Intent vc=new Intent(AdminHomeActivity.this, CreateWorkActivity.class);
                vc.putExtra("id","vc");
                startActivity(vc);
                break;
            case R.id.card_food_Request:
                startActivity(new Intent(AdminHomeActivity.this,AdminFoodRequestActivity.class));
                break;
        }

    }
}