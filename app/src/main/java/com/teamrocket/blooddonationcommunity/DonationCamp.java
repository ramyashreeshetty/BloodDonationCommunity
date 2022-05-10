package com.teamrocket.blooddonationcommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class DonationCamp extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_camp);


        String campUrl="http://blood-donation-camps-location.netlify.app";

        //Displaying WebView

        webView = (WebView)findViewById(R.id.campWebView);
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setSupportMultipleWindows(true);


        webView.setWebViewClient(new WebViewClient(){

            public void onPageFinished(WebView view,String url){
                progressBar = (ProgressBar) findViewById(R.id.campProgressBar);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(campUrl);
    }
}