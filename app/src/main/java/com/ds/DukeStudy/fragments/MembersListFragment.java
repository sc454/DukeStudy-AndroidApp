package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ds.DukeStudy.activities.MainActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.adapters.StudentViewHolder;
import com.ds.DukeStudy.items.Database;
import com.ds.DukeStudy.items.Student;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class MembersListFragment extends Fragment {

    // Fields

    private static final String TAG = "MembersListFragment";
    private static final String DB_PATH_ARG = "dbPath";
    private static final String DB_KEY_ARG = "dbKey";

    private String dbPath, dbKey;
    private FirebaseListAdapter<String> listAdapter;
    private ListView listView;

    private Student student;
    private DatabaseReference dbRef;
    private FirebaseRecyclerAdapter<String,StudentViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    // Constructors

    public MembersListFragment() {}

    public static MembersListFragment newInstance(String path, String key) {
        MembersListFragment fragment = new MembersListFragment();
        Bundle args = new Bundle();
        args.putString(DB_PATH_ARG, path);
        args.putString(DB_KEY_ARG, key);
        fragment.setArguments(args);
        return fragment;
    }

    // Other methods

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_members_list, null);

        // Get arguments
        dbPath = getArguments().getString(DB_PATH_ARG);
        if (dbPath == null) {
            throw new IllegalArgumentException("Must pass " + DB_PATH_ARG);
        }
        dbKey = getArguments().getString(DB_KEY_ARG);
        if (dbKey == null) {
            throw new IllegalArgumentException("Must pass " + DB_KEY_ARG);
        }

        // Get view items
        Log.i(TAG, "Getting students from " + dbPath + "/" + dbKey);
        dbRef = Database.ref.child(dbPath).child(dbKey).child("studentKeys");
        student = ((MainActivity)getActivity()).getStudent();
        mRecycler = (RecyclerView) view.findViewById(R.id.students_list);
        mRecycler.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Log.i(TAG, "Querying students from " + dbPath + "/" + dbKey);
        Query studentKeysQuery = dbRef.limitToFirst(100);
        mAdapter = new FirebaseRecyclerAdapter<String,StudentViewHolder>(String.class, R.layout.item_course, StudentViewHolder.class, studentKeysQuery) {
            @Override
            protected void populateViewHolder(final StudentViewHolder viewHolder, final String studentKey, final int position) {
                Log.i(TAG, "Populating viewholder for " + studentKey);

                // Set listener for view holder
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = getActivity().getApplicationContext();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.flContent, new ViewProfileFragment().newInstance(studentKey), null)
                                .addToBackStack(null)
                                .commit();
                        }
                });

                // Bind view to student
                viewHolder.bindToStudent(studentKey);
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
