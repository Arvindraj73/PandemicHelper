package com.mcet.pandemichelper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TollFreeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private DatabaseReference mRef;

    private FirebaseRecyclerAdapter<CallModel, TollViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toll_free);

        recyclerView = findViewById(R.id.toll_free);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRef = FirebaseDatabase.getInstance().getReference("HelplineNumbers");

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<CallModel>().setQuery(mRef, CallModel.class).build();

        adapter = new FirebaseRecyclerAdapter<CallModel, TollViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TollViewHolder holder, int position, @NonNull final CallModel model) {

                holder.dept.setText(model.getDept().toUpperCase());

                holder.call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(TollFreeActivity.this, "clicked" + model.getPhn(), Toast.LENGTH_SHORT).show();

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + model.getPhn()));


                        if (ActivityCompat.checkSelfPermission(TollFreeActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        
                        startActivity(callIntent);

                    }
                });

            }

            @NonNull
            @Override
            public TollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_layout,parent,false);
                return new TollViewHolder(view);
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    public class TollViewHolder extends RecyclerView.ViewHolder{

        TextView dept;
        FloatingActionButton call;

        public TollViewHolder(@NonNull View itemView) {
            super(itemView);

            dept = itemView.findViewById(R.id.deptCall);
            call = itemView.findViewById(R.id.callBtn);

        }
    }

}
