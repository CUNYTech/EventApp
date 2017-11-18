package com.example.natia.flock1;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;

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

    String s;
    String station;
    String line;

    StringTokenizer st;
    BufferedReader in;
    InputStream inputStream;
    AutoCompleteAdapter my_adapter;

    ArrayList<String> stations = new ArrayList<String>();
    Hashtable<String, ArrayList<String>> hash = new Hashtable<String, ArrayList<String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
        final AutoCompleteTextView station1 = (AutoCompleteTextView) findViewById(R.id.station1);
        final AutoCompleteTextView station2 = (AutoCompleteTextView) findViewById(R.id.station2);
        final AutoCompleteTextView line1 = (AutoCompleteTextView) findViewById(R.id.line1);
        final AutoCompleteTextView line2 = (AutoCompleteTextView) findViewById(R.id.line2);
        Button search = (Button) findViewById(R.id.search);

        AssetManager assetManager = getAssets();

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

                if (!hash.containsKey(s)) {
                    ArrayList<String> al = new ArrayList<String>();
                    hash.put(station, al);
                }

                else {
                    hash.get(station).add(line);
                }
            }

            my_adapter = new AutoCompleteAdapter(c, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, stations);
        }

        catch (IOException e) {}

        station1.setThreshold(1);
        station1.setAdapter(my_adapter);

        station1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String temp = my_adapter.getItem(position);
                station1.setText(temp);
                AutoCompleteAdapter ad = new AutoCompleteAdapter(c, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, hash.get(temp));
                line1.setAdapter(ad);
                line1.setVisibility(View.VISIBLE);
            }
        });

        line1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    line1.showDropDown();
                }
            }
        });

        station2.setThreshold(1);
        station2.setAdapter(my_adapter);

        station2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String temp = my_adapter.getItem(position);
                station2.setText(temp);
                AutoCompleteAdapter ad = new AutoCompleteAdapter(c, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, hash.get(temp));
                line2.setAdapter(ad);
                line2.setVisibility(View.VISIBLE);
            }
        });

        line2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                line2.showDropDown();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (station1.getText().toString() != "" ||
                        station2.getText().toString() != "" ||
                        line1.getText().toString() != "" ||
                        line2.getText().toString() != "") {

                    Search mySearch = new Search();
                    mySearch.setStart(station1.getText().toString());
                    mySearch.setDestination(station2.getText().toString());

                    ArrayList<String> ls = new ArrayList<String>();
                    ls.add(line1.getText().toString());

                    if (!ls.contains(line2.getText().toString())) {
                        ls.add(line2.getText().toString());
                    }

                    mySearch.setlines(ls);

                    Log.i("a", mySearch.getStart());
                    Log.i("a", mySearch.getDestination());
                    Log.i("a", mySearch.getLines().get(0));


                    Intent intent = new Intent(start.this, EventsActivity.class);
                    intent.putExtra("key", mySearch);
                    startActivity(intent);
                }
            }
        });
    }
}
