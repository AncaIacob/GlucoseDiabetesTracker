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

public class Values extends AppCompatActivity  {

    public static final String TAG = "TAG";
    private DatePickerDialog datePickerDialog;
    private Button dateButton, timeButton;
    TextView saveButton, glucose_value, xButton, typeText;
    int hour, min;
    //types
    private Spinner spinner;
    //add values to database
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    String userID;
    AlertDialog.Builder alert;


    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_values);
        initDatePicker();
        dateButton = findViewById(R.id.button_date);
        dateButton.setText(getTodaysDate());
        timeButton = findViewById(R.id.button_time);
        timeButton.setText(getTimesDate());
        saveButton = findViewById(R.id.save_button);
        glucose_value=findViewById(R.id.glucose_value);



        //types
        Types.initTypes();
        spinner = (Spinner)findViewById(R.id.spinner_type);
        SpinnerAdapterTypes customAdapter = new SpinnerAdapterTypes(this,R.layout.types_spinner,Types.getTypesArrayList());
        spinner.setAdapter(customAdapter);





        //add values to firestore

        auth =FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                String value_glucose = glucose_value.getText().toString();
                String value_date =  dateButton.getText().toString();
                String value_time = timeButton.getText().toString();
                int value_type_inter = spinner.getSelectedItemPosition();
                String value_type =getTypeFormat(value_type_inter);
                userID = auth.getCurrentUser().getUid();

                CollectionReference documentReference = fStore.collection("users").document(userID).collection("glucose_values");//.document("values");
              //Map<String,Object> glc_value = new HashMap<>();
               // glc_value.put("glucoseValue", value_glucose);
               // glc_value.put("dateValue", value_date);
                //glc_value.put("timeValue", value_time);
               // glc_value.put("typeValue", value_type);

                Insert insert = new Insert(value_glucose, value_date, value_time, value_type);


                Intent intent = new Intent(Values.this, Menu.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);



                documentReference.add(insert).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
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


                Intent intent = new Intent(Values.this, Menu.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });




    }

    //date

    private String getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        month = month +1;
        return makeDateString(dayOfMonth, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth ) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                dateButton.setText(date);

            }
        };

        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());



    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " + String.format("%02d",dayOfMonth) + " " + year;

    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";
        return "JAN";


    }

    public void openDatePicker(View view) {
        datePickerDialog.show();

    }

    //time

    public void openTimePicker(View view) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = makeTimeString(hourOfDay, minute);

                timeButton.setText(time);


            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;


         TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, min, true);
      //  timePickerDialog.getMinute().setMaxTime(System.currentTimeMillis());

         timePickerDialog.setTitle("Select Time");
         timePickerDialog.show();

    }

    private String makeTimeString(int hourOfDay, int minute) {
        return String.format("%02d:%02d",hourOfDay, minute);

        //return hourOfDay + ":" + minute;
    }
    private String getTimesDate() {
        Calendar time = Calendar.getInstance();
        int hourOfDay = time.get(Calendar.HOUR_OF_DAY);
        int minute = time.get(Calendar.MINUTE);

        return makeTimeString(hourOfDay, minute);
    }


 //value type format
    private String getTypeFormat(int value_type_intermediar) {
        if(value_type_intermediar== 0)
            return "Out of Bed";
        if(value_type_intermediar== 1)
            return "Before Bed";
        if(value_type_intermediar == 2)
            return "Before Breakfast";
        if(value_type_intermediar== 3)
            return "After Breakfast";
        if(value_type_intermediar== 4)
            return "Before Lunch";
        if(value_type_intermediar== 5)
            return "After Lunch";
        if(value_type_intermediar == 6)
            return "Before dinner";
        if(value_type_intermediar== 7)
            return "After dinner";
        if(value_type_intermediar == 8)
            return "Before tratament";
        if(value_type_intermediar== 9)
            return "After tratament";
        else
        return "Out of Bed";


    }




}