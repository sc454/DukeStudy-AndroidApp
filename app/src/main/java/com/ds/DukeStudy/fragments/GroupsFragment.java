package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ds.DukeStudy.R;
import com.ds.DukeStudy.objects.Database;
import com.ds.DukeStudy.objects.Group;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//This is the groups fragment that loads group specific data from the database and displays in
    //3 tab view
public class GroupsFragment extends Fragment {
    //Use tab_layout.xml to show three tabs in Groups
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;
    private FirebaseListAdapter<String> adapterPost;
    private FirebaseListAdapter<String> adapterGroup;
    private FirebaseListAdapter<String> adapterMember;
    private OnFragmentInteractionListener mListener;
    private String groupID="";
    private DatabaseReference databaseRef;
    public GroupsFragment() {
        // Required empty public constructor
    }


    public static GroupsFragment newInstance(String param1, String param2) {
        GroupsFragment fragment = new GroupsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    //Sets database listeners and populates listviews.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get stuff from bundle
        Bundle mybundle=getArguments();
        this.groupID=mybundle.getString("myid");
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getContext(), this.groupID, duration);
        toast.show();
        // Inflate the layout for this fragment
            View x =  inflater.inflate(R.layout.tab_layout,null);
            tabLayout = (TabLayout) x.findViewById(R.id.tabs);
            viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        /**
         *Set an Adapter for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        //Get the groupname from the key
        databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference curGroupRef=databaseRef.child("groups").child(this.groupID);
        curGroupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Group curGroup = dataSnapshot.getValue(Group.class);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(curGroup.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return x;
    }
    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PostsFragment();
                case 1:
                    return new EventsFragment();
                case 2:
                    MembersFragment myfrag=new MembersFragment();
                    myfrag.isCourse=Boolean.FALSE;
                    return myfrag;
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Posts";
                case 1:
                    return "Events";
                case 2:
                    return "Members";
            }
            return null;
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int tag,int view);
    }
}
