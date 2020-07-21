package com.mcet.pandemichelper;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class CoursesActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        webView = findViewById(R.id.webview);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (getIntent().getStringExtra("id").equals("cci")) {
            webView.loadUrl("http://cci.org.in/Counselling_Services.aspx");
        } else if (getIntent().getStringExtra("id").equals("course")) {
            webView.loadUrl("https://swayam.gov.in/NPTEL");
        } else if (getIntent().getStringExtra("id").equals("msme")) {
            webView.loadUrl("https://pandemichelper.000webhostapp.com/store/index.php");
        } else if (getIntent().getStringExtra("id").equals("ao")) {
            webView.loadUrl("http://cara.nic.in/");
        } else if (getIntent().getStringExtra("id").equals("io")) {
            webView.loadUrl("https://indianorphanage.com/");
        }

    }
}