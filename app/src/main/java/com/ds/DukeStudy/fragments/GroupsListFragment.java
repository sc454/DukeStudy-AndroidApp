package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.DukeStudy.LoginActivity;
import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.NewGroupActivity;
import com.ds.DukeStudy.NewPostActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.SignUpActivity;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Event;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Student;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//This fragment loads groups lists for a particular course from the database and displays in listview.
public class GroupsListFragment extends Fragment {

    private static final String COURSE_KEY_ARG = "courseKey";

    private DatabaseReference curGroupsRef;
    private ValueEventListener curGroupValueEventListener;

    private FirebaseListAdapter<String> listAdapter;
    private ListView groupsListView;
    private DatabaseReference allGroupsRef, groupRef;
    private ValueEventListener groupValueEventListener;
    private ListView membersListView;
    private String courseKey;
    private Student student;
    private TextView nameText, countText;
    private ImageButton toggleBtn;
    private Drawable plusIcon, minusIcon;
    private FloatingActionButton newGroupBtn;

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

        // Get arguments
        courseKey = getArguments().getString(COURSE_KEY_ARG);
        if (courseKey == null) {
            throw new IllegalArgumentException("Must pass " + COURSE_KEY_ARG);
        }

        // Get view
        final View view = inflater.inflate(R.layout.fragment_groups_list, null);
        groupsListView = (ListView) view.findViewById(R.id.groupsListListView);
        nameText = (TextView) view.findViewById(R.id.firstline);
        countText = (TextView) view.findViewById(R.id.secondline);
        toggleBtn = (ImageButton) view.findViewById(R.id.adddeletebutton);
        minusIcon = getResources().getDrawable(R.drawable.ic_indeterminate_check_box_black_24dp);
        plusIcon = getResources().getDrawable(R.drawable.ic_menu_addclass);

        // Set database
        student = ((MainActivity)GroupsListFragment.this.getActivity()).getStudent();
        groupRef = Database.ref.child("courses").child(courseKey).child("groupKeys");
        final DatabaseReference allGroupsRef=Database.ref.child("groups");

        // New post button
        newGroupBtn = (FloatingActionButton) view.findViewById(R.id.fab_new_group);
        newGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewGroupActivity.start(getActivity(), courseKey);
            }
        });

        curGroupsRef = Database.ref.child("courses").child(courseKey).child("groupKeys");
        final DatabaseReference groupsRef = Database.ref.child("groups");

        listAdapter = new FirebaseListAdapter<String>(getActivity(), String.class, R.layout.cutom_row_view_layout, curGroupsRef) {
            protected void populateView(final View v, final String model,final int position) {
                //Get reference to particular student in database
                final DatabaseReference curGroupRef = groupsRef.child(model);
                final TextView nameText = (TextView) v.findViewById(R.id.firstline);
                final TextView countText = (TextView) v.findViewById(R.id.secondline);
                final ImageButton toggleBtn = (ImageButton) v.findViewById(R.id.adddeletebutton);

//                toggleBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        curGroupRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                               @Override
//                               public void onDataChange(DataSnapshot dataSnapshot) {
//                                   final Group curGroup = dataSnapshot.getValue(Group.class);
//                                   nameText.setText(group.getName());
//                                   countText.setText("Members: " + group.getStudentKeys().size());
//                                   Boolean isMember = group.getStudentKeys().contains(student.getKey());
//                                   if (isMember) {
//                                       group.removeStudentKey(student.getKey());
//                                       groupsRef.child(group.getKey()).setValue(curGroup);
//                                       student.removeGroupKey(group.getKey());
//                                       student.put();
//                                   } else {
//                                       group.addStudentKey(student.getKey());
//                                       groupsRef.child(group.getKey()).setValue(curGroup);
//                                       student.addGroupKey(group.getKey());
//                                       student.put();
//                                   }
//                               }
//                               @Override
//                               public void onCancelled(DatabaseError databaseError) {}
//                           }
//                        );
//                    }
//                });

                curGroupValueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Group group = dataSnapshot.getValue(Group.class);
                        nameText.setText(group.getName());
                        countText.setText("Members: " + group.getStudentKeys().size());
                        Boolean isMember = group.getStudentKeys().contains(student.getKey());
                        if (isMember) toggleBtn.setImageDrawable(minusIcon);
                        else toggleBtn.setImageDrawable(plusIcon);
                        toggleBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    toggle(student, group);
                                }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                curGroupRef.addValueEventListener(curGroupValueEventListener);
            }};

        groupsListView.setAdapter(listAdapter);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        //curGroupsRef.removeEventListener(curGroupValueEventListener);
    }

    public void toggle(Student student, Group group) {
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