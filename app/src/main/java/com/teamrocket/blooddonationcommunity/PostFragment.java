package com.teamrocket.blooddonationcommunity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class PostFragment extends Fragment {

    //Button --> Post & Donor
    ExtendedFloatingActionButton donorFab,recFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
        donorFab = (ExtendedFloatingActionButton) rootView.findViewById(R.id.postDonorFab);
        recFab=(ExtendedFloatingActionButton) rootView.findViewById(R.id.postRecFab);
        recFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostReceiver.class);
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