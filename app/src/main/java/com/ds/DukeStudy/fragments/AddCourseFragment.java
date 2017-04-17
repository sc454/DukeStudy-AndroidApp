package com.ds.DukeStudy.fragments;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Course;
import com.ds.DukeStudy.objects.Student;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
/**
 * Created by cheli on 3/5/2017.
 */
//This is a fragment that retrieves class List from database and displays in listView
public class AddCourseFragment extends Fragment {
    private DatabaseReference databaseRef;
    private FirebaseListAdapter<Course> adapter1;
    private ListView courseListView;
    private Student student;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_course_list,null);
        courseListView=(ListView) view.findViewById(R.id.courseListListView);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference coursesRef=databaseRef.child("courses");
        student = ((MainActivity)AddCourseFragment.this.getActivity()).getStudent();
        adapter1=new FirebaseListAdapter<Course>(getActivity(),Course.class,R.layout.cutom_row_view_layout,coursesRef) {
            @Override


            protected void populateView(View v, final Course model, final int position) {
                final TextView mytext1 = (TextView) v.findViewById(R.id.firstline);
                final TextView mytext2 = (TextView) v.findViewById(R.id.secondline);
                final ImageButton mybutton=(ImageButton) v.findViewById(R.id.adddeletebutton);
                mytext1.setText(model.getDepartment()+model.getCode()+": "+model.getTitle());
                mytext2.setText("# Students:"+Integer.toString(model.getStudentKeys().size()));
                if (model.getStudentKeys().contains(student.getKey())){
                    if (isAdded()) {
                        mybutton.setImageDrawable(getResources().getDrawable(R.drawable.ic_indeterminate_check_box_black_24dp));
                    }
                    mybutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //If the course is clicked either add or remove your student key from it
                            model.removeStudentKey(student.getKey());
                            model.put();
                            student.removeCourseKey(model.getKey());
                            student.put();
                        }
                    });
                }else{
                    if (isAdded()) {
                        mybutton.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_addclass));};
                    mybutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //If the event is clicked on either add or remove your student key from it
                            model.addStudentKey(student.getKey());
                            model.put();
                            student.addCourseKey(model.getKey());
                            student.put();
                        }
                    });
                }
            }
        };
        courseListView.setAdapter(adapter1);
        return view;
    }
    public void toggleCourse(String key) {
        Student student = ((MainActivity)this.getActivity()).getStudent();
        ArrayList<String> courseKeys = student.getCourseKeys();
        if (courseKeys.contains(key)) {
            removeCourse(key);
        } else {
            addCourse(key);
        }
    }
    public void addCourse(String key) {
        Student student = ((MainActivity)this.getActivity()).getStudent();
        student.addCourseKey(key);
        student.put();
    }
    public void removeCourse(String key) {
        Student student = ((MainActivity)this.getActivity()).getStudent();
        student.removeCourseKey(key);
        student.put();
    }
}