package com.veevillefarm.vfarm.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.adapter.SelectedContactsAdapter;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.ChatContact;
import com.veevillefarm.vfarm.helper.ChatMessageDatabase;

import java.util.List;
import java.util.Objects;

public class NameChatGroupActivity extends AppCompatActivity {

    private final String TAG = NameChatGroupActivity.class.getSimpleName();
    private List<ChatContact> contacts;
    private TextView groupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_chat_group);
        groupName = findViewById(R.id.group_name);
        setUpToolbar();
        setUpRecyclerview();
    }


    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Create Group");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRecyclerview(){
        LinearLayoutManager manager = new GridLayoutManager(getApplicationContext(),3);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(manager);
        contacts = getIntent().getParcelableArrayListExtra("SelectedContacts");
        SelectedContactsAdapter adapter = new SelectedContactsAdapter(getApplicationContext(),contacts);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_contact_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String selectedItem = item.getTitle().toString();
        switch (selectedItem) {
            case "Create Group":
                onBackPressed();
                break;
            case "select":
                String groupname = groupName.getText().toString();
                if(!groupname.equals("")) {
                    insertNewlyCreatedGroupInDatabase(groupname,contacts);
                    onBackPressed();
                }else {
                    Toast.makeText(this, "please enter group name to continue", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void insertNewlyCreatedGroupInDatabase(String groupName,List<ChatContact> contacts){
        ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());
        String groupId = System.currentTimeMillis()/1000+"";
        database.insertNewChatGroup(groupId,groupName,contacts);
    }

    private void logMessage(String message){
        AppSingletonClass.logMessage(TAG,message);
    }

    private void logErrorMessage(String errorMessage){
        AppSingletonClass.logErrorMessage(TAG,errorMessage);
    }

}
