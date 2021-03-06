package com.teamrocket.blooddonationcommunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PostReceiver extends AppCompatActivity implements LocationListener {

    //For DatePicker--> Birthdate
    ImageView btnDatePicker;
    EditText txtDateRec;
    private int mYear, mMonth, mDay;

    //For Dropdown--> Blood group
    TextInputLayout tilRec;
    AutoCompleteTextView actRec;

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
    public String getBirthDate;
    public String getAvailTime;
    public String getMsg;
    public String getName;
    public String getPhone;
    public String getLongitude;
    public String getLatitude;

    ExtendedFloatingActionButton postReceiverBtn;
    EditText editMsgRec,editTimeRec;
    public String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_receiver);

        //For DatePicker--> Birthdate ------------------------------>
        btnDatePicker = (ImageView) findViewById(R.id.btn_date_rec);
        txtDateRec = (EditText) findViewById(R.id.dateRec);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(PostReceiver.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDateRec.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //For Dropdown--> Blood group------------------------------------>
        tilRec = findViewById(R.id.bloodGroupRec);
        actRec = findViewById(R.id.autoCompleteTextRec);

        String[] items = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(PostReceiver.this, R.layout.items_list, items);
        actRec.setAdapter(itemAdapter);


        //Grant Permission For the Location
        getCurLocBtn=(ImageView)findViewById(R.id.getCurrentLocRec);

        getCurLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAdd=1;
                Toast.makeText(PostReceiver.this,"Getting Location",Toast.LENGTH_SHORT).show();
                grantPermission();
                locationEnabled();
                getLocation();
            }
        });

        //Fetch Details ---->

        postReceiverBtn=findViewById(R.id.postReceiverBtn);
        editMsgRec=findViewById(R.id.editMsgRec);
        editTimeRec=findViewById(R.id.editTimeRec);
        locationDis = findViewById(R.id.locPostRec);

        //uid
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
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

        postReceiverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getBloodType=actRec.getText().toString().trim();
                getBirthDate=txtDateRec.getText().toString().trim();
                getAvailTime=editTimeRec.getText().toString().trim();
                getMsg=editMsgRec.getText().toString().trim();
                getLongitude=Double.toString(locLongitude);
                getLatitude=Double.toString(locLatitude);



                if (getBloodType.isEmpty() || getBirthDate.isEmpty() || getAvailTime.isEmpty() || getMsg.isEmpty()) {
                    Toast.makeText(PostReceiver.this, "Fields can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(checkAdd==0){
                    Toast.makeText(PostReceiver.this, "Please choose your Location", Toast.LENGTH_SHORT).show();
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
                map.put("date",getBirthDate);
                map.put("bloodGroup",getBloodType);
                map.put("city",FullAddress);
                map.put("time",getAvailTime);
                map.put("extraMsg",getMsg);
                map.put("phoneNumber",getPhone);
                map.put("name",getName);
                map.put("latitude",getLatitude);
                map.put("longitude",getLongitude);


                //Realtime DB
                //FirebaseDatabase database = FirebaseDatabase.getInstance("https://blood-donation-community-1000-default-rtdb.asia-southeast1.firebasedatabase.app");
                DatabaseReference myRef2 = database.getReference("Posts");

                //Pushing User Details
                myRef2.child("Receiver").push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(PostReceiver.this, "Post uploaded!", Toast.LENGTH_SHORT).show();

                        //Pushing data for notification -->
                        HashMap<String, String> mapNotif = new HashMap<>();
                        mapNotif.put("uid",uid);
                        mapNotif.put("bloodGroup",getBloodType);
                        mapNotif.put("city",FullAddress);
                        mapNotif.put("name",getName);


                        DatabaseReference myNotifRef = database.getReference("Notification");
                        //Pushing User Details
                        myNotifRef.child(getBloodType).setValue(mapNotif).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(PostReceiver.this, getBloodType + "updated", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Intent i = new Intent(PostReceiver.this,MainActivity.class);
                        startActivity(i);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ErrorAdding",e.getMessage());
                                Toast.makeText(PostReceiver.this, e.getMessage() , Toast.LENGTH_SHORT).show();
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
            new AlertDialog.Builder(PostReceiver.this)
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
            Toast.makeText(PostReceiver.this, "Error Fetching The Location", Toast.LENGTH_SHORT).show();
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