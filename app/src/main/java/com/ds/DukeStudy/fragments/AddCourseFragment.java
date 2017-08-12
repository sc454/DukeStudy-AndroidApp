package com.ds.DukeStudy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ds.DukeStudy.activities.CourseDetailActivity;
import com.ds.DukeStudy.activities.MainActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.adapters.AddCourseViewHolder;
import com.ds.DukeStudy.items.Course;
import com.ds.DukeStudy.items.Database;
import com.ds.DukeStudy.items.Student;
import com.ds.DukeStudy.items.Util;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class AddCourseFragment extends Fragment {

    // Fields

    private static final String TAG = "AddCourseFragment";

    private Student student;
    private DatabaseReference dbRef;
    private FirebaseRecyclerAdapter<Course,AddCourseViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    // Methods

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_add_course, container, false);

        // Get view items
        dbRef = Database.ref.child(Util.COURSE_ROOT);
        student = ((MainActivity)getActivity()).getStudent();
        mRecycler = (RecyclerView) view.findViewById(R.id.courses_list);
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
        Query coursesQuery = dbRef.orderByChild("department");
        mAdapter = new FirebaseRecyclerAdapter<Course, AddCourseViewHolder>(Course.class, R.layout.item_course, AddCourseViewHolder.class, coursesQuery) {
            @Override
            protected void populateViewHolder(final AddCourseViewHolder viewHolder, final Course course, final int position) {
                final String key = getRef(position).getKey();

                // Set listener for button
                viewHolder.toggleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        student.put(course);
                        Log.i(TAG, "Toggling " + course.getTitle() + " at " + course.getKey());
                    }
                });

                // Set listener for view holder
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CourseDetailActivity.start(getActivity(), course.getKey());
                    }
                });

                // Bind view to course
                Boolean enrolled = student.getCourseKeys().contains(course.getKey());
                viewHolder.bindToCourse(course, enrolled);
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