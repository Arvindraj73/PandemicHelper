package com.mcet.pandemichelper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class PersonalPassFragment extends Fragment implements View.OnClickListener {

    private ImageView imageView, imageView2;
    private TextView chosenFile, chosenReasonFile;
    private Spinner gender, idProofType, travelReason, vehicleType;
    private FloatingActionButton history;
    private TextInputLayout ageText, noOfPassengersText, dateText, nameText, idProofNoText, vehicleNoText;

    private View myView;

    private ArrayList<Uri> filePath = new ArrayList<Uri>();

    private StorageReference storageReference;
    private DatabaseReference databaseReference,databaseReference1;

    private SharedPreferences preferences;

    private String[] fileName = new String[]{"IdProof", "ReasonProof"};
    private String fromAddress, toAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_personal_pass, container, false);
        preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("UserData", Context.MODE_PRIVATE);

        storageReference = FirebaseStorage.getInstance().getReference(preferences.getString("uid", ""));
        databaseReference = FirebaseDatabase.getInstance().getReference("PersonalPass");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("UserInfo");
        gender = myView.findViewById(R.id.genderSpinner);
        travelReason = myView.findViewById(R.id.travel_reason_spinner);
        chosenFile = myView.findViewById(R.id.chosen_file);
        chosenReasonFile = myView.findViewById(R.id.chosen_reason_file);
        idProofType = myView.findViewById(R.id.idTypeSpinner);
        imageView = myView.findViewById(R.id.img);
        imageView2 = myView.findViewById(R.id.img2);
        vehicleType = myView.findViewById(R.id.vehicle_spinner);
        history = myView.findViewById(R.id.Epass_history);
        ageText = myView.findViewById(R.id.age);
        noOfPassengersText = myView.findViewById(R.id.passenger_count);
        dateText = myView.findViewById(R.id.travelDate);
        nameText = myView.findViewById(R.id.nameApplicant);
        vehicleNoText = myView.findViewById(R.id.vehicleNo);
        idProofNoText = myView.findViewById(R.id.idProofNo);

        ageText.getEditText().setEnabled(true);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent his= new Intent(getContext(), HistoryActivity.class);
                his.putExtra("id", "perPass");
                startActivity(his);
            }
        });
        ageText.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNumberDialog("Age");
            }
        });

        noOfPassengersText.getEditText().setEnabled(true);
        noOfPassengersText.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNumberDialog("No of Passengers");
            }
        });

        dateText.getEditText().setEnabled(true);
        dateText.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDateDialog();
            }
        });

        myView.findViewById(R.id.choose_file).setOnClickListener(this);
        myView.findViewById(R.id.from_location).setOnClickListener(this);
        myView.findViewById(R.id.to_location).setOnClickListener(this);
        myView.findViewById(R.id.choose_reason_file).setOnClickListener(this);
        myView.findViewById(R.id.submit1).setOnClickListener(this);

        return myView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.choose_file:
                openFileExplorer(10);
                break;
            case R.id.choose_reason_file:
                openFileExplorer(11);
                break;
            case R.id.to_location:
                openLocationPicker(13);
                break;
            case R.id.from_location:
                openLocationPicker(12);
                break;
            case R.id.submit1:
                submitDetails();
                break;
        }

    }

    private void submitDetails() {
//
//            ProgressDialog progressDialog = new ProgressDialog(getContext());
//            progressDialog.setTitle("Applying ePass");
//            progressDialog.show();
        PassDetailsModel model = new PassDetailsModel(
                nameText.getEditText().getText().toString(),
                ageText.getEditText().getText().toString(),
                gender.getSelectedItem().toString(),
                travelReason.getSelectedItem().toString(),
                idProofType.getSelectedItem().toString(),
                idProofNoText.getEditText().getText().toString(),
                fromAddress,
                toAddress,
                noOfPassengersText.getEditText().getText().toString(),
                vehicleType.getSelectedItem().toString(),
                vehicleNoText.getEditText().getText().toString(),
                dateText.getEditText().getText().toString(),
                "Pending"
        );
        String key=databaseReference1.child(preferences.getString("uid", "")).child("epass").push().getKey();
        databaseReference1.child(preferences.getString("uid", "")).child("epass/"+key).setValue(model);

        databaseReference.child(preferences.getString("uid", "")).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    databaseReference.child(preferences.getString("uid", "")).child("key").setValue(key);
                    storageReference.child(fileName[0]).putFile(filePath.get(0)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference.child(fileName[0]).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Log.d("urlm", task.getResult().toString());
                                    databaseReference.child(preferences.getString("uid", "") + "/idProof").setValue(task.getResult().toString());
                                    databaseReference1.child("epass/"+key).child("/idProof").setValue(task.getResult().toString());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("urle", e.toString());
                                }
                            });
                        }
                    });
                    storageReference.child(fileName[1]).putFile(filePath.get(1)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference.child(fileName[1]).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Log.d("urlm", task.getResult().toString());
                                    databaseReference.child(preferences.getString("uid", "") + "/reasonProof").setValue(task.getResult().toString());
                                    databaseReference1.child("epass/"+key).child("/reasonProof").setValue(task.getResult().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Wait For Approval", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getContext(), HomeActivity.class));
                                                getActivity().finish();
                                            }
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("urle", e.toString());
                                }
                            });
                        }
                    });

                    //new Notify(myView, "Success").showSnack("short");

                }

            }
        });

    }

    private void openLocationPicker(int requestCode) {

        Intent intent = new PlacePicker.IntentBuilder()
                .setLatLong(Double.parseDouble(preferences.getString("lat", "25.4670")), Double.parseDouble(preferences.getString("lon", "91.3662")))
                .showLatLong(true)
                .setMapType(MapType.NORMAL)
                .setPlaceSearchBar(true, String.valueOf(R.string.google_maps_key))
                .build(getActivity());
        startActivityForResult(intent, requestCode);

    }

    private void openDateDialog() {

        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("date", dayOfMonth + "/" + month + "/" + year);
                dateText.getEditText().setText(dayOfMonth + "/" + month + "/" + year);
            }
        }, y, m, d);
        datePickerDialog.show();

    }

    private void openNumberDialog(String s) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.number_picker_dialog_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        NumberPicker picker = view.findViewById(R.id.number_picker);
        if (s.equals("Age")) {
            picker.setMaxValue(100);
            picker.setMinValue(15);
        } else if (s.equals("No of Passengers")) {
            picker.setMaxValue(10);
            picker.setMinValue(1);
        }
        builder.setTitle("Select " + s)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (s.equals("Age")) {
                            Log.d("age", String.valueOf(picker.getValue()));
                            ageText.getEditText().setText(String.valueOf(picker.getValue()));
                        } else if (s.equals("No of Passengers")) {
                            Log.d("pass", String.valueOf(picker.getValue()));
                            noOfPassengersText.getEditText().setText(String.valueOf(picker.getValue()));
                        }
                    }
                });

        builder.show();
    }

    private void openFileExplorer(int requestCode) {

        Intent fileIntent = new Intent();
        fileIntent.setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(fileIntent, "Select Id Proof"), requestCode);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            filePath.add(data.getData());
            Log.d("dataINtent", data.toString());
            Cursor cursor = getActivity().getContentResolver().query(filePath.get(0), null, null, null, null);
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            Log.d("ki", cursor.getString(nameIndex));
            chosenFile.setText(cursor.getString(nameIndex));
            assert filePath != null;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath.get(0));
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 11 && resultCode == RESULT_OK && data != null) {
            filePath.add(data.getData());
            Log.d("dataINtent", data.toString());
            Cursor cursor = getActivity().getContentResolver().query(filePath.get(1), null, null, null, null);
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            Log.d("ki", cursor.getString(nameIndex));
            chosenReasonFile.setText(cursor.getString(nameIndex));
            assert filePath != null;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath.get(1));
                imageView2.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 12 && resultCode == RESULT_OK && data != null) {
            AddressData addressData = data.getParcelableExtra("ADDRESS_INTENT");
//            lat = String.valueOf(addressData.getLatitude());
//            lon = String.valueOf(addressData.getLongitude());
            Log.d("from", addressData.toString());
            fromAddress = addressData.toString();
        } else if (requestCode == 13 && resultCode == RESULT_OK && data != null) {
            AddressData addressData = data.getParcelableExtra("ADDRESS_INTENT");
//            lat = String.valueOf(addressData.getLatitude());
//            lon = String.valueOf(addressData.getLongitude());
            Log.d("to", addressData.toString());
            toAddress = addressData.toString();
        }

    }
}