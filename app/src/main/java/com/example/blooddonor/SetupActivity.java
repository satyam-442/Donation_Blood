package com.example.blooddonor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SetupActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button SaveInfoBtn;
    EditText setupName, setupEmail, setupPhone, setupGroup, setupDob, setupGender, setupCity, setupAddress, setupPassword;
    TextView selectDOBTap;
    FirebaseAuth mAuth;
    DatabaseReference UserRef,GroupRef;
    String currentUserID;
    ProgressDialog loadingBar;
    RadioGroup genderGroup;
    RadioButton radioMale, radioFemale;
    int dayVar, monthVar, yearVar;
    Spinner spinner;
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        //SPINNER USE FOR BLOOD GROUP
        spinner = findViewById(R.id.bloods_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bloods_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        setupName = findViewById(R.id.setupName);
        setupEmail = findViewById(R.id.setupEmail);
        setupPhone = findViewById(R.id.setupPhone);
        setupGroup = findViewById(R.id.setupBlood);
        setupDob = findViewById(R.id.setupDOB);
        setupGender = findViewById(R.id.setupGender);
        setupCity = findViewById(R.id.setupCity);
        setupAddress = findViewById(R.id.setupAddress);
        setupPassword = findViewById(R.id.setupPassword);
        SaveInfoBtn = (Button) findViewById(R.id.setupInfoBtn);
        SaveInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountInfo();
            }
        });

        selectDOBTap = findViewById(R.id.tvDOB);
        selectDOBTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(SetupActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                setupDob.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Donors").child(currentUserID);
        GroupRef = FirebaseDatabase.getInstance().getReference().child("BloodGroups");

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
                        HashMap<String, Object> donorMapNew = new HashMap<String, Object>();
                        donorMapNew.put("Name",name);
                        donorMapNew.put("Email",email);
                        donorMapNew.put("Phone",phone);
                        donorMapNew.put("Group",group);
                        donorMapNew.put("DOB",dob);
                        donorMapNew.put("Gender",gender);
                        donorMapNew.put("City",city);
                        donorMapNew.put("Address",addresss);
                        donorMapNew.put("Password",password);
                        donorMapNew.put("image","default");
                        donorMapNew.put("fuid",currentUserID);
                        GroupRef.child(group).child(currentUserID).updateChildren(donorMapNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                SendUserToMainActivity();
                                Toast.makeText(SetupActivity.this, "Your Account Created Successfully...", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                        });
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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioMale:
                if (checked)
                    setupGender.setText("Male");
                break;
            case R.id.radioFemale:
                if (checked)
                    setupGender.setText("Female");
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String itemSpinner = parent.getItemAtPosition(position).toString();
        setupGroup.setText(itemSpinner);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
