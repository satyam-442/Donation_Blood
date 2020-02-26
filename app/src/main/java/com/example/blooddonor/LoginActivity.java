package com.example.blooddonor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    Button loginBtn, regisBtnLink;
    RelativeLayout relay1,relay2;
    TextView slogan;
    EditText logEmail, logPass;
    ProgressDialog loadingBar;
    Handler handler= new Handler();
    Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            slogan.setVisibility(View.VISIBLE);
            relay1.setVisibility(View.VISIBLE);
            relay2.setVisibility(View.VISIBLE);
        }
    };

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logEmail = (EditText) findViewById(R.id.logEmail);
        logPass = (EditText) findViewById(R.id.logPass);

        loadingBar = new ProgressDialog(this);

        relay1 = (RelativeLayout) findViewById(R.id.rellay1);
        relay2 = (RelativeLayout) findViewById(R.id.rellay2);
        slogan = (TextView) findViewById(R.id.slogan);

        handler.postDelayed(runnable, 3000);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        regisBtnLink = (Button) findViewById(R.id.regisBtnLink);
        regisBtnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendToRegister();
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            SendToMain();
        }
    }

    private void AllowUserToLogin() {
        final String email = logEmail.getText().toString();
        final String password = logPass.getText().toString();
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "E-mail is empty...", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this, "E-mail address is badly formatted...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Password is empty...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("please wait...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        SendToMain();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(LoginActivity.this, "Error Occurred ;" + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void SendToRegister() {
        Intent mainReg = new Intent(this,RegisterActivity.class);
        mainReg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainReg);
        finish();
    }

    private void SendToMain() {
        Intent main = new Intent(this,MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);startActivity(main);
        finish();
    }
}
