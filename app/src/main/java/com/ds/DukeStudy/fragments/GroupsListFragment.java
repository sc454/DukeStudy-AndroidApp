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

import com.ds.DukeStudy.GroupDetailActivity;
import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.NewGroupActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.misc.GroupViewHolder;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Student;
import com.ds.DukeStudy.objects.Util;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

//This fragment loads groups lists for a particular course from the database and displays in listview.

public class GroupsListFragment extends Fragment {

    // Fields

    private static final String TAG = "GroupsListFragment";
    private static final String COURSE_KEY_ARG = "courseKey";
    private String courseKey;
    private FloatingActionButton newGroupBtn;

    private Student student;
    private DatabaseReference dbRef;
    private FirebaseRecyclerAdapter<Group,GroupViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    // Constructors

    public GroupsListFragment() {}

    public static GroupsListFragment newInstance(String key) {
        GroupsListFragment fragment = new GroupsListFragment();
        Bundle args = new Bundle();
        args.putString(COURSE_KEY_ARG, key);
        fragment.setArguments(args);
        return fragment;
    }

    // Other methods

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_groups_list, container, false);

        // Get arguments
        courseKey = getArguments().getString(COURSE_KEY_ARG);
        if (courseKey == null) {
            throw new IllegalArgumentException("Must pass " + COURSE_KEY_ARG);
        }

        // Get view items
        dbRef = Database.ref.child(Util.GROUP_ROOT).child(courseKey);
        student = ((MainActivity)getActivity()).getStudent();
        mRecycler = (RecyclerView) view.findViewById(R.id.courses_list);
        mRecycler.setHasFixedSize(true);

        // New group button
        newGroupBtn = (FloatingActionButton) view.findViewById(R.id.fab_new_group);
        newGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewGroupActivity.start(getActivity(), courseKey);
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
        Query groupsQuery = dbRef.limitToFirst(100);
        mAdapter = new FirebaseRecyclerAdapter<Group, GroupViewHolder>(Group.class, R.layout.item_course, GroupViewHolder.class, groupsQuery) {
            @Override
            protected void populateViewHolder(final GroupViewHolder viewHolder, final Group group, final int position) {
                final String key = getRef(position).getKey();
                Log.i(TAG, "Populating view for group " + group.getName());

                // Set listener for button
                viewHolder.toggleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        student.put(group);
                        Log.i(TAG, "Toggling group " + group.getName() + " at " + group.getKey());
                    }
                });

                // Set listener for view holder
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GroupDetailActivity.start(getActivity(), group.getKey());
                    }
                });

                // Bind view to course
                Boolean member = student.getGroupKeys().contains(group.getKey());
                viewHolder.bindToGroup(group, member);
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