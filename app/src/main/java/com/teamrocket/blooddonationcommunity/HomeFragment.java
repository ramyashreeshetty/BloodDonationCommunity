package com.teamrocket.blooddonationcommunity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
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

public class HomeFragment extends Fragment {

    MaterialCardView cardFeatured;
    MaterialCardView cardCamp;
    MaterialCardView cardBloodBanks;

    TextView cardFullName,cardBloodGroup,cardAge;
    CircleImageView cardProfileImg;

    FirebaseStorage storage;
    StorageReference reference;

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

        storage = FirebaseStorage.getInstance();
        reference = storage.getReference().child("Users/").child("images/").child(uid);
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.get().load(uri).into(cardProfileImg);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        cardFeatured=v.findViewById(R.id.cardFeatured);

        cardFeatured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Featured.class);
                getActivity().startActivity(intent);
            }
        });

        cardCamp=v.findViewById(R.id.cardCamp);

        cardCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DonationCamp.class);
                getActivity().startActivity(intent);
            }
        });


        cardBloodBanks=v.findViewById(R.id.cardBloodBanks);

        cardBloodBanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BloodBank.class);
                getActivity().startActivity(intent);
            }
        });

        return v;
    }
}