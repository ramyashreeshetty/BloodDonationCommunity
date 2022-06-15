package com.teamrocket.blooddonationcommunity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PostInfoFragment extends Fragment {

    GridView gridView;
    PostInfoAdapter postInfoAdapter;
    ArrayList<PostInfoData> list3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post_info, container, false);

        String uid= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        gridView = v.findViewById(R.id.gridViewPost);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Posts").child("Receiver");

        list3 = new ArrayList<>();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String key=dataSnapshot.child("uid").getValue(String.class);
                    if(key.equals(uid)){
                        PostInfoData postInfoData= dataSnapshot.getValue(PostInfoData.class);
                        list3.add(postInfoData);
                    }

                }


                postInfoAdapter= new PostInfoAdapter(getActivity(),list3);
                gridView.setAdapter(postInfoAdapter);
                postInfoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return v;
    }
}