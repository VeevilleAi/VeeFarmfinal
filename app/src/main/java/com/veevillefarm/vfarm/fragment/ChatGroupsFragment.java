package com.veevillefarm.vfarm.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.activity.SelectContactActivity;
import com.veevillefarm.vfarm.adapter.ChatGroupFragmentAdapter;
import com.veevillefarm.vfarm.helper.ChatGroup;
import com.veevillefarm.vfarm.helper.ChatMessageDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
* this fragment is used to group chat like creating group chat, create group.....
 */
public class ChatGroupsFragment extends Fragment {


    private List<ChatGroup> chatGroups;
    private View view;
    private ChatGroupFragmentAdapter adapter;
    public ChatGroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_chat_groups, container, false);
        intilizeUiElements();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
    }

    private void intilizeUiElements(){
        FloatingActionButton createGroup = view.findViewById(R.id.create_group);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SelectContactActivity.class);
                startActivity(intent);
            }
        });
    }

    //option menu for group chat like Search option menu item
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                updateRecyclerview(query);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpRecyclerview();

    }

    private void updateRecyclerview(String searchQuery){
        List<ChatGroup> list = new ArrayList<>();
        for (ChatGroup group: chatGroups) {
            if(group.groupName.contains(searchQuery)){
                list.add(group);
            }
        }
        adapter.updateNewGroups(list);
    }

    private void setUpRecyclerview(){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(manager);
        chatGroups = getGropuNamesWithMember();
        adapter = new ChatGroupFragmentAdapter(getActivity(),chatGroups);
        recyclerView.setAdapter(adapter);

    }

    private List<ChatGroup> getGropuNamesWithMember(){
        ChatMessageDatabase database = new ChatMessageDatabase(getActivity());
        return database.getAllChatGroups();
    }
}
