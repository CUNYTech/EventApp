package com.example.natia.flock1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
//import com.onesignal.OneSignal;


public class splash_page extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2000;
    private Handler mHandler;
    private Runnable mRunnable;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    public static FirebaseDatabase database;
    static String LoggedIn_User_Email;
    public static int Device_Width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //OneSignal.startInit(this).init();

        if (database == null) {
            database = FirebaseDatabase.getInstance();
            //mDatabase.setPersistenceEnabled(true);
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        setContentView(R.layout.activity_splash_page);

        //if the user is already logged in, it will push them past the login screen to the Maps page
        //if not, the user will have to login








        new Handler().postDelayed (new Runnable(){
            @Override
            public void run(){
                if(mUser!= null) {
                    LoggedIn_User_Email = mUser.getEmail();
                    Intent intent = new Intent(splash_page.this, MainHub.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(splash_page.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        },SPLASH_TIME_OUT);



    }


}

