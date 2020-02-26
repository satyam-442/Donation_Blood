package com.example.blooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity
{

    FirebaseAuth mAuth;
    DatabaseReference DonorRef;
    TextView Name, Email, Phone, DOB, BloodGrp, Gender, City, Address;
    Button editDetails;


    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Name = (TextView) findViewById(R.id.profileNameTv);
        Email = (TextView) findViewById(R.id.profileEmailTv);
        Phone = (TextView) findViewById(R.id.profilePhoneTv);
        DOB = (TextView) findViewById(R.id.profileDobTv);
        BloodGrp = (TextView) findViewById(R.id.profileBloodTv);
        Gender = (TextView) findViewById(R.id.profileGenderTv);
        City = (TextView) findViewById(R.id.profileCityTv);
        Address = (TextView) findViewById(R.id.profileAddressTv);
        editDetails = (Button) findViewById(R.id.editInfoBtn);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        DonorRef = FirebaseDatabase.getInstance().getReference().child("Donors").child(currentUserId);

        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendToEditPage();
            }
        });

        DonorRef.addValueEventListener(new ValueEventListener()
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
                    String address = dataSnapshot.child("Address").getValue().toString();

                    Name.setText(name);
                    Email.setText(email);
                    Phone.setText(phone);
                    Gender.setText(gender);
                    BloodGrp.setText(blood);
                    DOB.setText(dob);
                    City.setText(city);
                    Address.setText(address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendToEditPage()
    {
        Intent edit = new Intent(this, EditDetail.class);
        startActivity(edit);
    }
}
