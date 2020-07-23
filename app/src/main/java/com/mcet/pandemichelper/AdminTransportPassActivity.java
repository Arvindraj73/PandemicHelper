package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminTransportPassActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private FirebaseRecyclerAdapter<EssentialPassModel, EpassViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_transport_pass);
        recyclerView = findViewById(R.id.tpassView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance();


        mRef = mDatabase.getReference("TransportPass");
        try{
            FirebaseRecyclerOptions list = new FirebaseRecyclerOptions.Builder<EssentialPassModel>().setQuery(mRef,EssentialPassModel.class).build();
            adapter=new FirebaseRecyclerAdapter<EssentialPassModel,EpassViewHolder>(list) {

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

                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i=new Intent(AdminTransportPassActivity.this,AdminPassApproveActivity.class);
                            i.putExtra("id", "transPass");
                            i.putExtra("path",key);
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
