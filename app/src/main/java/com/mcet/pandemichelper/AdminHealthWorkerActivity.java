package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import java.util.Arrays;
import java.util.List;

public class AdminHealthWorkerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseUser user;

    private FirebaseRecyclerAdapter<WorkDetailsModel, WorkViewHolder> adapter;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_health_worker);
        recyclerView = findViewById(R.id.workView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();

        id = getIntent().getStringExtra("id");
        if (id.equals("admin")) {
            mRef = mDatabase.getReference("HealthWorks");
            try {
                FirebaseRecyclerOptions list = new FirebaseRecyclerOptions.Builder<HelpModel>().setQuery(mRef, HelpModel.class).build();
                FirebaseRecyclerAdapter<HelpModel, EpassViewHolder> adapter = new FirebaseRecyclerAdapter<HelpModel, EpassViewHolder>(list) {

                    @NonNull
                    @Override
                    public EpassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_view_layout, parent, false);
                        return new EpassViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull EpassViewHolder holder, int position, @NonNull HelpModel model) {
                        final String key = getRef(position).getKey();
                        holder.name.setText("Name : \n" + model.getName());
                        holder.date.setText("Symptoms : \n" + model.getSymptoms());
                        holder.to.setText("Disease : \n" + model.getDisease());
                        holder.from.setText("Address : \n" + model.getLocation());
                        if (model.getAssignedTo() == null) {
                            holder.status.setText("Status :\nNot Assigned");
                        } else {
                            mDatabase.getReference("UserInfo").child(model.getAssignedTo()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    UserModel model = dataSnapshot.getValue(UserModel.class);
                                    holder.status.setText("Assigned to :\n" + model.getName());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        List data = Arrays.asList(model.getLocation().split("\n"));
                        Log.d("list", String.valueOf(data));
                        String lat = data.get(0).toString();
                        String lon = data.get(1).toString();
                        String address = data.get(2).toString();
                        Log.d("lat", lat);
                        Log.d("lon", lon);
                        Log.d("address", address);
                        holder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(AdminHealthWorkerActivity.this, WorkerWorkMapsActivity.class);
                                i.putExtra("id", "hw");
                                i.putExtra("address", address);
                                i.putExtra("lat", lat);
                                i.putExtra("lon", lon);
                                i.putExtra("job", key);
                                startActivity(i);
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
        else if (id.equals("user")){
            mRef = mDatabase.getReference("UserInfo").child(user.getUid()).child("Works");
            try {
                FirebaseRecyclerOptions list = new FirebaseRecyclerOptions.Builder<HelpModel>().setQuery(mRef, HelpModel.class).build();
                FirebaseRecyclerAdapter<HelpModel, EpassViewHolder> adapter = new FirebaseRecyclerAdapter<HelpModel, EpassViewHolder>(list) {

                    @NonNull
                    @Override
                    public EpassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_view_layout, parent, false);
                        return new EpassViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull EpassViewHolder holder, int position, @NonNull HelpModel model) {
                        final String key = getRef(position).getKey();
                        holder.name.setText("Name : \n" + model.getName());
                        holder.date.setText("Symptoms : \n" + model.getSymptoms());
                        holder.to.setText("Disease : \n" + model.getDisease());
                        holder.from.setText("Address : \n" + model.getLocation());
                        holder.status.setVisibility(View.GONE);

                        List data = Arrays.asList(model.getLocation().split("\n"));
                        Log.d("list", String.valueOf(data));
                        String lat = data.get(0).toString();
                        String lon = data.get(1).toString();
                        String address = data.get(2).toString();
                        Log.d("lat", lat);
                        Log.d("lon", lon);
                        Log.d("address", address);
                        holder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(AdminHealthWorkerActivity.this, AdminFromMapsActivity.class);
                                i.putExtra("id", "hw");
                                i.putExtra("address", address);
                                i.putExtra("lat", lat);
                                i.putExtra("lon", lon);
                                i.putExtra("job", key);
                                startActivity(i);
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
        else if (id.equals("unorg")){
            mRef = mDatabase.getReference("UserInfo").child(user.getUid()).child("Works");
            try {
                FirebaseRecyclerOptions list = new FirebaseRecyclerOptions.Builder<CallModel>().setQuery(mRef, CallModel.class).build();
                FirebaseRecyclerAdapter<CallModel, EpassViewHolder> adapter = new FirebaseRecyclerAdapter<CallModel, EpassViewHolder>(list) {

                    @NonNull
                    @Override
                    public EpassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_view_layout, parent, false);
                        return new EpassViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull EpassViewHolder holder, int position, @NonNull CallModel model) {
                        final String key = getRef(position).getKey();
                        holder.to.setText("Work : \n" + model.getWorkName());
                        holder.from.setText("Address : \n" + model.getLocation());
                        holder.date.setVisibility(View.GONE);

                        mDatabase.getReference("UserInfo").child(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                                holder.name.setText("Requester Name :\n"+userModel.getName());
                                holder.status.setText("Requester Phone :\n"+userModel.getPhone());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        List data = Arrays.asList(model.getLocation().split("\n"));
                        Log.d("list", String.valueOf(data));
                        String lat = data.get(0).toString();
                        String lon = data.get(1).toString();
                        String address = data.get(2).toString();
                        Log.d("lat", lat);
                        Log.d("lon", lon);
                        Log.d("address", address);
                        holder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(AdminHealthWorkerActivity.this, AdminFromMapsActivity.class);
                                i.putExtra("id", "hw");
                                i.putExtra("address", address);
                                i.putExtra("lat", lat);
                                i.putExtra("lon", lon);
                                i.putExtra("job", key);
                                startActivity(i);
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
