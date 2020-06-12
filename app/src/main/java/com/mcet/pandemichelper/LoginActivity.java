package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.tv.TvContract;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.net.ssl.HostnameVerifier;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;

public class LoginActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    private MaterialButton mLogin;

    private TextInputLayout mEmail, mPass;

    private ProgressDialog progressDialog;

    private String email, pass;

    private TextView registerClick, admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.emailText);
        mPass = findViewById(R.id.passText);
        mLogin = findViewById(R.id.loginBtn);
        registerClick = findViewById(R.id.regView);
        admin = findViewById(R.id.admin);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, AdminLoginActivity.class));
            }
        });

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        registerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
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
                }
//                else if(email.){
//                    mEmail.setError("Enter a valid Phone Number");
//                    noErrors = false;
//                }
                else {
                    mEmail.setError(null);
                }

                if (pass.isEmpty()) {
                    mPass.setError("Field Must Not Be Empty");
                    noErrors = false;
                }
//                else if(pass.length()!=10){
//                    mPass.setError("Enter a valid Phone Number");
//                    noErrors = false;
//                }
                else {
                    mPass.setError(null);
                }

                if (noErrors) {

                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

//                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Admin");
//
//                            if (ref.child(mFirebaseUser.getUid()) != null) {
//                                progressDialog.dismiss();
//                                startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
//                            } else {

                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                            //}
                        }
                    });

                }

            }
        });

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CALL_PHONE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                        Toast.makeText(LoginActivity.this, "Permissions Granted", Toast.LENGTH_SHORT).show();
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
            Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();
        }

    }

}
