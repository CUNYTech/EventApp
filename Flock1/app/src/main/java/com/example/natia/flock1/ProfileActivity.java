package com.example.natia.flock1;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
/**
 * Created by junhaochen on 10/16/17.
 */

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileView";

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;

    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mListView = (ListView) findViewById(R.id.userProfilelistview);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
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
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            UserInformation uInfo = new UserInformation();

            //uInfo.setAge(ds.child(userID).getValue(UserInformation.class).getAge());
            //uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail());
            //uInfo.setFirst_Name(ds.child(userID).getValue(UserInformation.class).getFirst_Name());
            //uInfo.setGender(ds.child(userID).getValue(UserInformation.class).getGender());
            //uInfo.setLast_Nmae(ds.child(userID).getValue(UserInformation.class).getLast_Nmae());

            //display all the information
            //Log.d(TAG, "showData: firstname: " + uInfo.getFirst_Name());
            //Log.d(TAG, "showData: email: " + uInfo.getEmail());
            //Log.d(TAG, "showData: lastname: " + uInfo.getLast_Nmae());
            //Log.d(TAG, "showData: age: " + uInfo.getAge());
            //Log.d(TAG, "showData: gender: " + uInfo.getGender());

            ArrayList<String> array  = new ArrayList<>();
            //array.add(uInfo.getFirst_Name());
            //array.add(uInfo.getLast_Nmae());
            array.add("testing");
            array.add(myRef.child("MUsers").child(userID).child("Email").getKey().toString());
            //array.add(uInfo.getAge());
            //array.add(uInfo.getGender());
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
            mListView.setAdapter(adapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
