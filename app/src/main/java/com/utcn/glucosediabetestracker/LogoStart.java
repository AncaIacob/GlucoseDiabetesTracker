package com.utcn.glucosediabetestracker;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

public class LogoStart extends AppCompatActivity {
    ImageView logo;
    TextView title;
    Timer timer;

    FirebaseFirestore fStore;
    FirebaseAuth auth;
    String userID;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_start);


       timer = new Timer();


        logo = findViewById(R.id.logo);
        title = findViewById(R.id.title);



    }
    @Override
    protected void onStart() {
        super.onStart();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Intent intent = new Intent(LogoStart.this, Menu.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(LogoStart.this, RegisterAndLogin.class);
                    startActivity(intent);
                    finish();
                }
            }
        },4000);


        logo.animate().translationY(1400).setDuration(1000).setStartDelay(3000);
        title.animate().translationY(1400).setDuration(1000).setStartDelay(3000);
    }


}