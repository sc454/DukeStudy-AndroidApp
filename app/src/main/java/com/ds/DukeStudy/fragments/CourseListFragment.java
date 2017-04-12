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

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Course;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by cheli on 3/5/2017.
 */
//This is a fragment that retrieves class List from database and displays in listView
public class CourseListFragment extends Fragment {
    private DatabaseReference databaseRef;
    private FirebaseListAdapter<Course> adapter1;
    private ListView courseListView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_course_list,null);
        courseListView=(ListView) view.findViewById(R.id.courseListListView);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference coursesRef=databaseRef.child("courses");
        adapter1=new FirebaseListAdapter<Course>(getActivity(),Course.class,android.R.layout.two_line_list_item,coursesRef) {
            @Override
            protected void populateView(View v, Course model, final int position) {
                TextView mytext=(TextView) v.findViewById(android.R.id.text1);
                TextView mytext2=(TextView) v.findViewById(android.R.id.text2);
                mytext.setText(model.getTitle());
                mytext2.setText(model.getInstructor());
                v.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Context context = getActivity().getApplicationContext();
                        CharSequence text = getRef(position).getKey();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });
            }
        };
        courseListView.setAdapter(adapter1);
        return view;

    }
}
