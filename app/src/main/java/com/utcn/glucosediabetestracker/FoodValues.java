package com.utcn.glucosediabetestracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;


public class FoodValues extends AppCompatActivity {
    public static final String TAG = "TAG";

    TextView saveButton, glucose_value_before,glucose_value_after,food_value, xButton;

    //add values to database
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String userID;
   View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_values);
        saveButton = findViewById(R.id.save_button);
        glucose_value_before=findViewById(R.id.glucose_value_before);
        glucose_value_after=findViewById(R.id.glucose_value_after);
        food_value=findViewById(R.id.food_values);



        //add values to firestore

        auth =FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                String value_glucose_before = glucose_value_before.getText().toString();
                String value_glucose_after = glucose_value_after.getText().toString();
                String value_food = food_value.getText().toString();
                userID = auth.getCurrentUser().getUid();

                CollectionReference documentReference = fStore.collection("users").document(userID).collection("meal_values");//.document("values");

                InsertFood insertfood = new InsertFood(value_glucose_before, value_glucose_after, value_food);


               Intent intent = new Intent(FoodValues.this, Menu.class);
                startActivity(intent);

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);




                documentReference.add(insertfood).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,"onSuccess: Value added" + userID );
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"onFailure:" + e.toString() );
                    }
                });



            }
        });

        //exit

        xButton = findViewById(R.id.x);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(FoodValues.this, Menu.class);
                startActivity(intent);

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });






    }
}