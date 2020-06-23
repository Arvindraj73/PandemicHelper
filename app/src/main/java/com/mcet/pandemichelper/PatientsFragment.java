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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PatientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientsFragment extends Fragment {

    private RecyclerView recyclerView;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef, mWorkRef;

    private FirebaseRecyclerAdapter<WorkDetailsModel, WorkViewHolder> adapter;

    private FirebaseUser user;

    private ArrayList<String> list;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PatientsFragment() {
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


        mDatabase = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("uid", user.toString());
        mWorkRef = mDatabase.getReference("UserInfo/" + user.getUid() + "/Patients");

        list = new ArrayList<String>();

        mRef = mDatabase.getReference("VolunteerWorks");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_patients, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.patientsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Inflate the layout for this fragment
        try {

            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<WorkDetailsModel>().setQuery(mWorkRef, WorkDetailsModel.class).build();

            adapter = new FirebaseRecyclerAdapter<WorkDetailsModel, WorkViewHolder>(options) {
                @NonNull
                @Override
                public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_view_layout, parent, false);
                    return new WorkViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull WorkViewHolder holder, final int position, @NonNull final WorkDetailsModel model) {

                    holder.work.setText(model.getName());
                    holder.num.setVisibility(View.GONE);

                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent i = new Intent(getContext(), WorkAssignActivity.class);
                            i.putExtra("key", model.getKey());
                            i.putExtra("id", "P");
                            startActivity(i);
                        }
                    });

                }
//                        else{
//
//                        }
//                    }
//
//                }
            };

        } catch (Exception e) {

            Toast.makeText(getContext(), "No Works", Toast.LENGTH_SHORT).show();

        }


        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        return view;
    }
}