package com.ds.DukeStudy.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.NewEventActivity;
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

import java.util.ArrayList;

//This is an events fragment that retrieves events listed and displays them in a list for a given
    //course
public class EventsListFragment extends Fragment {

    // Fields

    public static final String GROUP_KEY_ARG = "groupKey";
    private FirebaseListAdapter<String> listAdapter;
    private ListView listView;
    private Drawable plusIcon, minusIcon;
    private String groupKey;
    private Student student;
    private DatabaseReference eventKeysRef;
    private FloatingActionButton newEventBtn;

    // Constructors

    public EventsListFragment() {}

    public static EventsListFragment newInstance(String groupKey) {
        EventsListFragment fragment = new EventsListFragment();
        Bundle args = new Bundle();
        args.putString(GROUP_KEY_ARG, groupKey);
        fragment.setArguments(args);
        return fragment;
    }

    // Other methods

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_events_list, container, false);

        // Get arguments
        groupKey = getArguments().getString(GROUP_KEY_ARG);
        if (groupKey == null) {
            throw new IllegalArgumentException("Must pass " + GROUP_KEY_ARG);
        }

        // Get view items
        student = ((MainActivity) getActivity()).getStudent();
        listView = (ListView) view.findViewById(R.id.eventList);
        plusIcon = getResources().getDrawable(R.drawable.ic_menu_addclass);
        minusIcon = getResources().getDrawable(R.drawable.ic_indeterminate_check_box_black_24dp);
        eventKeysRef = Database.ref.child("groups").child(groupKey).child("eventKeys");

        // Create adapter to list all events
        listAdapter = new FirebaseListAdapter<String>(getActivity(), String.class, R.layout.general_row_view_btn, eventKeysRef) {
            protected void populateView(final View v, final String eventKey, final int position) {

                // Get view items
                DatabaseReference eventRef = Database.ref.child("events").child(eventKey);
                final TextView titleText = (TextView) v.findViewById(R.id.firstLine);
                final TextView detailsText = (TextView) v.findViewById(R.id.secondLine);
                final ImageButton toggleBtn = (ImageButton) v.findViewById(R.id.toggleButton);

                // Get event
                eventRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Event event = dataSnapshot.getValue(Event.class);

                        // Set title and details
                        titleText.setText("Date: " + event.getDate() + "  Time: " + event.getTime() + "");
                        detailsText.setText("@: " + event.getLocation() + "  #Going: " + Integer.toString(event.getStudentKeys().size()));

                        // Set icon
                        Boolean isMember = event.getStudentKeys().contains(student.getKey());
                        toggle(toggleBtn, isMember);

                        // Toggle on click
                        toggleBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                toggle(event);
                            }
                        });
                    }
//                        if (event.getStudentKeys().contains(student.getKey())) {
//                            if (isAdded()) {
//                                toggleBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_indeterminate_check_box_black_24dp));
//                            }
//                            v.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
////                                    //If the event is clicked on either add or remove your student key from it
////                                    event.removeStudent(student.getKey());
////                                    eventsRef.child(event.getKey()).setValue(event);
////                                    student.removeEventKey(event.getKey());
////                                    student.put();
//                                }
//                            });
//                            toggleBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    event.removeStudent(student.getKey());
//                                    eventsRef.child(event.getKey()).setValue(event);
//                                    student.removeEventKey(event.getKey());
//                                    student.put();
//                                }
//                            });
//                        } else {
//                            if (isAdded())
//                                toggleBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_addclass));
//                            v.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
////                                    event.addStudent(student.getKey());
////                                    eventsRef.child(event.getKey()).setValue(event);
////                                    student.addEventKey(event.getKey());
////                                    student.put();
//                                }
//                            });
//                            toggleBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    //If the event is clicked on either add or remove your student key from it
//                                    event.removeStudent(student.getKey());
//                                    eventsRef.child(event.getKey()).setValue(event);
//                                    student.removeEventKey(event.getKey());
//                                    student.put();
//                                }
//                            });
//                        }
//
//                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        };
        listView.setAdapter(listAdapter);

        // New event button
        newEventBtn = (FloatingActionButton) view.findViewById(R.id.fab_new_event);
        newEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewEventActivity.start(getActivity(), groupKey);
            }
        });

        return view;
    }

    public void toggle(ImageButton button, Boolean isChecked) {
        if (isChecked) {
            button.setImageDrawable(minusIcon);
        } else {
            button.setImageDrawable(plusIcon);
        }
    }

    public void toggle(Event event) {
        ArrayList<String> groupKeys = student.getEventKeys();
        String key = event.getKey();
        if (groupKeys.contains(key)) {
            //remove
            student.removeGroupKey(key);
            event.removeStudentKey(student.getKey());
            student.put(); event.put();
        } else {
            //add
            student.addGroupKey(key);
            event.addStudentKey(student.getKey());
            student.put(); event.put();
        }
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
