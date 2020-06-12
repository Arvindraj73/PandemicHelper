package com.mcet.pandemichelper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
//import com.razorpay.Checkout;
//import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class DonateActivity extends AppCompatActivity implements PaymentResultListener {

    private MaterialButton mDonate;

    private CoordinatorLayout coordinatorLayout;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private FirebaseUser user;

    private String name, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        user = FirebaseAuth.getInstance().getCurrentUser();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDonate = findViewById(R.id.donateBtn);

        Checkout.preload(getApplicationContext());

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel model = dataSnapshot.getValue(UserModel.class);
                name = model.getName();
                phoneNumber = model.getPhone();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        coordinatorLayout = findViewById(R.id.coordinator);
        mDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });

        TextView privacyPolicy = findViewById(R.id.pp);

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                httpIntent.setData(Uri.parse("https://razorpay.com/sample-application/"));
                startActivity(httpIntent);
            }
        });

    }

    public void startPayment() {

        final Activity activity = this;
        /**
         * Instantiate Checkout
         */
        final Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.earth);

        try {
            JSONObject options = new JSONObject();

            options.put("name", name);

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */

            //options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */

            JSONObject theme = new JSONObject();
            theme.put("color", "#FD9725");

            JSONObject prefill = new JSONObject();
            prefill.put("contact", phoneNumber);

            options.put("theme", theme);
            options.put("prefill", prefill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {

        try {
            Toast.makeText(this, "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentSuccess", e);
        }

    }

    @Override
    public void onPaymentError(int i, String s) {

        try {
            Log.d("TAG", "Payment failed: " + i + " " + s);
        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentError", e);
        }

    }
}
