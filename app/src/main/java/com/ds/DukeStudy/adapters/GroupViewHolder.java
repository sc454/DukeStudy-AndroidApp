package com.ds.DukeStudy.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.items.Group;

public class GroupViewHolder extends RecyclerView.ViewHolder {

    // Fields

    private static final String TAG = "GroupViewHolder";
    public TextView titleView, bodyView;
    public ToggleButton toggleBtn;

    // Methods

    public GroupViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.course_title);
        bodyView = (TextView) itemView.findViewById(R.id.course_body);
        toggleBtn = (ToggleButton) itemView.findViewById(R.id.toggle_btn);
    }

    public void bindToGroup(Group group, Boolean checked) {
        Log.i(TAG, "Binding group");
        titleView.setText(group.getName());
        bodyView.setText("Members: " + group.getStudentKeys().size());
        toggleBtn.setChecked(checked);
    }
}
