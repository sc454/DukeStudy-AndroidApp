package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Course;
import com.ds.DukeStudy.misc.CourseAdapter;
import com.ds.DukeStudy.objects.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class CourseFragment extends Fragment {

    // Fields

    public static final String TAG = "CourseFragment";
    public static final String COURSE_KEY_ARG = "courseKey";

    private String courseKey;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    private OnFragmentInteractionListener fragmentListener;

    // Methods

    public CourseFragment() {}

    public static CourseFragment newInstance(String courseKey) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putString(COURSE_KEY_ARG, courseKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.general_tabs, null);

        // Get arguments
        courseKey = getArguments().getString(COURSE_KEY_ARG);
        if (courseKey == null) {
            throw new IllegalArgumentException("Must pass " + COURSE_KEY_ARG);
        }

        // Create view
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        // Set adapter for view pager
        viewPager.setAdapter(new CourseAdapter(getChildFragmentManager(), courseKey));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        // Get the course name and change the title view
        DatabaseReference courseRef = Database.ref.child("courses").child(courseKey);
        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "OnDataChange: loadCourse");
                setTitle(dataSnapshot.getValue(Course.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "OnDataChangeCancelled: loadCourse", databaseError.toException());
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            fragmentListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int tag, int view);
    }

    void setTitle(Course course) {
        String title = course.getDepartment() + " " + course.getCode() + ": " + course.getTitle();
        ((MainActivity)this.getActivity()).getSupportActionBar().setTitle(title);
    }
}
