package com.ds.DukeStudy.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ds.DukeStudy.fragments.EventsListFragment;
import com.ds.DukeStudy.fragments.MembersListFragment;
import com.ds.DukeStudy.fragments.PostsFragment;
import com.ds.DukeStudy.items.Util;

public class GroupAdapter extends FragmentPagerAdapter {

    // Fields

    private static final String TAG = "GroupAdapter";
    private String groupKey;
    private static final int numTabs = 3;

    // Methods

    public GroupAdapter(FragmentManager fm, String groupKey) {
        super(fm);
        this.groupKey = groupKey;
    }

    @Override
    public int getCount() {return numTabs;}

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new PostsFragment().newInstance(Util.removeFromPath(groupKey, 0));
            case 1: return new EventsListFragment().newInstance(groupKey);
            case 2: return new MembersListFragment().newInstance(Util.GROUP_ROOT, groupKey);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Posts";
            case 1: return "Events";
            case 2: return "Members";
        }
        return null;
    }


}