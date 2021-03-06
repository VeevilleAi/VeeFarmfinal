package com.veevillefarm.vfarm.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.adapter.FriendsListAdapter;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.ChatContact;
import com.veevillefarm.vfarm.helper.ChatMessageDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
* it will have list of friends and it will be loaded from local database it local databse contacts will getting updated after every 5 seconds
*/

public class FriendFragment extends Fragment {


    private final String TAG = FriendFragment.class.getSimpleName();
    private List<ChatContact> farmerContacts;
    private FriendsListAdapter adapter;
    private View view;
    RecyclerView recyclerView;
    private Handler handler;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend, container, false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.friends_fragment_menu, menu);
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
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
        setUpRecyclerview();
        handler = new Handler();
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable,5000);
    }

    private void updateRecyclerview(String query) {

        List<ChatContact> tempCntacts = new ArrayList<>();
        for (ChatContact contact : farmerContacts) {
            if (contact.name.toLowerCase().contains(query.toLowerCase())) {
                tempCntacts.add(contact);
            }
            adapter.updateContacts(tempCntacts);
        }
    }

    private void setUpRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(manager);
        farmerContacts = getContacts();
        String fromAddress = getUserAddressAsEmail();
        adapter = new FriendsListAdapter(farmerContacts, this.getActivity(),fromAddress);
        recyclerView.setAdapter(adapter);

    }
    private String getUserAddressAsEmail(){
        ChatMessageDatabase database = new ChatMessageDatabase(Objects.requireNonNull(getActivity()).getApplicationContext());
        return database.getUserEmailAsFromAddress();
    }

    private List<ChatContact> getContacts() {

        String fromAddress = getUserAddressAsEmail();
        ChatMessageDatabase database = new ChatMessageDatabase(Objects.requireNonNull(getActivity()).getApplicationContext());
        List<ChatContact> temp = database.getChatContacts();
        List<ChatContact> chatContacts = new ArrayList<>();
        for (ChatContact contact: temp) {
            if(!contact.email.equals(fromAddress)){
                chatContacts.add(contact);
            }
        }
       return chatContacts;
    }

    private void updateContacts(){
        int prevousSize = farmerContacts.size();
        List<ChatContact> chatContacts = getContacts();
        for (int i = prevousSize; i < chatContacts.size(); i++) {
            farmerContacts.add(chatContacts.get(i));
        }
        adapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(farmerContacts.size()-1);
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateContacts();
                        if (handler != null)
                            handler.postDelayed(runnable, 5000);
                    }
                });
            }catch (Exception e){
                logErrorMessage("run: "+e.toString());
            }
        }
    };

    //use this method only  to ebug message
    private void logMessage(String message){
        AppSingletonClass.logMessage(TAG,message);
    }

    private void logErrorMessage(String errorMessage){
        AppSingletonClass.logErrorMessage(TAG,errorMessage);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        handler = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String selectedItem = item.getTitle().toString();
        switch (selectedItem){
            case "Suggest friends":
                Toast.makeText(getActivity(), selectedItem, Toast.LENGTH_SHORT).show();
                break;
            case "Near By":
                Toast.makeText(getActivity(), selectedItem, Toast.LENGTH_SHORT).show();
                break;
            case "Growing Same Crop":
                Toast.makeText(getActivity(), selectedItem, Toast.LENGTH_SHORT).show();
                break;
            case "All":
                Toast.makeText(getActivity(), selectedItem, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<ChatContact> getTempContacts(){

        List<ChatContact> contacts= new ArrayList<>();
        contacts.add(new ChatContact("Prashant Veeville","prashant@veeville.com","picurl","","e7nvKoKOjS4:APA91bGIcLQRDqfHlS_9AJELPCww0HBFyEMH496OrHjmN9fs7X18Sq14qrIjpWbMBjUgXfnBwbOWB1vki1Fxo_LzlwxwGdIgXFQaaM4GDudaENrjSmJpsuEU1EjITRZo1p58Vfp_v59b"));
        //contacts.add(new ChatContact("Prashant Chikkalaki","prashantchikkalaki108@gmail.com","picurl","","eHONjzsTEFE:APA91bHY1K5nYGXZbI3-nuBQuWSTWfWgoCHDFpSy7vfFhmREdrb8B3RvMMnjBstgVc7z1ZWRcrp57ZJinztYkySlJ4Ig9anpIEKPV17-hS820GTnipz29CvKVZ8tFYRkGUPRN-HbKu_w"));
        return contacts;
    }
}
