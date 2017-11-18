package com.example.natia.flock1;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Fragments.MapsFragment;
import Fragments.ProfileFragment;
import Model.Customer;

public class MainHub extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private String email;
    private String fullName;
    private String userid;
    private Uri image;
    private String imagePath;
    private StorageReference mFirebaseStorage;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        //Creates a new database which will hold our users
        mDatabaseReference = mDatabase.getReference().child("MUsers");
        setContentView(R.layout.activity_main_hub);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseStorage = FirebaseStorage.getInstance().getReference().child("MFlock_Profile_Pics");

        userid = mAuth.getCurrentUser().getUid();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Customer currentUser = new Customer();

                currentUser.setfirstName(dataSnapshot.child(userid).child("firstName").getValue(String.class));
                currentUser.setlastName(dataSnapshot.child(userid).child("lastName").getValue(String.class));
                currentUser.setemail(mAuth.getCurrentUser().getEmail());
                currentUser.setImage(dataSnapshot.child(userid).child("image").getValue(String.class));

                fullName = currentUser.getfirstName() + " " + currentUser.getlastName();
                email = currentUser.getemail();
                //image = currentUser.getImage();
                imagePath = currentUser.getImage();

                Log.d("CurrentUser2",fullName + " " + email + " " + imagePath);

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


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

        fullName = shared.getString("fullName","");
        email = shared.getString("email","");
        imagePath = shared.getString("image","");
        View header = navigationView.getHeaderView(0);
        TextView nav_user = header.findViewById(R.id.userNavMainHub);
        TextView nav_userEmail = header.findViewById(R.id.userEmailNavMainHub);
        ImageView nav_imgView = header.findViewById(R.id.imgViewMainHub);
        Log.d("CurrentUser1",fullName + " " + email + imagePath);
        nav_user.setText(fullName);
        nav_imgView.setImageURI(image);
        Glide.with(this).load(imagePath).into(nav_imgView);

        nav_userEmail.setText(email);
//
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.main_navi, new MapsFragment()).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainHub.this, start.class);
                startActivity(intent);
            }
        });
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
            //fm.beginTransaction().replace(R.id.main_navi, new ProfileFragment()).commit();
            //fm.beginTransaction().replace(R.id.main_navi, new ProfileFragment()).commit();
            Intent intent = new Intent(MainHub.this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_map) {
            // Will handle the map action
            //need to reference the container for our fragments which is in content_main_hub
            fm.beginTransaction().replace(R.id.main_navi, new MapsFragment()).commit();

        } else if (id == R.id.nav_chat) {
            // Will handle the map action
            //need to reference the container for our fragments which is in content_main_hub
            Intent intent = new Intent(MainHub.this, ChatActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_friends) {
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
}
