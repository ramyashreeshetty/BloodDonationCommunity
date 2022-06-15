package com.teamrocket.blooddonationcommunity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Objects;

public class NotifFragment extends Fragment {

    public String notifBlood;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notif, container, false);
        TextView notifText = v.findViewById(R.id.notifText);
        //Retrieving Data From
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://blood-donation-community-1000-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Users");

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notifBlood = dataSnapshot.child("blood_group").getValue(String.class);
                //Toast.makeText(getContext(), notifBlood+"is my bg", Toast.LENGTH_SHORT).show();

                //Retrieving Data From
                DatabaseReference myRef2 = database.getReference("Notification");
                myRef2.child(notifBlood).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String getName = dataSnapshot.child("name").getValue(String.class);
                        String getBlood = dataSnapshot.child("bloodGroup").getValue(String.class);
                        String getCity= dataSnapshot.child("city").getValue(String.class);

                        if(getName==null){
                            notifText.setText("Notifications will appear here!!");
                        }
                        else{
                            String notifMsg = getName+" living in "+getCity+" needs "+getBlood+" group";
                            notifText.setText(notifMsg);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return v;
    }
}