package com.teamrocket.blooddonationcommunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateAcc extends AppCompatActivity {

    //Phone Auth -->
    Button createNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        //Phone Auth -->
        createNextBtn=findViewById(R.id.createNextBtn);
        final EditText phoneAuth=findViewById(R.id.phoneAuth);

        createNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String num=phoneAuth.getText().toString().trim();

                if(!num.isEmpty()) {
                    if (num.length() == 10) {

                        Intent intent = new Intent(CreateAcc.this, SetPassword.class);
                        intent.putExtra("mobile",num);
                        startActivity(intent);


                    } else {
                        Toast.makeText(CreateAcc.this, "Please enter correct phone number", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(CreateAcc.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}