
package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class VolunteerListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mReference,workRef;
    private FirebaseRecyclerAdapter<UserModel,VolunteerListHolder> adapter;

    private MaterialButton mSelect;

    private ArrayList<String> volunteer;

    String key,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_list);

        recyclerView = findViewById(R.id.vList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSelect = findViewById(R.id.select);

        key = getIntent().getStringExtra("key");
        name = getIntent().getStringExtra("name");

        volunteer = new ArrayList<String>();

        mReference = FirebaseDatabase.getInstance().getReference("UserInfo");
        workRef = FirebaseDatabase.getInstance().getReference("VolunteerWorks");

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<UserModel>().setQuery(mReference,UserModel.class).build();

        adapter = new FirebaseRecyclerAdapter<UserModel, VolunteerListHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull VolunteerListHolder holder, int position, @NonNull final UserModel model) {

                if (model.getRole().equals("Volunteer")){
                    holder.checkBox.setText(model.getName());
                    Log.d("MODEL",model.getUid());

                    holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked){
                                Log.d("MODEL",model.getUid());
                                volunteer.add(model.getUid());
                            }
                            else{
                                volunteer.remove(model.getUid());
                            }
                        }
                    });
                }

            }

            @NonNull
            @Override
            public VolunteerListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.volunteer_list_layout,parent,false);

                return new VolunteerListHolder(view);
            }
        };

        mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("VOLUNTEER",volunteer.toString());

                //DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserInfo");

                WorkDetailsModel model = new WorkDetailsModel(name,key);

                workRef.child(key+"/status").setValue("Assigned");

                for (String s : volunteer){

                    mReference.child(s).child("Works").push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.d("D","Success");
                            }
                        }
                    });

                }

            }
        });

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    public class VolunteerListHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        public VolunteerListHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
