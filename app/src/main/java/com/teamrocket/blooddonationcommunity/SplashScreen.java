package com.teamrocket.blooddonationcommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    ImageView gifSplash;
    FirebaseAuth currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.

        int SPLASH_SCREEN_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (hasLoggedIn()) {
                    //Toast.makeText(SplashScreen.this, "Yo", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(i);
                } else {
                    Intent i=new Intent(SplashScreen.this,WelcomePage.class);
                    startActivity(i);
                }
                finish();

            }
        }, SPLASH_SCREEN_TIME_OUT);

    }

    public boolean hasLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("prefs_tag",Context.MODE_PRIVATE);
        return prefs.getBoolean("login", false);
    }
}