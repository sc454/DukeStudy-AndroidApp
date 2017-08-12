package com.ds.DukeStudy.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.items.Course;
import com.ds.DukeStudy.items.Database;
import com.ds.DukeStudy.items.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class CourseDetailActivity extends AppCompatActivity {

    // Fields

    private static final String TAG = "CourseDetailActivity";
    private static final String COURSE_KEY_ARG = "courseKey";
    private String courseKey;
    private TextView titleView, instructorView, timeView, locationView;

    // Constructors

    public static void start(Context context, String key) {
        Intent intent = new Intent(context, CourseDetailActivity.class);
        intent.putExtra(COURSE_KEY_ARG, key);
        context.startActivity(intent);
    }

    // Other methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        setTitle("Course Details");

        // Get arguments
        courseKey = getIntent().getStringExtra(COURSE_KEY_ARG);
        if (courseKey == null) {
            throw new IllegalArgumentException("Must pass " + COURSE_KEY_ARG);
        }

        // Get view items
        titleView = (TextView) findViewById(R.id.course_title_text);
        instructorView = (TextView) findViewById(R.id.course_instructor_text);
        timeView = (TextView) findViewById(R.id.course_time_text);
        locationView = (TextView) findViewById(R.id.course_location_text);

        // Load course
        Database.ref.child(Util.COURSE_ROOT).child(courseKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Course course = dataSnapshot.getValue(Course.class);
                if (course == null) {
                    Log.e(TAG, "Course is unexpectedly null");
                    Toast.makeText(CourseDetailActivity.this, "Error: Could not fetch course.", Toast.LENGTH_SHORT).show();
                } else {
                    titleView.setText(course.getDepartment() + " " + course.getCode() + ": " + course.getTitle());
                    instructorView.setText(course.getInstructor());
                    timeView.setText(course.getStartTime() + " to " + course.getEndTime());
                    locationView.setText(course.getLocation());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "OnDataChangeCancelled: loadCourse", databaseError.toException());
            }
        });
    }
}
