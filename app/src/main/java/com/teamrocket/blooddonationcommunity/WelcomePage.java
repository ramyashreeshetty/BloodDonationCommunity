package com.teamrocket.blooddonationcommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class WelcomePage extends AppCompatActivity {

    TextView createAcc;
    ExtendedFloatingActionButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        //create Account
        createAcc=findViewById(R.id.createAccBtn);
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(WelcomePage.this,CreateAcc.class);
                startActivity(i);
            }
        });

        //log in
        loginBtn=findViewById(R.id.logInBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(WelcomePage.this,LogIn.class);
                startActivity(i);
            }
        });

    }
}