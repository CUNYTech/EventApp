package com.example.natia.flock1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import Model.UserInformation;

public class OtherProfile extends AppCompatActivity {

    private static final String TAG = "ProfileView";

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String user;
    private ImageView imageView;
    private FirebaseStorage fstorage;
    private StorageReference storageRef;
    private String emailAdd;
    private String email;
    private String url;
    private String image;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        mListView = findViewById(R.id.otherProfileListview);
        imageView = findViewById(R.id.otherProfilePicAct);
        Intent intent = getIntent();
        email = intent.getStringExtra("Other Email");
        user = intent.getStringExtra("Current User");
        url = intent.getStringExtra("FIREBASE_URL");
        image = intent.getStringExtra("image");


        fstorage = FirebaseStorage.getInstance();
        storageRef = fstorage.getReference();

        //mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        myRef = mFirebaseDatabase.getReference()
                .child("MUsers").child(user);

        Log.d("userinfo",user);
        Log.d("userref",myRef.toString());


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("snap",dataSnapshot.toString());
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
        Log.d("gotthisfar", uInfo.getFirstName());

        //get image name from database
        uInfo.setImage(image);
        Glide.with(this).load(image).into(imageView);

        //holder until database is fix
        //String picname ="cropped8295209993791726610.jpg";

        //String picname = uInfo.getImage().substring(uInfo.getImage().lastIndexOf("/")+1);
        //String picname =uInfo.getImage(); //after change the database to only storage picture's name

        /*storageRef.child("MFlock_Profile_Pics/MFlock_Profile_Pics/").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>(){
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });*/


        //display all the information

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
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OtherProfile.this, ExampleActivity.class);
        startActivity(intent);
    }

}
