package com.ds.DukeStudy.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ds.DukeStudy.activities.MainActivity;
import com.ds.DukeStudy.activities.NewEventActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.adapters.EventViewHolder;
import com.ds.DukeStudy.items.Database;
import com.ds.DukeStudy.items.Event;
import com.ds.DukeStudy.items.Student;
import com.ds.DukeStudy.items.Util;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

//This is an events fragment that retrieves events listed and displays them in a list for a given
//course

public class EventsListFragment extends Fragment {

    // Fields

    public static final String TAG = "EventsListFragment";
    public static final String DB_PATH_ARG = "dbKey";
    private String dbPath;
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
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Log.i(TAG, "Querying events from " + dbPath);
        Query eventsQuery = dbRef.limitToFirst(100);
        mAdapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(Event.class, R.layout.item_course, EventViewHolder.class, eventsQuery) {
            @Override
            protected void populateViewHolder(final EventViewHolder viewHolder, final Event event, final int position) {
                final String key = getRef(position).getKey();

                // Set listener for button
                viewHolder.toggleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        student.put(event);
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