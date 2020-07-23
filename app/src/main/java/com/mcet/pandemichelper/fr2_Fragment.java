package com.mcet.pandemichelper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fr2_Fragment extends Fragment {

    private RecyclerView recyclerView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private FirebaseRecyclerAdapter<MaterialModel,WorkViewHolder> adapter;

    private String id;
    TextView tv;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fr2_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientsFragment newInstance(String param1, String param2) {
        PatientsFragment fragment = new PatientsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.d("Crt:","fragment1");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fr2_, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.ReliefList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tv=(TextView)view.findViewById(R.id.tv);

        mDatabase = FirebaseDatabase.getInstance();


        Log.d("dbb",mDatabase.toString());
        Log.d("dataurl",mDatabase.getApp().toString());

        mRef = mDatabase.getReference("ReliefMaterials");
        // Inflate the layout for this fragment
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("data",dataSnapshot.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("dataerror",databaseError.toString());
            }
        });
        try {
            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<MaterialModel>().setQuery(mRef,MaterialModel.class).build();

            adapter = new FirebaseRecyclerAdapter<MaterialModel, WorkViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull WorkViewHolder holder, int position, @NonNull MaterialModel model) {
                    final String key = getRef(position).getKey();
                    holder.work.setText("Name:" + model.getName());
                    holder.num.setText("Total : " + model.getTotal());
                    if (model.getAssignedTo().equals("Pending")) {
                        holder.asd.setText("Status :\nNot Assigned");
                    } else {
                        mDatabase.getReference("UserInfo").child(model.getAssignedTo()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserModel model = dataSnapshot.getValue(UserModel.class);
                                holder.asd.setText("Assigned to :\n" + model.getName());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getContext(),Relief_items.class);
                            i.putExtra("key",key);

                            i.putExtra("name",model.getName());
                            startActivity(i);
                        }
                    });
                }
                @NonNull
                @Override
                public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_view_layout,parent,false);
                    return new WorkViewHolder(view);
                }
            };

        }catch (Exception e){

            Log.d("exce",e.toString());
            Toast.makeText(getContext(),"No Works",Toast.LENGTH_SHORT).show();

        }

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        Log.d("adpter", String.valueOf(recyclerView.getAdapter().getItemCount()));
        return view;
    }
}