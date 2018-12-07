package com.veeville.farm.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.veeville.farm.R;
import com.veeville.farm.adapter.FriendsListAdapter;
import com.veeville.farm.helper.FarmerContact;

import java.util.ArrayList;
import java.util.List;

public class FriendFragment extends Fragment {


    List<FarmerContact> farmerContacts;
    FriendsListAdapter adapter;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend, container, false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
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
    public void onStart() {
        super.onStart();
        setUpRecyclerview();
    }

    private void updateRecyclerview(String query) {

        List<FarmerContact> tempCntacts = new ArrayList<>();
        for (FarmerContact contact : farmerContacts) {
            if (contact.name.toLowerCase().contains(query.toLowerCase())) {
                tempCntacts.add(contact);
            }
            adapter.updateContacts(tempCntacts);
        }
    }

    private void setUpRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(manager);
        farmerContacts = getContacts();
        adapter = new FriendsListAdapter(farmerContacts, this.getActivity());
        recyclerView.setAdapter(adapter);

    }

    private List<FarmerContact> getContacts() {

        List<FarmerContact> farmerContacts = new ArrayList<>();
        farmerContacts.add(new FarmerContact("Sachin", "9986371392", "https://firebasestorage.googleapis.com/v0/b/mybot-d1bce.appspot.com/o/IMG_20181106_094131.jpg?alt=media&token=ebb26b3f-9374-4de1-af2f-fa86665c4d13"));
        farmerContacts.add(new FarmerContact("Devaraj", "9986371392", "https://firebasestorage.googleapis.com/v0/b/mybot-d1bce.appspot.com/o/IMG_20181112_094514.jpg?alt=media&token=fe17d134-8369-4b54-ac7b-c17bda989b4b"));
        farmerContacts.add(new FarmerContact("Ningaraj", "9986371392", "https://firebasestorage.googleapis.com/v0/b/mybot-d1bce.appspot.com/o/IMG_20181118_070142.jpg?alt=media&token=bb52d89b-a1ed-47a7-8e7d-52ffdfbffea7"));
        farmerContacts.add(new FarmerContact("Ramachandra", "9986371392", "https://firebasestorage.googleapis.com/v0/b/mybot-d1bce.appspot.com/o/IMG_20181124_124700.jpg?alt=media&token=5f0bc9b8-f6a2-41b8-a3ff-59e965bff325"));
        farmerContacts.add(new FarmerContact("Raghavendra", "9986371392", "https://firebasestorage.googleapis.com/v0/b/mybot-d1bce.appspot.com/o/IMG_20181124_124700.jpg?alt=media&token=5f0bc9b8-f6a2-41b8-a3ff-59e965bff325"));
        farmerContacts.add(new FarmerContact("Shivaraj", "9986371392", "https://firebasestorage.googleapis.com/v0/b/mybot-d1bce.appspot.com/o/IMG_20181124_124700.jpg?alt=media&token=5f0bc9b8-f6a2-41b8-a3ff-59e965bff325"));

        return farmerContacts;
    }

}
