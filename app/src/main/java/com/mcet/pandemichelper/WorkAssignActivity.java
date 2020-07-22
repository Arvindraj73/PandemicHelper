package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WorkAssignActivity extends AppCompatActivity {

    private TextView work, desc, phone, num, status, t1, t2;

    private MaterialButton assign;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private LinearLayout linearLayout;

    private String key, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_assign);

        work = findViewById(R.id.workView);
        desc = findViewById(R.id.descView);
        phone = findViewById(R.id.phoneView);
        num = findViewById(R.id.numView);
        status = findViewById(R.id.statusView);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);

        linearLayout = findViewById(R.id.linearLayout);

        assign = findViewById(R.id.assignBtn);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        key = getIntent().getStringExtra("key");
        name = getIntent().getStringExtra("name");


        if (getIntent().getStringExtra("id").equals("admin")) {

            linearLayout.setVisibility(View.GONE);

            assign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent assignIntent = new Intent(WorkAssignActivity.this, VolunteerListActivity.class);
                    assignIntent.putExtra("key", key);
                    assignIntent.putExtra("name", name);
                    startActivity(assignIntent);

                }
            });

        }
        else{
            assign.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }

        reference.child("VolunteerWorks/"+key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                WorkDetailsModel model = dataSnapshot.getValue(WorkDetailsModel.class);

                if (getIntent().getStringExtra("id").equals("admin")){

                    work.setText(model.getName());
                    desc.setText(model.getDescription());
                    phone.setText(model.getPhoneNumber());
                    num.setText(model.getNoOfWorkers());
                    status.setText(model.getStatus());

                }
                else {
                    work.setText(model.getName());
                    desc.setText(model.getDescription());
                    phone.setText(model.getPhoneNumber());
                    num.setVisibility(View.GONE);
                    status.setVisibility(View.GONE);
                    t1.setVisibility(View.GONE);
                    t2.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
