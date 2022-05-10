package com.teamrocket.blooddonationcommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class BloodBank extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);

        String url="http://blood-banks-location.netlify.app/";

        WebView webView = (WebView)findViewById(R.id.bloodBankWebView);
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setSupportMultipleWindows(true);


        webView.setWebViewClient(new WebViewClient(){

            public void onPageFinished(WebView view,String url){
                ProgressBar progressbar = (ProgressBar) findViewById(R.id.bbProgressBar);
                progressbar.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(url);
    }
}