package com.teamrocket.blooddonationcommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class WelcomePage extends AppCompatActivity {

    ExtendedFloatingActionButton createAcc;
    ExtendedFloatingActionButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_welcome_page);

        //create Account
        createAcc=findViewById(R.id.signUpBtn);
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(WelcomePage.this,CreateAcc.class);
                startActivity(i);
                finish();
            }
        });

    }
}