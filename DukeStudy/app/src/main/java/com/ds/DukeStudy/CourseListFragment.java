package com.ds.DukeStudy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by cheli on 3/5/2017.
 */
//This is a fragment that retrieves class List from database and displays in listView
public class CourseListFragment extends Fragment {
    private DatabaseReference databaseRef;
    private FirebaseListAdapter<String> adapter1;
    private ListView courseListView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_course_list,null);
        courseListView=(ListView) view.findViewById(R.id.courseListListView);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postsRef=databaseRef.child("ClassList");
        adapter1=new FirebaseListAdapter<String>(getActivity(),String.class,android.R.layout.simple_expandable_list_item_1,postsRef) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView mytext=(TextView) v.findViewById(android.R.id.text1);
                mytext.setText(model);
            }
        };
        courseListView.setAdapter(adapter1);
        return view;

    }
}
