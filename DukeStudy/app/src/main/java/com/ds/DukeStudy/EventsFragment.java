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
//This is an events fragment that retrieves events listed and displays them in a list for a given
    //course
public class EventsFragment extends Fragment {
    private DatabaseReference databaseRef;
    private FirebaseListAdapter<String> adapter1;
    private ListView eventsListView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.events_layout, container, false);
        eventsListView=(ListView) view.findViewById(R.id.eventsListView);
        databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postsRef=databaseRef.child("Events");
        adapter1=new FirebaseListAdapter<String>(getActivity(),String.class,android.R.layout.simple_expandable_list_item_1,postsRef) {
            @Override
            protected void populateView(View v, String model, int position) {
                TextView mytext=(TextView) v.findViewById(android.R.id.text1);
                mytext.setText(model);
            }
        };
        eventsListView.setAdapter(adapter1);
        return view;

    }
}
