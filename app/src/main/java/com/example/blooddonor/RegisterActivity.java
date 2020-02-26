package com.example.blooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity
{

    Button loginBtnLink, loginBtn;

    private EditText UserEmail, UserPassword, UserConfirmPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference studReference;

    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        UserEmail = (EditText) findViewById(R.id.regisEmail);
        UserPassword = (EditText) findViewById(R.id.regisPass);
        UserConfirmPassword = (EditText) findViewById(R.id.regisConPass);

        loadingBar = new ProgressDialog(this);

        loginBtn = (Button) findViewById(R.id.regisBtn);
        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CreateNewAccount();
            }
        });

        loginBtnLink = (Button) findViewById(R.id.loginBtnLink);
        loginBtnLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendToLogin();
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser studcurrentUser = mAuth.getCurrentUser();
        if(studcurrentUser != null)
        {
            SendUserToMainActivity();
        }
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent=new Intent(this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }


    private void CreateNewAccount() {
        final String email = UserEmail.getText().toString();
        final String password = UserPassword.getText().toString();
        final String confirmpassword = UserConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "E-mail is Empty!!!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Password is Empty!!!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmpassword))
        {
            Toast.makeText(this, "Confirm Password field is Empty!!!", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmpassword))
        {
            Toast.makeText(this, "Password Mismatched!!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("please wait...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        SendToSetupActivity();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(RegisterActivity.this, "Error Occurred ;" + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void SendToSetupActivity()
    {
        Intent setupIntent = new Intent(this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }

    private void SendToLogin() {
        Intent login = new Intent(this,LoginActivity.class);
        startActivity(login);
        finish();
    }
}
