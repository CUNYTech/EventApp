package com.example.natia.flock1;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

import Model.Customer;
import Model.Search;

/**
 * Created by pprickle on 10/22/17.
 */

public class EventsActivity extends AppCompatActivity {
    Context c = this;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    DatabaseReference stationRef = database.getReference().child("Stations");
    DatabaseReference eventRef = database.getReference().child("Events");
    DatabaseReference userRef = database.getReference().child("MUsers");

    String startStation;
    String destStation;
    ArrayList<String> lines;
    String date;
    String time;

    //whatever page this activity needs to interact with before creation needs to pass info to it
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //collect our intent
        Intent intent = getIntent();
        final Search search = intent.getParcelableExtra("key");

        startStation = search.getStart();
        destStation = search.getDestination();
        lines = search.getLines();
        //date = search.getDate();
        //time = search.getTime();

        setContentView(R.layout.activity_event_list);

        final LinearLayout ll = (LinearLayout) this.findViewById(R.id.eventList);


        final Button addEvent = (Button) findViewById(R.id.addEvent);
        addEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("key", search);
                i.setClass(getBaseContext(), AddEventActivity.class);
                startActivity(i);
            }
        });

        eventRef.orderByChild("startStationName")
                .equalTo(startStation)
                .addChildEventListener(new ChildEventListener() {
                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                        String id = snapshot.child("hostID").getValue().toString();


                        userRef.orderByKey()
                                .equalTo(id)
                                .addChildEventListener(new ChildEventListener() {
                                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                                        //Customer cust = (snapshot.getValue(Customer.class));
                                        String fName = snapshot.child("firstName").getValue().toString();

                                        TextView text = new TextView(c);
                                        //text.setTextColor(Color.parseColor("FF8492A6"));
                                        text.setText(fName);
                                        ll.addView(text);
                                    }

                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                                    public void onCancelled(DatabaseError databaseError) {}
                                });
                    }

                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    public void onCancelled(DatabaseError databaseError) {}
                });
        }
    }