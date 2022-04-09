package com.ngo.blooddonationcommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SetPassword extends AppCompatActivity {

    Button setnextbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        setnextbtn=findViewById(R.id.setnextbtn);

        setnextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SetPassword.this,PersonalDetails.class);
                startActivity(i);
            }
        });


    }
}