package com.teamrocket.blooddonationcommunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyOtp extends AppCompatActivity {

    TextView phone_num;
    ExtendedFloatingActionButton btnVerify;
    ExtendedFloatingActionButton resendOTPTV;
    FloatingActionButton back;

    EditText otpEditText;

    public String phonenumber_value;
    private FirebaseAuth mAuth;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendOTPtoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        //Back Button
        back=findViewById(R.id.backOtpBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Getting Phone Number
        phone_num = findViewById(R.id.pho_number);
        otpEditText = findViewById(R.id.enterOtp);

        phonenumber_value = getIntent().getStringExtra("mobile");
        phone_num.setText(phonenumber_value);

        mAuth = FirebaseAuth.getInstance();
        phonenumber_value = "+91" + phonenumber_value;

        btnVerify = findViewById(R.id.vernextbtn);
        resendOTPTV = findViewById(R.id.resendOtp);
        sendVerificationCode(phonenumber_value);

        //Displaying keyboard
        otpEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        //Verifying The OTP --> Toast
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = otpEditText.getText().toString();

                if (code.isEmpty()){
                    Toast.makeText(VerifyOtp.this, "Please Enter the OTP", Toast.LENGTH_SHORT).show();
                }
                else if (code.length()<6) {
                    Toast.makeText(VerifyOtp.this, "Please Enter the correct OTP", Toast.LENGTH_SHORT).show();
                }
                else {
                    verifyCode(code);
                }
            }
        });

        //Resend OTP -->
        resendOTPTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(phonenumber_value, resendOTPtoken);
            }
        });

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBack)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(VerifyOtp.this, SetPassword.class);
                            intent.putExtra("mobile",phonenumber_value);
                            startActivity(intent);
                            Toast.makeText(VerifyOtp.this, "User verified", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(VerifyOtp.this, "Fail to verify the user..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBack)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            resendOTPtoken = forceResendingToken;

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.e("TAG", "ERROR IS " + e.getMessage());
            Toast.makeText(VerifyOtp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}