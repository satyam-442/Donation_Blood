package com.example.blooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    String currentUserId;
    private TextView navProfileName, navProfileEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navView = navigationView.inflateHeaderView(R.layout.nav_header);
        navProfileName = (TextView) navView.findViewById(R.id.navProfileName);
        navProfileEmail = (TextView) navView.findViewById(R.id.navProfileEmail);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open,R.string.drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        /*if(savedInstanceState == null)
        {
            SendToDonor();
            navigationView.setCheckedItem(R.id.donors);
        }*/

        //FIREBASE CONNECTIVITY
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Donors");

        UserRef.child(currentUserId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("Name"))
                    {
                        String fullname = dataSnapshot.child("Name").getValue().toString();
                        navProfileName.setText(fullname);
                    }
                        if(dataSnapshot.hasChild("Email"))
                    {
                        String email = dataSnapshot.child("Email").getValue().toString();
                        navProfileEmail.setText(email);
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Profile do not exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            SendUserToLoginActivity();
        }
        else
        {
            CheckUserExistance();
        }
    }


    private void CheckUserExistance()
    {
        final String currentUserId = mAuth.getCurrentUser().getUid();
        UserRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.hasChild(currentUserId))
                {
                    SendUserToSetupActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }


    private void SendUserToSetupActivity() {
        Intent setupIntent = new Intent(this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.donors:
                SendToDonor();
                break;
            case R.id.saved_donors:
                SendToSavedDonor();
                break;
            case R.id.blood_group:
                SendToBloodGroup();
                break;
            case R.id.profile:
                SendToProfile();
                break;
            case R.id.logout:
                mAuth.signOut();
                SendToLogout();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void SendToLogout() {
        Intent logout = new Intent(this, LoginActivity.class);
        startActivity(logout);
        finish();
    }

    private void SendToProfile() {
        Intent profile = new Intent(this, ProfileActivity.class);
        startActivity(profile);
    }

    private void SendToBloodGroup() {
        Intent bloodgroup = new Intent(this, BloodGroup.class);
        startActivity(bloodgroup);
    }

    private void SendToSavedDonor() {
        Intent saveddonor = new Intent(this, SavedDonor.class);
        startActivity(saveddonor);
    }

    private void SendToDonor() {
        Intent donors = new Intent(this, DonorActivity.class);
        startActivity(donors);
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }
}
