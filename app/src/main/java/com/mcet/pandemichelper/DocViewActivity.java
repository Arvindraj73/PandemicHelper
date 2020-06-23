package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DocViewActivity extends AppCompatActivity {

    private DatabaseReference mRef;

    private FirebaseUser mUser;

    private TextView name, email, phone;
    private MaterialButton appoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_view);

        name = findViewById(R.id.docName);
        phone = findViewById(R.id.docPhone);
        email = findViewById(R.id.docEmail);
        appoint = findViewById(R.id.appoint);

        mRef = FirebaseDatabase.getInstance().getReference("UserInfo/");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        String uid = getIntent().getStringExtra("uid");
        String nameUser = getIntent().getStringExtra("name");
        Log.d("namep", nameUser);

        mRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel mModel = dataSnapshot.getValue(UserModel.class);
                name.setText(mModel.getName());
                email.setText(mModel.getEmail());
                phone.setText(mModel.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkDetailsModel model = new WorkDetailsModel(nameUser, uid);
                mRef.child(uid).child("Patients").push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DocViewActivity.this, "Appoinment Registered.\nWait for Confirmation", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}