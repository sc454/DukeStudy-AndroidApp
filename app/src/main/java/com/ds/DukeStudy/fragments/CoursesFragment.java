package com.ds.DukeStudy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ds.DukeStudy.R;
import com.firebase.ui.database.FirebaseListAdapter;

//  Use tab_layout.xml to show three tabs in Groups ???

public class CoursesFragment extends Fragment {

    // Fields

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;
    private FirebaseListAdapter<String> adapterPost;
    private FirebaseListAdapter<String> adapterGroup;
    private FirebaseListAdapter<String> adapterMember;
    private OnFragmentInteractionListener mListener;

    // Methods

    public CoursesFragment() {} // required

    public static CoursesFragment newInstance(String param1, String param2) {
        CoursesFragment fragment = new CoursesFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View x =  inflater.inflate(R.layout.tab_layout,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        // Set adapter for view pager
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        return x;
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        // Return fragment with respect to Position

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new PostsFragment();
                case 1: return new GroupsListFragment();
                case 2: return new MembersFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        // Returns title of tab

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "Posts";
                case 1: return "Groups";
                case 2: return "Members";
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
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /* Interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that activity.
     * See http://developer.android.com/training/basics/fragments/communicating.html
     */
    public interface OnFragmentInteractionListener {
    //  TODO: Update argument type and name
        void onFragmentInteraction(int tag, int view);
    }
}
