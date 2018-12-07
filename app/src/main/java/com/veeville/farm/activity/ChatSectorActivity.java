package com.veeville.farm.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.adapter.ViewPagerAdapter;
import com.veeville.farm.fragment.CerebroFragment;
import com.veeville.farm.fragment.FriendFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatSectorActivity extends AppCompatActivity {


    private final String TAG = ChatSectorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_sector);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ViewPager viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getTabsTitle(), getTabs());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private List<Fragment> getTabs() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CerebroFragment());
        fragments.add(new FriendFragment());
        return fragments;
    }

    private List<String> getTabsTitle() {
        List<String> titles = new ArrayList<>();
        titles.add("Cerebro");
        titles.add("Friends");
        return titles;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
        if (item.getTitle() == null)
            onBackPressed();
        return true;
    }
}
