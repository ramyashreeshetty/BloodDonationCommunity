package com.ngo.blooddonationcommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreateAcc extends AppCompatActivity {

    Button createnextbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        createnextbtn=findViewById(R.id.createnextbtn);

        createnextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateAcc.this,VerifyOTP.class);
                startActivity(i);
            }
        });
    }
}