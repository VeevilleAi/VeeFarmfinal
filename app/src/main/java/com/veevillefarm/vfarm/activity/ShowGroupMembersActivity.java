package com.veevillefarm.vfarm.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.ChatContact;

import java.util.List;
import java.util.Objects;

public class ShowGroupMembersActivity extends AppCompatActivity {

    private final String TAG = ShowGroupMembersActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_group_members);
        setUpToolbar();
        setUpSoilPhRecyclerview();
        logErrorMessage("onCreate");
        logErrorMessage("gottilla");
    }
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        String name = getIntent().getStringExtra("name");
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
    void setUpSoilPhRecyclerview() {
        logMessage("setUpSoilPhRecyclerview");
        logMessage("Gottaytu ");
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        List<ChatContact> contacts = getIntent().getParcelableArrayListExtra("contacts");
        GroupMemberAdapter adapter = new GroupMemberAdapter(getApplicationContext(),contacts);
        recyclerView.setAdapter(adapter);
    }

    class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.SingleNameHolder>{
        private Context context;
        List<ChatContact> contacts;
        GroupMemberAdapter(Context context, List<ChatContact> contacts){
            this.contacts = contacts;
            this.context = context;
        }
        @NonNull
        @Override
        public SingleNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.contact_card,parent,false);
            return new SingleNameHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SingleNameHolder holder, int position) {
            ChatContact contact = contacts.get(position);
            holder.name.setText(contact.name);
            holder.mobileNumber.setText(contact.email);
        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }

        class SingleNameHolder extends RecyclerView.ViewHolder{
            TextView name,mobileNumber;
            SingleNameHolder(View view){
                super(view);
                name = view.findViewById(R.id.name);
                mobileNumber = view.findViewById(R.id.most_recent_message);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void logMessage(String message){
        AppSingletonClass.logMessage(TAG,message);
    }
    private void logErrorMessage(String message){
        AppSingletonClass.logErrorMessage(TAG,message);
    }
}
