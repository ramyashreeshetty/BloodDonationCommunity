package com.teamrocket.blooddonationcommunity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class PostFragment extends Fragment {

    //Button --> Post & Donor
    ExtendedFloatingActionButton donorFab,recFab;
    String uid,getName,getPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
        donorFab = (ExtendedFloatingActionButton) rootView.findViewById(R.id.postDonorFab);
        recFab=(ExtendedFloatingActionButton) rootView.findViewById(R.id.postRecFab);

        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        recFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Realtime DB
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://blood-donation-community-1000-default-rtdb.asia-southeast1.firebasedatabase.app");
                DatabaseReference myRef = database.getReference("Users");

                myRef.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        getName = dataSnapshot.child("full_name").getValue(String.class);
                        getPhone = dataSnapshot.child("phone_number").getValue(String.class);

                        Toast.makeText(getActivity(), getName, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(getActivity(), PostReceiver.class);
                intent.putExtra("namePost",getName);
                intent.putExtra("phonePost",getPhone);
                ((MainActivity) getActivity()).startActivity(intent);
            }
        });

        donorFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostDonor.class);
                ((MainActivity) getActivity()).startActivity(intent);
            }
        });

        return rootView;
    }
}