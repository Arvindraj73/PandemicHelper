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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminFoodRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private FirebaseRecyclerAdapter<HelpModel, EpassViewHolder> adapter;

    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food_request);

        recyclerView = findViewById(R.id.foodRequestView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance();


        mRef = mDatabase.getReference("FoodRequests");
        try{
            FirebaseRecyclerOptions list = new FirebaseRecyclerOptions.Builder<HelpModel>().setQuery(mRef,HelpModel.class).build();
            adapter=new FirebaseRecyclerAdapter<HelpModel,EpassViewHolder>(list) {

                @NonNull
                @Override
                public EpassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_view_layout,parent,false);
                    return new EpassViewHolder(view);

                }

                @Override
                protected void onBindViewHolder(@NonNull EpassViewHolder holder, int position, @NonNull HelpModel model) {
                    final String key = getRef(position).getKey();
                    holder.name.setText(model.getName());
                    holder.from.setText(model.person_count);
                    holder.to.setText(model.location);
                    holder.date.setText("");
                    holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            mRef.child(key).removeValue();
                            Toast.makeText(AdminFoodRequestActivity.this,"data deleted",Toast.LENGTH_SHORT).show();
                            return false;
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

    }}