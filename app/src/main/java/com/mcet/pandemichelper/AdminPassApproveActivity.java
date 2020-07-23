package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class AdminPassApproveActivity extends AppCompatActivity {
    TextView name, age, gender, from, to, reason, imgurl, test,status;
    Button map;
    ImageView id, rproof;
    Button accept, reject;
    String key, flat, flog, tlat, tlog;
    FirebaseDatabase database;
    DatabaseReference reference;
    String fromaddress, toaddress,strstatus, path;
    Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pass_approve);
        name = findViewById(R.id.TName);
        age = findViewById(R.id.TAge);
        gender = findViewById(R.id.Tgender);
        imgurl = findViewById(R.id.t4);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        id = findViewById(R.id.proof);
        reason = findViewById(R.id.RProof);
        rproof = findViewById(R.id.ReasonProof);
        accept = findViewById(R.id.actbtn);
        reject = findViewById(R.id.rjtbtn);
        map = findViewById(R.id.map);
        status=findViewById(R.id.statustatus);
        test = findViewById(R.id.t4);
        key = getIntent().getStringExtra("path");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        reference.child("PersonalPass/" + key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PassDetailsModel model = dataSnapshot.getValue(PassDetailsModel.class);
                name.setText(model.getName());
                gender.setText(model.getGender());
                age.setText(model.getAge());
                from.setText(model.getFromAddress());
                to.setText(model.getToAddress());
                fromaddress = model.getFromAddress();
                toaddress = model.getToAddress();
                status.setText(model.getStatus());
                strstatus=model.getStatus();
                Picasso.get().load(model.getIdProof()).into(id);
                reason.setText(model.getReason());

                Picasso.get().load(model.getReasonProof()).into(rproof);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flat = fromaddress.substring(0, fromaddress.indexOf('\n'));
                flog = fromaddress.substring(fromaddress.indexOf('\n'), fromaddress.lastIndexOf('\n'));
                tlat = toaddress.substring(0, toaddress.indexOf('\n'));
                tlog = toaddress.substring(toaddress.indexOf('\n'), toaddress.lastIndexOf('\n'));

                Intent fm = new Intent(AdminPassApproveActivity.this, AdminFromMapsActivity.class);
                fm.putExtra("flat", flat);
                fm.putExtra("flog", flog);
                fm.putExtra("tlat", tlat);
                fm.putExtra("tlog", tlog);
                test.setText(tlat);
                startActivity(fm);
            }
        });
        if((status.equals("Accepted")||(status.equals("Rejected")))) {
            accept.setVisibility(View.GONE);
            reject.setVisibility(View.GONE);
        }
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("PersonalPass/" + key).child("status").setValue("Accepted");

                reference.child("PersonalPass/" + key).child("key").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        path = dataSnapshot.getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                reference.child("UserInfo/" + key).child("epass/"+path).child("status").setValue("Accepted");
                startActivity(new Intent(AdminPassApproveActivity.this,AdminPersonalPassActivity.class));
                Toast.makeText(AdminPassApproveActivity.this,"Accepted",Toast.LENGTH_SHORT).show();
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference.child("PersonalPass/" + key).child("status").setValue("Rejected");
                reference.child("PersonalPass/" + key).child("key").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        path = dataSnapshot.getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                reference.child("UserInfo/" + key).child("epass/"+path).child("status").setValue("Rejected");

                startActivity(new Intent(AdminPassApproveActivity.this,AdminPersonalPassActivity.class));
                Toast.makeText(AdminPassApproveActivity.this,"Rejected",Toast.LENGTH_SHORT).show();
            }
        });
    }
}