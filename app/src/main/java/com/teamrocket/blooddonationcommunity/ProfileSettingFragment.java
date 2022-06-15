package com.teamrocket.blooddonationcommunity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettingFragment extends Fragment {

    FirebaseStorage storage;
    StorageReference reference;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_setting, container, false);

        EditText fullName = v.findViewById(R.id.et_firstname);
        EditText email = v.findViewById(R.id.et_email);
        EditText dob = v.findViewById(R.id.et_dob);
        EditText blood = v.findViewById(R.id.et_bloodGroup);
        EditText city = v.findViewById(R.id.et_city);

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
                String getCity= dataSnapshot.child("current_location").getValue(String.class);
                String getEmail= dataSnapshot.child("email_id").getValue(String.class);


                fullName.setText(getName);
                blood.setText(getBlood);
                dob.setText(getAge);
                email.setText(getEmail);
                city.setText(getCity);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        CircleImageView pfp=v.findViewById(R.id.et_pfp);

        storage = FirebaseStorage.getInstance();
        reference = storage.getReference().child("Users/").child("images/").child(uid);
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.get().load(uri).into(pfp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        //Donor Profile

        MaterialButton donorBtn=v.findViewById(R.id.donorProfileBtn);

        donorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getActivity().getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.main_container,new DonorFragment());
                fr.commit();
            }
        });

        MaterialButton saveBtn = v.findViewById(R.id.profileSaveBtn);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMessage();

            }
        });

        return v;
    }

    public void popupMessage(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Can be changed only after 15 days!");
        alertDialogBuilder.setIcon(R.drawable.construction_icon);
        alertDialogBuilder.setTitle("Privacy Alert");
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("internet","Ok btn pressed");
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}