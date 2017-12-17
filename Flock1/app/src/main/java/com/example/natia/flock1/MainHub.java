package com.example.natia.flock1;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import Fragments.MapsFragment;
import Model.Customer;

public class MainHub extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private String email;
    private String fullName;
    private String userid;
    private String imagePath;
    private StorageReference mFirebaseStorage;
    private StorageReference mFirebaseStorage2;
    private Context context;
    private GoogleMap mMap;
    private InputStream inputStream;
    private BufferedReader in;
    private FirebaseUser mUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mAuth.getCurrentUser() == null){
            login();
        }

        mDatabase = FirebaseDatabase.getInstance();


        setContentView(R.layout.activity_main_hub);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("MFlock_Profile_Pics");
        mFirebaseStorage2 = FirebaseStorage.getInstance().getReference();
        userid = mAuth.getCurrentUser().getUid();
        mDatabaseReference = mDatabase.getReference().child("MUsers").child(userid);

        try{
            AssetManager assetManager = getAssets();
            inputStream = assetManager.open("stations.json");
            in = new BufferedReader(new InputStreamReader(inputStream));

        } catch (IOException e) {}



        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Customer currentUser = new Customer();

                currentUser.setfirstName(dataSnapshot.child("firstName").getValue(String.class));
                currentUser.setlastName(dataSnapshot.child("lastName").getValue(String.class));
                currentUser.setemail(mAuth.getCurrentUser().getEmail());
                currentUser.setImage(dataSnapshot.child("image").getValue(String.class));
                //uInfo.setImage(dataSnapshot.getValue(UserInformation.class).getImage());

                fullName = currentUser.getfirstName() + " " + currentUser.getlastName();
                email = currentUser.getemail();
                //image = currentUser.getImage();
                imagePath = currentUser.getImage();
                Log.d("imageref", imagePath);




                /**UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .setPhotoUri(Uri.parse(imagePath))
                        .build();

                mAuth.getCurrentUser().updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //Log.d(TAG, "User profile updated.");
                                }
                            }
                        });*/

                //Log.d("CurrentUser2",fullName + " " + email + " " + imagePath);

                SharedPreferences shared = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("firstName", dataSnapshot.child(userid).child("firstName").getValue(String.class));
                editor.putString("lastName", dataSnapshot.child(userid).child("lastName").getValue(String.class));
                editor.putString("fullName", fullName);
                editor.putString("image", imagePath);

                editor.putString("email", email);
                editor.apply();


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences shared = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        //fullName = shared.getString("fullName","");
        fullName = mAuth.getCurrentUser().getDisplayName();
        //email = shared.getString("email","");
        email = mAuth.getCurrentUser().getEmail();
        imagePath = shared.getString("image","");
        //imagePath = mAuth.getCurrentUser().getPhotoUrl().toString();
        Log.d("imageref2", imagePath);

        Customer customer = new Customer();
        customer.setImage(imagePath);
        View header = navigationView.getHeaderView(0);
        TextView nav_user = header.findViewById(R.id.userNavMainHub);
        TextView nav_userEmail = header.findViewById(R.id.userEmailNavMainHub);
        final ImageView nav_imgView = header.findViewById(R.id.imgViewMainHub);
        //Log.d("CurrentUser1",fullName + " " + email + imagePath);
        nav_user.setText(fullName);
        //nav_imgView.setImageURI(mAuth.getCurrentUser().getPhotoUrl());
        //nav_imgView.setImageURI(Uri.parse(imagePath));
        Glide.with(getApplicationContext()).load(Uri.parse(imagePath)).into(nav_imgView);
        //convert string to picture name
        String picname = customer.getImage().substring(customer.getImage().lastIndexOf("/")+1);

        mFirebaseStorage2.child("MFlock_Profile_Pics/MFlock_Profile_Pics/"+picname).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>(){
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).into(nav_imgView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //Log.d(TAG, "fail to retrive imageDL url");
            }
        });

        nav_userEmail.setText(email);
//
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.main_navi, new MapsFragment()).commit();

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainHub.this, ExampleActivity.class);
                startActivity(intent);
            }
        });*/
    }

    private void login() {
        Intent intent = new Intent(MainHub.this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_hub, menu);
        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    //this corresponds to all the things in the sliding menu
    public boolean onNavigationItemSelected(MenuItem item) {

        //need to import the fragment manager to handle our different fragments
        FragmentManager fm = getFragmentManager();
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Will handle the profile action
            //need to reference the container for our fragments which is in content_main_hub
            Intent intent = new Intent(MainHub.this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_map) {
            // Will handle the map action
            //need to reference the container for our fragments which is in content_main_hub
            Intent intent = new Intent(MainHub.this, start.class);
            startActivity(intent);

        } else if (id == R.id.nav_chat) {
            // Will handle the map action
            //need to reference the container for our fragments which is in content_main_hub
            // User is already signed in. Therefore, display
            // a welcome Toast

            Intent intent = new Intent(MainHub.this, ChatActivity.class);

            startActivity(intent);

        } else if (id == R.id.nav_events) {

            Intent intent = new Intent(MainHub.this, EventsActivity.class);

            startActivity(intent);
        } else if (id == R.id.nav_signout) {
            //will sign the user out
            mAuth.signOut();
            Intent intent = new Intent(MainHub.this, MainActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng nyc = new LatLng(-73.96837899960818, 40.799446000334825);
        mMap.addMarker(new MarkerOptions().position(nyc).title("Marker in NYC"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nyc));
    }
}
