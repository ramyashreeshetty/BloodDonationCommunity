package com.teamrocket.blooddonationcommunity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
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

public class ProfileFragment extends Fragment  {

    ExtendedFloatingActionButton logOut;
    CircleImageView profileImg;
    TextView profileName;

    FirebaseStorage storage;
    StorageReference reference;

    ImageButton ProfileInfoBtn;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        ProfileInfoBtn = v.findViewById(R.id.ib_profile);


        //on click will load profile setting fragment on main_container
        ProfileInfoBtn.setOnClickListener(view -> {

            FragmentTransaction fr = getActivity().getSupportFragmentManager().beginTransaction();
            fr.replace(R.id.main_container,new ProfileSettingFragment());
            fr.commit();
        });


        ImageButton PostInfoBtn = v.findViewById(R.id.ib_posts);

        PostInfoBtn.setOnClickListener(view -> {

            FragmentTransaction fr = getActivity().getSupportFragmentManager().beginTransaction();
            fr.replace(R.id.main_container,new PostInfoFragment());
            fr.commit();
        });


        //Displaying Details

        profileImg=v.findViewById(R.id.profile_picture);
        profileName=v.findViewById(R.id.view_for_name);

        //Retrieving Data From
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://blood-donation-community-1000-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Users");

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String getName = dataSnapshot.child("full_name").getValue(String.class);

                profileName.setText(getName);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Retrieving Data from Storage
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference().child("Users/").child("images/").child(uid);
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.get().load(uri).into(profileImg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        //User LogOut -->
        logOut= v.findViewById(R.id.profileLogOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Shared Preferences
                SharedPreferences prefs = getActivity().getSharedPreferences("prefs_tag",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("login", false).apply();

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), WelcomePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ((MainActivity) getActivity()).startActivity(intent);
            }
        });

        //Under Construction

        ImageButton setting=v.findViewById(R.id.ib_setting);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMessage();
            }
        });

        ImageButton support=v.findViewById(R.id.ib_support);

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMessage();
            }
        });

        ImageButton term=v.findViewById(R.id.ib_term);

        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMessage();
            }
        });

        return v;
    }


    public void popupMessage(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Will be available soon!");
        alertDialogBuilder.setIcon(R.drawable.construction_icon);
        alertDialogBuilder.setTitle("Under Construction");
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

