package com.mcet.pandemichelper;

import androidx.appcompat.app.AppCompatActivity;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.material.internal.NavigationMenu;

public class
LabourActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour);

        FabSpeedDial fabSpeedDial = findViewById(R.id.fabfilter);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                // TODO: Do something with yout menu items, or return false if you don't want to show them
                return true;
            }
        });

//        webView = findViewById(R.id.wv);
        //webView.setWebViewClient(new MyBrowser());

//        webView.getSettings().setLoadsImagesAutomatically(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webView.loadUrl("https://spoken-tutorial.org/");
//        //https://swayam.gov.in/

    }
//    private class MyBrowser extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
//    }
}
