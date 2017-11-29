package com.example.natia.flock1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Customer;

/**
 * Created by napti on 11/13/2017.
 */

public class ExampleActivity extends Activity {

    private Button button;
    private EditText input;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private String email;
    private FirebaseUser mUser;
    public static String FIREBASE_URL = "https://flock-a5c97.firebaseio.com/;";
    private DatabaseReference myRef;
    //private String fullName;
    private String userid;
    private String image;
    //private String imagePath;
    //private StorageReference mFirebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        button = findViewById(R.id.button3);
        input = findViewById(R.id.editText);
        //email = input.getText().toString().trim();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MUsers");
        mUser = mAuth.getCurrentUser();
        userid = mUser.getUid();
        myRef = mDatabase.getReference().child("MUsers").child(userid);
        //image = myRef.child(image).toString();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 Customer currentUser = new Customer();

                 currentUser.setImage(dataSnapshot.child(userid).child("image").getValue(String.class));

                 image = currentUser.getImage();


             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });




        //Log.d("crazy", email);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExampleActivity.this, OtherProfile.class);

                intent.putExtra("Other Email", email);
                intent.putExtra("Current User", userid);
                intent.putExtra("FIREBASE_URL", myRef.toString());
                intent.putExtra("image", image);
                Log.e("crazy", userid);
                startActivity(intent);


            }
        });
    }



    }

