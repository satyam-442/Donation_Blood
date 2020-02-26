package com.example.blooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorsProfile extends AppCompatActivity
{

    private TextView donorName,  donorGroup, donorDob, donorGender, donorPhone, donorEmail, donorAddress;
    private CircleImageView donorProfile;
    private Button requestByMail, saveDonor, requestByMessage, callDonor;
    private DatabaseReference donorsProfileRef, StudFollowingRef;
    private FirebaseAuth mAuth;
    String currentUserId, senderUserId, recieverUserId, CURRENT_STATE, saveCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors_profile);

        mAuth = FirebaseAuth.getInstance();

        currentUserId = getIntent().getExtras().get("visit_user_id").toString();
        senderUserId = mAuth.getCurrentUser().getUid();
        donorsProfileRef = FirebaseDatabase.getInstance().getReference().child("Donors").child(currentUserId);

        donorName = (TextView) findViewById(R.id.donorProfile_username);
        donorGroup = (TextView) findViewById(R.id.donorProfile_group);
        donorDob = (TextView) findViewById(R.id.donorProfile_dob);
        donorGender = (TextView) findViewById(R.id.donorProfile_gender);
        donorPhone = (TextView) findViewById(R.id.donorProfile_contact);
        donorEmail = (TextView) findViewById(R.id.donorProfile_email);
        donorAddress = (TextView) findViewById(R.id.donorProfile_address);
        donorProfile = (CircleImageView) findViewById(R.id.donorProfile_image);

        requestByMail = (Button) findViewById(R.id.requestByMail);
        saveDonor = (Button) findViewById(R.id.saveDonor);
        callDonor = (Button) findViewById(R.id.callDonor);
        requestByMessage = (Button) findViewById(R.id.messageDonor);

        donorsProfileRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String name = dataSnapshot.child("Name").getValue().toString();
                    String group = dataSnapshot.child("Group").getValue().toString();
                    String dob = dataSnapshot.child("DOB").getValue().toString();
                    String gender = dataSnapshot.child("Gender").getValue().toString();
                    String phone = dataSnapshot.child("Phone").getValue().toString();
                    String email = dataSnapshot.child("Email").getValue().toString();
                    String address = dataSnapshot.child("Address").getValue().toString();

                    donorName.setText(name);
                    donorGroup.setText(group);
                    donorDob.setText(dob);
                    donorGender.setText(gender);
                    donorPhone.setText(phone);
                    donorEmail.setText(email);
                    donorAddress.setText(address);

                    MaintainanceOfButton();

                    final String image = dataSnapshot.child("image").getValue().toString();
                    if(!image.equals("default"))
                    {
                        Picasso.with(DonorsProfile.this).load(image).placeholder(R.drawable.default_avatar).into(donorProfile);
                        Picasso.with(DonorsProfile.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(donorProfile, new Callback()
                        {
                            @Override
                            public void onSuccess()
                            { }

                            @Override
                            public void onError()
                            {
                                Picasso.with(DonorsProfile.this).load(image).placeholder(R.drawable.default_avatar).into(donorProfile);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            { }
        });
    }

    private void MaintainanceOfButton()
    {
        requestByMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendMailToDonor();
            }
        });

        callDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallToDonor();
            }
        });


        requestByMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessageToDonor();
            }
        });
    }

    private void SendMessageToDonor() {
        donorsProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String message = dataSnapshot.child("Phone").getValue().toString();
                    Uri mess = Uri.parse("sms:" + message);
                    Intent contactIntent = new Intent(Intent.ACTION_VIEW,mess);
                    contactIntent.putExtra(Intent.EXTRA_TEXT,"Dear Sir/Madam\n" +
                            "By donating the blood you will be hero someone's eye.\n " +
                            "Your small help can help and save someone's life which \n" +
                            "will be good deed for you. If interested to donate\n" +
                            "Please contact the following person\n" +
                            "Contact Details\n" +
                            "Your Email Here\n" +
                            "Your Phone Number here\n");
                    startActivity(Intent.createChooser(contactIntent,"Choose SMS Client"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            { }
        });
    }

    private void CallToDonor() {
        donorsProfileRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String phone = dataSnapshot.child("Phone").getValue().toString();
                    Uri call = Uri.parse("tel:" + phone);
                    Intent contactIntent = new Intent(Intent.ACTION_DIAL,call);
                    //contactIntent.setType("");
                    startActivity(Intent.createChooser(contactIntent,"Choose Calling Client"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            { }
        });
    }

    private void SendMailToDonor()
    {
        donorsProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String email = dataSnapshot.child("Email").getValue().toString();
                    Uri uri = Uri.parse(email);
                    Intent intent = new Intent(Intent.ACTION_SEND, uri);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Request for the Blood.");
                    intent.putExtra(Intent.EXTRA_TEXT,"Dear Sir/Madam\n" +
                            "\n" +
                            "By donating the blood you will be hero someone's eye. Your small help\n" +
                            "can help and save someone's life which will be very deed for you. If interested to donate\n" +
                            "please contact the following person\n" +
                            "\n" +
                            "If you are intrested please .contact as soon as possible\n" +
                            "Contact Details\n" +
                            "Your Email Here\n" +
                            "Your Phone Number here\n" +
                            "\n");
                    startActivity(Intent.createChooser(intent,"Choose Mailing Client"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            { }
        });
    }
}
