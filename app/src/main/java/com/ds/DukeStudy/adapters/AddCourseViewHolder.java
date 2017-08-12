package com.ds.DukeStudy.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.items.Course;

public class AddCourseViewHolder extends RecyclerView.ViewHolder {

    // Fields

    private static final String TAG = "courseViewHolder";
    public TextView titleView, bodyView;
    public ToggleButton toggleBtn;

    // Methods

    public AddCourseViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.course_title);
        bodyView = (TextView) itemView.findViewById(R.id.course_body);
        toggleBtn = (ToggleButton) itemView.findViewById(R.id.toggle_btn);
    }

    public void bindToCourse(Course course, Boolean checked) {
        Log.i(TAG, "BINDING COURSE");
        titleView.setText(course.getDepartment() + " " + course.getCode() + ": " + course.getTitle());
        bodyView.setText("Students: " + course.getStudentKeys().size());
        toggleBtn.setChecked(checked);
    }
}
