package com.example.natia.flock1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.Search;

/**
 * Created by pprickle on 11/6/17.
 */

public class AddEventActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference eventRef = database.getReference().child("Events");

    EditText start = (EditText) findViewById(R.id.startStation);
    EditText end = (EditText) findViewById(R.id.endStation);
    TimePicker time = (TimePicker) findViewById(R.id.timepicker);
    DatePicker date = (DatePicker) findViewById(R.id.datepicker);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final Search search = intent.getParcelableExtra("key");

        start.setText(search.getStart());

        if (!search.getDestination().isEmpty()) {
            end.setText(search.getDestination());
        }

        if (!search.getTime().isEmpty()) {
            //time.setText(search.getTime());
        }

        if (!search.getDate().isEmpty()) {
            //date.setText(search.getDate());
        }


        final Button create = (Button) findViewById(R.id.addEvent);
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search.setStart(start.getText().toString());
                search.setDestination(end.getText().toString());
                //String st = time.getHour();
                //search.setTime(time.getBaseline());
                createEvent(search);
            }
        });

        final Button cancel = (Button) findViewById(R.id.addEvent);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getBaseContext(), EventsActivity.class);
                startActivity(i);
            }
        });
    }


    public void createEvent(Search s) {
        String key = eventRef.push().getKey();
        eventRef.child(key);
        eventRef = eventRef.child(key);
        eventRef.child("startStation").setValue(s.getStart());
        eventRef.child("destStation").setValue(s.getDestination());
        eventRef.child("lines").setValue(s.getLines());
        eventRef.child("date").setValue(s.getDate());
        eventRef.child("time").setValue(s.getTime());
    }
}
