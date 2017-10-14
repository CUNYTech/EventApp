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
        final String em = email.getText().toString().trim();
        final String name = firstName.getText().toString().trim();
        final String lname = lastName.getText().toString().trim();
        final String pwd = password.getText().toString().trim();
        final String gen = gender.getText().toString().trim();
        final String ag = age.getText().toString().trim();

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

                    //this creates the user and then adds properties to that user as its children
                    //will be shown in the database as a json object
                    DatabaseReference currenUserDb = mDatabaseReference.child(userid);
                    currenUserDb.child("email").setValue(em);
                    currenUserDb.child("First Name").setValue(name);
                    currenUserDb.child("Last Name").setValue(lname);
                    currenUserDb.child("Password").setValue(pwd);
                    currenUserDb.child("Gender").setValue(gen);
                    currenUserDb.child("Age").setValue(ag);

                    mProgressDialog.dismiss();

                    //Send users to MainHub
                    Intent intent = new Intent(CreateAccountActivity.this, MainHub.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //brings this activity to the top

                    startActivity(intent);
                }
                }
            });
        }
    }
}
