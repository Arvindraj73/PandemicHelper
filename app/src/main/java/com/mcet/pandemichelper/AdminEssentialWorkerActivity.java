package com.mcet.pandemichelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminEssentialWorkerActivity extends AppCompatActivity {
    private FloatingActionButton fab;

    private RecyclerView recyclerView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private FirebaseRecyclerAdapter<WorkDetailsModel, WorkViewHolder> adapter;
    private FirebaseUser user;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_essential_worker);
        fab = findViewById(R.id.floating_action_button);
        recyclerView = findViewById(R.id.workView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance();
user= FirebaseAuth.getInstance().getCurrentUser();
        id = getIntent().getStringExtra("id");
        mRef = mDatabase.getReference("EssentialWorks");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AdminEssentialWorkerActivity.this, CreateWorkActivity.class);
                i.putExtra("id", "ew");
                startActivity(i);
            }
        });
        if (getIntent().getStringExtra("id").equals("admin")) {
            try {
                FirebaseRecyclerOptions list = new FirebaseRecyclerOptions.Builder<WorkDetailsModel>().setQuery(mRef, WorkDetailsModel.class).build();
                FirebaseRecyclerAdapter<WorkDetailsModel, EpassViewHolder> adapter = new FirebaseRecyclerAdapter<WorkDetailsModel, EpassViewHolder>(list) {

                    @NonNull
                    @Override
                    public EpassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_view_layout, parent, false);
                        return new EpassViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull EpassViewHolder holder, int position, @NonNull WorkDetailsModel model) {
                        final String key = getRef(position).getKey();
                        holder.name.setText("Name : \n" + model.getName());
                        holder.date.setText("Description : \n" + model.getDescription());
                        holder.to.setText("PhoneNumber : \n" + model.getPhoneNumber());
                        holder.from.setText("No of worker : \n" + model.getNoOfWorkers());
                        holder.from.setText("No of worker : \n" + model.getNoOfWorkers());

                        if (model.getAssignedTo().equals("Not Assigned")) {
                            holder.status.setText("Status :\n" + model.getAssignedTo());
                        } else {
                            String[] workers = model.getAssignedTo().split(",");
                            StringBuilder name = new StringBuilder();
                            for (String w : workers) {
                                Log.d("workers", w);
                                mDatabase.getReference("UserInfo").child(w).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        UserModel model = dataSnapshot.getValue(UserModel.class);
                                        name.append(model.getName()).append("\n");
                                        Log.d("name", name.toString());
                                        holder.status.setText("Assigned to :\n" + name.toString());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                Log.d("nameout", name.toString());
                            }
                            holder.status.setText("Assigned to :\n" + name.toString());

                        }
                        holder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(AdminEssentialWorkerActivity.this, VolunteerListActivity.class);
                                i.putExtra("id", "ew");
                                i.putExtra("key", key);
                                startActivity(i);
                                finish();
                            }
                        });
                    }
                };
                adapter.startListening();
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (getIntent().getStringExtra("id").equals("user")) {
            mRef = mDatabase.getReference("UserInfo").child(user.getUid()).child("Works");
            try {
                FirebaseRecyclerOptions list = new FirebaseRecyclerOptions.Builder<WorkDetailsModel>().setQuery(mRef, WorkDetailsModel.class).build();
                FirebaseRecyclerAdapter<WorkDetailsModel, EpassViewHolder> adapter = new FirebaseRecyclerAdapter<WorkDetailsModel, EpassViewHolder>(list) {

                    @NonNull
                    @Override
                    public EpassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_view_layout, parent, false);
                        return new EpassViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull EpassViewHolder holder, int position, @NonNull WorkDetailsModel model) {
                        String key=model.getKey();
                        Log.d("key", key);
                        mDatabase.getReference("EssentialWorks").child(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                WorkDetailsModel modelW = dataSnapshot.getValue(WorkDetailsModel.class);
                                holder.name.setText("Name:"+modelW.getName());
                                holder.date.setText("Desc:"+modelW.getDescription());
                                holder.from.setText("no of worker:"+modelW.getNoOfWorkers());
                                holder.status.setVisibility(View.GONE);
                                holder.to.setVisibility(View.GONE);
                                holder.cardView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(AdminEssentialWorkerActivity.this, AdminFromMapsActivity.class);
                                        i.putExtra("id", "ew");
                                        i.putExtra("lat", modelW.getLat());
                                        i.putExtra("lon", modelW.getLon());
                                        i.putExtra("job", modelW.getName());
                                        startActivity(i);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });




                    }
                };

                adapter.startListening();
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}