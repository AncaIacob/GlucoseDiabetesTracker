package com.utcn.glucosediabetestracker;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class Login extends AppCompatActivity {
    EditText email_login, password_login;
    TextView forgot_password_txt;
    Button button_login ;

    AlertDialog.Builder forgot_password_alert;
    LayoutInflater inflater;
    FirebaseAuth auth;
    //facebook
    private CallbackManager mCallbackManager;
    private FirebaseAuth mFireBaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    private LoginButton fbLoginButton;
    private static final String TAG = "FacebookAuthentication";

    //google
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private String TAG2 = "Login";
    private int RC_SIGN_IN = 123;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_login = findViewById(R.id.email_login);
        password_login = findViewById(R.id.password_login);
        button_login = findViewById(R.id.button_login);
        forgot_password_txt = findViewById(R.id.forgot_password);
        auth = FirebaseAuth.getInstance();
        forgot_password_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();





        //login email and password


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Login.this, Menu.class);
                //startActivity(intent);
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                String emaillogin = email_login.getText().toString().trim();
                String passwordlogin = password_login.getText().toString().trim();


                if (TextUtils.isEmpty(emaillogin)) {
                    email_login.setError("Email can not be null !");
                    return;
                }
                if (TextUtils.isEmpty(passwordlogin)) {
                    password_login.setError("Password can not be null !");
                    return;
                }
                //aut
                auth.signInWithEmailAndPassword(emaillogin, passwordlogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Login Successffuly", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Menu.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Bad email or password. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }


                });



            }
        });

        forgot_password_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start alertDialog

                View view = inflater.inflate(R.layout.forgot_password_pop, null);
                forgot_password_alert.setTitle("Reset Password")
                        .setMessage("Enter your email to get the link for password reset")
                        .setPositiveButton("Send link", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //validate the email adress
                                EditText email_reset_password = view.findViewById(R.id.forgot_pass_pop);
                                if (email_reset_password.getText().toString().isEmpty()) {
                                    email_reset_password.setError("Required Field");
                                    Toast.makeText(Login.this, "Required Field", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                //send the rest link
                                auth.sendPasswordResetEmail(email_reset_password.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(Login.this, "An email was sent to this adress", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });



                            }
                        }).setNegativeButton("Cancel", null)
                        .setView(view)
                        .create().show();

            }
        });

        //facebook
        mFireBaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        fbLoginButton = findViewById(R.id.login_button_facebook);
        fbLoginButton.setReadPermissions("email","public_profile");
        mCallbackManager = CallbackManager.Factory.create();
        fbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSucces" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError" + error);

            }
        });


        //google
        signInButton = findViewById(R.id.login_button_google);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });



    }
    //facebook
    private void handleFacebookToken (AccessToken token){
        Log.d(TAG, "handleFacebookToken" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFireBaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "sign in with credential: successful");
                    FirebaseUser user = mFireBaseAuth.getCurrentUser();
                    Intent intent1 = new Intent(Login.this, Menu.class);
                    startActivity(intent1);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();




                }else{
                    Log.d(TAG, "sign in with credential: failure", task.getException());
                    Toast.makeText( Login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    //google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(Login.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
           FirebaseGoogleAuth(acc);


        } catch (ApiException e) {
            Toast.makeText(Login.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }

    }
    private  void FirebaseGoogleAuth(GoogleSignInAccount acct){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Successful", Toast.LENGTH_SHORT).show();
                    FirebaseUser user2 = mAuth.getCurrentUser();
                    Intent intent2 = new Intent(Login.this, Menu.class);
                    startActivity(intent2);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
                else{
                    Toast.makeText(Login.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}