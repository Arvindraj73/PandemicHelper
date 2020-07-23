package com.mcet.pandemichelper;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.admin.DelegatedAdminReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.util.ArrayList;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MaterialDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MaterialDetailsFragment extends Fragment {
    private DatabaseReference reference;

    private Chip toilet, cloth, sanitary, stationery, med, food;

    private SharedPreferences preferences;
    private LinearLayout l_toilet, l_cloth, l_sanitary, l_stationery, l_med, l_food;
    private RelativeLayout chipGroup;
    private Double dbl_lon,dbl_lat;
    private int REQUEST_CODE = 100;
    private String lat,name;
    private String lon,location;
    private TextView col_location_view;
    private CheckBox ch1, ch2, ch3, ch4, ch5, ch6, ch7, ch8, ch9, ch10,
            ch11, ch12, ch13, ch14, ch15, ch16, ch17, ch18, ch19, ch20,
            ch21, ch22, ch23, ch24, ch25, ch26, ch27, ch28, ch29, ch30,
            ch31, ch32, ch33, ch34, ch35, ch36, ch37, ch38, ch39, ch40,
            ch41, ch42, ch43, ch44, ch45, ch46, ch47, ch48, ch49, ch50, ch51;

    private ArrayList<String> itemName, quantity;
    private String quan;

    private MaterialButton submit;

    private FirebaseUser user;
    private DatabaseReference mRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 30 && resultCode == RESULT_OK) {
            AddressData addressData = data.getParcelableExtra("ADDRESS_INTENT");
            lat = String.valueOf(addressData.getLatitude());
            lon = String.valueOf(addressData.getLongitude());
            location = addressData.toString();
            Toast.makeText(getContext(), addressData.toString(), Toast.LENGTH_SHORT).show();
            col_location_view.setText(addressData.toString());
        }
    }

    public MaterialDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MaterialDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MaterialDetailsFragment newInstance(String param1, String param2) {
        MaterialDetailsFragment fragment = new MaterialDetailsFragment();
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

        user = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference("ReliefMaterials");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_material_details, container, false);

        cloth = view.findViewById(R.id.chip4);
        toilet = view.findViewById(R.id.chip);
        food = view.findViewById(R.id.chip1);
        stationery = view.findViewById(R.id.chip2);
        sanitary = view.findViewById(R.id.chip3);
        med = view.findViewById(R.id.chip5);
        chipGroup = view.findViewById(R.id.chipGroup);
        col_location_view = view.findViewById(R.id.col_location_view);

        l_cloth = view.findViewById(R.id.lcloth);
        l_med = view.findViewById(R.id.lmed);
        l_food = view.findViewById(R.id.lfood);
        l_toilet = view.findViewById(R.id.ltoilet);
        l_sanitary = view.findViewById(R.id.lsan);
        l_stationery = view.findViewById(R.id.lstat);
        preferences = getActivity().getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);

        ch1 = view.findViewById(R.id.ch_1);
        ch2 = view.findViewById(R.id.ch_2);
        ch3 = view.findViewById(R.id.ch_3);
        ch4 = view.findViewById(R.id.ch_4);
        ch5 = view.findViewById(R.id.ch_5);
        ch6 = view.findViewById(R.id.ch_6);
        ch7 = view.findViewById(R.id.ch_7);
        ch8 = view.findViewById(R.id.ch_8);
        ch9 = view.findViewById(R.id.ch_9);
        ch10 = view.findViewById(R.id.ch_10);
        ch11 = view.findViewById(R.id.ch_11);
        ch12 = view.findViewById(R.id.ch_12);
        ch13 = view.findViewById(R.id.ch_13);
        ch14 = view.findViewById(R.id.ch_14);
        ch15 = view.findViewById(R.id.ch_15);
        ch16 = view.findViewById(R.id.ch_16);
        ch17 = view.findViewById(R.id.ch_17);
        ch18 = view.findViewById(R.id.ch_18);
        ch19 = view.findViewById(R.id.ch_19);
        ch20 = view.findViewById(R.id.ch_20);
        ch21 = view.findViewById(R.id.ch_21);
        ch22 = view.findViewById(R.id.ch_22);
        ch23 = view.findViewById(R.id.ch_23);
        ch24 = view.findViewById(R.id.ch_24);
        ch25 = view.findViewById(R.id.ch_25);
        ch26 = view.findViewById(R.id.ch_26);
        ch27 = view.findViewById(R.id.ch_27);
        ch28 = view.findViewById(R.id.ch_28);
        ch29 = view.findViewById(R.id.ch_29);
        ch30 = view.findViewById(R.id.ch_30);
        ch31 = view.findViewById(R.id.ch_31);
        ch32 = view.findViewById(R.id.ch_32);
        ch33 = view.findViewById(R.id.ch_33);
        ch34 = view.findViewById(R.id.ch_34);
        ch35 = view.findViewById(R.id.ch_35);
        ch36 = view.findViewById(R.id.ch_36);
        ch37 = view.findViewById(R.id.ch_37);
        ch38 = view.findViewById(R.id.ch_38);
        ch39 = view.findViewById(R.id.ch_39);
        ch40 = view.findViewById(R.id.ch_40);
        ch41 = view.findViewById(R.id.ch_41);
        ch42 = view.findViewById(R.id.ch_42);
        ch43 = view.findViewById(R.id.ch_43);
        ch44 = view.findViewById(R.id.ch_44);
        ch45 = view.findViewById(R.id.ch_45);
        ch46 = view.findViewById(R.id.ch_46);
        ch47 = view.findViewById(R.id.ch_47);
        ch48 = view.findViewById(R.id.ch_48);
        ch49 = view.findViewById(R.id.ch_49);
        ch50 = view.findViewById(R.id.ch_50);
        ch51 = view.findViewById(R.id.ch_51);

        submit = view.findViewById(R.id.submitItems);

        itemName = new ArrayList<String>();
        quantity = new ArrayList<String>();

        CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.ch_1:
                        showPicker(isChecked, ch1.getText().toString());
                        break;
                    case R.id.ch_2:
                        showPicker(isChecked, ch2.getText().toString());
                        break;
                    case R.id.ch_3:
                        showPicker(isChecked, ch3.getText().toString());
                        break;
                    case R.id.ch_4:
                        showPicker(isChecked, ch4.getText().toString());
                        break;
                    case R.id.ch_5:
                        showPicker(isChecked, ch5.getText().toString());
                        break;
                    case R.id.ch_6:
                        showPicker(isChecked, ch6.getText().toString());
                        break;
                    case R.id.ch_7:
                        showPicker(isChecked, ch7.getText().toString());
                        break;
                    case R.id.ch_8:
                        showPicker(isChecked, ch8.getText().toString());
                        break;
                    case R.id.ch_9:
                        showPicker(isChecked, ch9.getText().toString());
                        break;
                    case R.id.ch_10:
                        showPicker(isChecked, ch10.getText().toString());
                        break;
                    case R.id.ch_11:
                        showPicker(isChecked, ch11.getText().toString());
                        break;
                    case R.id.ch_12:
                        showPicker(isChecked, ch12.getText().toString());
                        break;
                    case R.id.ch_13:
                        showPicker(isChecked, ch13.getText().toString());
                        break;
                    case R.id.ch_14:
                        showPicker(isChecked, ch14.getText().toString());
                        break;
                    case R.id.ch_15:
                        showPicker(isChecked, ch15.getText().toString());
                        break;
                    case R.id.ch_16:
                        showPicker(isChecked, ch16.getText().toString());
                        break;
                    case R.id.ch_17:
                        showPicker(isChecked, ch17.getText().toString());
                        break;
                    case R.id.ch_18:
                        showPicker(isChecked, ch18.getText().toString());
                        break;
                    case R.id.ch_19:
                        showPicker(isChecked, ch19.getText().toString());
                        break;
                    case R.id.ch_20:
                        showPicker(isChecked, ch20.getText().toString());
                        break;
                    case R.id.ch_21:
                        showPicker(isChecked, ch21.getText().toString());
                        break;
                    case R.id.ch_22:
                        showPicker(isChecked, ch22.getText().toString());
                        break;
                    case R.id.ch_23:
                        showPicker(isChecked, ch23.getText().toString());
                        break;
                    case R.id.ch_24:
                        showPicker(isChecked, ch24.getText().toString());
                        break;
                    case R.id.ch_25:
                        showPicker(isChecked, ch25.getText().toString());
                        break;
                    case R.id.ch_26:
                        showPicker(isChecked, ch26.getText().toString());
                        break;
                    case R.id.ch_27:
                        showPicker(isChecked, ch27.getText().toString());
                        break;
                    case R.id.ch_28:
                        showPicker(isChecked, ch28.getText().toString());
                        break;
                    case R.id.ch_29:
                        showPicker(isChecked, ch29.getText().toString());
                        break;
                    case R.id.ch_30:
                        showPicker(isChecked, ch30.getText().toString());
                        break;
                    case R.id.ch_31:
                        showPicker(isChecked, ch31.getText().toString());
                        break;
                    case R.id.ch_32:
                        showPicker(isChecked, ch32.getText().toString());
                        break;
                    case R.id.ch_33:
                        showPicker(isChecked, ch33.getText().toString());
                        break;
                    case R.id.ch_34:
                        showPicker(isChecked, ch34.getText().toString());
                        break;
                    case R.id.ch_35:
                        showPicker(isChecked, ch35.getText().toString());
                        break;
                    case R.id.ch_36:
                        showPicker(isChecked, ch36.getText().toString());
                        break;
                    case R.id.ch_37:
                        showPicker(isChecked, ch37.getText().toString());
                        break;
                    case R.id.ch_38:
                        showPicker(isChecked, ch38.getText().toString());
                        break;
                    case R.id.ch_39:
                        showPicker(isChecked, ch39.getText().toString());
                        break;
                    case R.id.ch_40:
                        showPicker(isChecked, ch40.getText().toString());
                        break;
                    case R.id.ch_41:
                        showPicker(isChecked, ch41.getText().toString());
                        break;
                    case R.id.ch_42:
                        showPicker(isChecked, ch42.getText().toString());
                        break;
                    case R.id.ch_43:
                        showPicker(isChecked, ch43.getText().toString());
                        break;
                    case R.id.ch_44:
                        showPicker(isChecked, ch44.getText().toString());
                        break;
                    case R.id.ch_45:
                        showPicker(isChecked, ch45.getText().toString());
                        break;
                    case R.id.ch_46:
                        showPicker(isChecked, ch46.getText().toString());
                        break;
                    case R.id.ch_47:
                        showPicker(isChecked, ch47.getText().toString());
                        break;
                    case R.id.ch_48:
                        showPicker(isChecked, ch48.getText().toString());
                        break;
                    case R.id.ch_49:
                        showPicker(isChecked, ch49.getText().toString());
                        break;
                    case R.id.ch_50:
                        showPicker(isChecked, ch50.getText().toString());
                        break;
                    case R.id.ch_51:
                        showPicker(isChecked, ch51.getText().toString());
                        break;
                }
            }
        };

        ImageButton col_location = view.findViewById(R.id.col_location);
        col_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new PlacePicker.IntentBuilder()
                        .setLatLong(Double.parseDouble(preferences.getString("lat", "25.4670")), Double.parseDouble(preferences.getString("lon", "91.3662")))
                        .showLatLong(true)
                        .setMapType(MapType.NORMAL)
                        .build(getActivity());
                startActivityForResult(intent, 30);
            }
        });

        ch1.setOnCheckedChangeListener(checkListener);
        ch2.setOnCheckedChangeListener(checkListener);
        ch3.setOnCheckedChangeListener(checkListener);
        ch4.setOnCheckedChangeListener(checkListener);
        ch5.setOnCheckedChangeListener(checkListener);
        ch6.setOnCheckedChangeListener(checkListener);
        ch7.setOnCheckedChangeListener(checkListener);
        ch8.setOnCheckedChangeListener(checkListener);
        ch9.setOnCheckedChangeListener(checkListener);
        ch10.setOnCheckedChangeListener(checkListener);
        ch11.setOnCheckedChangeListener(checkListener);
        ch12.setOnCheckedChangeListener(checkListener);
        ch13.setOnCheckedChangeListener(checkListener);
        ch14.setOnCheckedChangeListener(checkListener);
        ch15.setOnCheckedChangeListener(checkListener);
        ch16.setOnCheckedChangeListener(checkListener);
        ch17.setOnCheckedChangeListener(checkListener);
        ch18.setOnCheckedChangeListener(checkListener);
        ch19.setOnCheckedChangeListener(checkListener);
        ch20.setOnCheckedChangeListener(checkListener);
        ch21.setOnCheckedChangeListener(checkListener);
        ch22.setOnCheckedChangeListener(checkListener);
        ch23.setOnCheckedChangeListener(checkListener);
        ch24.setOnCheckedChangeListener(checkListener);
        ch25.setOnCheckedChangeListener(checkListener);
        ch26.setOnCheckedChangeListener(checkListener);
        ch27.setOnCheckedChangeListener(checkListener);
        ch28.setOnCheckedChangeListener(checkListener);
        ch29.setOnCheckedChangeListener(checkListener);
        ch30.setOnCheckedChangeListener(checkListener);
        ch31.setOnCheckedChangeListener(checkListener);
        ch32.setOnCheckedChangeListener(checkListener);
        ch33.setOnCheckedChangeListener(checkListener);
        ch34.setOnCheckedChangeListener(checkListener);
        ch35.setOnCheckedChangeListener(checkListener);
        ch36.setOnCheckedChangeListener(checkListener);
        ch37.setOnCheckedChangeListener(checkListener);
        ch38.setOnCheckedChangeListener(checkListener);
        ch39.setOnCheckedChangeListener(checkListener);
        ch40.setOnCheckedChangeListener(checkListener);
        ch41.setOnCheckedChangeListener(checkListener);
        ch42.setOnCheckedChangeListener(checkListener);
        ch43.setOnCheckedChangeListener(checkListener);
        ch44.setOnCheckedChangeListener(checkListener);
        ch45.setOnCheckedChangeListener(checkListener);
        ch46.setOnCheckedChangeListener(checkListener);
        ch47.setOnCheckedChangeListener(checkListener);
        ch48.setOnCheckedChangeListener(checkListener);
        ch49.setOnCheckedChangeListener(checkListener);
        ch50.setOnCheckedChangeListener(checkListener);
        ch51.setOnCheckedChangeListener(checkListener);

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.chip4:
                        showLayout(isChecked, l_cloth);
                        break;
                    case R.id.chip:
                        showLayout(isChecked, l_toilet);
                        break;
                    case R.id.chip1:
                        showLayout(isChecked, l_food);
                        break;
                    case R.id.chip2:
                        showLayout(isChecked, l_stationery);
                        break;
                    case R.id.chip3:
                        showLayout(isChecked, l_sanitary);
                        break;
                    case R.id.chip5:
                        showLayout(isChecked, l_med);
                        break;
                }
            }
        };

        cloth.setOnCheckedChangeListener(listener);
        toilet.setOnCheckedChangeListener(listener);
//        cloth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked)
//                    l_cloth.setVisibility(View.VISIBLE);
//                else
//                    l_cloth.setVisibility(View.GONE);
//            }
//        });
//        toilet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked)
//                    l_toilet.setVisibility(View.VISIBLE);
//                else
//                    l_toilet.setVisibility(View.GONE);
//            }
//        });
        med.setOnCheckedChangeListener(listener);
        stationery.setOnCheckedChangeListener(listener);
        sanitary.setOnCheckedChangeListener(listener);
        food.setOnCheckedChangeListener(listener);
//
//
//        med.setOnCloseIconClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                med.setCheckedIconVisible(View.INVISIBLE);
//                l_med.setVisibility(View.GONE);
//            }
//        });
//
//        sanitary.setOnCloseIconClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                sanitary.setCheckedIconVisible(View.INVISIBLE);
//                l_sanitary.setVisibility(View.GONE);
//            }
//        });
//
//        stationery.setOnCloseIconClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                stationery.setCheckedIconVisible(View.INVISIBLE);
//                l_stationery.setVisibility(View.GONE);
//            }
//        });
//
//        food.setOnCloseIconClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                food.setCheckedIconVisible(View.INVISIBLE);
//                l_food.setVisibility(View.GONE);
//            }
//        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick", itemName.toString() + " " + quantity.toString());
                MaterialModel modelUser = new MaterialModel(preferences.getString("name","null"),preferences.getString("phone","null"),lat,lon,location,String.valueOf(itemName.size()), "Pending");
                mRef.child(user.getUid()).setValue(modelUser);
                for (int i = 0; i < quantity.size() && i < itemName.size(); i++) {
                    Log.d("for", itemName.get(i) + " " + quantity.get(i));
                    MaterialModel modelItem = new MaterialModel(itemName.get(i), quantity.get(i));
                    mRef.child(user.getUid()+"/Items").push().setValue(modelItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Thank you for Donating.\nWait for your pickup", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getContext(), HomeActivity.class));
                                getActivity().finish();
                            }
                        }
                    });
                }

            }
        });

        return view;
    }

    private void showPicker(boolean isChecked, String s) {
        Log.d("d", "showPicker");

        if (isChecked) {
            Log.d("d", "showPickerif");

            itemName.add(s);

            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.number_picker_dialog_layout, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(view);

            NumberPicker numberPicker = view.findViewById(R.id.number_picker);
            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(100);
            builder.setTitle("Select a Quantity")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("picker", String.valueOf(numberPicker.getValue()));
                            quan = String.valueOf(numberPicker.getValue());
                            quantity.add(quan);
                            Log.d("showPickerif", itemName.toString() + " " + quantity.toString());
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            itemName.remove(s);
                            dialog.cancel();
                        }

                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#0077c2"));
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.WHITE);
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#0077c2"));
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setBackgroundColor(Color.WHITE);
        } else {
            Log.d("d", "showPickerelse");
            itemName.remove(s);
            quantity.remove(quan);
            Log.d("showPickerelse", itemName.toString() + " " + quantity.toString());
        }

    }

    private void showLayout(boolean isChecked, LinearLayout layout) {
        if (isChecked) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
        }
    }
}