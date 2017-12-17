package com.example.natia.flock1;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import Model.Events;

/**
 * Created by napti on 12/11/2017.
 */

public class EventsAdapter extends FirebaseRecyclerAdapter<Events, EventFirebaseHolder> {

    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list.
     *                        You will be responsible for populating an instance of the corresponding
     *                        view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */

    private Context context;

    public EventsAdapter(Class<Events> modelClass, int modelLayout, Class<EventFirebaseHolder> viewHolderClass,
                         DatabaseReference ref, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }

    @Override
    protected void populateViewHolder(EventFirebaseHolder viewHolder, Events model, int position) {
        viewHolder.startTitle.setText("Start:");
        viewHolder.start.setText(model.getStart());
        viewHolder.endTitle.setText("End:");
        viewHolder.end.setText(model.getDestination());
        viewHolder.timeTitle.setText("Time:");
        viewHolder.time.setText(model.getTime());
        viewHolder.dateTitle.setText("Date:");
        viewHolder. date.setText(model.getDate());
        viewHolder.linesTitle.setText("Lines:");
        viewHolder.lines.setText(model.getLines().toString());
        viewHolder.nameTitle.setText("Created by:");
        viewHolder.name.setText(model.getName());
        Glide.with(context).load(model.getImage()).into(viewHolder.userImage);
    }
}
