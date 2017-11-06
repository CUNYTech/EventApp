package com.example.natia.flock1;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private Button loginButton;
    private TextView createActButton;
    private TextView forgotPwdButton;
    private EditText emailField;
    private EditText passwordField;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mAuth = FirebaseAuth.getInstance();

        loginButton = (Button) findViewById(R.id.loginButtonEt);
        forgotPwdButton = (TextView) findViewById(R.id.passwordRecover);
        createActButton = (TextView) findViewById(R.id.signUp);
        emailField = (EditText) findViewById(R.id.loginEmailEt);
        passwordField = (EditText) findViewById(R.id.loginPasswordEt);
        //databaseReference = database.getReference("message");
        createActButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateAccountActivity.class));
                finish();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                mUser = firebaseAuth.getCurrentUser();

                //take the current user and let the user know if they are signed in
                if (mUser != null) {
                    Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Not Signed In", Toast.LENGTH_LONG).show();
                }
            }

        };

        //happens when you press the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(emailField.getText().toString())
                        && !TextUtils.isEmpty(passwordField.getText().toString())) {

                    String email = emailField.getText().toString();
                    String pwd = passwordField.getText().toString();

                    //pass the email and password to the login method
                    login(email, pwd);


                } else {

                }
            }
        });

        //take the user to Forgot Password page if they press the button
        forgotPwdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });


    }

    //email and password are passed to the method
    private void login(String email, String pwd) {
        if(!email.equals("") && !pwd.equals("")) {
            mAuth.signInWithEmailAndPassword(email, pwd).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_LONG).
                                show();

                        startActivity(new Intent(MainActivity.this, MainHub.class));
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to Signed In",
                                Toast.LENGTH_LONG).show();
                    }

                }
                });

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_signout) {
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }


}

