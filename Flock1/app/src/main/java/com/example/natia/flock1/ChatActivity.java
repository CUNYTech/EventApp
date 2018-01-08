package com.example.natia.flock1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.firebase.ui.database.FirebaseListAdapter;
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

import Model.Customer;
import Model.Show_Chat_Conversation_Data_Items;

/**
 * Created by napti on 11/13/2017.
 */

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseListAdapter<Show_Chat_Conversation_Data_Items> adapter;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mUserReference;
    private FirebaseUser mUser;
    private Customer customer = new Customer();
    private StorageReference mFirebaseStorage = FirebaseStorage.getInstance().getReference();
    private ImageView messageImage;
    private TextView messageText;
    private TextView messageTime;
    private TextView messageUser;
    private String userId;
    private String picname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUser = mAuth.getCurrentUser();
        userId = mUser.getUid();
        //Creates a new database which will hold our users
        mDatabaseReference = mDatabase.getReference().child("Chat");
        mUserReference = mDatabase.getReference().child("MUsers").child(userId).child("image");

        if(mUser == null) {
            // Start sign in/sign up activity
            onBackPressed();

        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            mUserReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    // Load chat room contents
                    displayChatMessages(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }


        setContentView(R.layout.activity_chat_main);



        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
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
                                                .getDisplayName(), dataSnapshot.getValue(String.class)));


                        // Clear the input
                        input.setText("");

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    private void displayChatMessages(DataSnapshot dataSnapshot) {
        ListView listOfMessages = findViewById(R.id.list_of_messages);
        listOfMessages.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        //listOfMessages.setStackFromBottom(true);

        adapter = new FirebaseListAdapter<Show_Chat_Conversation_Data_Items>(this,
                Show_Chat_Conversation_Data_Items.class,
                R.layout.show_chat_layout, mDatabaseReference) {
            @Override
            protected void populateView(View v, Show_Chat_Conversation_Data_Items model,
                                        int position) {
                // Get references to the views of message.xml
                messageText = v.findViewById(R.id.chat_message);
                messageUser = v.findViewById(R.id.chat_user);
                messageTime = v.findViewById(R.id.chat_date);

                messageImage = v.findViewById(R.id.chat_image);
                
                // Get references to the views of message.xml
                //TextView messageText = v.findViewById(R.id.chat_person_text);
                //TextView messageUser = v.findViewById(R.id.chat_person_name);
                //TextView messageTime = v.findViewById(R.id.chat_person_time);
                //ImageView messageImage = v.findViewById(R.id.chat_person_image);

                // Set their text
                messageText.setText(model.getMessage());
                messageUser.setText(model.getSender());
                picname = model.getSenderPic().substring(model.getSenderPic().lastIndexOf("/")+1);
                //messageImage.setImageURI(Uri.parse(picname));

                mFirebaseStorage.child("MFlock_Profile_Pics/MFlock_Profile_Pics/"+picname).getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>(){
                            @Override
                            public void onSuccess(Uri uri) {
                                GlideApp.
                                        with(getApplicationContext()).
                                        load(uri).
                                        error(R.mipmap.flock_icon).
                                        diskCacheStrategy(DiskCacheStrategy.NONE).
                                        transforms(new CircleCrop()).
                                        into(messageImage);

                            }
                        }).addOnFailureListener(new OnFailureListener()

                            {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    //Log.d(TAG, "fail to retrive imageDL url");
                                }
                            });


                //Log.d("messageUser",messageUser.toString());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("MM-dd-yyyy (HH:mm:ss)",
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