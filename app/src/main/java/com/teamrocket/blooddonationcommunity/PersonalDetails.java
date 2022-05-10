package com.teamrocket.blooddonationcommunity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalDetails extends AppCompatActivity implements LocationListener {

    //For Details
    public String uid;
    EditText firstname;
    public String firstName;
    EditText lastname;
    public String lastName;
    EditText email;
    public String emailId;
    String emailPattern = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    public String birthDate;
    public String bloodGroup;
    EditText aadharnum;
    public String aadharNum;

    //Intent details
    public String phoneNumber;
    //public String pinKey;

    //For Intent
    ExtendedFloatingActionButton saveBtn;

    //For DatePicker--> Birthdate
    ImageView btnDatePicker;
    EditText txtDate;
    private int mYear, mMonth, mDay;

    //For Dropdown--> Blood group
    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;

    //For Profile Image
    CircleImageView profileImage;
    ImageView addImage;
    ImagePicker imagePicker;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference reference;
    private  static final int PICK_IMAGE=1;
    int checkImage=0;

    //For Location -->
    ImageView getCurLocBtn;
    EditText locationDis;
    LocationManager locationManager;
    public String locCity;
    public String locState;
    public String locPinCode;
    public String locCountry;
    public String locArea;
    public String FullAddress;
    public int checkAdd=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        //For DatePicker--> Birthdate ------------------------------>
        btnDatePicker = (ImageView) findViewById(R.id.btn_date);
        txtDate = (EditText) findViewById(R.id.in_date);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(PersonalDetails.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //For Dropdown--> Blood group------------------------------------>
        textInputLayout = findViewById(R.id.bloodgroup);
        autoCompleteTextView = findViewById(R.id.bloodGroupAutoText);

        String[] items = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(PersonalDetails.this, R.layout.items_list, items);
        autoCompleteTextView.setAdapter(itemAdapter);


        //For Profile Image-------------------------------------------------->
        storage = FirebaseStorage.getInstance();
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        addImage = findViewById(R.id.addimage);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent gallery=new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery,"Select Picture"),PICK_IMAGE);
            }
        });

        //Grant Permission For the Location
        getCurLocBtn=(ImageView)findViewById(R.id.getCurrentLoc);

        getCurLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAdd=1;
                Toast.makeText(PersonalDetails.this,"Getting Location",Toast.LENGTH_SHORT).show();
                grantPermission();
                locationEnabled();
                getLocation();
            }
        });

        //details
        saveBtn=findViewById(R.id.detailnextbtn);
        firstname=findViewById(R.id.firstname);
        lastname=findViewById(R.id.lastname);
        email=findViewById(R.id.emailAdd);
        aadharnum=findViewById(R.id.aadharcard);
        locationDis=findViewById(R.id.residential);

        //uid
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName=firstname.getText().toString().trim();
                lastName=lastname.getText().toString().trim();
                emailId=email.getText().toString().trim();
                birthDate=txtDate.getText().toString().trim();
                bloodGroup=autoCompleteTextView.getText().toString().trim();
                aadharNum=aadharnum.getText().toString().trim();

                //intent extras
                phoneNumber = getIntent().getStringExtra("mobile");
                //pinKey=getIntent().getStringExtra("pin");

                //Verifying Name -->
                if (firstName.isEmpty() || lastName.isEmpty()) {
                    Toast.makeText(PersonalDetails.this, "Name can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Verifying Email Id -->
                if (emailId.isEmpty()) {
                    Toast.makeText(PersonalDetails.this, "Email-Address can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!emailId.matches(emailPattern)) {
                    Toast.makeText(PersonalDetails.this, "Enter valid an Email Id", Toast.LENGTH_SHORT).show();
                    return;
                }

                String fullName = firstName+" "+lastName;

                //Verifying BirthDate -->
                if (birthDate.isEmpty()) {
                    Toast.makeText(PersonalDetails.this, "BirthDate can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Verifying BloodGroup -->
                if (bloodGroup.isEmpty()) {
                    Toast.makeText(PersonalDetails.this, "BloodGroup can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Verifying BloodGroup -->
                if (aadharNum.isEmpty()) {
                    Toast.makeText(PersonalDetails.this, "AadharNumber can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(checkAdd==0){
                    Toast.makeText(PersonalDetails.this, "Location can not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(checkImage==0){
                    Toast.makeText(PersonalDetails.this, "Please select an Image", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Upload the image;
                upload();

                //Pushing the Data onto the FireBase -->
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    // No session user
                    return;
                }

                HashMap<String, String> map = new HashMap<>();
                map.put("uid",uid);
                map.put("phone_number",phoneNumber);
                //map.put("pin",pinKey);
                map.put("full_name",fullName);
                map.put("email_id",emailId);
                map.put("birth_date",birthDate);
                map.put("blood_group",bloodGroup);
                map.put("current_location",FullAddress);
                map.put("aadhar_number",aadharNum);

                //Realtime DB
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://blood-donation-community-1000-default-rtdb.asia-southeast1.firebasedatabase.app");
                DatabaseReference myRef = database.getReference("Users");

                //Pushing User Details
                myRef.child(uid).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        //Shared Preferences
                        SharedPreferences prefs = getSharedPreferences("prefs_tag",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("login", true).apply();

                        Toast.makeText(PersonalDetails.this, "Welcome!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(PersonalDetails.this,MainActivity.class);
                        startActivity(i);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ErrorAdding",e.getMessage());
                                Toast.makeText(PersonalDetails.this, e.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        });

            }

        });
    }

    //Function upload the image -->
    private void upload(){
         if (imageUri!= null){
             reference= storage.getReference().child("Users").child("images/"+uid);
         }


        assert imageUri != null;
        reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                 if(task.isSuccessful()){
                     Toast.makeText(PersonalDetails.this,"Image Upload Successful",Toast.LENGTH_SHORT).show();
                 }
                 else{
                     Toast.makeText(PersonalDetails.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                 }
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
            new AlertDialog.Builder(PersonalDetails.this)
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


    //Function to pick the Image -->
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            checkImage=1;
            assert data != null;
            imageUri=data.getData();
            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                profileImage.setImageBitmap(bitmap);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            locCity=addresses.get(0).getSubAdminArea();
            locState=addresses.get(0).getAdminArea();
            locCountry=addresses.get(0).getCountryName();

            FullAddress=locCity+","+locState+","+locCountry;
            locationDis.setText(FullAddress);

//            locPinCode=addresses.get(0).getPostalCode();
//            locArea = addresses.get(0).getAddressLine(0);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(PersonalDetails.this, "Error Fetching The Location", Toast.LENGTH_SHORT).show();
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