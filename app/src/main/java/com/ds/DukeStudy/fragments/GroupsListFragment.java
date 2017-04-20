package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.NewGroupActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Student;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//This fragment loads groups lists for a particular course from the database and displays in listview.

public class GroupsListFragment extends Fragment {

    // Fields

    private static final String TAG = "GroupsListFragment";
    private static final String COURSE_KEY_ARG = "courseKey";
    private Student student;
    private String courseKey;

    private ListView listView;
    private FirebaseListAdapter<String> listAdapter;
    private Drawable plusIcon, minusIcon;
    private FloatingActionButton newGroupBtn;
    private DatabaseReference groupKeysRef;
    private ValueEventListener groupListener;

    // Methods

    public GroupsListFragment() {}

    public static GroupsListFragment newInstance(String key) {
        GroupsListFragment fragment = new GroupsListFragment();
        Bundle args = new Bundle();
        args.putString(COURSE_KEY_ARG, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_groups_list, null);

        // Get arguments
        courseKey = getArguments().getString(COURSE_KEY_ARG);
        if (courseKey == null) {
            throw new IllegalArgumentException("Must pass " + COURSE_KEY_ARG);
        }

        // Get view items
        listView = (ListView) view.findViewById(R.id.groupList);
        student = ((MainActivity) getActivity()).getStudent();
        plusIcon = getResources().getDrawable(R.drawable.ic_menu_addclass);
        minusIcon = getResources().getDrawable(R.drawable.ic_indeterminate_check_box_black_24dp);
        groupKeysRef = Database.ref.child("courses").child(courseKey).child("groupKeys");

        // Create adapter to list all groups
        listAdapter = new FirebaseListAdapter<String>(getActivity(), String.class, R.layout.general_row_view_btn, groupKeysRef) {
            protected void populateView(final View view, final String groupKey, final int position) {

                // Get view items
                final DatabaseReference groupRef = Database.ref.child("groups").child(groupKey);
                final TextView nameText = (TextView) view.findViewById(R.id.firstLine);
                final TextView countText = (TextView) view.findViewById(R.id.secondLine);
                final ImageButton toggleBtn = (ImageButton) view.findViewById(R.id.toggleButton);

                // Get group
                Log.i(TAG, "Populating view for " + groupKey);
                groupListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i(TAG, "OnDataChange: groupListener");
                        final Group group = dataSnapshot.getValue(Group.class);

                        // Set name and count
                        nameText.setText(group.getName());
                        countText.setText("Members: " + group.getStudentKeys().size());

                        // Set icon
                        Boolean isMember = group.getStudentKeys().contains(student.getKey());
                        toggle(toggleBtn, isMember);
                        Log.i(TAG, "Group " + group.getName() + ": isMember " + isMember);

                        // Toggle on click
                        toggleBtn.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                toggle(group);
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "OnDataChangeCancelled: groupListener", databaseError.toException());
                    }
                };
                groupRef.addValueEventListener(groupListener);
            }};
        listView.setAdapter(listAdapter);

        // New post button
        newGroupBtn = (FloatingActionButton) view.findViewById(R.id.fab_new_group);
        newGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewGroupActivity.start(getActivity(), courseKey);
            }
        });

        return view;
    }

//    public void updateUi(Student student, Group group, TextView nameText, TextView countText) {
//        nameText.setText(group.getName());
//        countText.setText("Members: " + group.getStudentKeys().size());
//        Boolean isMember = group.getStudentKeys().contains(student.getKey());
//        if (isMember) {
//            toggleBtn.setImageDrawable(minusIcon);
//        } else {
//            toggleBtn.setImageDrawable(plusIcon);
//        }
//    }

    public void toggle(ImageButton button, Boolean isChecked) {
        if (isChecked) {
            button.setImageDrawable(minusIcon);
        } else {
            button.setImageDrawable(plusIcon);
        }
    }

    public void toggle(Group group) {
        ArrayList<String> groupKeys = student.getGroupKeys();
        String key = group.getKey();
        if (groupKeys.contains(key)) {
            //remove
            student.removeGroupKey(key);
            group.removeStudentKey(student.getKey());
            student.put(); group.put();
        } else {
            //add
            student.addGroupKey(key);
            group.addStudentKey(student.getKey());
            student.put(); group.put();
        }
    }

    public void onDetach() {
        super.onDetach();
    }
}