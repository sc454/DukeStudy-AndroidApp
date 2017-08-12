package com.ds.DukeStudy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.items.Database;
import com.ds.DukeStudy.items.Post;
import com.ds.DukeStudy.items.Student;
import com.ds.DukeStudy.items.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class NewPostActivity extends AppCompatActivity {

    // Fields

    private static final String TAG = "NewPostActivity";
    private static final String DB_PATH = "dbPath";

    private String path;
    private EditText titleField, bodyField;
    private FloatingActionButton submitBtn;

    // Methods

    public static void start(Context context, String path) {
        Intent intent = new Intent(context, NewPostActivity.class);
        intent.putExtra(DB_PATH, path);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        setTitle("New Post");

        // Get arguments
        path = getIntent().getStringExtra(DB_PATH);
        if (path == null) {
            throw new IllegalArgumentException("Must pass " + DB_PATH);
        }

        // Set view
        titleField = (EditText) findViewById(R.id.field_title);
        bodyField = (EditText) findViewById(R.id.field_body);
        submitBtn = (FloatingActionButton) findViewById(R.id.fab_submit_post);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void submitPost() {
        final String title = titleField.getText().toString();
        final String body = bodyField.getText().toString();

        // Require title
        if (!Util.validateString(title, titleField)) return;
        if (!Util.validateString(body, bodyField)) return;

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        //
        final String uid = Database.getUser().getUid();
        Database.ref.child(Util.STUDENT_ROOT).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "OnDataChange: loadStudent");
                Student user = dataSnapshot.getValue(Student.class);
                if (user == null) {
                    Log.e(TAG, "User " + uid + " is unexpectedly null");
                    Toast.makeText(NewPostActivity.this, "Error: Could not fetch student.", Toast.LENGTH_SHORT).show();
                } else {
                    Post post = new Post(title, body, user.getName());
                    post.put(path);
                }
                setEditingEnabled(true);
                finish();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "OnDataChangeCancelled: loadStudent", databaseError.toException());
                setEditingEnabled(true);
            }
        });
    }

    private void setEditingEnabled(boolean enabled) {
        titleField.setEnabled(enabled);
        bodyField.setEnabled(enabled);
        if (enabled) {
            submitBtn.setVisibility(View.VISIBLE);
        } else {
            submitBtn.setVisibility(View.GONE);
        }
    }
}