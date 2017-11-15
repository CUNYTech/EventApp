package com.example.natia.flock1;

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

import Model.Search;

/**
 * Created by pprickle on 10/22/17.
 */

public class EventsActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    DatabaseReference stationRef = database.getReference().child("Stations");
    DatabaseReference eventRef = database.getReference().child("Events");
    DatabaseReference userRef = database.getReference().child("MUsers");

    Search search = new Search();
    String startStationID = "";
    String storage = "";
    ArrayList<String> storageArr = new ArrayList<String>();
    ArrayList<Search> events = new ArrayList<Search>();
    ArrayList<String> ids = new ArrayList<String>();


    LinearLayout ll = (LinearLayout) this.findViewById(R.id.eventList);

    //whatever page this activity needs to interact with before creation needs to pass info to it
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //collect our intent
        Intent intent = getIntent();
        final Search search = intent.getParcelableExtra("key");

        String startStation = search.getStart();
        //String destStation = search.getDestination();
        //ArrayList<String> lines = search.getLines();
        //String date = search.getDate();
        //String time = search.getTime();

        resultsValues(stationRef, "Stop Name", startStation, "Station ID");
        startStationID = storageArr.get(0);

        setContentView(R.layout.activity_event_list);

        resultsValues(eventRef, "startStationID", startStationID, "hostID");
        ids = storageArr;


        //TODO: Make it so you can apply to events, make it so you can't apply to own event, better filtering options
        for (int i = 0; i < ids.size(); i++) {
            resultsValues(userRef, "firstName", ids.get(i), "firstName");//do I have to new userRef, order once, can't order again?
            TextView text = new TextView(this);
            text.setTextColor(Color.parseColor("FF8492A6"));
            text.setText(storageArr.get(0));
            ll.addView(text);
        }

        final Button addEvent = (Button) findViewById(R.id.addEvent);
        addEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("key", search);
                i.setClass(getBaseContext(), AddEventActivity.class);
                startActivity(i);
            }
        });
    }

    void resultsValues(DatabaseReference collection, String orderBy, String where, String select) {
        final String key = select;
        storageArr.clear();

        collection.orderByChild(orderBy)
                .equalTo(where)
                .addChildEventListener(new ChildEventListener() {
                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                        storageArr.add(snapshot.child(key).getValue().toString());
                    }

                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }

                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    void resultsSearch(DatabaseReference collection, String orderBy, String where, String select) {
        final String s = select;
        events.clear();

        collection.orderByChild(orderBy)
                .equalTo(where)
                .addChildEventListener(new ChildEventListener() {
                    public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                        events.add(snapshot.getValue(Search.class));
                    }

                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                    }

                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}