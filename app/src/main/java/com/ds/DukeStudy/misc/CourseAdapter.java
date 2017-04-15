package com.ds.DukeStudy.misc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ds.DukeStudy.fragments.GroupsListFragment;
import com.ds.DukeStudy.fragments.MembersFragment;
import com.ds.DukeStudy.fragments.PostsFragment;

public class CourseAdapter extends FragmentPagerAdapter {

    private String courseKey;
    private static final int numTabs = 3;

    public CourseAdapter(FragmentManager fm, String courseKey) {
        super(fm);
        this.courseKey = courseKey;
    }

    @Override
    public int getCount() {return numTabs;}

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new PostsFragment().newInstance("courses", courseKey);
            case 1: return new GroupsListFragment().newInstance(courseKey);
            case 2: return new MembersFragment().newInstance("courses", courseKey);
        }
        return null;
    }

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