package com.example.natia.flock1;

/**
 * Created by wuraolasonubi on 11/1/17.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class FromTo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_to2);}

public void onClick (View view ){

        Intent place = new Intent(this, PlaceActivity.class);
        startActivity(place);

        }

}

