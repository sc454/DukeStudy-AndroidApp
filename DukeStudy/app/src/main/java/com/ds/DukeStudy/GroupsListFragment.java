package com.ds.DukeStudy;

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

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by cheli on 3/5/2017.
 */
//This fragment loads groups lists for a particular course from the database and displays in listview.
public class GroupsListFragment extends Fragment {
    private DatabaseReference databaseRef;
    private FirebaseListAdapter<Group> adapter1;
    private ListView membersListView;
    private String myid;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle curBundle=getArguments();
        View view=inflater.inflate(R.layout.groupslist_layout,null);
        membersListView=(ListView) view.findViewById(R.id.groupsListListView);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postsRef=databaseRef.child("groups");//This needs to be changed dynamically
        adapter1=new FirebaseListAdapter<Group>(getActivity(),Group.class,android.R.layout.two_line_list_item,postsRef) {
            @Override
            protected void populateView(View v, final Group model, int position) {
                TextView mytext=(TextView) v.findViewById(android.R.id.text1);
                TextView mytext1=(TextView) v.findViewById(android.R.id.text2);
                mytext.setText(model.getName());
                mytext1.setText("Member Count: "+Integer.toString(model.getStudentKeys().size()));
                v.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Context context = getActivity().getApplicationContext();
                        CharSequence text = model.getKey();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                });
            }

        };
        membersListView.setAdapter(adapter1);
        return view;

    }
}
