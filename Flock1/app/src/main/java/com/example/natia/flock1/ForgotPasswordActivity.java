package com.example.natia.flock1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Button submitButton;
    private EditText emailField;
    private FirebaseDatabase database;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        submitButton = findViewById(R.id.submitPwd);
        emailField = findViewById(R.id.emailForgotPwd);

        //progressBar = (ProgressBar) findViewById(R.id.progressBarForgotPwd);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@Nonnull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "We have sent you " +
                                                    "instructions to reset your password!",
                                            Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(ForgotPasswordActivity.this,
                                            MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //brings this activity to the top

                                    startActivity(intent);
                                } else {
                                    Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset " +
                                                    "email!",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });




    }
}