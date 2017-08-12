package com.ds.DukeStudy.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.items.Database;
import com.ds.DukeStudy.items.Student;
import com.ds.DukeStudy.items.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class StudentViewHolder extends RecyclerView.ViewHolder {

    // Fields

    private static final String TAG = "StudentViewHolder";
    public TextView titleView, bodyView;
    public ToggleButton toggleBtn;

    // Methods

    public StudentViewHolder(View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.course_title);
        bodyView = (TextView) itemView.findViewById(R.id.course_body);
        toggleBtn = (ToggleButton) itemView.findViewById(R.id.toggle_btn);
        toggleBtn.setVisibility(View.GONE);
    }

    public void bindToStudent(String studentKey) {
        Log.i(TAG, "Binding Student");
        Database.ref.child(Util.STUDENT_ROOT).child(studentKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                if (student != null) {
                    Log.w(TAG, "Student is " + student.getName());
                    titleView.setText(student.getName());
                    bodyView.setText(student.getEmail());
                } else {
                    Log.w(TAG, "Student is null");
//                        Toast.makeText(getActivity().this, "Error: Could not fetch student.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "OnDataChangeCancelled: loadStudentKey", databaseError.toException());
            }
        });
    }
}
