package com.ds.DukeStudy.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.items.Course;
import com.ds.DukeStudy.items.Database;
import com.ds.DukeStudy.items.Group;
import com.ds.DukeStudy.items.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class NewGroupActivity extends AppCompatActivity {

    // Fields

    private static final String TAG = "NewGroupActivity";
    private static final String COURSE_KEY_ARG = "courseKey";

    private String courseKey;
    private EditText nameField, descriptionField;
    private FloatingActionButton submitBtn;

    // Methods

    public static void start(Context context, String courseKey) {
        Intent intent = new Intent(context, NewGroupActivity.class);
        intent.putExtra(COURSE_KEY_ARG, courseKey);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        setTitle("New Group");

        // Get arguments
        courseKey = getIntent().getStringExtra(COURSE_KEY_ARG);
        if (courseKey == null) {
            throw new IllegalArgumentException("Must pass " + COURSE_KEY_ARG);
        }

        // Get fields
        nameField = (EditText) findViewById(R.id.field_title);
        descriptionField = (EditText) findViewById(R.id.field_description);
        submitBtn = (FloatingActionButton) findViewById(R.id.fab_submit_group);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitGroup();
            }
        });
    }

    private void submitGroup() {
        final String title = nameField.getText().toString();
        final String description = descriptionField.getText().toString();

        // Require fields
        if (!Util.validateString(title, nameField)) return;
        if (!Util.validateString(description, descriptionField)) return;

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // Add group to course
        final String userId = Database.getUser().getUid();
        Database.ref.child(Util.COURSE_ROOT).child(courseKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "OnDataChange: loadCourse");
                Course course = dataSnapshot.getValue(Course.class);
                if (course == null) {
                    Log.e(TAG, "Course " + courseKey + " is unexpectedly null");
                    Toast.makeText(NewGroupActivity.this, "Error: Could not fetch group.", Toast.LENGTH_SHORT).show();
                } else {
                    // Create group
                    Group group = new Group(title, description, courseKey);
                    group.put(courseKey);
                    // Add to course
                    course.addGroupKey(group.getKey());
                    course.put();
                }
                setEditingEnabled(true);
                finish();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "OnDataChangeCancelled: loadCourse", databaseError.toException());
                setEditingEnabled(true);
            }
        });
    }

    private void setEditingEnabled(boolean enabled) {
        nameField.setEnabled(enabled);
        if (enabled) {
            submitBtn.setVisibility(View.VISIBLE);
        } else {
            submitBtn.setVisibility(View.GONE);
        }
    }
}
