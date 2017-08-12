package com.ds.DukeStudy.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.items.Event;

public class EventViewHolder extends RecyclerView.ViewHolder {

    // Fields

    private static final String TAG = "EventViewHolder";
    public TextView titleView, bodyView;
    public ToggleButton toggleBtn;

    // Methods

    public EventViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.course_title);
        bodyView = (TextView) itemView.findViewById(R.id.course_body);
        toggleBtn = (ToggleButton) itemView.findViewById(R.id.toggle_btn);
    }

    public void bindToEvent(Event event, Boolean checked) {
        Log.i(TAG, "Binding Event");
        String info = event.getDate() + " at " + event.getTime() + " in " + event.getLocation();
        String count = "Attendees: " + event.getStudentKeys().size();
        titleView.setText(event.getTitle());
        bodyView.setText(info + "\n" + count);
        toggleBtn.setChecked(checked);
    }
}
