package com.example.blooddonor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BloodGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_group);
    }

    public void aPosBlood(View view) {
        Intent apos = new Intent(this,GroupWiseBlood.class);
        apos.putExtra("group","A+");
        startActivity(apos);
    }

    public void aNegBlood(View view) {
        Intent aneg = new Intent(this,GroupWiseBlood.class);
        aneg.putExtra("group","A-");
        startActivity(aneg);
    }


    public void bPosBlood(View view) {
        Intent bpos = new Intent(this,GroupWiseBlood.class);
        bpos.putExtra("group","B+");
        startActivity(bpos);
    }

    public void bNegBlood(View view) {
        Intent bneg = new Intent(this,GroupWiseBlood.class);
        bneg.putExtra("group","B-");
        startActivity(bneg);
    }

    public void abPosBlood(View view) {
        Intent abpos = new Intent(this,GroupWiseBlood.class);
        abpos.putExtra("group","AB+");
        startActivity(abpos);
    }

    public void abNegBlood(View view) {
        Intent abneg = new Intent(this,GroupWiseBlood.class);
        abneg.putExtra("group","AB-");
        startActivity(abneg);
    }

    public void oPosBlood(View view) {
        Intent opos = new Intent(this,GroupWiseBlood.class);
        opos.putExtra("group","O+");
        startActivity(opos);
    }

    public void oNegBlood(View view) {
        Intent oneg = new Intent(this,GroupWiseBlood.class);
        oneg.putExtra("group","O-");
        startActivity(oneg);
    }
}
