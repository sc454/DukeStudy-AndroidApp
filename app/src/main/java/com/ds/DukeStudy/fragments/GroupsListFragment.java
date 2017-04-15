package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Student;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//This fragment loads groups lists for a particular course from the database and displays in listview.
public class GroupsListFragment extends Fragment {

    private static final String COURSE_KEY_ARG = "courseId";

    private DatabaseReference databaseRef;
    private FirebaseListAdapter<Group> adapter1;
    private ListView membersListView;
    private String courseKey;

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

        View view = inflater.inflate(R.layout.groupslist_layout,null);
        membersListView = (ListView) view.findViewById(R.id.groupsListListView);
        DatabaseReference postsRef = Database.ref.child("groups");

        adapter1= new FirebaseListAdapter<Group>(getActivity(),Group.class,android.R.layout.two_line_list_item,postsRef) {
            @Override
            protected void populateView(View v, final Group model, int position) {
                TextView mytext=(TextView) v.findViewById(android.R.id.text1);
                TextView mytext1=(TextView) v.findViewById(android.R.id.text2);
                mytext.setText(model.getName());
                mytext1.setText("Member Count: "+Integer.toString(model.getStudentKeys().size()));
                v.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        String groupKey = model.getKey();
                        Context context = getActivity().getApplicationContext();
                        GroupFragment groupFragment = new GroupFragment().newInstance(groupKey);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.flContent, groupFragment, null)
                                .addToBackStack(null)
                                .commit();
                        toggleGroup(groupKey);
                    }
                });
            }

        };
        membersListView.setAdapter(adapter1);
        return view;

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
