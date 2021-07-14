package com.utcn.glucosediabetestracker;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import maes.tech.intentanim.CustomIntent;


public class RegisterAndLogin extends AppCompatActivity {

     Button button_login_start, button_register_start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_and_login);

        button_login_start=findViewById(R.id.button_login_start);
        button_register_start=findViewById(R.id.button_register_start);

        button_login_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(RegisterAndLogin.this, Login.class);
                startActivity(intent1);

                CustomIntent.customType(RegisterAndLogin.this,"left-to-right");
            }
        });

        button_register_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(RegisterAndLogin.this, Register.class);
                startActivity(intent2);

                CustomIntent.customType(RegisterAndLogin.this,"right-to-left");
            }
        });






    }
}