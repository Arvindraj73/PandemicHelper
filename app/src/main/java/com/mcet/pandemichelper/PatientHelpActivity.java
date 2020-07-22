package com.mcet.pandemichelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.card.MaterialCardView;

public class PatientHelpActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView arrow2, arrow, alarmBtn;
    private MaterialCardView cardView, cardView1, cardView2, cardView3;
    private LinearLayout expandable1, expandable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_help);

        findViewById(R.id.arrowBtn).setOnClickListener(this);
        findViewById(R.id.alarmBtn).setOnClickListener(this);
        findViewById(R.id.arrowBtn2).setOnClickListener(this);
        findViewById(R.id.reqFoodBtn).setOnClickListener(this);
        findViewById(R.id.reqMedBtn).setOnClickListener(this);
        findViewById(R.id.viewFoodHistory).setOnClickListener(this);
        findViewById(R.id.viewMedHistory).setOnClickListener(this);
        findViewById(R.id.cardView3).setOnClickListener(this);

        arrow = findViewById(R.id.arrowBtn);
        alarmBtn = findViewById(R.id.alarmBtn);
        arrow2 = findViewById(R.id.arrowBtn2);
        expandable = findViewById(R.id.expandableView);
        expandable1 = findViewById(R.id.expandableView1);
        cardView = findViewById(R.id.cardView);
        cardView2 = findViewById(R.id.cardView2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrowBtn:
                if (expandable.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandable.setVisibility(View.VISIBLE);
                    arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                    break;
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandable.setVisibility(View.GONE);
                    arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    break;
                }

            case R.id.arrowBtn2:
                if (expandable1.getVisibility() == View.GONE) {
                    TransitionManager.beginDelayedTransition(cardView2, new AutoTransition());
                    expandable1.setVisibility(View.VISIBLE);
                    arrow2.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                    break;
                } else {
                    TransitionManager.beginDelayedTransition(cardView2, new AutoTransition());
                    expandable1.setVisibility(View.GONE);
                    arrow2.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    break;
                }

            case R.id.alarmBtn:
                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_HOUR, 10);
                intent.putExtra(AlarmClock.EXTRA_MINUTES, 20);
                startActivity(intent);
                break;

            case R.id.reqFoodBtn:
                Intent foodIntent = new Intent(PatientHelpActivity.this, RequestHelpActivity.class);
                foodIntent.putExtra("id", "food");
                startActivity(foodIntent);
                break;

            case R.id.reqMedBtn:
                Intent medIntent = new Intent(PatientHelpActivity.this, RequestHelpActivity.class);
                medIntent.putExtra("id", "med");
                startActivity(medIntent);
                break;

            case R.id.cardView3:
                Intent i = new Intent(PatientHelpActivity.this, CoursesActivity.class);
                i.putExtra("id", "hos");
                startActivity(i);
                break;

            case R.id.viewMedHistory:
                Intent j = new Intent(PatientHelpActivity.this, HistoryActivity.class);
                j.putExtra("id", "med");
                startActivity(j);
                break;

            case R.id.viewFoodHistory:
                Intent k = new Intent(PatientHelpActivity.this, HistoryActivity.class);
                k.putExtra("id", "food");
                startActivity(k);
                break;

        }
    }
}