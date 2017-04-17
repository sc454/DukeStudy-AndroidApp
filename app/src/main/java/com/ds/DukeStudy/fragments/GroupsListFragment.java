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
import com.ds.DukeStudy.NewGroupActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Student;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//This fragment loads groups lists for a particular course from the database and displays in listview.
public class GroupsListFragment extends Fragment {

    // Fields

    private static final String COURSE_KEY_ARG = "courseKey";
    private Student student;
    private String courseKey;

    private FirebaseListAdapter<String> listAdapter;
    private ListView groupsListView;
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
        final View view = inflater.inflate(R.layout.fragment_groups_list, null);

        // Get arguments
        courseKey = getArguments().getString(COURSE_KEY_ARG);
        if (courseKey == null) {
            throw new IllegalArgumentException("Must pass " + COURSE_KEY_ARG);
        }

        // Get view
        groupsListView = (ListView) view.findViewById(R.id.groupsListListView);
        plusIcon = getResources().getDrawable(R.drawable.ic_menu_addclass);
        minusIcon = getResources().getDrawable(R.drawable.ic_indeterminate_check_box_black_24dp);
        student = ((MainActivity)getActivity()).getStudent();

        // New post button
        newGroupBtn = (FloatingActionButton) view.findViewById(R.id.fab_new_group);
        newGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewGroupActivity.start(getActivity(), courseKey);
            }
        });

        // List adapter
        groupKeysRef = Database.ref.child("courses").child(courseKey).child("groupKeys");

        listAdapter = new FirebaseListAdapter<String>(getActivity(), String.class, R.layout.general_row_view, groupKeysRef) {
            protected void populateView(final View v, final String model, final int position) {
                final DatabaseReference groupRef = Database.ref.child("groups").child(model);
                final TextView nameText = (TextView) v.findViewById(R.id.firstLine);
                final TextView countText = (TextView) v.findViewById(R.id.secondLine);
                final ImageButton toggleBtn = (ImageButton) v.findViewById(R.id.toggleButton);

                groupListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Group group = dataSnapshot.getValue(Group.class);
                        nameText.setText(group.getName());
                        countText.setText("Members: " + group.getStudentKeys().size());
                        Boolean isMember = group.getStudentKeys().contains(student.getKey());
                        if (isMember) {
                            toggleBtn.setImageDrawable(minusIcon);
                        } else {
                            toggleBtn.setImageDrawable(plusIcon);
                        }

                        // Button listener
                        toggleBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                toggle(group);
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                groupRef.addValueEventListener(groupListener);
            }};
        groupsListView.setAdapter(listAdapter);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        //groupKeysRef.removeEventListener(groupListener);
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

    public void toggleGroup(String key) {
        Student student = ((MainActivity)this.getActivity()).getStudent();
        ArrayList<String> groupKeys = student.getGroupKeys();
        if (groupKeys.contains(key)) {
            removeGroup(key);
        } else {
            addGroup(key);
        }
    }

    public void addGroup(String key) {
        Student student = ((MainActivity)this.getActivity()).getStudent();
        student.addGroupKey(key);
        student.put();
    }

    public void removeGroup(String key) {
        Student student = ((MainActivity)this.getActivity()).getStudent();
        student.removeGroupKey(key);
        student.put();
    }
}