package com.ds.DukeStudy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.NewPostActivity;
import com.ds.DukeStudy.PostDetailActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.misc.AddCourseViewHolder;
import com.ds.DukeStudy.misc.PostViewHolder;
import com.ds.DukeStudy.objects.Course;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Post;
import com.ds.DukeStudy.objects.Student;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class AddCourseFragment extends Fragment {

    // Fields

    private static final String TAG = "AddCourseFragment";
//    private DatabaseReference databaseRef;
//    private FirebaseListAdapter<Course> adapter1;
//    private ListView courseListView;
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
        dbRef = Database.ref.child("courses");
        student = ((MainActivity)getActivity()).getStudent();
        mRecycler = (RecyclerView) view.findViewById(R.id.courses_list);
        mRecycler.setHasFixedSize(true);

        // New post button
//        newPostBtn = (FloatingActionButton) view.findViewById(R.id.fab_new_post);
//        newPostBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NewPostActivity.start(getActivity(), dbPath);
//            }
//        });

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
//        Query coursesQuery = dbRef.limitToFirst(100);
        Query coursesQuery = dbRef.orderByChild("department");
        mAdapter = new FirebaseRecyclerAdapter<Course, AddCourseViewHolder>(Course.class, R.layout.item_course, AddCourseViewHolder.class, coursesQuery) {
            @Override
            protected void populateViewHolder(final AddCourseViewHolder viewHolder, final Course course, final int position) {
                final String key = getRef(position).getKey();
                // Set click listener for the whole post view
                viewHolder.toggleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        student.toggleAndPut(course);
                    }
                });
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        PostDetailActivity.start(getActivity(), dbPath + "/" + key);
//                    }
//                });
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

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        View view = inflater.inflate(R.layout.fragment_add_course,null);
//
//        // Get view items
//        courseListView=(ListView) view.findViewById(R.id.courseListListView);
//        databaseRef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference coursesRef=databaseRef.child("courses");
//        student = ((MainActivity)AddCourseFragment.this.getActivity()).getStudent();
//
//        // Create adapter
//        adapter1=new FirebaseListAdapter<Course>(getActivity(), Course.class, R.layout.general_row_view_btn, coursesRef) {
//            @Override
//            protected void populateView(View v, final Course course, final int position) {
//                final TextView mytext1 = (TextView) v.findViewById(R.id.firstLine);
//                final TextView mytext2 = (TextView) v.findViewById(R.id.secondLine);
//                final ImageButton mybutton=(ImageButton) v.findViewById(R.id.toggleButton);
//                mytext1.setText(course.getDepartment() + " " + course.getCode() + ": " + course.getTitle());
//                mytext2.setText("Students: " + course.getStudentKeys().size());
//                if (course.getStudentKeys().contains(student.getKey())){
//                    if (isAdded()) {
//                        mybutton.setImageDrawable(getResources().getDrawable(R.drawable.ic_indeterminate_check_box_black_24dp));
//                    }
//                    mybutton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //If the course is clicked either add or remove your student key from it
//                            course.removeStudentKey(student.getKey());
//                            course.put();
//                            student.removeCourseKey(course.getKey());
//                            student.put();
//                        }
//                    });
//                }else{
//                    if (isAdded()) {
//                        mybutton.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_addclass));};
//                    mybutton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //If the event is clicked on either add or remove your student key from it
//                            course.addStudentKey(student.getKey());
//                            course.put();
//                            student.addCourseKey(course.getKey());
//                            student.put();
//                        }
//                    });
//                }
//            }
//        };
//        courseListView.setAdapter(adapter1);
//        return view;
//    }

//    public void toggleCourse(Course course) {
//
//        // Get keys
//        String sKey = student.getKey();
//        String cKey = course.getKey();
//
//        // Check
//        if (student.getCourseKeys().contains(cKey)) {
//            student.removeCourseKey(cKey);
//            course.removeStudentKey(sKey);
//        } else {
//            student.addCourseKey(cKey);
//            course.removeStudentKey(sKey);
//        }
//
//        // Update database
//        student.put();
//        course.put();
//    }
}