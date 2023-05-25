package com.example.registerloginexample;


import android.content.Intent;

import android.os.Bundle;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;


public class MapActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        String binLoc = intent.getStringExtra("binLoc");

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });

        // 최초 웹뷰 로드
        webView.loadUrl("http://gamhs44.ivyro.net/addr_map.html?binloc="+binLoc);

}
}


