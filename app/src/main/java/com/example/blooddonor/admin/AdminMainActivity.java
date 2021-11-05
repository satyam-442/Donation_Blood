package com.example.blooddonor.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.blooddonor.DonorActivity;
import com.example.blooddonor.R;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
    }

    public void addCamp(View view) {
    }

    public void viewReceiverForAdmin(View view) {
    }

    public void viewDonorForAdmin(View view) {
        Intent main = new Intent(this, AdminDonorActivity.class);
        startActivity(main);
    }
}