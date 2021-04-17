package com.utcn.glucosediabetestracker;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class LogoStart extends AppCompatActivity {
    ImageView logo;
    TextView title;


    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_start);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(LogoStart.this, RegisterAndLogin.class);
                startActivity(intent);
                finish();


            }
        }, 5000);

        logo = findViewById(R.id.logo);
        title = findViewById(R.id.title);

        logo.animate().translationY(1400).setDuration(1000).setStartDelay(4000);
        title.animate().translationY(1400).setDuration(1000).setStartDelay(4000);

    }
}