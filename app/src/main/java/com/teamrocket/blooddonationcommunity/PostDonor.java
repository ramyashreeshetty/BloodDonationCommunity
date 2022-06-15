package com.teamrocket.blooddonationcommunity;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PostDonor extends AppCompatActivity implements LocationListener {

    //For Dropdown--> Blood group
    TextInputLayout tilDon;
    AutoCompleteTextView actDon;

    //For Location -->
    ImageView getCurLocBtn;
    EditText locationDis;
    LocationManager locationManager;
    public String locCity;
    public String locState;
    public String locCountry;
    public String FullAddress;
    public int checkAdd=0;

    public double locLongitude;
    public double locLatitude;

    //Details
    public String getBloodType;
    public String getMsg;
    public String getName;
    public String getPhone;
    public String getLongitude;
    public String getLatitude;

    ExtendedFloatingActionButton postDonorBtn;
    EditText editMsgDon,editTimeDon;
    public String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_donor);

        //For Dropdown--> Blood group------------------------------------>
        tilDon = findViewById(R.id.bloodGroupDon);
        actDon = findViewById(R.id.autoCompleteTextViewDon);

        String[] items = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(PostDonor.this, R.layout.items_list, items);
        actDon.setAdapter(itemAdapter);


        //Grant Permission For the Location
        getCurLocBtn=(ImageView)findViewById(R.id.getCurrentLoc2);

        getCurLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAdd=1;
                Toast.makeText(PostDonor.this,"Getting Location",Toast.LENGTH_SHORT).show();
                grantPermission();
                locationEnabled();
                getLocation();
            }
        });

        //Fetch Details ---->

        postDonorBtn=findViewById(R.id.postDonorBtn);
        editMsgDon=findViewById(R.id.editMsgDon);
        locationDis = findViewById(R.id.locPostDon);

        //uid
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        int serialNum = 0;
        //Realtime DB
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://blood-donation-community-1000-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("Users");

        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getName = dataSnapshot.child("full_name").getValue(String.class);
                getPhone = dataSnapshot.child("phone_number").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postDonorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getBloodType=actDon.getText().toString().trim();
                getMsg=editMsgDon.getText().toString().trim();
                getLongitude=Double.toString(locLongitude);
                getLatitude=Double.toString(locLatitude);



                if (getBloodType.isEmpty() || getMsg.isEmpty()) {
                    Toast.makeText(PostDonor.this, "Fields can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(checkAdd==0){
                    Toast.makeText(PostDonor.this, "Please choose your Location", Toast.LENGTH_SHORT).show();
                    return;
                }



                //Pushing the Data onto the FireBase -->
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    // No session user
                    return;
                }



                HashMap<String, String> map = new HashMap<>();
                map.put("uid",uid);
                map.put("bloodGroup",getBloodType);
                map.put("city",FullAddress);
                map.put("extraMsg",getMsg);
                map.put("phoneNumber",getPhone);
                map.put("name",getName);
                map.put("latitude",getLatitude);
                map.put("longitude",getLongitude);


                //Realtime DB
                //FirebaseDatabase database = FirebaseDatabase.getInstance("https://blood-donation-community-1000-default-rtdb.asia-southeast1.firebasedatabase.app");
                DatabaseReference myRef2 = database.getReference("Posts");

                //Pushing User Details
                myRef2.child("Donor").child(uid).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PostDonor.this, "Post uploaded!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(PostDonor.this,MainActivity.class);
                        startActivity(i);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ErrorAdding",e.getMessage());
                                Toast.makeText(PostDonor.this, e.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

    //Function to get the Location -->
    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    //Function to check Location Enabled -->
    private void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(PostDonor.this)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    //Function to grant perm -->
    private void grantPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},100);

        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            //Fetching Longitude and Latitude

            locLongitude=location.getLongitude();
            locLatitude=location.getLatitude();


            locCity=addresses.get(0).getSubAdminArea();
            locState=addresses.get(0).getAdminArea();
            locCountry=addresses.get(0).getCountryName();

            FullAddress=locCity;
            locationDis.setText(FullAddress);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(PostDonor.this, "Error Fetching The Location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}