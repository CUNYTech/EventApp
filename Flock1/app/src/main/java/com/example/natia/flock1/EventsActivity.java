package com.example.natia.flock1;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

import Model.Events;
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

/**
 * Created by pprickle on 10/22/17.
 */

public class EventsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mUserReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private FirebaseRecyclerAdapter mRAdapter;
    private FirebaseListAdapter<Events> mAdapter;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    private RecyclerView eventRecyclerview;
    private static StorageReference mFirebaseStorage2 = FirebaseStorage.getInstance().getReference();
    private ClickListener clicklistener;
    private GestureDetector gestureDetector;

    //whatever page this activity needs to interact with before creation needs to pass info to it
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list2);


        //mListView = findViewById(R.id.events_list_view);

        //INITIALIZE FIREBASE DB
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Events2");

        eventRecyclerview = findViewById(R.id.event_list_recyclerview);


        eventRecyclerview.setHasFixedSize(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        eventRecyclerview.setLayoutManager(linearLayoutManager);
        eventRecyclerview.setItemAnimator(new SlideInDownAnimator());
        eventRecyclerview.addItemDecoration(new VerticalSpaceDecoration(50));
//        eventRecyclerview.setOnClickListener((View.OnClickListener) this);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                startActivity(new Intent(EventsActivity.this,start.class));
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(EventsActivity.this,"Click to create event",Toast.LENGTH_SHORT).show();
                return true;
            }

        });

        mRAdapter = new FirebaseRecyclerAdapter<Events,EventsViewHolder>(
                Events.class,
                R.layout.display_event_card,
                EventsViewHolder.class,
                mDatabaseReference
        ) {
            @Override
            protected Events parseSnapshot(DataSnapshot snapshot) {
                Events events = super.parseSnapshot(snapshot);
                if (events != null){
                    events.setId(snapshot.getKey());
                }
                return events;
            }


            @Override
            protected void populateViewHolder(EventsViewHolder viewHolder, Events model, int position) {
                //viewHolder.startTitle.setText("Start:");
                viewHolder.start.setText(model.getStart());
                //viewHolder.endTitle.setText("End:");
                viewHolder.end.setText(model.getDestination());
                //viewHolder.timeTitle.setText("Time:");
                viewHolder.time.setText(model.getTime());
                //viewHolder.dateTitle.setText("Date:");
                viewHolder. date.setText(model.getDate());
                //viewHolder.linesTitle.setText("Lines:");
                viewHolder.line1.setText(model.getLines().get(0));
                viewHolder.line2.setText(model.getLines().get(1));
                //viewHolder.nameTitle.setText("Created by:");
                viewHolder.name.setText(model.getName());
                viewHolder.setImage(model.getImage());

                //viewHolder.userImage.setImageURI(Uri.parse(model.getImage()));


            }


        };

        eventRecyclerview.setAdapter(mRAdapter);
        mRAdapter.notifyDataSetChanged();

        //registerForContextMenu(eventRecyclerview);
        eventRecyclerview.setTag("Events");

        eventRecyclerview.addOnItemTouchListener(new RecyclerItemClickListener(this,
                eventRecyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(EventsActivity.this, "Single Click on position        :" +
                                mRAdapter.getRef(position).getKey(), Toast.LENGTH_LONG).show();
                //mRAdapter.getRef(position).removeValue();
                String key = mRAdapter.getRef(position).getKey();
                mDatabaseReference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String id = dataSnapshot.child("id").getValue(String.class);
                        String image = dataSnapshot.child("image").getValue(String.class);
                        Intent intent = new Intent(EventsActivity.this,OtherProfile.class);
                        intent.putExtra("user",id);
                        intent.putExtra("image",image);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onLongClick(View view, final int position) {
                final String cUser = mAuth.getCurrentUser().getUid();
                //Toast.makeText(EventsActivity.this, "Long press on position :"+view,
                        //Toast.LENGTH_LONG).show();

                String key = mRAdapter.getRef(position).getKey();
                mDatabaseReference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String id = dataSnapshot.child("id").getValue(String.class);
                        //Toast.makeText(EventsActivity.this, cUser + " " + id,Toast.LENGTH_LONG).show();
                        try {
                            if((id.equals(cUser)) == false){
                                Toast.makeText(EventsActivity.this, "You are not authorized to remove an " +
                                                "event you didnt create",
                                        Toast.LENGTH_LONG).show();

                            } else if (id.equals(cUser)){
                                mRAdapter.getRef(position).removeValue();
                                return;
                            }
                        } catch (NullPointerException ex){
                            ex.printStackTrace();
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }



        }));
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener{
        private ImageView userImage;
        private TextView start, end, time, date, line1, line2, name;
        View mView;

        public EventsViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            userImage = itemView.findViewById(R.id.eventProfilePic);
            //startTitle = itemView.findViewById(R.id.event_start_title);
            start = itemView.findViewById(R.id.eventStartLoc);
            //endTitle = itemView.findViewById(R.id.event_end_title);
            end = itemView.findViewById(R.id.eventEndLoc);
            //timeTitle = itemView.findViewById(R.id.event_time_title);
            time = itemView.findViewById(R.id.eventStartTime);
            //dateTitle = itemView.findViewById(R.id.event_date_title);
            date = itemView.findViewById(R.id.eventDate);
            //linesTitle = itemView.findViewById(R.id.event_lines_title);
            //lines = itemView.findViewById(R.id.eventLines);
            line1 = itemView.findViewById(R.id.eventLine1);
            line2 = itemView.findViewById(R.id.eventLine2);
            //nameTitle = itemView.findViewById(R.id.event_name_title);
            name = itemView.findViewById(R.id.eventDisplayName);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);



        }

        public void setImage(final String image) {
            //Picasso.with(mView.getContext()).load(image).into(userImage);
            //Customer customer = new Customer();
            String picname = image.substring(image.lastIndexOf("/")+1);

            mFirebaseStorage2 = FirebaseStorage.getInstance().getReference();
            mFirebaseStorage2.child("MFlock_Profile_Pics/MFlock_Profile_Pics/"+picname).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>(){
                        @Override
                        public void onSuccess(Uri uri) {
                            GlideApp.
                                    with(mView.getContext()).
                                    load(uri).
                                    transform(new CircleCrop()).
                                    into(userImage);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    //Log.d(TAG, "fail to retrive imageDL url");
                }
            });

        }


        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EventsActivity.this, MainHub.class);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_long_press_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //super.onContextItemSelected(item);

        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Toast.makeText(this, "Soon to come from " + info , Toast.LENGTH_SHORT).show();


        if(item.getItemId() == R.id.event_menu_profile){

            //Toast.makeText(this, "Soon to come from " + user, Toast.LENGTH_SHORT).show();
            //Log.d(" context menu", "index: " + index + " view: " + view);
            //startActivity(new Intent(this,OtherProfile.class));
            return true;
        } else if(item.getItemId() == R.id.event_menu_chat){
            startActivity(new Intent(EventsActivity.this, ChatActivity.class));
        } else if(item.getItemId() == R.id.event_menu_delete){
            Toast.makeText(this, "Soon to come", Toast.LENGTH_SHORT).show();
            //mRAdapter.getRef((Integer) mRAdapter.getItem(info.position));
            //root.removeValue((DatabaseReference.CompletionListener) mRAdapter.getItem(info.position));
        }
        else {return false;}
        //return true;

        return super.onContextItemSelected(item);
    }

    public interface ClickListener{
        void onClick(View view, int position);
        void onLongClick(View view,int position);
    }

}

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