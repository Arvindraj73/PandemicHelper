package com.mcet.pandemichelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class AdminLoginActivity extends AppCompatActivity {

    private MaterialButton mLogin;

    private TextInputLayout user, mPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        user = findViewById(R.id.aUser);
        mPass = findViewById(R.id.aPass);
        mLogin = findViewById(R.id.aLogin);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getEditText().getText().toString().equals("admin1") && mPass.getEditText().getText().toString().equals("admin1")){
                    startActivity(new Intent(AdminLoginActivity.this,AdminHomeActivity.class));
                }
                else{
                    Toast.makeText(AdminLoginActivity.this,"Invalid Admin",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}