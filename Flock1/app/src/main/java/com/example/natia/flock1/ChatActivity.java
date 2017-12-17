package com.example.natia.flock1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.Show_Chat_Conversation_Data_Items;

/**
 * Created by napti on 11/13/2017.
 */

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseListAdapter<Show_Chat_Conversation_Data_Items> adapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUser = mAuth.getCurrentUser();
        //Creates a new database which will hold our users
        mDatabaseReference = mDatabase.getReference().child("Chat");


        setContentView(R.layout.activity_chat_main);

        if(mUser == null) {
            // Start sign in/sign up activity
            onBackPressed();

        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                    "Welcome " + mUser
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();

            // Load chat room contents
            displayChatMessages();
        }


        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.chat_input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                mDatabaseReference
                        .push()
                        .setValue(new Show_Chat_Conversation_Data_Items(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName())
                        );

                // Clear the input
                input.setText("");
            }
        });


    }

    private void displayChatMessages() {
        ListView listOfMessages = findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<Show_Chat_Conversation_Data_Items>(this,
                Show_Chat_Conversation_Data_Items.class,
                R.layout.message, mDatabaseReference) {
            @Override
            protected void populateView(View v, Show_Chat_Conversation_Data_Items model,
                                        int position) {
                // Get references to the views of message.xml
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                // Get references to the views of message.xml
                //TextView messageText = v.findViewById(R.id.chat_person_text);
                //TextView messageUser = v.findViewById(R.id.chat_person_name);
                //TextView messageTime = v.findViewById(R.id.chat_person_time);
                //ImageView messageImage = v.findViewById(R.id.chat_person_image);

                // Set their text
                messageText.setText(model.getMessage());
                messageUser.setText(model.getSender());
               // messageImage.setImageURI(Uri.parse(model.getUserImage()));
                //Log.d("messageUser",messageUser.toString());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        //listOfMessages.smoothScrollToPosition();
        listOfMessages.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChatActivity.this, MainHub.class);
        startActivity(intent);
    }
}