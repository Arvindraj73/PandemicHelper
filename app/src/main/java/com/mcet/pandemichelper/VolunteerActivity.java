package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VolunteerActivity extends AppCompatActivity {

    private RecyclerView recyclerView, reliefView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef, mWorkRef, mWorkRef1;

    private FirebaseRecyclerAdapter<WorkDetailsModel, WorkViewHolder> adapter;
    private FirebaseRecyclerAdapter<MaterialModel, WorkViewHolder> adapter1;

    private FirebaseUser user;

    private ArrayList<String> list;
    private SharedPreferences preferences;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
//        reliefView = findViewById(R.id.ReliefView);
        recyclerView = findViewById(R.id.recyclerView3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        reliefView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("uid", user.toString());
        mWorkRef = mDatabase.getReference("UserInfo/" + user.getUid() + "/Works");
//        mWorkRef1 = mDatabase.getReference("UserInfo/" + user.getUid() + "/CollectionWorks");
        list = new ArrayList<String>();
        preferences = getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);

        Log.d("pre", preferences.toString());
        role = preferences.getString("role", "");
//        mWorkRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                Log.d("D", dataSnapshot.toString());
//
//                for (DataSnapshot s : dataSnapshot.getChildren()) {
//                    Log.d("W", String.valueOf(s.child("work").getValue()));
//                    list.add(String.valueOf(s.child("work").getValue()));
//                }
//                Log.d("L", list.toString());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        for (String s : list){
//
//        }


        try {

            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<WorkDetailsModel>().setQuery(mWorkRef, WorkDetailsModel.class).build();

            adapter = new FirebaseRecyclerAdapter<WorkDetailsModel, WorkViewHolder>(options) {
                @NonNull
                @Override
                public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_view_layout, parent, false);
                    return new WorkViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull WorkViewHolder holder, final int position, @NonNull final WorkDetailsModel model) {

//                    for (int i=0;i<list.size();i++) {
//
//                        //Log.d("SRT", list.get(i));
//
//                        final String key = getRef(position).getKey();
//
//                        if (key.equals(list.get(i))) {

                    holder.work.setText(model.getName());
                    holder.num.setVisibility(View.GONE);
                    Log.d("gfhg",model.getName());
                    Log.d("gfhg",model.getKey());

                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (model.getName().equals("Collection of Relief Materials")) {
                                Log.d("gfhg",model.getName());
                                Intent i = new Intent(VolunteerActivity.this, Relief_items.class);
                                i.putExtra("key", model.getKey());
                                i.putExtra("id", "user");
                                startActivity(i);
                            }
                            else {
                                Intent i = new Intent(VolunteerActivity.this, WorkAssignActivity.class);
                                i.putExtra("key", model.getKey());
                                i.putExtra("id", "user");
                                startActivity(i);

                            }
                            }
                    });

                }
//                        else{
//
//                        }
//                    }
//
//                }
            };

        } catch (Exception e) {

            Toast.makeText(VolunteerActivity.this, "No Works", Toast.LENGTH_SHORT).show();

        }


        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
//
//
//        FirebaseRecyclerOptions options1 = new FirebaseRecyclerOptions.Builder<MaterialModel>().setQuery(mWorkRef1, MaterialModel.class).build();
//
//        adapter1 = new FirebaseRecyclerAdapter<MaterialModel, WorkViewHolder>(options1) {
//            @NonNull
//            @Override
//            public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_view_layout, parent, false);
//                return new WorkViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull WorkViewHolder holder, final int position, @NonNull final MaterialModel model) {
//
////                    for (int i=0;i<list.size();i++) {
////
////                        //Log.d("SRT", list.get(i));
////
////
////
////                        if (key.equals(list.get(i))) {
//                final String key = getRef(position).getKey();
//                holder.work.setText(model.getName());
//                holder.num.setVisibility(View.GONE);
//
//                holder.cardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Intent i = new Intent(VolunteerActivity.this,Relief_items.class);
//                        i.putExtra("key",key);
//                        i.putExtra("id", "user");
//                        startActivity(i);
//                    }
//                });
//
//            }
////                        else{
////
////                        }
////                    }
////
////                }
//        };
//
//
//
//
//
//        adapter1.startListening();
//        adapter1.notifyDataSetChanged();
//        reliefView.setAdapter(adapter1);
//    }
    }
}
