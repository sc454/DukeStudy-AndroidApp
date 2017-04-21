package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Student;
import com.ds.DukeStudy.objects.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "EditProfileFragment";
    private Student student;
    private View EditProfileView;
    private Button submitBtn;
    private EditText nameField, emailField, majorField, yearField;
    private OnFragmentInteractionListener mListener;

    public EditProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        EditProfileView =  inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Get view items
        submitBtn = (Button)EditProfileView.findViewById(R.id.submitProfileButton);
        nameField = (EditText)EditProfileView.findViewById(R.id.userNameEdit);
        emailField = (EditText)EditProfileView.findViewById(R.id.userEmailEdit);
        majorField = (EditText)EditProfileView.findViewById(R.id.userMajorEdit);
        yearField = (EditText)EditProfileView.findViewById(R.id.userYearEdit);

        // Fill fields
        student = ((MainActivity)this.getActivity()).getStudent();
        if (student != null) {
            nameField.setText(student.getName());
            emailField.setText(student.getEmail());
            majorField.setText(student.getMajor());
            yearField.setText(student.getGradYear());
        }

        // Submit button
        submitBtn.setOnClickListener(this);

        return EditProfileView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        // Read input
        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String major = majorField.getText().toString();
        String year = yearField.getText().toString();

        // Require all fields
        if (!Util.validateString(name, nameField)) return;
        if (!Util.validateEmail(email, emailField)) return;
        if (!Util.validateString(major, majorField)) return;
        if (!Util.validateNumber(year, yearField)) return;

        // Update fields
        student.setName(name);
        student.setEmail(email);
        student.setMajor(major);
        student.setGradYear(year);
        student.put();

        getFragmentManager().beginTransaction().replace(R.id.flContent, new ProfileFragment()).commit();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int tag,String userName);
    }
}
