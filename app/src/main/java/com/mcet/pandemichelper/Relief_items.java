package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class Relief_items extends AppCompatActivity {
    private RecyclerView recyclerView;

    private FirebaseDatabase database;
    private DatabaseReference reference,refItems;
    private TextView itemName, itemPhone;
    private String key, lat, lon, address, userKey;
    private MaterialButton selectVolunteer;
    private FirebaseRecyclerAdapter<MaterialModel,WorkViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relief_items);

        itemName = findViewById(R.id.itemName);
        itemPhone = findViewById(R.id.itemPhone);
        selectVolunteer = findViewById(R.id.iBtn);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        recyclerView = (RecyclerView) findViewById(R.id.ItemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent().getStringExtra("id").equals("user")){
            selectVolunteer.setText(R.string.viewLocation);
            selectVolunteer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Relief_items.this, AdminFromMapsActivity.class);
                    i.putExtra("id", "ri");
                    i.putExtra("address", address);
                    i.putExtra("lat", lat);
                    i.putExtra("lon", lon);
                    i.putExtra("job", key);
                    startActivity(i);
                    finish();
                }
            });
        }
        else if (getIntent().getStringExtra("id").equals("userView")){
            selectVolunteer.setVisibility(View.INVISIBLE);
        }
        else {
            selectVolunteer.setText(R.string.select_volunteer);
            selectVolunteer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Relief_items.this, WorkerWorkMapsActivity.class);
                    i.putExtra("id", "ri");
                    i.putExtra("address", address);
                    i.putExtra("lat", lat);
                    i.putExtra("lon", lon);
                    i.putExtra("job", key);
                    i.putExtra("key", userKey);
                    startActivity(i);
                    finish();
                }
            });
        }


        key = getIntent().getStringExtra("key");
        reference.child("ReliefMaterials/"+key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                MaterialModel model = dataSnapshot.getValue(MaterialModel.class);
                itemName.setText(model.getName());
                itemPhone.setText(model.getPhone());
                List data = Arrays.asList(model.getLocation().split("\n"));
                Log.d("list", String.valueOf(data));
                lat = data.get(0).toString();
                lon = data.get(1).toString();
                address = data.get(2).toString();
                userKey = model.getKey();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        RecyclerView.ItemDecoration divider=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
        refItems = FirebaseDatabase.getInstance().getReference("ReliefMaterials/"+key+"/Items");

        try {
            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<MaterialModel>().setQuery(refItems,MaterialModel.class).build();

            Log.d("options",options.getSnapshots().toString());
            adapter = new FirebaseRecyclerAdapter<MaterialModel,WorkViewHolder>(options) {
                @NonNull
                @Override
                public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_view_layout,parent,false);
                    Log.d("viweI",view.toString());
                    return new WorkViewHolder(view);
                }

                @Override
                public void onError(@NonNull DatabaseError error) {
                    super.onError(error);
                    Log.d("errorI",error.toString());
                }

                @Override
                protected void onBindViewHolder(@NonNull WorkViewHolder holder, final int position, @NonNull final MaterialModel model) {

                    final String key = getRef(position).getKey();

                    holder.work.setText(model.getItemName());
                    holder.num.setText(model.getQuantity());

                }
            };

        }catch (Exception e){

            Log.d("exce",e.toString());
            Toast.makeText(Relief_items.this,"No Works",Toast.LENGTH_SHORT).show();

        }

        adapter.startListening();
        adapter.notifyDataSetChanged();
        Log.d("AI", String.valueOf(adapter.getItemCount()));
        recyclerView.setAdapter(adapter);
    }
}