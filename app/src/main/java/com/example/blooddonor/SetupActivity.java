package com.example.blooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SetupActivity extends AppCompatActivity
{
    Button SaveInfoBtn;
    TextView setupName, setupEmail, setupPhone, setupGroup, setupDob, setupGender, setupCity, setupAddress, setupPassword;
    FirebaseAuth mAuth;
    DatabaseReference UserRef;
    String currentUserID;
    ProgressDialog loadingBar;
    RadioGroup genderGroup;
    RadioButton radioMale, radioFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        setupName = (TextView) findViewById(R.id.setupName);
        setupEmail = (TextView) findViewById(R.id.setupEmail);
        setupPhone = (TextView) findViewById(R.id.setupPhone);
        setupGroup = (TextView) findViewById(R.id.setupBlood);
        setupDob = (TextView) findViewById(R.id.setupDOB);
        setupGender = (TextView) findViewById(R.id.setupGender);
        setupCity = (TextView) findViewById(R.id.setupCity);
        setupAddress = (TextView) findViewById(R.id.setupAddress);
        setupPassword = (TextView) findViewById(R.id.setupPassword);
        SaveInfoBtn = (Button) findViewById(R.id.setupInfoBtn);
        SaveInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountInfo();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Donors").child(currentUserID);

        loadingBar = new ProgressDialog(this);
    }

    private void SaveAccountInfo() {
        final String name = setupName.getText().toString();
        final String email = setupEmail.getText().toString();
        final String phone = setupPhone.getText().toString();
        final String group = setupGroup.getText().toString();
        final String dob = setupDob.getText().toString();
        final String gender = setupGender.getText().toString();
        final String city = setupCity.getText().toString();
        final String addresss = setupAddress.getText().toString();
        final String password = setupPassword.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Name field is empty!!!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "E-mail field is empty!!!", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this, "E-mail wrongly formatted", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Phone field is empty!!!", Toast.LENGTH_SHORT).show();
        }
        else if(phone.length()!=10)
        {
            Toast.makeText(this, "Phone number not valid", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(group))
        {
            Toast.makeText(this, "Blood Group field is empty!!!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(dob))
        {
            Toast.makeText(this, "DOB field is empty!!!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(gender))
        {
            Toast.makeText(this, "Gender field is empty!!!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(city))
        {
            Toast.makeText(this, "City field is empty!!!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addresss))
        {
            Toast.makeText(this, "Address field is empty!!!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Password field is empty!!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("please wait...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);
            HashMap<String, Object> donorMap = new HashMap<String, Object>();
            donorMap.put("Name",name);
            donorMap.put("Email",email);
            donorMap.put("Phone",phone);
            donorMap.put("Group",group);
            donorMap.put("DOB",dob);
            donorMap.put("Gender",gender);
            donorMap.put("City",city);
            donorMap.put("Address",addresss);
            donorMap.put("Password",password);
            donorMap.put("image","default");
            donorMap.put("fuid",currentUserID);
            UserRef.updateChildren(donorMap).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        SendUserToMainActivity();
                        Toast.makeText(SetupActivity.this, "Your Account Created Successfully...", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error Occurred:" + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
