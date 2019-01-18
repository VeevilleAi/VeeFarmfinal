package com.veevillefarm.vfarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.adapter.ViewPagerAdapter;
import com.veevillefarm.vfarm.fragment.CerebroFragment;
import com.veevillefarm.vfarm.fragment.ChatGroupsFragment;
import com.veevillefarm.vfarm.fragment.FriendFragment;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.ChatMessageDatabase;
import com.veevillefarm.vfarm.helper.SyncContactsService;

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
        toolbar.setTitle("Chat");
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

    Intent syncContactService;
    //start service to sync contacts and messages in background
    @Override
    protected void onResume() {
        super.onResume();
        ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());
        String fromAddress = database.getUserEmailAsFromAddress();
        logMessage("onResume: from address:" + fromAddress);


        syncContactService = new Intent(getApplicationContext(), SyncContactsService.class);
        syncContactService.putExtra("fromAddress", "Dummy");
        startService(syncContactService);
    }


    //get all 3 fragments for tablayout
    private List<Fragment> getTabs() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CerebroFragment());
        fragments.add(new FriendFragment());
        fragments.add(new ChatGroupsFragment());
        logErrorMessage("got it");
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
        logMessage(item.getTitle().toString());
        if (item.getTitle() == "Chat") {
            onBackPressed();
            return true;
        } else
            return false;
    }
    private void logMessage(String message){
        AppSingletonClass.logMessage(TAG,message);
    }

    private void logErrorMessage(String errorMessage){
        AppSingletonClass.logErrorMessage(TAG,errorMessage);
    }

}
