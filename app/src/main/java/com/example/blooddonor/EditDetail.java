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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditDetail extends AppCompatActivity
{

    Button UpdateInfoBtn;
    TextView updateName, updateEmail, updatePhone, updateGroup, updateDob, updateGender, updateCity, updateAddress;
    FirebaseAuth mAuth;
    DatabaseReference UserRef;
    String currentUserID;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail);


        mAuth=FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Donors").child(currentUserID);
        UserRef.keepSynced(true);


        updateName = (TextView) findViewById(R.id.updateName);
        updateEmail = (TextView) findViewById(R.id.updateEmail);
        updatePhone = (TextView) findViewById(R.id.updatePhone);
        updateGroup = (TextView) findViewById(R.id.updateBlood);
        updateDob = (TextView) findViewById(R.id.updateDOB);
        updateGender = (TextView) findViewById(R.id.updateGender);
        updateCity = (TextView) findViewById(R.id.updateCity);
        updateAddress = (TextView) findViewById(R.id.updateAddress);

        UpdateInfoBtn = (Button) findViewById(R.id.setupInfoBtn);
        UpdateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAccountInfo();
            }
        });

        loadingBar = new ProgressDialog(this);

        UserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String name = dataSnapshot.child("Name").getValue().toString();
                    String email = dataSnapshot.child("Email").getValue().toString();
                    String phone = dataSnapshot.child("Phone").getValue().toString();
                    String gender = dataSnapshot.child("Gender").getValue().toString();
                    String blood = dataSnapshot.child("Group").getValue().toString();
                    String dob = dataSnapshot.child("DOB").getValue().toString();
                    String city = dataSnapshot.child("City").getValue().toString();
                    String addresss = dataSnapshot.child("Address").getValue().toString();

                    updateName.setText(name);
                    updateEmail.setText(email);
                    updatePhone.setText(phone);
                    updateGender.setText(gender);
                    updateGroup.setText(blood);
                    updateDob.setText(dob);
                    updateCity.setText(city);
                    updateAddress.setText(addresss);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void UpdateAccountInfo() {
        final String name = updateName.getText().toString();
        final String email = updateEmail.getText().toString();
        final String phone = updatePhone.getText().toString();
        final String group = updateGroup.getText().toString();
        final String dob = updateDob.getText().toString();
        final String gender = updateGender.getText().toString();
        final String city = updateCity.getText().toString();
        final String addresss = updateAddress.getText().toString();

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
            donorMap.put("image","default");
            donorMap.put("fuid",currentUserID);
            UserRef.updateChildren(donorMap).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        SendToProfile();
                        Toast.makeText(EditDetail.this, "Data Updated", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(EditDetail.this, "Error Occurred:" + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void SendToProfile() {
        Intent profile = new Intent(this, ProfileActivity.class);
        startActivity(profile);
        finish();
    }
}
