package com.example.natia.flock1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Search;

/**
 * Created by pprickle on 10/22/17.
 */

public class EventsActivity extends AppCompatActivity {
    Context c = this;
    private ListView mListView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private FirebaseListAdapter mAdapter;
    private ArrayAdapter adapter;

    //DatabaseReference stationRef = database.getReference().child("Stations");
    //DatabaseReference eventRef = database.getReference().child("Events");
    //DatabaseReference userRef = database.getReference().child("MUsers");

    //String startStation;
    //String destStation;
    //ArrayList<String> lines;
    //String date;
    //String time;

    //whatever page this activity needs to interact with before creation needs to pass info to it
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list2);

        mListView = findViewById(R.id.events_list_view);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Events");



        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Search event = new Search();

                dataSnapshot.getChildren();
                //currentUser.setlastName(dataSnapshot.child(userid).child("lastName").getValue(String.class));
                //currentUser.setemail(mAuth.getCurrentUser().getEmail());
                //currentUser.setImage(dataSnapshot.child(userid).child("image").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //final ArrayList<Recipe> recipeList = Recipe.getRecipesFromFile(, this);

        //String[] listItems = new String[recipeList.size()];

        //for(int i = 0; i < recipeList.size(); i++){
          //  Recipe recipe = recipeList.get(i);
            //listItems[i] = recipe.title;
        //}

        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        //mListView.setAdapter(adapter);

        /**
        //collect our intent
        Intent intent = getIntent();
        final Search search = intent.getParcelableExtra("key");

        startStation = search.getStart();
        destStation = search.getDestination();
        lines = search.getLines();
        //date = search.getDate();
        //time = search.getTime();



        final LinearLayout ll = this.findViewById(R.id.eventList);
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


    }*/
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EventsActivity.this, MainHub.class);
        startActivity(intent);
    }
}