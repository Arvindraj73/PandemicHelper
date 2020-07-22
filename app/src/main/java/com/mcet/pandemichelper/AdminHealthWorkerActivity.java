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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminHealthWorkerActivity extends AppCompatActivity {
    private FloatingActionButton fab;

    private RecyclerView recyclerView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private FirebaseRecyclerAdapter<WorkDetailsModel, WorkViewHolder> adapter;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_health_worker);
        fab = findViewById(R.id.floating_action_button);
        recyclerView = findViewById(R.id.workView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance();

        id = getIntent().getStringExtra("id");
        mRef = mDatabase.getReference("HealthWorks");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AdminHealthWorkerActivity.this, CreateWorkActivity.class);
                i.putExtra("id", "hw");
                startActivity(i);
            }
        });
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

//        try {
//
//            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<WorkDetailsModel>().setQuery(mRef, WorkDetailsModel.class).build();
//
//            adapter = new FirebaseRecyclerAdapter<WorkDetailsModel, WorkViewHolder>(options) {
//                @NonNull
//                @Override
//                public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_view_layout,parent,false);
//                    return new WorkViewHolder(view);
//                }
//
//                @Override
//                protected void onBindViewHolder(@NonNull WorkViewHolder holder, final int position, @NonNull final WorkDetailsModel model) {
//
//                    final String key = getRef(position).getKey();
//
//                    holder.work.setText(model.getName());
//                    Log.d("WOrk",model.getName());
//                    holder.num.setText("No of Workers Required : "+model.getNoOfWorkers());
//
//                    holder.cardView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            Intent i = new Intent(AdminHealthWorkerActivity.this, WorkAssignActivity.class);
//                            i.putExtra("key",key);
//                            i.putExtra("id","admin");
//                            i.putExtra("id1", "hw");
//                            i.putExtra("name",model.getName());
//                            startActivity(i);
//                        }
//                    });
//
//                }
//            };
//
//        }catch (Exception e){
//
//            Toast.makeText(AdminHealthWorkerActivity.this,"No Works",Toast.LENGTH_SHORT).show();
//
//        }
//
//        adapter.startListening();
//        adapter.notifyDataSetChanged();
//        recyclerView.setAdapter(adapter);

    }
