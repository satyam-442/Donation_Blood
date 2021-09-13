package com.example.blooddonor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class EditDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    DatePickerDialog picker;
    Button UpdateInfoBtn;
    EditText updateName, updateEmail, updatePhone, updateGroup, updateDob, updateGender, updateCity, updateAddress;
    FirebaseAuth mAuth;
    TextView selectDOBTap;
    DatabaseReference UserRef;
    String currentUserID;
    ProgressDialog loadingBar;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail);

        //SPINNER USE FOR BLOOD GROUP
        spinner = findViewById(R.id.bloods_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bloods_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Donors").child(currentUserID);
        UserRef.keepSynced(true);

        updateName = findViewById(R.id.updateName);
        updateEmail = findViewById(R.id.updateEmail);
        updatePhone = findViewById(R.id.updatePhone);
        updateGroup = findViewById(R.id.updateBlood);
        updateDob = findViewById(R.id.updateDOB);
        updateGender = findViewById(R.id.updateGender);
        updateCity = findViewById(R.id.updateCity);
        updateAddress = findViewById(R.id.updateAddress);

        selectDOBTap = findViewById(R.id.selectDOBTap);

        UpdateInfoBtn = (Button) findViewById(R.id.setupInfoBtn);
        UpdateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAccountInfo();
            }
        });

        selectDOBTap = findViewById(R.id.selectDOBTap);
        selectDOBTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(EditDetail.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                updateDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        loadingBar = new ProgressDialog(this);

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
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

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        updateGroup.setText(item);
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.maleEdit:
                if (checked)
                    updateGender.setText("Male");
                break;
            case R.id.femaleEdit:
                if (checked)
                    updateGender.setText("Female");
                break;
        }
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

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name field is empty!!!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "E-mail field is empty!!!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "E-mail wrongly formatted", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Phone field is empty!!!", Toast.LENGTH_SHORT).show();
        } else if (phone.length() != 10) {
            Toast.makeText(this, "Phone number not valid", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(group)) {
            Toast.makeText(this, "Blood Group field is empty!!!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(dob)) {
            Toast.makeText(this, "DOB field is empty!!!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(gender)) {
            Toast.makeText(this, "Gender field is empty!!!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "City field is empty!!!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(addresss)) {
            Toast.makeText(this, "Address field is empty!!!", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("please wait...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);
            HashMap<String, Object> donorMap = new HashMap<String, Object>();
            donorMap.put("Name", name);
            donorMap.put("Email", email);
            donorMap.put("Phone", phone);
            donorMap.put("Group", group);
            donorMap.put("DOB", dob);
            donorMap.put("Gender", gender);
            donorMap.put("City", city);
            donorMap.put("Address", addresss);
            donorMap.put("image", "default");
            donorMap.put("fuid", currentUserID);
            UserRef.updateChildren(donorMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        SendToProfile();
                        Toast.makeText(EditDetail.this, "Data Updated", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    } else {
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
