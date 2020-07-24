package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import static android.app.PendingIntent.getActivity;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String id;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView=findViewById(R.id.historyView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance();
        preferences = getApplicationContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        id = getIntent().getStringExtra("id");

        if (id.equals("food")) {
            mRef = mDatabase.getReference("UserInfo").child(preferences.getString("uid", "")).child("FoodRequests");
            try{
                FirebaseRecyclerOptions list = new FirebaseRecyclerOptions.Builder<HelpModel>().setQuery(mRef,HelpModel.class).build();
                FirebaseRecyclerAdapter<HelpModel, EpassViewHolder> adapter=new FirebaseRecyclerAdapter<HelpModel,EpassViewHolder>(list) {

                    @NonNull
                    @Override
                    public EpassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_view_layout,parent,false);
                        return new EpassViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull EpassViewHolder holder, int position, @NonNull HelpModel model) {
                        final String key = getRef(position).getKey();
                        holder.name.setText("Name : \n"+model.getName());
                        holder.date.setText("Number of Persons : \n"+model.getPerson_count());
                        holder.from.setText("Address : \n"+model.getLocation());
                        holder.to.setVisibility(View.GONE);

                    }
                };
                adapter.startListening();
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id.equals("med")) {
            mRef = mDatabase.getReference("UserInfo").child(preferences.getString("uid", "")).child("MedRequests");
            try{
                FirebaseRecyclerOptions list = new FirebaseRecyclerOptions.Builder<HelpModel>().setQuery(mRef,HelpModel.class).build();
                FirebaseRecyclerAdapter<HelpModel, EpassViewHolder> adapter=new FirebaseRecyclerAdapter<HelpModel,EpassViewHolder>(list) {

                    @NonNull
                    @Override
                    public EpassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_view_layout,parent,false);
                        return new EpassViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull EpassViewHolder holder, int position, @NonNull HelpModel model) {
                        final String key = getRef(position).getKey();
                        holder.name.setText("Name : \n"+model.getName());
                        holder.date.setText("Symptoms : \n"+model.getSymptoms());
                        holder.to.setText("Disease : \n"+model.getDisease());
                        holder.from.setText("Address : \n"+model.getLocation());

                    }
                };
                adapter.startListening();
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id.equals("perPass")) {
            mRef = mDatabase.getReference("UserInfo").child(preferences.getString("uid", "")).child("epass");
            try{
                FirebaseRecyclerOptions list = new FirebaseRecyclerOptions.Builder<PassDetailsModel>().setQuery(mRef,PassDetailsModel.class).build();
                FirebaseRecyclerAdapter<PassDetailsModel, EpassViewHolder> adapter=new FirebaseRecyclerAdapter<PassDetailsModel,EpassViewHolder>(list) {

                    @NonNull
                    @Override
                    public EpassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_view_layout,parent,false);
                        return new EpassViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull EpassViewHolder holder, int position, @NonNull PassDetailsModel model) {
                        final String key = getRef(position).getKey();
                        holder.name.setText("Name : \n"+model.getName());
                        holder.from.setText("FromAddress : \n"+model.getFromAddress());
                        holder.to.setText("ToAddress :\n"+model.getToAddress());
                        holder.date.setText("TravelDate :\n"+model.getTravelDate());

                    }
                };

                adapter.startListening();
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id.equals("transPass")) {
            mRef = mDatabase.getReference("UserInfo").child(preferences.getString("uid", "")).child("TPass");
            try{
                FirebaseRecyclerOptions list = new FirebaseRecyclerOptions.Builder<EssentialPassModel>().setQuery(mRef,EssentialPassModel.class).build();
                FirebaseRecyclerAdapter<EssentialPassModel, EpassViewHolder> adapter=new FirebaseRecyclerAdapter<EssentialPassModel,EpassViewHolder>(list) {

                    @NonNull
                    @Override
                    public EpassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_view_layout,parent,false);
                        return new EpassViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull EpassViewHolder holder, int position, @NonNull EssentialPassModel model) {
                        final String key = getRef(position).getKey();
                        holder.name.setText("Name : \n"+model.getApplicantName());
                        holder.from.setText("Organisation Name :\n"+model.getOrName());
                        holder.to.setText("Vehicle Count :\n"+model.getVehicleCount());
                        holder.status.setText("Status :\n"+model.getStatus());
                    }
                };

                adapter.startListening();
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id.equals("unorg")) {
            mRef = mDatabase.getReference("UserInfo").child(preferences.getString("uid", "")).child("UnorganisedWorkRequests");
            try{
                FirebaseRecyclerOptions list = new FirebaseRecyclerOptions.Builder<CallModel>().setQuery(mRef,CallModel.class).build();
                FirebaseRecyclerAdapter<CallModel, EpassViewHolder> adapter=new FirebaseRecyclerAdapter<CallModel,EpassViewHolder>(list) {

                    @NonNull
                    @Override
                    public EpassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_view_layout,parent,false);
                        return new EpassViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull EpassViewHolder holder, int position, @NonNull CallModel model) {
                        final String key = getRef(position).getKey();
                        holder.name.setText("Worker Name : \n"+model.getWorkerName());
                        holder.from.setText("Work : \n"+model.getWorkName());
                        holder.to.setText("Address :\n"+model.getLocation());
                        holder.date.setText("Worker Phone Number :\n"+model.getWorkerPhone());

                    }
                };
                adapter.startListening();
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (id.equals("rm")) {
            mRef = mDatabase.getReference("UserInfo").child(preferences.getString("uid", "")).child("ReliefMaterials");
            try{
                FirebaseRecyclerOptions list = new FirebaseRecyclerOptions.Builder<MaterialModel>().setQuery(mRef,MaterialModel.class).build();
                FirebaseRecyclerAdapter<MaterialModel, EpassViewHolder> adapter=new FirebaseRecyclerAdapter<MaterialModel,EpassViewHolder>(list) {

                    @NonNull
                    @Override
                    public EpassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_view_layout,parent,false);
                        return new EpassViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull EpassViewHolder holder, int position, @NonNull MaterialModel model) {
                        final String key = getRef(position).getKey();

                        holder.name.setText("Name : \n"+model.getName());
                        holder.from.setText("Phone : \n"+model.getPhone());
                        if(model.getAssignedTo().equals("Pending")) {
                            holder.to.setText("AssignedTo :\n" + model.getAssignedTo());
                        }
                        else
                        {
                            mDatabase.getReference("UserInfo").child(model.getAssignedTo()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    UserModel userModel=dataSnapshot.getValue(UserModel.class);

                                    holder.to.setText("AssignedTo:\n" + userModel.getName());
                                    holder.date.setText("Volunteer No:\n"+userModel.getPhone());
                                    holder.status.setText("Assigned");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        holder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(HistoryActivity.this, Relief_items.class);
                                i.putExtra("key",preferences.getString("uid",""));
                                i.putExtra("id", "userView");
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