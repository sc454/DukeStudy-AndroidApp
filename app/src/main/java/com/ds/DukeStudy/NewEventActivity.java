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

    private static final String TAG = "NewGroupActivity";
    private static final String GROUP_KEY_ARG = "groupKey";

    private String groupKey;
    private EditText titleField, dateField, timeField, locationField;
    private FloatingActionButton submitBtn;

    // Methods

    public static void start(Context context, String groupKey) {
        Intent intent = new Intent(context, NewEventActivity.class);
        intent.putExtra(GROUP_KEY_ARG, groupKey);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get arguments
        groupKey = getIntent().getStringExtra(GROUP_KEY_ARG);
        if (groupKey == null) {
            throw new IllegalArgumentException("Must pass " + GROUP_KEY_ARG);
        }

//        final View view = inflater.inflate(R.layout.groupslist_layout,null);

        setContentView(R.layout.activity_new_event);
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

//        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel(dateTimeText, myCalendar);
//            }
//
//        };
//
//        dateTimeText.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    new DatePickerDialog(view.getContext(), date, myCalendar
//                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                }
//                return true;
//            }
//        });
    }

    private void submitEvent() {
        final String title = titleField.getText().toString();

        // Require fields
        if (!Util.validateString(title, titleField)) return;

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        //
        final String userId = getUid();
        Database.ref.child("courses").child(groupKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Group group = dataSnapshot.getValue(Group.class);
//                if (group == null) {
//                    Log.e(TAG, "Group " + groupKey + " is unexpectedly null");
//                    Toast.makeText(NewEventActivity.this, "Error: Could not fetch user.", Toast.LENGTH_SHORT).show();
//                } else {
////                    // Create group
////                    Course course = new Group(title);
////                    course.put();
////                    // Add to course
////                    group.addGroupKey(group.getKey());
////                    group.put();
////                    // Add student
//                }
//                setEditingEnabled(true);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
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

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void updateLabel(EditText editText, Calendar myCal) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCal.getTime()));
    }
}
    //Code from http://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
//    final Calendar myCalendar = Calendar.getInstance();
//    final EditText dateTimeText = (EditText) view.findViewById(R.id.eventDateTimeText);
//
//    final EditText locationText=(EditText) view.findViewById(R.id.locationEntry);
//    final EditText timeText=(EditText) view.findViewById(R.id.timeEntry);
//        timeText.setOnTouchListener(new View.OnTouchListener() {
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            Calendar mcurrentTime = Calendar.getInstance();
//            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//            int minute = mcurrentTime.get(Calendar.MINUTE);
//            TimePickerDialog mTimePicker;
//            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                    timeText.setText( selectedHour + ":" + selectedMinute);
//                }
//            }, hour, minute, true);//Yes 24 hour time
//            mTimePicker.setTitle("Select Time");
//            mTimePicker.show();
//            return true;
//        }
//
//    });
//    //Set the onClick to add a new event
//    Button addEventButton=(Button) view.findViewById(R.id.addeventbutton);
//        addEventButton.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            //Creating firebase object
//            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//            //database.child("note").push().setValue(usernameEdit.getText().toString());
//            final Event event = new Event(dateTimeText.getText().toString(), timeText.getText().toString(),locationText.getText().toString());
//            event.addStudent(student.getKey());
//            event.put();
//            //Need to add the event key to eventKeys for the group
//            final DatabaseReference curGroupsRef=databaseRef.child("groups").child(groupKey);
//            curGroupsRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    final Group curObj= dataSnapshot.getValue(Group.class);
//                    curObj.addEventKey(event.getKey());
//                    curGroupsRef.setValue(curObj);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//        }
//    });
//
//
//}


