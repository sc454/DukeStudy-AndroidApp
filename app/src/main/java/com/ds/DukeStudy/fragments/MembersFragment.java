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

public class MembersFragment extends Fragment {

    private static final String PREFIX_ARG = "dbPrefix";
    private static final String KEY_ARG = "key";

    private String dbPrefix;
    private String dbKey;
    private DatabaseReference databaseRef;
    private FirebaseListAdapter<String> adapter1;
    private ListView membersListView;
    public Boolean isCourse=Boolean.TRUE;

    public MembersFragment() {}

    public static MembersFragment newInstance(String dpPath, String key) {
        MembersFragment fragment = new MembersFragment();
        Bundle args = new Bundle();
        args.putString(PREFIX_ARG, dpPath);
        args.putString(KEY_ARG, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_members,null);

        // Get arguments
        dbPrefix = getArguments().getString(PREFIX_ARG);
        if (dbPrefix == null) {
            throw new IllegalArgumentException("Must pass " + PREFIX_ARG);
        }

        dbKey = getArguments().getString(KEY_ARG);
        if (dbKey == null) {
            throw new IllegalArgumentException("Must pass " + KEY_ARG);
        }

        // Create view
        membersListView = (ListView) view.findViewById(R.id.membersListView);

        // Create database listener
        DatabaseReference studentKeysRef = Database.ref.child(dbPrefix).child(dbKey).child("studentKeys");
        adapter1 = new FirebaseListAdapter<String>(getActivity(), String.class, android.R.layout.two_line_list_item, studentKeysRef) {
            @Override
            protected void populateView(final View v, final String model,final int position) {
                //Get reference to particular student in database
                final TextView mytext1=(TextView) v.findViewById(android.R.id.text1);
                final TextView mytext2=(TextView) v.findViewById(android.R.id.text2);
                Database.ref.child("students").child(model).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Student curStudent = dataSnapshot.getValue(Student.class);
                        mytext1.setText(curStudent.getName());
                        mytext2.setText(curStudent.getEmail());
                        v.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                Context context = getActivity().getApplicationContext();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.flContent, new ViewProfileFragment().newInstance(model.toString()), null)
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
        membersListView.setAdapter(adapter1);
        return view;
    }
}
