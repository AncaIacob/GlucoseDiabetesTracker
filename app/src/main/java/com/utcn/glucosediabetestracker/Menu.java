package com.utcn.glucosediabetestracker;

import androidx.annotation.NonNull;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Menu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


       BottomNavigationView bottomNavigationView = findViewById(R.id.menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_contanier,new Glucose()).commit();



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragmemnt = null;

                    switch (menuItem.getItemId())
                    {
                        case R.id.glucose:
                            selectedFragmemnt = new Glucose();
                            break;
                        case R.id.insights:
                            selectedFragmemnt= new Insights();
                            break;
                       case R.id.chart:
                            selectedFragmemnt = new Chart();
                            break;
                        case R.id.meal:
                            selectedFragmemnt = new Meal();
                            break;
                        case R.id.me:
                            selectedFragmemnt = new Me();

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contanier,selectedFragmemnt).commit();
                    return true;
                }
            };


}