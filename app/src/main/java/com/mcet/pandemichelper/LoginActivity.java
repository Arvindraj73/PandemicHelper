package com.mcet.pandemichelper;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;

public class LoginActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    private MaterialButton mLogin;

    private TextInputLayout mEmail, mPass;

    private ProgressDialog progressDialog;

    private String email, pass, role;

    private TextView registerClick;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.emailText);
        mPass = findViewById(R.id.passText);
        mLogin = findViewById(R.id.loginBtn);
        registerClick = findViewById(R.id.regView);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        View nv = findViewById(android.R.id.content).getRootView();
        preferences = getApplicationContext().getSharedPreferences("UserData", MODE_PRIVATE);
        editor = preferences.edit();

        registerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean noErrors = true;

                email = mEmail.getEditText().getText().toString();
                pass = mPass.getEditText().getText().toString();

                if (email.isEmpty()) {
                    mEmail.setError("Field Must Not Be Empty");
                    noErrors = false;
                } else if (!(email.contains("@") && email.contains("."))) {
                    mEmail.setError("Enter a valid Email");
                    Log.d("sf", "adgdag");
                    noErrors = false;
                } else {
                    mEmail.setError(null);
                }

                if (pass.isEmpty()) {
                    mPass.setError("Field Must Not Be Empty");
                    noErrors = false;
                } else if (pass.length() < 6) {
                    mPass.setError("Maximum 6 characters required.");
                    noErrors = false;
                } else {
                    mPass.setError(null);
                }

                if (noErrors) {

                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

//                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Admin");
                            Log.d("login", String.valueOf(task.isSuccessful()));
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("UserInfo");
                                mRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        UserModel model = dataSnapshot.getValue(UserModel.class);
                                        role = model.getRole();
                                        Log.d("role", dataSnapshot.toString());
                                        if (role.equals("admin")) {
                                            editor.putString("role", model.getRole());
                                            editor.commit();
                                            progressDialog.dismiss();
                                            startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                                            finish();
                                        } else {
                                            editor.putString("name", model.getName());
                                            editor.putString("role", model.getRole());
                                            editor.putString("email", model.getEmail());
                                            editor.putString("phone", model.getPhone());
                                            editor.commit();
                                            progressDialog.dismiss();
                                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.d("role", databaseError.toString());
                                    }
                                });

                            } else {
                                progressDialog.dismiss();
                                new Notify(nv, "Login Failed. Invalid Email and Password.").showSnack("long");
                                mEmail.getEditText().setText("");
                                mPass.getEditText().setText("");
                            }
                        }
                    });
                }

            }
        });

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CALL_PHONE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                        Log.d("LoginActivity.this", "Permissions Granted");
                    else {

                        Toast.makeText(LoginActivity.this, "Permissions Denied", Toast.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CALL_PHONE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mFirebaseUser != null) {

            if (preferences.getString("role", "").equals("admin")) {
                progressDialog.dismiss();
                startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                finish();
            } else {
                progressDialog.dismiss();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
//            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("UserInfo");
//            mRef.child(mFirebaseUser.getUid()+"/role").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    role = dataSnapshot.getValue().toString();
//                    Log.d("role",role);
//                    if (role.equals("admin")){
//                        progressDialog.dismiss();
//                        startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
//                        finish();
//                    }
//                    else {
//                        progressDialog.dismiss();
//                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                        finish();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Log.d("role",databaseError.toString());
//                }
//            });
        }

    }

}
