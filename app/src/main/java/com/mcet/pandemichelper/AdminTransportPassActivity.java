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

    private FirebaseRecyclerAdapter<PassDetailsModel, EpassViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_transport_pass);
        recyclerView = findViewById(R.id.perPassView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance();


        mRef = mDatabase.getReference("PersonalPass");
        try{
            FirebaseRecyclerOptions list = new FirebaseRecyclerOptions.Builder<PassDetailsModel>().setQuery(mRef,PassDetailsModel.class).build();
            adapter=new FirebaseRecyclerAdapter<PassDetailsModel,EpassViewHolder>(list) {

                @NonNull
                @Override
                public EpassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_view_layout,parent,false);
                    return new EpassViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull EpassViewHolder holder, int position, @NonNull PassDetailsModel model) {
                    final String key = getRef(position).getKey();
                    holder.name.setText(model.getName());
                    holder.from.setText(model.getFromAddress());
                    holder.to.setText(model.getToAddress());
                    holder.date.setText(model.getTravelDate());

                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i=new Intent(AdminTransportPassActivity.this,AdminPassApproveActivity.class);
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
