package com.example.natia.flock1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.Events;

/**
 * Created by napti on 12/4/2017.
 */

public class EventFirebaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context mContext;

    public EventFirebaseHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindEvents(Events events) {
        ImageView userImage = mView.findViewById(R.id.eventUserImage);
        TextView startTitle = mView.findViewById(R.id.event_start_title);
        TextView start = mView.findViewById(R.id.event_start);
        TextView endTitle = mView.findViewById(R.id.event_end_title);
        TextView end = mView.findViewById(R.id.event_end);
        TextView timeTitle = mView.findViewById(R.id.event_time_title);
        TextView time = mView.findViewById(R.id.event_time);
        TextView dateTitle = mView.findViewById(R.id.event_date_title);
        TextView date = mView.findViewById(R.id.event_date);
        TextView linesTitle = mView.findViewById(R.id.event_lines_title);
        TextView lines = mView.findViewById(R.id.event_lines);
        TextView nameTitle = mView.findViewById(R.id.event_name_title);
        TextView name = mView.findViewById(R.id.event_name);


        Picasso.with(mContext)
                .load(events.getImage())
                .centerCrop()
                .into(userImage);

        startTitle.setText("Start:");
        start.setText(events.getStart());
        endTitle.setText("End:");
        end.setText(events.getDestination());
        timeTitle.setText("Time:");
        time.setText(events.getTime());
        dateTitle.setText("Date:");
        date.setText(events.getDate());
        linesTitle.setText("Lines:");
        lines.setText(events.getLines().toString());
        nameTitle.setText("Created by:");
        name.setText(events.getName());
    }

    @Override
    public void onClick(View view) {
        final ArrayList<Events> events = new ArrayList<>();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Events2");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    events.add(snapshot.getValue(Events.class));
                }

                int itemPosition = getLayoutPosition();

                Intent intent = new Intent(mContext, OtherProfile.class);
                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}