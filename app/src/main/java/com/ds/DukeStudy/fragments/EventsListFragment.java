package com.ds.DukeStudy.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.NewEventActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.misc.AddCourseViewHolder;
import com.ds.DukeStudy.misc.EventViewHolder;
import com.ds.DukeStudy.objects.Course;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Event;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Student;
import com.ds.DukeStudy.objects.Util;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//This is an events fragment that retrieves events listed and displays them in a list for a given
    //course
public class EventsListFragment extends Fragment {

    // Fields

    public static final String TAG = "EventsListFragment";
    public static final String DB_PATH_ARG = "dbKey";
//    private FirebaseListAdapter<String> listAdapter;
//    private ListView listView;
//    private Drawable plusIcon, minusIcon;
    private String dbPath;
//    private Student student;
//    private DatabaseReference eventKeysRef;
    private FloatingActionButton newEventBtn;

    private Student student;
    private DatabaseReference dbRef;
    private FirebaseRecyclerAdapter<Event,EventViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    // Constructors

    public EventsListFragment() {}

    public static EventsListFragment newInstance(String path) {
        EventsListFragment fragment = new EventsListFragment();
        Bundle args = new Bundle();
        args.putString(DB_PATH_ARG, path);
        fragment.setArguments(args);
        return fragment;
    }

    // Other methods

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_groups_list, container, false);

        // Get arguments
        dbPath = getArguments().getString(DB_PATH_ARG);
        if (dbPath == null) {
            throw new IllegalArgumentException("Must pass " + DB_PATH_ARG);
        }

        // Get view items
        dbRef = Database.ref.child(Util.EVENT_ROOT).child(dbPath);
        student = ((MainActivity)getActivity()).getStudent();
        mRecycler = (RecyclerView) view.findViewById(R.id.courses_list);
        mRecycler.setHasFixedSize(true);

        // New event button
        newEventBtn = (FloatingActionButton) view.findViewById(R.id.fab_new_group);
        newEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Starting newEventActivity wit arg " + dbPath);
                NewEventActivity.start(getActivity(), dbPath);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
//        mManager.setReverseLayout(true);
//        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Log.i(TAG, "Querying events from " + dbPath);
        Query eventsQuery = dbRef.limitToFirst(100);
//        Query eventsQuery = dbRef.orderByChild("department");
        mAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(Event.class, R.layout.item_course, EventViewHolder.class, eventsQuery) {
            @Override
            protected void populateViewHolder(final EventViewHolder viewHolder, final Event event, final int position) {
                final String key = getRef(position).getKey();

                // Set listener for button
                viewHolder.toggleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        student.toggleAndPut(event);
                    }
                });

                // Set listener for view holder
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        EventDetailActivity.start(getActivity(), course.getKey());
//                    }
//                });

                // Bind view to course
                Boolean attending = student.getEventKeys().contains(event.getKey());
                viewHolder.bindToEvent(event, attending);
            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

}

//    public void toggle(ImageButton button, Boolean isChecked) {
//        if (isChecked) {
//            button.setImageDrawable(minusIcon);
//        } else {
//            button.setImageDrawable(plusIcon);
//        }
//    }
//
//    public void toggle(Event event) {
//        ArrayList<String> groupKeys = student.getEventKeys();
//        String key = event.getKey();
//        if (groupKeys.contains(key)) {
//            //remove
//            student.removeEventKey(key);
//            event.removeStudentKey(student.getKey());
//            student.put(); event.put();
//        } else {
//            //add
//            student.addEventKey(key);
//            event.addStudentKey(student.getKey());
//            student.put(); event.put();
//        }
//    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        final View view = inflater.inflate(R.layout.fragment_events_list, container, false);
//
//        // Get arguments
//        groupKey = getArguments().getString(GROUP_KEY_ARG);
//        if (groupKey == null) {
//            throw new IllegalArgumentException("Must pass " + GROUP_KEY_ARG);
//        }
//
//        // Get view items
//        student = ((MainActivity) getActivity()).getStudent();
//        listView = (ListView) view.findViewById(R.id.eventList);
//        plusIcon = getResources().getDrawable(R.drawable.ic_menu_addclass);
//        minusIcon = getResources().getDrawable(R.drawable.ic_indeterminate_check_box_black_24dp);
//        eventKeysRef = Database.ref.child("groups").child(groupKey).child("eventKeys");
//
//        // Create adapter to list all events
//        listAdapter = new FirebaseListAdapter<String>(getActivity(), String.class, R.layout.general_row_view_btn, eventKeysRef) {
//            protected void populateView(final View v, final String eventKey, final int position) {
//
//                // Get view items
//                DatabaseReference eventRef = Database.ref.child("events").child(eventKey);
//                final TextView titleText = (TextView) v.findViewById(R.id.firstLine);
//                final TextView detailsText = (TextView) v.findViewById(R.id.secondLine);
//                final ImageButton toggleBtn = (ImageButton) v.findViewById(R.id.toggleButton);
//
//                // Get event
//                Log.i(TAG, "Populating view for " + eventKey);
//                eventRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Log.i(TAG, "OnDataChange: loadEvent");
//                        final Event event = dataSnapshot.getValue(Event.class);
//
//                        // Set title and details
//                        titleText.setText(event.getTitle());
//                        detailsText.setText(event.getDate() + " at " + event.getTime() + " in " + event.getLocation());
////                        detailsText.setText("Location: " + event.getLocation() + "\tAttendees: " + event.getStudentKeys().size());
//
//                        // Set icon
//                        Boolean isMember = event.getStudentKeys().contains(student.getKey());
//                        Log.i(TAG, "Event " + event.getTitle() + ": isMember " + isMember);
//                        toggle(toggleBtn, isMember);
//
//                        // Toggle on click
//                        toggleBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                toggle(event);
//                            }
//                        });
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.w(TAG, "OnDataChangeCancelled: loadEvent", databaseError.toException());
//                    }
//                });
//            }
//        };
//        listView.setAdapter(listAdapter);
//
//        // New event button
//        newEventBtn = (FloatingActionButton) view.findViewById(R.id.fab_new_event);
//        newEventBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NewEventActivity.start(getActivity(), groupKey);
//            }
//        });
//
//        return view;
//    }
