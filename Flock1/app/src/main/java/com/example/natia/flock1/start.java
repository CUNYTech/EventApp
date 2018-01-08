package com.example.natia.flock1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;

import Model.AutoCompleteAdapter;
import Model.Events;


public class start extends AppCompatActivity{
    Context c = this;
    private TextView time;
    private TextView date;
    private String name;
    private TextView startingHeader;
    private TextView endingHeader;

    String s;
    String station;
    String line;

    Boolean changedS1 = true;
    Boolean changedS2 = true;

    StringTokenizer st;
    BufferedReader in;
    InputStream inputStream;
    AutoCompleteAdapter my_adapter;

    ArrayList<String> stations = new ArrayList<String>();
    Hashtable<String, ArrayList<String>> hash = new Hashtable<String, ArrayList<String>>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private String userId = mUser.getUid();
    private DatabaseReference mUserReference = null;
    private DateFormat formatter = new SimpleDateFormat("mm/dd/yy");
    private Date dateObject;
    private String image = null;
    private DatePickerDialog.OnDateSetListener mDateListener;
    private TimePickerDialog.OnTimeSetListener mTimeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_start2);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Events2");
        mUserReference =  mDatabase.getReference().child("MUsers").
                child(userId).child("image");

        startingHeader = findViewById(R.id.StartingStation);
        endingHeader = findViewById(R.id.EndingStation);
        final AutoCompleteTextView station1 = findViewById(R.id.station1);
        final AutoCompleteTextView station2 = findViewById(R.id.station2);
        final Spinner line1 = findViewById(R.id.line1);
        final Spinner line2 = findViewById(R.id.line2);
        time = findViewById(R.id.editTextTimeStartAct);
        date = findViewById(R.id.editTextDateStartAct);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        start.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        start.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeListener, hour, minute, false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                time.setText(hour + ":" + minute);

                dialog.show();
            }
        });

        mDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month +1;
                String dateSel = month + "/" + dayOfMonth + "/" + year;
                date.setText(dateSel);
            }
        };

        mTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String AMPM = "SM";
                if(hourOfDay > 11){
                    AMPM = "PM";

                    if (hourOfDay > 12){
                        hourOfDay = hourOfDay - 12;
                    }
                } else {
                    AMPM = "AM";
                    if (hourOfDay == 0){
                        hourOfDay = 12;
                    }
                }

                time.setText(hourOfDay + ":" + minute + " " + AMPM);
            }
        };



        name = mAuth.getCurrentUser().getDisplayName().toString();
        //image = mAuth.getCurrentUser().getPhotoUrl();
        Button search = findViewById(R.id.search);

        AssetManager assetManager = getAssets();
        //reconsider stations.json.txt, each combination is explicit for separate listing, aggregate now
        try {
            inputStream = assetManager.open("stations.txt");
            in = new BufferedReader(new InputStreamReader(inputStream));

            while((s = in.readLine()) != null) {
                s = s.trim();
                st = new StringTokenizer(s, ":");
                station = st.nextToken();
                station = station.trim();

                line = st.nextToken();
                line = line.trim();

                if (!stations.contains(station)) {
                    stations.add(station);
                }

                if (!hash.containsKey(station)) {
                    ArrayList<String> al = new ArrayList<String>();
                    hash.put(station, al);
                }

                hash.get(station).add(line);
            }

            my_adapter = new AutoCompleteAdapter(c, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, stations);
        }

        catch (IOException e) {}


        station1.setThreshold(1);
        station1.setAdapter(my_adapter);

        station1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changedS1 = false;
                String temp = my_adapter.getItem(position);
                station1.setText(temp);
                line1.setVisibility(View.VISIBLE);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(c, R.layout.spinner_item, hash.get(temp));
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                line1.setAdapter(dataAdapter);
            }
        });

        station1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (stations.contains(station1.getText().toString())) {
                    changedS1 = false;
                    line1.setVisibility(View.VISIBLE);
                }

                else {
                    changedS1 = true;
                    line1.setVisibility(View.INVISIBLE);
                }
            }
        });

        station2.setThreshold(1);
        station2.setAdapter(my_adapter);

        station2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changedS2 = false;
                String temp = my_adapter.getItem(position);
                station2.setText(temp);
                line2.setVisibility(View.VISIBLE);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(c, R.layout.spinner_item, hash.get(temp));
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                line2.setAdapter(dataAdapter);
            }
        });

        station2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (stations.contains(station2.getText().toString())) {
                    changedS2 = false;
                    line2.setVisibility(View.VISIBLE);
                }

                else {
                    changedS2 = true;
                    line2.setVisibility(View.INVISIBLE);
                }
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changedS1 == true || changedS2 == true
                        || line1.getSelectedItem() == null
                        || line2.getSelectedItem() == null) {}

                else {



                    mUserReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Events events = new Events();
                            events.setStart(station1.getText().toString());
                            events.setDestination(station2.getText().toString());
                            events.setDate(date.getText().toString());
                            events.setTime(time.getText().toString());
                            events.setName(name);
                            events.setImage(dataSnapshot.getValue(String.class));
                            events.setId(mUser.getUid());

                            ArrayList<String> ls = new ArrayList<String>();
                            ls.add(line1.getSelectedItem().toString());

                            if (!ls.contains(line2.getSelectedItem().toString())) {
                                ls.add(line2.getSelectedItem().toString());
                            }

                            //mySearch.setlines(ls);
                            events.setLines(ls);

                            mDatabaseReference
                                    .push()
                                    .setValue(events);

                            Intent intent = new Intent(start.this, EventsActivity.class);
                            intent.putExtra("user", events.getId());
                            intent.putExtra("image", events.getImage());
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });








                }
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}