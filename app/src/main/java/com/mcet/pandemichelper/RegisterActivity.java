package com.mcet.pandemichelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private MaterialButton mReg;

    private TextInputLayout mName,mEmail,mPhone,mPass,mConfirmPass;

    private TextView errorView;

    private String name,email,pass,cpass,phone;

    private ProgressDialog progressDialog;

    private FirebaseDatabase mData;
    private DatabaseReference mRef;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mName = findViewById(R.id.nameText);
        mPhone = findViewById(R.id.phoneText);
        mPass = findViewById(R.id.passwordText);
        mConfirmPass = findViewById(R.id.passText);
        mReg = findViewById(R.id.regBtn);
        errorView = findViewById(R.id.errorView);
        mEmail = findViewById(R.id.emailText);
        spinner = findViewById(R.id.spinner);

        progressDialog = new ProgressDialog(this);

        mData = FirebaseDatabase.getInstance();
        mRef = mData.getReference();

        mReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean noErrors = true;
                phone = mPhone.getEditText().getText().toString();
                name = mName.getEditText().getText().toString();
                email = mEmail.getEditText().getText().toString();
                pass = mPass.getEditText().getText().toString();
                cpass = mConfirmPass.getEditText().getText().toString();

                //Log.d("df",String.valueOf(phoneStr.length()));
                if (name.isEmpty()) {
                    mName.setError("Field Must Not Be Empty");
                    noErrors = false;
                }
//                else if(name.length()!=10){
//                    mName.setError("Enter a valid Phone Number");
//                    noErrors = false;
//                }
                else {
                    mName.setError(null);
                }

                if (phone.isEmpty()) {
                    mPhone.setError("Field Must Not Be Empty");
                    noErrors = false;
                }
                else if(phone.length()!=10){
                    mPhone.setError("Enter a valid Phone Number");
                    noErrors = false;
                }
                else {
                    mPhone.setError(null);
                }

                if (email.isEmpty()) {
                    mEmail.setError("Field Must Not Be Empty");
                    noErrors = false;
                }
//                else if(email.length()!=10){
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

                if (cpass.isEmpty()) {
                    mConfirmPass.setError("Field Must Not Be Empty");
                    noErrors = false;
                }
//                else if(cpass.length()!=10){
//                    mConfirmPass.setError("Enter a valid Phone Number");
//                    noErrors = false;
//                }
                else {
                    mConfirmPass.setError(null);
                }

                if (spinner.getSelectedItem().equals("Select Your Role")){
                    errorView.setText("Please select a valid role");
                    noErrors = false;
                }
                else{
                    noErrors = true;
                }

                if (!pass.equals(cpass)) {
                    mConfirmPass.setError("Passwords does not match");
                    mPass.setError("Passwords does not match");
                    mConfirmPass.getEditText().setText("");
                    mPass.getEditText().setText("");
                    noErrors = false;
                }

                if(noErrors){

                    progressDialog.setMessage("Registering your details...");
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

                                UserModel mUser = new UserModel(name,phone,email,pass,mFirebaseUser.getUid(),spinner.getSelectedItem().toString(),"","");
                                mRef.child("UserInfo/"+mFirebaseUser.getUid()).setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){

                                            progressDialog.dismiss();
                                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

                                        }

                                    }
                                });

                            }

                        }
                    });

                }

            }
        });

    }
}
