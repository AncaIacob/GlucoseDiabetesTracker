package com.utcn.glucosediabetestracker;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.facebook.CallbackManager;



import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;


public class Register extends AppCompatActivity {

    EditText emial_register, password_register, repassword_register;
    Button register_button;
    FirebaseAuth auth;
    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //butoane
        emial_register = findViewById(R.id.email_register);
        password_register = findViewById(R.id.password_register);
        repassword_register = findViewById(R.id.repassword_register);
        register_button = findViewById(R.id.button_register);

        auth = FirebaseAuth.getInstance();

        //inregistare email si parola

       register_button.setOnClickListener(new View.OnClickListener() {
            @Override
          public void onClick(View v) {
                String emailregister = emial_register.getText().toString().trim();
                String passwordregister = password_register.getText().toString().trim();
                String repasswordregister = repassword_register.getText().toString().trim();

                if(TextUtils.isEmpty(emailregister))
                {
                    emial_register.setError("Email can not be null !");
                    return;
                }
                if(TextUtils.isEmpty(passwordregister))
                {
                    password_register.setError("Password can not be null !");
                    return;
                }
                if(TextUtils.isEmpty(repasswordregister))
                {
                    repassword_register.setError("Please rewrite the password !");
                    return;
                }
                if(passwordregister.length() < 6)
                {
                    password_register.setError("Password must have more than 6 characters");
                }
                if (!passwordregister.equals(repasswordregister))
                {
                    repassword_register.setError("Password must be the same !");
                    return;
                }
               if(auth.getCurrentUser() != null)
                {
                    register_button.setError("This email is already registered !");
                }

                // register user in firebase
                auth.createUserWithEmailAndPassword(emailregister, passwordregister).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }
                        else
                        {
                            Toast.makeText(Register.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }


                    }
                });




            }
        });
    }


}