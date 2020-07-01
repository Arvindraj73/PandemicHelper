package com.mcet.pandemichelper;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.intellij.lang.annotations.JdkConstants;

import java.io.FileDescriptor;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocProfileFragment extends Fragment implements View.OnClickListener {

    private TextView name, phone, mail, limit, timeV, timeE;
    private EditText numberTxt;
    private DatabaseReference mRef;
    private FirebaseUser mUser;
    private ImageButton plus, minus;
    private int number = 0;
    private View view;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public DocProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DocProfileFragment.
     */
    public static DocProfileFragment newInstance(String param1, String param2) {
        DocProfileFragment fragment = new DocProfileFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_doc_profile, container, false);

        LinearLayout editButton = view.findViewById(R.id.editL);
        LinearLayout cancelButton = view.findViewById(R.id.cancelL);

        name = view.findViewById(R.id.docName);
        phone = view.findViewById(R.id.docPhone);
        mail = view.findViewById(R.id.docEmail);
        timeV = view.findViewById(R.id.docPA);
        limit = view.findViewById(R.id.docTi);
        numberTxt = view.findViewById(R.id.txtNumbers);
        timeE = view.findViewById(R.id.timeE);

        view.findViewById(R.id.imgPlus).setOnClickListener(this);
        view.findViewById(R.id.imgMinus).setOnClickListener(this);
        view.findViewById(R.id.pickTime).setOnClickListener(this);
        view.findViewById(R.id.changeDetailsBtn).setOnClickListener(this);

        mRef = FirebaseDatabase.getInstance().getReference("UserInfo");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel model = dataSnapshot.getValue(UserModel.class);
                name.setText(model.getName());
                phone.setText(model.getPhone());
                mail.setText(model.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        try {
            mRef.child(mUser.getUid() + "/DocDetails").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel model = dataSnapshot.getValue(UserModel.class);
                    try {
                        timeV.setText(model.getTime());
                        limit.setText(model.getLimit());
                        view.findViewById(R.id.editLayout).setVisibility(View.GONE);
                        view.findViewById(R.id.editL).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.cancelL).setVisibility(View.GONE);
                        view.findViewById(R.id.viewLayout).setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        view.findViewById(R.id.editLayout).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.editL).setVisibility(View.GONE);
                        view.findViewById(R.id.cancelL).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.viewLayout).setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            view.findViewById(R.id.editLayout).setVisibility(View.GONE);
            view.findViewById(R.id.editL).setVisibility(View.VISIBLE);
            view.findViewById(R.id.cancelL).setVisibility(View.GONE);
            view.findViewById(R.id.viewLayout).setVisibility(View.VISIBLE);
        } catch (Exception e) {
            view.findViewById(R.id.editLayout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.editL).setVisibility(View.GONE);
            view.findViewById(R.id.cancelL).setVisibility(View.VISIBLE);
            view.findViewById(R.id.viewLayout).setVisibility(View.GONE);
        }

//        if (mRef.child(mUser.getUid()+"DocDetails") == null){
//
//        }

        editButton.setOnClickListener(v -> {
            view.findViewById(R.id.editLayout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.editL).setVisibility(View.GONE);
            view.findViewById(R.id.cancelL).setVisibility(View.VISIBLE);
            view.findViewById(R.id.viewLayout).setVisibility(View.GONE);
        });

        cancelButton.setOnClickListener(v -> {
            view.findViewById(R.id.editLayout).setVisibility(View.GONE);
            view.findViewById(R.id.editL).setVisibility(View.VISIBLE);
            view.findViewById(R.id.cancelL).setVisibility(View.GONE);
            view.findViewById(R.id.viewLayout).setVisibility(View.VISIBLE);
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgPlus:
                number++;
                numberTxt.setText(String.valueOf(number));
                break;
            case R.id.imgMinus:
                number--;
                numberTxt.setText(String.valueOf(number));
                break;
            case R.id.pickTime:
                openDialog(getContext());
                break;
            case R.id.changeDetailsBtn:
                changeDetails();
                break;
        }
    }

    private void changeDetails() {

        UserModel model = new UserModel(numberTxt.getText().toString(), timeE.getText().toString());
        mRef.child(mUser.getUid() + "/DocDetails").setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Details Changed", Toast.LENGTH_SHORT).show();
                    view.findViewById(R.id.editLayout).setVisibility(View.GONE);
                    view.findViewById(R.id.editL).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.cancelL).setVisibility(View.GONE);
                    view.findViewById(R.id.viewLayout).setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void openDialog(Context context) {

        View viewD = LayoutInflater.from(getContext()).inflate(R.layout.time_picker_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(viewD);

        TextView from = viewD.findViewById(R.id.from);
        TextView to = viewD.findViewById(R.id.to);

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        from.setText(hourOfDay + ":" + minute);
                    }
                }, hour, min, false);
                timePickerDialog.show();

            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        to.setText(hourOfDay + ":" + minute);
                    }
                }, hour, min, false);
                timePickerDialog.show();
            }
        });

        builder.setTitle("Select your Timing")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timeE.setText("From " + from.getText().toString() + " to " + to.getText().toString());
                    }
                });
        builder.show();

    }
}
