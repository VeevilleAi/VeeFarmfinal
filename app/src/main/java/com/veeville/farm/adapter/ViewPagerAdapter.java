package com.veeville.farm.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Prashant C on 07/12/18.
 * this adapter is for fragments (FriendsFragment,CerebroFragment,GroupChatFragment)
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<String> title;
    private List<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager manager, List<String> title, List<Fragment> fragments) {
        super(manager);
        this.fragments = fragments;
        this.title = title;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
