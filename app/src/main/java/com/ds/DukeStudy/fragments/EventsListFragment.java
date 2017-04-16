package com.ds.DukeStudy.fragments;

import android.app.DatePickerDialog;

import android.app.TimePickerDialog;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.NewEventActivity;
import com.ds.DukeStudy.NewGroupActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Event;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Student;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

//This is an events fragment that retrieves events listed and displays them in a list for a given

public class EventsListFragment extends Fragment {

    public static final String GROUP_KEY_ARG = "groupKey";

    private DatabaseReference databaseRef;
    private FirebaseListAdapter<String> adapter1;
    private ListView eventsListView;
//    private String sourceID;
    private FirebaseUser user;
    private String groupKey;
    private Student student;
    private FloatingActionButton newEventBtn;

    public EventsListFragment() {}

    public static EventsListFragment newInstance(String groupKey) {
        EventsListFragment fragment = new EventsListFragment();
        Bundle args = new Bundle();
        args.putString(GROUP_KEY_ARG, groupKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Get arguments
        groupKey = getArguments().getString(GROUP_KEY_ARG);
        if (groupKey == null) {
            throw new IllegalArgumentException("Must pass " + GROUP_KEY_ARG);
        }

        student = ((MainActivity) EventsListFragment.this.getActivity()).getStudent();

        final View view = inflater.inflate(R.layout.events_layout, container, false);

        eventsListView = (ListView) view.findViewById(R.id.eventsListView);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference curGroupsRef = Database.ref.child("groups").child(groupKey).child("eventKeys");
        final DatabaseReference eventsRef = Database.ref.child("events");

        // New post button
        newEventBtn = (FloatingActionButton) view.findViewById(R.id.fab_new_event);
        newEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewEventActivity.start(getActivity(), groupKey);
            }
        });

        adapter1 = new FirebaseListAdapter<String>(getActivity(), String.class, R.layout.cutom_row_view_layout, curGroupsRef) {
            protected void populateView(final View v, final String model, final int position) {
                //Get reference to particular student in database
                DatabaseReference curStudentRef = eventsRef.child(model);
                final TextView mytext1 = (TextView) v.findViewById(R.id.firstline);
                final TextView mytext2 = (TextView) v.findViewById(R.id.secondline);
                final ImageButton mybutton = (ImageButton) v.findViewById(R.id.adddeletebutton);
                curStudentRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Event curEvent = dataSnapshot.getValue(Event.class);
                        mytext1.setText("Date: " + curEvent.getDate() + "  Time: " + curEvent.getTime() + "");

                        mytext1.setText("Date: " + curEvent.getDate() + "  Time: " + curEvent.getTime() + "");
                        mytext2.setText("@: " + curEvent.getLocation() + "  #Going: " + Integer.toString(curEvent.getStudentKeys().size()));

                        if (curEvent.getStudentKeys().contains(student.getKey())) {
                            if (isAdded()) {
                                mybutton.setImageDrawable(getResources().getDrawable(R.drawable.ic_indeterminate_check_box_black_24dp));
                            }
                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
//                                    //If the event is clicked on either add or remove your student key from it
//                                    curEvent.removeStudent(student.getKey());
//                                    eventsRef.child(curEvent.getKey()).setValue(curEvent);
//                                    student.removeEventKey(curEvent.getKey());
//                                    student.put();
                                }
                            });
                            mybutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    curEvent.removeStudent(student.getKey());
                                    eventsRef.child(curEvent.getKey()).setValue(curEvent);
                                    student.removeEventKey(curEvent.getKey());
                                    student.put();
                                }
                            });
                        } else {
                            if (isAdded())
                                mybutton.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_addclass));
                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
//                                    curEvent.addStudent(student.getKey());
//                                    eventsRef.child(curEvent.getKey()).setValue(curEvent);
//                                    student.addEventKey(curEvent.getKey());
//                                    student.put();
                                }
                            });
                            mybutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //If the event is clicked on either add or remove your student key from it
                                    curEvent.removeStudent(student.getKey());
                                    eventsRef.child(curEvent.getKey()).setValue(curEvent);
                                    student.removeEventKey(curEvent.getKey());
                                    student.put();
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        eventsListView.setAdapter(adapter1);
        return view;
    }
}

//    //Code from http://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
//    final Calendar myCalendar = Calendar.getInstance();
//    final EditText dateTimeText = (EditText) view.findViewById(R.id.eventDateTimeText);
//    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear,
//                              int dayOfMonth) {
//            // TODO Auto-generated method stub
//            myCalendar.set(Calendar.YEAR, year);
//            myCalendar.set(Calendar.MONTH, monthOfYear);
//            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//            updateLabel(dateTimeText, myCalendar);
//        }
//
//    };
//        dateTimeText.setOnTouchListener(new View.OnTouchListener() {
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                new DatePickerDialog(view.getContext(), date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//            return true;
//        }
//    });
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
//        return view;
//}
//
//    private void updateLabel(EditText myeditText, Calendar myCal) {
//
//        String myFormat = "MM/dd/yy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//        myeditText.setText(sdf.format(myCal.getTime()));
//    }
