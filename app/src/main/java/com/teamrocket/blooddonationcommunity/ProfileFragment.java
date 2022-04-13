package com.teamrocket.blooddonationcommunity;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.appbar.MaterialToolbar;

public class ProfileFragment extends Fragment  {
    MaterialToolbar topAppBar;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        setting current fragment view
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

//        selecting image button
        ImageButton ImgBtn = (ImageButton) v.findViewById(R.id.ib_profile);

//        on click will load profile setting fragment on main_container
        ImgBtn.setOnClickListener(view -> {
            FragmentTransaction fr = getActivity().getSupportFragmentManager().beginTransaction();
            fr.replace(R.id.main_container,new ProfileSettingFragment());
            fr.commit();
//                topAppBar.setTitle("Profile Settings");
        });
        return v;
    }
}

