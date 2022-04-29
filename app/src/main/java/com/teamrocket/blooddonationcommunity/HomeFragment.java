package com.teamrocket.blooddonationcommunity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    TextView cardFullName,cardBloodGroup,cardAge;
    CircleImageView cardProfileImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        cardFullName= v.findViewById(R.id.cardFullName);
        cardBloodGroup= v.findViewById(R.id.cardBloodGroup);
        cardAge= v.findViewById(R.id.cardAge);
        cardProfileImg= v.findViewById(R.id.cardProfileImg);

        //Retrieving Data From
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://blood-donation-community-1000-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Users");

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String getName = dataSnapshot.child("full_name").getValue(String.class);
                String getAge = dataSnapshot.child("birth_date").getValue(String.class);
                String getBlood = dataSnapshot.child("blood_group").getValue(String.class);


                cardFullName.setText(getName);
                cardBloodGroup.setText(getBlood);
                cardAge.setText(getAge);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }
}