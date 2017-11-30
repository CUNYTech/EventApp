package com.example.natia.flock1;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import Model.AutoCompleteAdapter;
import Model.Search;


public class start extends AppCompatActivity {
    Context c = this;
    private EditText time;
    private EditText date;
    private String name;

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
    private Uri image = mAuth.getCurrentUser().getPhotoUrl();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Events");

        final AutoCompleteTextView station1 = findViewById(R.id.station1);
        final AutoCompleteTextView station2 = findViewById(R.id.station2);
        final Spinner line1 = findViewById(R.id.line1);
        final Spinner line2 = findViewById(R.id.line2);
        time = findViewById(R.id.editTextTimeStartAct);
        date = findViewById(R.id.editTextDateStartAct);
        name = mAuth.getCurrentUser().getDisplayName().toString();
        image = mAuth.getCurrentUser().getPhotoUrl();
        Button search = findViewById(R.id.search);

        AssetManager assetManager = getAssets();
        //reconsider stations.txt, each combination is explicit for separate listing, aggregate now
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

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, hash.get(temp));
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

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, hash.get(temp));
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
                    Search mySearch = new Search();
                    mySearch.setStart(station1.getText().toString());
                    mySearch.setDestination(station2.getText().toString());
                    mySearch.setDate(date.getText().toString());
                    mySearch.setTime(time.getText().toString());
                    mySearch.setName(name);
                    mySearch.setImage(image);

                    ArrayList<String> ls = new ArrayList<String>();
                    ls.add(line1.getSelectedItem().toString());

                    if (!ls.contains(line2.getSelectedItem().toString())) {
                        ls.add(line2.getSelectedItem().toString());
                    }

                    mySearch.setlines(ls);

                    Log.i("a", mySearch.getStart());
                    Log.i("a", mySearch.getDestination());
                    Log.i("a", mySearch.getLines().get(0));

                    mDatabaseReference
                            .push()
                            .setValue(mySearch);


                    Intent intent = new Intent(start.this, EventsActivity.class);
                    intent.putExtra("key", mySearch);
                    startActivity(intent);
                }
            }
        });
    }
}