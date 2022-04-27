package com.teamrocket.blooddonationcommunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SetPassword extends AppCompatActivity {

    ExtendedFloatingActionButton setnextbtn;
    EditText pin1;
    EditText pin2;
    FloatingActionButton back;
    public String phonenumber_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);

        //Back Button
        back=findViewById(R.id.backSetPass);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //Set Password
        pin1 = findViewById(R.id.enterpass);
        pin2 = findViewById(R.id.reenterpass);

        //Next btn
        setnextbtn = findViewById(R.id.setnextbtn);

        setnextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phonenumber_value = getIntent().getStringExtra("mobile");

                String pinString = pin1.getText().toString();
                String pin2String = pin2.getText().toString();

                //pin max size is 4
                if (pinString.length() != 4) {
                    Toast.makeText(SetPassword.this, "Minimum pin size not matching", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pinString.equals(pin2String)) {
                    Toast.makeText(SetPassword.this, "Pins are not matching", Toast.LENGTH_SHORT).show();
                    return;
                }

//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user == null) {
//                    // No session user
//                    return;
//                }

//                //Generating Hashmap
//                HashMap<String, String> map = new HashMap<>();
//                map.put("phone_number",phonenumber_value);
//                map.put("pin",pinString);

//                //Realtime DB
//                FirebaseDatabase database = FirebaseDatabase.getInstance("https://blood-donation-community-1000-default-rtdb.asia-southeast1.firebasedatabase.app");
//                DatabaseReference myRef = database.getReference("Users");

//                //Generating Key
//                String key= myRef.push().getKey();

//                //Push Value
//                myRef.push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(SetPassword.this, "User added", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.e("ErrorAdding",e.getMessage());
//                                Toast.makeText(SetPassword.this, e.getMessage() , Toast.LENGTH_SHORT).show();
//                            }
//                        });



                //Intent -------------->
                Intent i = new Intent(SetPassword.this, PersonalDetails.class);
                i.putExtra("pin", pinString);
                i.putExtra("phone_number",phonenumber_value);
                startActivity(i);
            }
        });
    }
}