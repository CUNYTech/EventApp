package com.example.natia.flock1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText gender;
    private EditText age;
    private Button createAccountBtn;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MUsers");

        mAuth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog(this);

        email = (EditText) findViewById(R.id.emailAct);
        firstName = (EditText) findViewById(R.id.firstNameAct);
        lastName = (EditText) findViewById(R.id.lastNameAct);
        password = (EditText) findViewById(R.id.passwordAct);
        gender = (EditText) findViewById(R.id.genderAct);
        age = (EditText) findViewById(R.id.ageAct);
        createAccountBtn = (Button) findViewById(R.id.createAccountAct);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }

    private void createNewAccount() {
        String em = email.getText().toString().trim();
        String name = firstName.getText().toString().trim();
        String lname = lastName.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        String gen = gender.getText().toString().trim();
        String ag = age.getText().toString().trim();

        if(!TextUtils.isEmpty(em) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(lname)
                && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(gen) && !TextUtils.isEmpty(ag)) {

            mProgressDialog.setMessage("Creating Account...");
            mProgressDialog.show();

            mAuth.createUserWithEmailAndPassword(em, pwd)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if(authResult != null) {
                                String userid = mAuth.getCurrentUser().getUid();
                                DatabaseReference currenUserDb = mDatabaseReference.child(userid);
                                currenUserDb.child("email").setValue(email);
                                currenUserDb.child("firstname").setValue(firstName);
                                currenUserDb.child("lastname").setValue(lastName);
                                currenUserDb.child("image").setValue("none");

                                mProgressDialog.dismiss();
                                Intent intent = new Intent(CreateAccountActivity.this, MainHub.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                startActivity(intent);
                            }
                        }
                    });
        }
    }
}
