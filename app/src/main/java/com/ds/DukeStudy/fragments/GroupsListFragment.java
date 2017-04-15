package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.R;
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

/**
 * Created by cheli on 3/5/2017.
 */

//This fragment loads groups lists for a particular course from the database and displays in listview.
public class GroupsListFragment extends Fragment {
    private DatabaseReference databaseRef;
    private FirebaseListAdapter<String> adapter1;
    private ListView groupsListView;
    private String myid;
    private DatabaseReference curGroupsRef;
    private ValueEventListener curGroupValueEventListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle curBundle=getArguments();
        myid=curBundle.getString("myid");
        final View view=inflater.inflate(R.layout.groupslist_layout,null);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        final MainActivity main = (MainActivity)GroupsListFragment.this.getActivity();
        groupsListView = (ListView) view.findViewById(R.id.groupsListListView);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        curGroupsRef = databaseRef.child("courses").child(myid).child("groupKeys");
        final DatabaseReference groupsRef=databaseRef.child("groups");
        adapter1=new FirebaseListAdapter<String>(getActivity(),String.class,R.layout.cutom_row_view_layout,curGroupsRef) {
            protected void populateView(final View v, final String model,final int position) {
                //Get reference to particular student in database
                DatabaseReference curGroupRef = groupsRef.child(model);
                final TextView mytext1 = (TextView) v.findViewById(R.id.firstline);
                final TextView mytext2 = (TextView) v.findViewById(R.id.secondline);
                final ImageButton mybutton=(ImageButton) v.findViewById(R.id.adddeletebutton);
                curGroupValueEventListener=new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Group curGroup = dataSnapshot.getValue(Group.class);
                        mytext1.setText("Name: " + curGroup.getName());
                        mytext2.setText("# Members: "+Integer.toString(curGroup.getStudentKeys().size()));
                        if (curGroup.getStudentKeys().contains(main.student.getKey())) {
                            mybutton.setImageDrawable(getResources().getDrawable(R.drawable.ic_indeterminate_check_box_black_24dp));
                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //If the event is clicked on either add or remove your student key from it
                                    curGroup.removeStudentKey(main.student.getKey());
                                    groupsRef.child(curGroup.getKey()).setValue(curGroup);
                                    main.student.removeGroupKey(curGroup.getKey());
                                    main.student.put();
                                }
                            });
                            mybutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //If the event is clicked on either add or remove your student key from it
                                    curGroup.removeStudentKey(main.student.getKey());
                                    groupsRef.child(curGroup.getKey()).setValue(curGroup);
                                    main.student.removeGroupKey(curGroup.getKey());
                                    main.student.put();
                                }
                            });
                        }else{
                            mybutton.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_addclass));
                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //If the event is clicked on either add or remove your student key from it
                                    curGroup.addStudentKey(main.student.getKey());
                                    groupsRef.child(curGroup.getKey()).setValue(curGroup);
                                    main.student.addGroupKey(curGroup.getKey());
                                    main.student.put();

                                }
                            });
                            mybutton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //If the event is clicked on either add or remove your student key from it
                                    curGroup.addStudentKey(main.student.getKey());
                                    groupsRef.child(curGroup.getKey()).setValue(curGroup);
                                    main.student.addGroupKey(curGroup.getKey());
                                    main.student.put();

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                //curGroupRef.addValueEventListener(curGroupValueEventListener);
                }};




        groupsListView.setAdapter(adapter1);
        return view;

    }

    @Override
    public void onPause() {
        super.onPause();
        //curGroupsRef.removeEventListener(curGroupValueEventListener);


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
