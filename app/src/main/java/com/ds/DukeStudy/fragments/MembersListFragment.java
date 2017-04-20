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

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Student;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MembersListFragment extends Fragment {

    // Fields

    private static final String TAG = "MembersListFragment";
    private static final String PATH_ARG = "dbPath";

    private String dbPath;
    private FirebaseListAdapter<String> listAdapter;
    private ListView listView;

    // Constructors

    public MembersListFragment() {}

    public static MembersListFragment newInstance(String dpPath) {
        MembersListFragment fragment = new MembersListFragment();
        Bundle args = new Bundle();
        args.putString(PATH_ARG, dpPath);
        fragment.setArguments(args);
        return fragment;
    }

    // Other methods

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_members_list, null);

        // Get arguments
        dbPath = getArguments().getString(PATH_ARG);
        if (dbPath == null) {
            throw new IllegalArgumentException("Must pass " + PATH_ARG);
        }

        // Get view items
        listView = (ListView) view.findViewById(R.id.memberList);
        DatabaseReference studentKeysRef = Database.ref.child(dbPath).child("studentKeys");

        // Create adapter to list all members
        listAdapter = new FirebaseListAdapter<String>(getActivity(), String.class, R.layout.general_row_view, studentKeysRef) {
            @Override
            protected void populateView(final View view, final String studentKey,final int position) {

                // Get view items
                final TextView memberName = (TextView) view.findViewById(R.id.firstLine);
                final TextView memberEmail = (TextView) view.findViewById(R.id.secondLine);

                // Get each student
                Database.ref.child("students").child(studentKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Student student = dataSnapshot.getValue(Student.class);

                        // Set name and email
                        memberName.setText(student.getName());
                        memberEmail.setText(student.getEmail());

                        // Send to profile on click
                        view.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                Context context = getActivity().getApplicationContext();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.flContent, new ViewProfileFragment().newInstance(studentKey), null)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        };
        listView.setAdapter(listAdapter);
        return view;
    }
}
