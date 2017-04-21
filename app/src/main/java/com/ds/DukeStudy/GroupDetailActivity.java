package com.ds.DukeStudy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.DukeStudy.objects.Course;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class GroupDetailActivity extends AppCompatActivity {

    // Fields

    private static final String TAG = "GroupDetailActivity";
    private static final String GROUP_KEY_ARG = "groupKey";
    private String groupKey;
    private TextView titleView, descriptionView;

    // Constructors

    public static void start(Context context, String key) {
        Intent intent = new Intent(context, GroupDetailActivity.class);
        intent.putExtra(GROUP_KEY_ARG, key);
        context.startActivity(intent);
    }

    // Other methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        setTitle("Group Details");

        // Get arguments
        groupKey = getIntent().getStringExtra(GROUP_KEY_ARG);
        if (groupKey == null) {
            throw new IllegalArgumentException("Must pass " + GROUP_KEY_ARG);
        }

        // Get view items
        titleView = (TextView) findViewById(R.id.group_title_text);
        descriptionView = (TextView) findViewById(R.id.group_description_text);

        // Load course
        Database.ref.child(Util.GROUP_ROOT).child(groupKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Group group = dataSnapshot.getValue(Group.class);
                if (group == null) {
                    Log.e(TAG, "Course is unexpectedly null");
                    Toast.makeText(GroupDetailActivity.this, "Error: Could not fetch course.", Toast.LENGTH_SHORT).show();
                } else {
                    titleView.setText(group.getName());
                    descriptionView.setText(group.getDescription());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "OnDataChangeCancelled: loadCourse", databaseError.toException());
            }
        });
    }
}
