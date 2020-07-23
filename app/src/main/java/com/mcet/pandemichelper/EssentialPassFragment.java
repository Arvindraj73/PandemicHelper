package com.mcet.pandemichelper;

import android.app.AlertDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sucho.placepicker.AddressData;
import com.sucho.placepicker.MapType;
import com.sucho.placepicker.PlacePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.app.Activity.RESULT_OK;

public class EssentialPassFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout vehicleReqText;
    private LinearLayout vehicleLayout;
    private FloatingActionButton history;
    private ArrayList<Uri> filePath = new ArrayList<Uri>();
    private ArrayList<View> views = new ArrayList<View>();

    private ImageView imageView3, imageView4;
    private TextView chosen_file_app, chosen_or_proof_file;
    private Spinner applicantProofSpinner, oRProofType;

    private TextInputLayout emp_countText, orAddress, nameAText, idProofNoAText, orNameText, orTypeText, orCatText;

    private View myView;

    private StorageReference storageReference;
    private DatabaseReference databaseReference,databaseReference1;

    private SharedPreferences preferences;

    private String[] fileName = new String[]{"IdProof", "OrganisationProof"};
    private ArrayList<String> vehicle_no = new ArrayList<String>();
    private ArrayList<String> vehicle_type = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_essential_pass, container, false);

        preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("UserData", Context.MODE_PRIVATE);

        storageReference = FirebaseStorage.getInstance().getReference(preferences.getString("uid", ""));
        databaseReference = FirebaseDatabase.getInstance().getReference("TransportPass");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("UserInfo");
        history = myView.findViewById(R.id.Tpass_history);
        applicantProofSpinner = myView.findViewById(R.id.applicantProofSpinner);
        chosen_file_app = myView.findViewById(R.id.chosen_file_app);
        chosen_or_proof_file = myView.findViewById(R.id.chosen_or_proof_file);
        oRProofType = myView.findViewById(R.id.oRProofType);
        imageView3 = myView.findViewById(R.id.img3);
        imageView4 = myView.findViewById(R.id.img4);
        orAddress = myView.findViewById(R.id.orAddress);
        orCatText = myView.findViewById(R.id.orCat);
        orTypeText = myView.findViewById(R.id.orType);
        orNameText = myView.findViewById(R.id.orName);

        emp_countText = myView.findViewById(R.id.emp_count);
        nameAText = myView.findViewById(R.id.nameApplicantOr);
        idProofNoAText = myView.findViewById(R.id.idProofNoA);

        orAddress.getEditText().setEnabled(true);
        orAddress.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLocationPicker();
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent his= new Intent(getContext(), HistoryActivity.class);
                his.putExtra("id", "transPass");
                startActivity(his);
            }
        });

        myView.findViewById(R.id.choose_file_app).setOnClickListener(this);
        myView.findViewById(R.id.choose_or_proof_file).setOnClickListener(this);
        myView.findViewById(R.id.submitOr).setOnClickListener(this);
        myView.findViewById(R.id.orAddress).setOnClickListener(this);
        vehicleLayout = myView.findViewById(R.id.vehicleInfo);

        vehicleReqText = myView.findViewById(R.id.vehicle_count);
        vehicleReqText.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNumberDialog("Number of Vehicles Required");
            }
        });

        return myView;

    }

    private void openNumberDialog(String s) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.number_picker_dialog_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        NumberPicker picker = view.findViewById(R.id.number_picker);
        if (s.equals("Number of Vehicles Required")) {
            picker.setMaxValue(10);
            picker.setMinValue(1);
        } else if (s.equals("No of Employees")) {
            picker.setMaxValue(10000);
            picker.setMinValue(10);
        }
        builder.setTitle("Select " + s)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (s.equals("Number of Vehicles Required")) {
                            Log.d("VehiclesRequired", String.valueOf(picker.getValue()));
                            vehicleReqText.getEditText().setText(String.valueOf(picker.getValue()));
                            showViews(picker.getValue());
                        } else if (s.equals("No of Employees")) {
                            Log.d("VehiclesRequired", String.valueOf(picker.getValue()));
                            emp_countText.getEditText().setText(String.valueOf(picker.getValue()));
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

        if (requestCode == 23 && resultCode == RESULT_OK && data != null) {
            filePath.add(data.getData());
            Log.d("dataINtent", data.toString());
            Cursor cursor = getActivity().getContentResolver().query(filePath.get(0), null, null, null, null);
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            Log.d("ki", cursor.getString(nameIndex));
            chosen_file_app.setText(cursor.getString(nameIndex));
            assert filePath != null;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath.get(0));
                //imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 22 && resultCode == RESULT_OK && data != null) {
            filePath.add(data.getData());
            Log.d("dataINtent", data.toString());
            Cursor cursor = getActivity().getContentResolver().query(filePath.get(1), null, null, null, null);
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            Log.d("ki", cursor.getString(nameIndex));
            chosen_or_proof_file.setText(cursor.getString(nameIndex));
            assert filePath != null;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath.get(1));
                //imageView2.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 21 && resultCode == RESULT_OK && data != null) {
            AddressData addressData = data.getParcelableExtra("ADDRESS_INTENT");
//            lat = String.valueOf(addressData.getLatitude());
//            lon = String.valueOf(addressData.getLongitude());
            Log.d("from", addressData.toString());
            orAddress.getEditText().setText(addressData.toString());
        }

    }

    private void showViews(int count) {
        View childView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < count; i++) {
            childView = inflater.inflate(R.layout.vehicle_layout, null);
            views.add(i, childView);
            vehicleLayout.addView(childView);
        }
        for (int i = 0; i < count; i++) {
            View v = views.get(i);
            MaterialButton button = v.findViewById(R.id.submit_vehicle);
            TextInputLayout textInputLayout = v.findViewById(R.id.vehicleNo);
            Spinner spinner = v.findViewById(R.id.vehicle_spinner);
            int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vehicle_no.add(finalI,textInputLayout.getEditText().getText().toString());
                    vehicle_type.add(finalI,spinner.getSelectedItem().toString());

                    Log.d("veNoIN", vehicle_no.get(finalI));
                    Log.d("veTyIN", vehicle_type.get(finalI));
                    vehicleLayout.removeView(v);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_file_app:
                openFileExplorer(23);
                break;
            case R.id.choose_or_proof_file:
                openFileExplorer(22);
                break;
            case R.id.submitOr:
                submitDetails();
                break;
        }
    }
        private void submitDetails() {

            Log.d("veNo", String.valueOf(vehicle_no.size()));
            Log.d("veTy", String.valueOf(vehicle_type.size()));

//            ProgressDialog progressDialog = new ProgressDialog(getContext());
//            progressDialog.setTitle("Applying ePass");
//            progressDialog.show();

            EssentialPassModel model = new EssentialPassModel(
                    nameAText.getEditText().getText().toString(),
                    applicantProofSpinner.getSelectedItem().toString(),
                    idProofNoAText.getEditText().getText().toString(),
                    orNameText.getEditText().getText().toString(),
                    orTypeText.getEditText().getText().toString(),
                    orCatText.getEditText().getText().toString(),
                    oRProofType.getSelectedItem().toString(),
                    emp_countText.getEditText().getText().toString(),
                    orAddress.getEditText().getText().toString(),
                    vehicleReqText.getEditText().getText().toString(),
                    "Pending"
            );
            String key=databaseReference1.child(preferences.getString("uid", "")).child("TPass").push().getKey();
            databaseReference1.child(preferences.getString("uid", "")).child("TPass/"+key).setValue(model);

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
                                        databaseReference.child(preferences.getString("uid", "") + "/applicantProof").setValue(task.getResult().toString());
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
                                        databaseReference.child(preferences.getString("uid", "") + "/orProof").setValue(task.getResult().toString());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("urle", e.toString());
                                    }
                                });
                            }
                        });

                        for (int i = 0; i < vehicle_no.size() && i < vehicle_type.size(); i++) {
                            Log.d("for", vehicle_no.get(i) + " " + vehicle_type.get(i));
                            EssentialPassModel modelItem = new EssentialPassModel(vehicle_type.get(i), vehicle_no.get(i));
                            databaseReference.child(preferences.getString("uid", "")+"/Vehicles").push().setValue(modelItem).addOnCompleteListener(new OnCompleteListener<Void>() {
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

                }
            });


        }

    private void openLocationPicker() {

        Intent intent = new PlacePicker.IntentBuilder()
                .setLatLong(Double.parseDouble(preferences.getString("lat", "25.4670")), Double.parseDouble(preferences.getString("lon", "91.3662")))
                .showLatLong(true)
                .setMapType(MapType.NORMAL)
                .setPlaceSearchBar(true, String.valueOf(R.string.google_maps_key))
                .build(getActivity());
        startActivityForResult(intent, 21);

    }
}