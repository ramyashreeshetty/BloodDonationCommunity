package com.ngo.blooddonationcommunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    //bottom nav
    BottomNavigationView bottomNavigation;
    FloatingActionButton fabNavPost;

    //top nav
    MaterialToolbar topAppBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fab Post
        fabNavPost=findViewById(R.id.fabnavblood);
        fabNavPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new PostFragment()).commit();
            }
        });

        //bottom nav
        bottomNavigation = findViewById(R.id.bottomNavView);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new HomeFragment()).commit();
        bottomNavigation.setSelectedItemId(R.id.navhome);

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment=null;
                switch (item.getItemId()) {
                    case R.id.navhome:
                        fragment=new HomeFragment();
                        break;
                    case R.id.navdonor:
                        fragment=new DonorFragment();
                        break;
                    case R.id.navreceiver:
                        fragment=new ReceiverFragment();
                        break;
                    case R.id.navprofile:
                        fragment=new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container,fragment).commit();
                return true;
            }
        });

        //top Nav Bar
        topAppBar=findViewById(R.id.topAppBar);
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()== R.id.notifBtn){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new NotifFragment()).commit();
                }
                return true;
            }
        });

    }
}