package com.ds.DukeStudy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ds.DukeStudy.objects.Course;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Event;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Student;
import com.ds.DukeStudy.objects.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class NewEventActivity extends AppCompatActivity {


    // Fields

    private static final String TAG = "NewEventActivity";
    private static final String DB_PATH_ARG = "dbPath";

    private String groupKey; //deprecated
    private String dbPath;
    private Student student;
    private EditText titleField, dateField, timeField, locationField;
    private FloatingActionButton submitBtn;
    private Calendar cal;

    // Constructors

    public static void start(Context context, String path) {
        Intent intent = new Intent(context, NewEventActivity.class);
        intent.putExtra(DB_PATH_ARG, path);
        context.startActivity(intent);
    }

    // Other methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        // Get arguments
        dbPath = getIntent().getStringExtra(DB_PATH_ARG);
        if (dbPath == null) {
            throw new IllegalArgumentException("Must pass " + DB_PATH_ARG);
        }

        // Get view items
        loadStudent();
        cal = Calendar.getInstance();
        titleField = (EditText) findViewById(R.id.event_title);
        dateField = (EditText) findViewById(R.id.event_date);
        timeField = (EditText) findViewById(R.id.event_time);
        locationField = (EditText) findViewById(R.id.event_location);
        submitBtn = (FloatingActionButton) findViewById(R.id.fab_submit_event);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitEvent();
            }
        });

        // Set listeners

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dateField, cal);
            }
        };

        dateField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    new DatePickerDialog(view.getContext(), date, cal
                            .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }
        });

        timeField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Calendar currCal = Calendar.getInstance();
                    int hour = currCal.get(Calendar.HOUR_OF_DAY);
                    int minute = currCal.get(Calendar.MINUTE);
                    TimePickerDialog timePicker = new TimePickerDialog(NewEventActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    timeField.setText(selectedHour + ":" + selectedMinute);
                                }
                            }, hour, minute, false);//Yes 24 hour time
                    timePicker.setTitle("Select Time");
                    timePicker.show();
                }
                    return true;

            }
        });
    }

    private void submitEvent() {
        final String title = titleField.getText().toString();
        final String date = dateField.getText().toString();
        final String time = timeField.getText().toString();
        final String location = locationField.getText().toString();

        // Require fields
        if (!Util.validateString(title, titleField)) return;
        if (!Util.validateString(date, dateField)) return;
        if (!Util.validateString(time, timeField)) return;
        if (!Util.validateString(location, locationField)) return;

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // Add event to group
        final String uid = Database.getUser().getUid();
        Log.i(TAG, "Looking for group at " + dbPath);
        Database.ref.child(Util.GROUP_ROOT).child(dbPath).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "OnDataChange: loadGroup");
                Group group = dataSnapshot.getValue(Group.class);

                // Update group
                if (group == null) {
                    Log.e(TAG, "Group is unexpectedly null");
                    Toast.makeText(NewEventActivity.this, "Error: Could not fetch group.", Toast.LENGTH_SHORT).show();
                } else {
                    // Create event
                    Event event = new Event(title, date, time, location, dbPath);
                    event.addStudentKey(student.getKey());
//                    event.setKey(dbPath);
                    event.put(dbPath);
                    Log.i(TAG, "Stored event at " + event.getKey());
//                    event.put("events/" + dbPath);
                    // Add to group and student
                    group.addEventKey(event.getKey());
                    student.addEventKey(event.getKey());
                    group.put(); student.put();
                }
                setEditingEnabled(true);
                finish();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "OnDataChangeCancelled: loadGroup", databaseError.toException());
                setEditingEnabled(true);
            }
        });
    }

    private void setEditingEnabled(boolean enabled) {
        titleField.setEnabled(enabled);
        if (enabled) {
            submitBtn.setVisibility(View.VISIBLE);
        } else {
            submitBtn.setVisibility(View.GONE);
        }
    }

    private void updateLabel(EditText editText, Calendar cal) {
        String format = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editText.setText(sdf.format(cal.getTime()));
    }

    private void loadStudent() {
        final String uid = Database.getUser().getUid();
        Database.ref.child(Util.STUDENT_ROOT).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "OnDataChange: loadStudent");
                student = dataSnapshot.getValue(Student.class);
                if (student == null) {
                    Log.e(TAG, "Student " + uid + " is unexpectedly null");
                    Toast.makeText(NewEventActivity.this, "Error: Could not fetch student.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "OnDataChangeCancelled: loadStudent", databaseError.toException());
            }
        });
    }
}

