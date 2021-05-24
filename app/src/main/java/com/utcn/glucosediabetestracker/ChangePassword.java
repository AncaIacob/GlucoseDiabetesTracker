package com.utcn.glucosediabetestracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    EditText userPassword, confirmPassword;
    Button savePassword;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        userPassword = findViewById(R.id.change_password1);
        confirmPassword = findViewById(R.id.change_password2);
        savePassword = findViewById(R.id.button_save_password);

        user = FirebaseAuth.getInstance().getCurrentUser();

        savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userPassword.getText().toString().isEmpty())
                {
                    userPassword.setError("Required Field");
                    return;
                }
                if(confirmPassword.getText().toString().isEmpty())
                {
                    confirmPassword.setError("Required Field");
                    return;
                }
                if(!userPassword.getText().toString().equals(confirmPassword.getText().toString()))
                {
                    confirmPassword.setError("Password must be the same");
                    return;
                }
                if(userPassword.length() < 6)
                {
                    confirmPassword.setError("Password must have more than 6 characters");
                    return;
                }
                user.updatePassword(userPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChangePassword.this,"Password updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChangePassword.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}