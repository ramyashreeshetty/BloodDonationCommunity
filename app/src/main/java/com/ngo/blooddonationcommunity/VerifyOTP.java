package com.ngo.blooddonationcommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VerifyOTP extends AppCompatActivity {

    Button vernextbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        vernextbtn=findViewById(R.id.vernextbtn);

        vernextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VerifyOTP.this,SetPassword.class);
                startActivity(i);
            }
        });
    }
}