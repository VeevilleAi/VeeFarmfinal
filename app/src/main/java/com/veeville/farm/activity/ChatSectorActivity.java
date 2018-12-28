package com.veeville.farm.activity;

import android.content.Intent;
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
import com.veeville.farm.fragment.ChatGroupsFragment;
import com.veeville.farm.fragment.FriendFragment;
import com.veeville.farm.helper.ChatMessageDatabase;
import com.veeville.farm.helper.SyncContactsService;
import com.veeville.farm.helper.SyncMessagesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/*
this ativity has 3 fragments
1 - Cerebro(ChatBot)
2 - Contacts for chat's
3 - Group chat (not yet implemented)

star service here only for background sync for contacts and messages

*/
public class ChatSectorActivity extends AppCompatActivity {

    private final String TAG = ChatSectorActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_sector);
        setupToolbar();
        setUpTabs();
    }

    //settingup custom toolbar
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cerebro");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    //settingup tablayout with 3 fragments
    private void setUpTabs() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getTabsTitle(), getTabs());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    //start service to sync contacts and messages in background
    @Override
    protected void onResume() {
        super.onResume();
        ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());
        String fromAddress = database.getUserEmailAsFromAddress();
        Log.d(TAG, "onResume: from address:" + fromAddress);
        Intent intent = new Intent(getApplicationContext(), SyncMessagesService.class);
        intent.putExtra("fromAddress", fromAddress);
        startService(intent);

        Intent intent1 = new Intent(getApplicationContext(), SyncContactsService.class);
        intent1.putExtra("fromAddress", "Dummy");
        startService(intent1);
    }

    //get all 3 fragments for tablayout
    private List<Fragment> getTabs() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CerebroFragment());
        fragments.add(new FriendFragment());
        fragments.add(new ChatGroupsFragment());
        return fragments;
    }


    //get title for all three fragments
    private List<String> getTabsTitle() {
        List<String> titles = new ArrayList<>();
        titles.add("Cerebro");
        titles.add("Friends");
        titles.add("Groups");
        return titles;
    }


    //when option menu item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: " + item.getTitle());
        if (item.getTitle() == "Cerebro") {
            onBackPressed();
            return true;
        } else
            return false;
    }


}
