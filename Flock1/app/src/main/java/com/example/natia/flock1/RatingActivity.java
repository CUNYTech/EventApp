package com.example.natia.flock1;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Model.UserInformation;

/**
 * Created by junhaochen on 11/29/17.
 */

public class RatingActivity extends AppCompatActivity {
    private static final String TAG = "RatingView";

    private RatingBar rb;
    private TextView rateView;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;
    private FirebaseStorage fstorage;
    private StorageReference storageRef;
    private String holdemail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rb = findViewById(R.id.ratingBar2);
        rateView = findViewById(R.id.Textview7);

        //fstorage = FirebaseStorage.getInstance();
        //storageRef = fstorage.getReference();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        holdemail = user.getEmail();
        myRef = mFirebaseDatabase.getReference()
                .child("MUsers").child(userID);
        /*mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
            }
        };*/


        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {

                final String rate = String.valueOf(rating);
                rateView.setText(String.valueOf(rating));
                myRef.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInformation uInfo = new UserInformation();
                        if(dataSnapshot.hasChild("totalRating"))
                        {
                            uInfo.setTotalRating(dataSnapshot.getValue(UserInformation.class).getTotalRating());
                            uInfo.setRatedCounter(dataSnapshot.getValue(UserInformation.class).getRatedCounter());
                            myRef.child("totalRating").setValue(uInfo.getTotalRating()+rating);
                            myRef.child("ratedCounter").setValue(uInfo.getRatedCounter()+1);
                            rb.setIsIndicator(true);

                        }else
                        {
                            myRef.child("totalRating").setValue(rating);
                            myRef.child("ratedCounter").setValue(1);
                            rb.setIsIndicator(true);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });//end of ratingbar change listener
    }
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
