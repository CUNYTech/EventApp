package com.example.natia.flock1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class spalsh_page extends AppCompatActivity {

    //private static int SPLASH_TIME_OUT=3000;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //setContentView(R.layout.activity_spalsh_page);

        //gets the instance of the current user and sets it to mUser
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //if the user is already logged in, it will push them past the login screen to the Maps page
        //if not, the user will have to login
        if(mUser!= null) {
            super.onCreate(savedInstanceState);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onCreate(savedInstanceState);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }






        /**

        new Handler().postDelayed (new Runnable(){
            @Override
            public void run(){
                Intent HomeIntent=new Intent(spalsh_page.this,  MainActivity.class);
                startActivity(HomeIntent);
                finish();
            }

        },SPLASH_TIME_OUT);*/



    }


}

