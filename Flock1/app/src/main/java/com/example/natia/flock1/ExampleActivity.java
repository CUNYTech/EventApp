package com.example.natia.flock1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by napti on 11/13/2017.
 */

public class ExampleActivity extends Activity {

    private Button button;
    private EditText input;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private String email;
    //private String fullName;
    //private String userid;
    //private Uri image;
    //private String imagePath;
    //private StorageReference mFirebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        button = findViewById(R.id.button3);
        input = findViewById(R.id.editText);
        email = input.getText().toString().trim();
        Log.d("crazy", email);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExampleActivity.this, OtherProfile.class);

                intent.putExtra("Other Email", email);
                Log.e("crazy", email);
                startActivity(intent);


            }
        });
    }
}
