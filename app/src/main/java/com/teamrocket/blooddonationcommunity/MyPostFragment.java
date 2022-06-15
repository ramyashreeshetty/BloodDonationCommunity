package com.teamrocket.blooddonationcommunity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyPostFragment extends Fragment {


    RecyclerView recyclerView;
    ReceiverAdapter receiverAdapter;
    ArrayList<ReceiverData> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_post, container, false);

        recyclerView = v.findViewById(R.id.postRecyclerView);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Posts").child("Receiver");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();



        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    ReceiverData receiverData= dataSnapshot.getValue(ReceiverData.class);
                    list.add(receiverData);

                }

                receiverAdapter = new ReceiverAdapter(getActivity(),list);
                recyclerView.setAdapter(receiverAdapter);
                receiverAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return  v;
    }
}