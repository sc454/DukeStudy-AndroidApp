package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ds.DukeStudy.MainActivity;
import com.ds.DukeStudy.R;
import com.ds.DukeStudy.misc.GroupAdapter;
import com.ds.DukeStudy.objects.Group;
import com.ds.DukeStudy.objects.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class GroupFragment extends Fragment {

    // Fields

    public static final String TAG = "GroupFragment";
    public static final String GROUP_KEY_ARG = "groupKey";

    private String groupKey;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private GroupFragment.OnFragmentInteractionListener fragmentListener;

    // Methods

    public GroupFragment() {}

    public static GroupFragment newInstance(String groupKey) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString(GROUP_KEY_ARG, groupKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.general_tabs, null);

        // Get arguments
        groupKey = getArguments().getString(GROUP_KEY_ARG);
        if (groupKey == null) {
            throw new IllegalArgumentException("Must pass " + GROUP_KEY_ARG);
        }

        // Get view items
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        // Set adapter for view pager
        viewPager.setAdapter(new GroupAdapter(getChildFragmentManager(), groupKey));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        // Get the Group name and change the title view
        DatabaseReference GroupRef = Database.ref.child("groups").child(groupKey);
        GroupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "OnDataChange: loadGroup");
                setTitle(dataSnapshot.getValue(Group.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "OnDataChangeCancelled: loadGroup", databaseError.toException());
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GroupFragment.OnFragmentInteractionListener) {
            fragmentListener = (GroupFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int tag, int view);
    }

    void setTitle(Group group) {
        String title = group.getName();
        ((MainActivity)this.getActivity()).getSupportActionBar().setTitle(title);
    }
}