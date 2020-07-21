package com.mcet.pandemichelper;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EPassActivity extends AppCompatActivity {

    private TextInputLayout pincode, state, district;
    private MaterialButton mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_pass);
        showEpassDialog();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        pincode = findViewById(R.id.pincode);
//        state = findViewById(R.id.state);
//        district = findViewById(R.id.district);
//
//        mSubmit = findViewById(R.id.submit1);
//
//        OkHttpClient client = new OkHttpClient();
//
//        mSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String pin = pincode.getEditText().getText().toString();
//                String url = "https://api.postalpincode.in/pincode/"+pin;
//                Log.d("url",url);
//
//                Request request = new Request.Builder()
//                        .url(url)
//                        .build();
//
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                        Log.d("DataError",e.toString());
//                    }
//
//                    @Override
//                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                        Log.d("data",response.body().string());
//                    }
//                });
//            }
//        });
//
//    }

    private void showEpassDialog() {

        View v = LayoutInflater.from(this).inflate(R.layout.epass_category_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(v)
                .setTitle("Select a Category");
        AlertDialog dialog = builder.create();
        v.findViewById(R.id.personal_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new PersonalPassFragment());
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.est_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new EssentialPassFragment());
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.e_container, fragment);
        fragmentTransaction.commit();
    }

}