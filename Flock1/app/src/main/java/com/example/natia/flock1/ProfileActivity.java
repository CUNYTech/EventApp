package com.example.natia.flock1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.util.ArrayList;

import Model.UserInformation;

/**
 * Created by junhaochen on 10/16/17.
 */

public class ProfileActivity extends FragmentActivity {
    private static final String TAG = "ProfileView";

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;
    private ImageView imageView;
    private FirebaseStorage fstorage;
    private StorageReference storageRef;
    private String emailAdd;

    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mListView = findViewById(R.id.userProfilelistview);
        imageView = findViewById(R.id.profilePicAct);

        fstorage = FirebaseStorage.getInstance();
        storageRef = fstorage.getReference();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        emailAdd = user.getEmail();
        myRef = mFirebaseDatabase.getReference()
                .child("MUsers").child(userID);

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
        UserInformation uInfo = new UserInformation();

        uInfo.setAge(dataSnapshot.getValue(UserInformation.class).getAge());
        uInfo.setFirstName(dataSnapshot.getValue(UserInformation.class).getFirstName());
        uInfo.setLastName(dataSnapshot.getValue(UserInformation.class).getLastName());
        uInfo.setGender(dataSnapshot.getValue(UserInformation.class).getGender());

        //get image name from database
        uInfo.setImage(dataSnapshot.getValue(UserInformation.class).getImage());

        //convert string to picture name
        String picname = uInfo.getImage().substring(uInfo.getImage().lastIndexOf("/")+1);

        storageRef.child("MFlock_Profile_Pics/MFlock_Profile_Pics/"+picname).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>(){
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "fail to retrive imageDL url");
            }
        });

        //url holder for testing until database fix so i can get correct download url for the login user
        //String url = "https://firebasestorage.googleapis.com/v0/b/flock-a5c97.appspot.com/o/MFlock_Profile_Pics%2FMFlock_Profile_Pics%2Fcropped8295209993791726610.jpg?alt=media&token=0d68fb79-9931-4240-bbb1-4250cec05483";

        //Glide.with(getApplicationContext()).load(url).into(imageView);

        //display all the information
        Log.d(TAG, "showData: firstname: " + uInfo.getFirstName());
        Log.d(TAG, "showData: email: " + uInfo.getEmail());
        Log.d(TAG, "showData: lastname: " + uInfo.getLastName());
        Log.d(TAG, "showData: age: " + uInfo.getAge());
        Log.d(TAG, "showData: gender: " + uInfo.getGender());
        Log.d(TAG, "showData: email: " + uInfo.getEmail());
        ArrayList<String> array = new ArrayList<>();
        array.add("First Name: "+uInfo.getFirstName());
        array.add("Last Name:"+uInfo.getLastName());
        array.add("Age: "+uInfo.getAge());
        array.add("Gender: "+uInfo.getGender());
        array.add("Email: "+emailAdd);
        //array.add(uInfo.getImage());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array);
        mListView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id== R.id.action_signout) {
            mAuth.signOut();
        }



        return super.onOptionsItemSelected(item);
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
     *
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, MainHub.class);
        startActivity(intent);
    }
}