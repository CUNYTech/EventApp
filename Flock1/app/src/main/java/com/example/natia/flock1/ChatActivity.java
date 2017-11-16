package com.example.natia.flock1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Customer;

/**
 * Created by napti on 11/13/2017.
 */

public class ChatActivity extends AppCompatActivity {

    TextView person_name,person_email;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    public FirebaseRecyclerAdapter<Customer, Show_Chat_ViewHolder> mFirebaseAdapter;
    ProgressBar progressBar;
    LinearLayoutManager mLinearLayoutManager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_chat_layout);

        firebaseDatabase = FirebaseDatabase.getInstance();

        myRef = firebaseDatabase.getReference().child("Chat");
        //myRef.keepSynced(true);


        progressBar = findViewById(R.id.show_chat_progressBar2);

        //Recycler View
        recyclerView = findViewById(R.id.show_chat_recyclerView);

        mLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        //mLinearLayoutManager.setStackFromEnd(true);
        Log.d("1st", "Will Start Calling populateViewHolder : ");

        recyclerView.setLayoutManager(mLinearLayoutManager);
    }

    @Override
    public void onStart() {
        super.onStart();
        progressBar.setVisibility(ProgressBar.VISIBLE);



        mFirebaseAdapter = new FirebaseRecyclerAdapter<Customer, Show_Chat_ViewHolder>
                                                    (Customer.class,
                                                        R.layout.show_chat_single_item,
                                                        Show_Chat_ViewHolder.class,
                                                        myRef) {

            public void populateViewHolder(final Show_Chat_ViewHolder viewHolder,
                                           Customer model,
                                           final int position) {
                Log.d("LOGGED", "populateViewHolder Called: ");
                progressBar.setVisibility(ProgressBar.INVISIBLE);

                if (!model.getfirstName().equals("Null")) {
                    Log.d("LOGGED", "populating ViewHolder: ");
                    viewHolder.Person_Name(model.getfirstName());
                    viewHolder.Person_Image(model.getImage());
                    //viewHolder.Person_Email(model.getEmail());

                    if(model.getemail().equals(MainActivity.LoggedIn_User_Email)||
                            model.getemail().equals(splash_page.LoggedIn_User_Email))
                    {
                        //viewHolder.itemView.setVisibility(View.GONE);
                        viewHolder.Layout_hide();

                        //recyclerView.getChildAdapterPosition(viewHolder.itemView.getRootView());
                        // viewHolder.itemView.set;


                    }
                    else
                        viewHolder.Person_Email(model.getemail());
                }


                //OnClick Item
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        DatabaseReference ref = mFirebaseAdapter.getRef(position);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String retrieve_name = dataSnapshot.child("firstName").getValue(String.class);
                                String retrieve_Email = mAuth.getCurrentUser().getEmail();
                                String retrieve_url = dataSnapshot.child("image").getValue(String.class);



                                Intent intent = new Intent(getApplicationContext(), ChatConversationActivity.class);
                                intent.putExtra("image_id", retrieve_url);
                                intent.putExtra("email", retrieve_Email);
                                intent.putExtra("name", retrieve_name);

                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        };

        recyclerView.setAdapter(mFirebaseAdapter);





    }


    //View Holder For Recycler View
    public class Show_Chat_ViewHolder extends RecyclerView.ViewHolder {
        private final TextView person_name, person_email;
        private final ImageView person_image;
        private final LinearLayout layout;
        final LinearLayout.LayoutParams params;

        public Show_Chat_ViewHolder(final View itemView) {
            super(itemView);
            person_name = itemView.findViewById(R.id.chat_persion_name);
            person_email = itemView.findViewById(R.id.chat_persion_email);
            person_image =  itemView.findViewById(R.id.chat_persion_image);
            layout = itemView.findViewById(R.id.show_chat_single_item_layout);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }


        void Person_Name(String title) {
            // Log.d("LOGGED", "Setting Name: ");
            person_name.setText(title);
        }
        void Layout_hide() {
            params.height = 0;
            //itemView.setLayoutParams(params);
            layout.setLayoutParams(params);

        }


        void Person_Email(String title) {
            person_email.setText(title);
        }


            void Person_Image(String url) {

                if (!url.equals("Null")) {
                    Glide.with(itemView.getContext())
                            .load(url)
                            //.crossFade()
                            .thumbnail(0.5f)
                            //.placeholder(R.drawable.loading)
                            //.bitmapTransform(new CircleTransform(itemView.getContext()))
                            //.diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(person_image);
                }

            }


    }

}
