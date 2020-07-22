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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.content.SharedPreferences;
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

    }
}