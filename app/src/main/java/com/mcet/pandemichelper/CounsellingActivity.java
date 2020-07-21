package com.mcet.pandemichelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.card.MaterialCardView;

public class CounsellingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_councelling);

        findViewById(R.id.counselling_videos).setOnClickListener(this);
        findViewById(R.id.psychiatrist).setOnClickListener(this);
        findViewById(R.id.cci).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.counselling_videos:
                Intent i = new Intent(CounsellingActivity.this, YoutubeActivity.class);
                i.putExtra("id", "c_videos");
                startActivity(i);
                break;

            case R.id.psychiatrist:
                Intent j = new Intent(CounsellingActivity.this, DoctorActivity.class);
                j.putExtra("id", "psy");
                startActivity(j);
                break;

            case R.id.cci:
                Intent k = new Intent(CounsellingActivity.this, CoursesActivity.class);
                k.putExtra("id", "cci");
                startActivity(k);
                break;
        }
    }
}