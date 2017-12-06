package com.example.natia.flock1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.Events;

/**
 * Created by pprickle on 10/22/17.
 */

public class EventsActivity extends AppCompatActivity {
    private Context c;
    private ListView mListView;
    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private FirebaseRecyclerAdapter mRAdapter;
    private FirebaseListAdapter<Events> mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String[] menu;



    //whatever page this activity needs to interact with before creation needs to pass info to it
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list2);

        mListView = findViewById(R.id.events_list_view);
        menu = getResources().getStringArray(R.array.event_menu);

        //INITIALIZE FIREBASE DB
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Events2");

        mAdapter = new FirebaseListAdapter<Events>(this, Events.class, R.layout.list_items_events,
                mDatabaseReference) {
            @Override
            protected void populateView(View v, final Events model, int position) {
                // Get start element
                ((TextView) v.findViewById(R.id.event_start)).setText(model.getStart());
                ((TextView) v.findViewById(R.id.event_end)).setText(model.getDestination());
                ((TextView) v.findViewById(R.id.event_lines)).setText(model.getLines().toString());
                ((TextView) v.findViewById(R.id.event_name)).setText(model.getName());
                ((TextView) v.findViewById(R.id.event_time)).setText(model.getTime());
                ((TextView) v.findViewById(R.id.event_date)).setText(model.getDate());
                ((ImageView) v.findViewById(R.id.eventUserImage)).setImageURI(Uri.parse(model.getImage()));
                ((TextView) v.findViewById(R.id.event_start_title)).setText("Start:");
                ((TextView) v.findViewById(R.id.event_end_title)).setText("End:");
                ((TextView) v.findViewById(R.id.event_time_title)).setText("Time:");
                ((TextView) v.findViewById(R.id.event_date_title)).setText("Date:");
                ((TextView) v.findViewById(R.id.event_lines_title)).setText("Lines:");
                ((TextView) v.findViewById(R.id.event_name_title)).setText("Created by:");

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(EventsActivity.this, "You fool", Toast.LENGTH_LONG).show();
                        Intent profileIntent = new Intent(EventsActivity.this,OtherProfile.class);
                        profileIntent.putExtra("user", model.getId());
                        profileIntent.putExtra("image", model.getImage());
                        startActivity(profileIntent);
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return false;
                    }
                });
            }
        };

        mListView.setAdapter(mAdapter);
        registerForContextMenu(mListView);



        /**
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //ArrayList<String> ids = new ArrayList<>();
                eventList.clear();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Search mySearch = childSnapshot.getValue(Search.class);
                    eventList.add(mySearch);

                    //Log.e("mysearch", mySearch.getName());
                    //Log.d("mysearch2", mySearch2.toString());

                    //eventList.add(mySearch);
                    //This creates and sets a simple adapter for the ListView. The ArrayAdapter takes in the
                    // current context, a layout file specifying what each row in the list should look like,
                    // and the data that will populate the list as arguments

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getKey();

                Search mySearch = new Search();
                mySearch.setName(dataSnapshot.child(value).child("name").getValue(String.class));
                mySearch.setTime(dataSnapshot.child(value).child("time").getValue(String.class));
                mySearch.setDate(dataSnapshot.child(value).child("date").getValue(String.class));
                mySearch.setDestination(dataSnapshot.child(value).child("destination").getValue(String.class));
                mySearch.setId(dataSnapshot.child(value).getValue(String.class));
                mySearch.setlines(dataSnapshot.child(value).child("lines").getValue(ArrayList.class));
                mySearch.setStart(dataSnapshot.child(value).child("start").getValue(String.class));
                mySearch.setImage(dataSnapshot.child(value).child("image").getValue(String.class));



                Log.d("mysearch", mySearch.toString());

                Search mySearch2 = new Search();
                mySearch2.getStart();
                mySearch2.getDestination();
                mySearch2.getDate();
                mySearch2.getId();
                mySearch2.getImage();
                mySearch2.getLines();
                mySearch2.getName();
                mySearch2.getTime();

                eventList.add(mySearch);

                //This creates and sets a simple adapter for the ListView. The ArrayAdapter takes in the
                // current context, a layout file specifying what each row in the list should look like,
                // and the data that will populate the list as arguments
                mAdapter = new EventAdapter(EventsActivity.this, eventList);
                mListView.setAdapter(mAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

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


    }
    }

    private void displayInputDialog() {
        Dialog d = new Dialog(this);
        d.setTitle("Save To Firebase");
        d.setContentView(R.layout.activity_start);

        nameEditTxt = (EditText) d.findViewById(R.id.nameEditText);
        propTxt = (EditText) d.findViewById(R.id.propellantEditText);
        descTxt = (EditText) d.findViewById(R.id.descEditText);
        Button saveBtn = (Button) d.findViewById(R.id.saveBtn);
        //SAVE
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GET DATA
                String name = nameEditTxt.getText().toString();
                String propellant = propTxt.getText().toString();
                String desc = descTxt.getText().toString();
                //SET DATA
                Spacecraft s = new Spacecraft();
                s.setName(name);
                s.setPropellant(propellant);
                s.setDescription(desc);
                //SIMPLE VALIDATION
                if (name != null && name.length() > 0) {
                    //THEN SAVE
                    if (helper.save(s)) {
                        //IF SAVED CLEAR EDITXT
                        nameEditTxt.setText("");
                        propTxt.setText("");
                        descTxt.setText("");
                        adapter = new CustomAdapter(MainActivity.this, helper.retrieve());
                        lv.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Name Must Not Be Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        d.show();
    }*/
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EventsActivity.this, MainHub.class);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.event_long_press_menu, menu);
    }
}
