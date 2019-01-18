package com.veevillefarm.vfarm.farmer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.adapter.FarmProfilesAdapter;
import com.veevillefarm.vfarm.farmer.FarmerHelperClasses.FarmProfile;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.CircleTransform;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
* farmer profile with shorter farm profiles with short description
* */
public class FarmerProfileActivity extends AppCompatActivity {

    private final String TAG = FarmerProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_profile);
        setUpToolbar();
        setUpFarmsProfile();
        ImageView profilePic;
        String imgUrl = "https://wle.cgiar.org/sites/default/files/header%20images/Vietnamese%20farmer%20header%20format.jpg";
        profilePic = findViewById(R.id.profile_image);
        Picasso.with(getApplicationContext()).load(imgUrl).resize(500, 500).centerCrop().transform(new CircleTransform()).into(profilePic);
    }

    @Override
    protected void onStart() {
        super.onStart();
        logMessage("onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logMessage("onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logMessage("onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logMessage("onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logMessage("onDestroy called");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        String logMessage = "Setting toolbar";
        logMessage(logMessage);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setUpFarmsProfile() {

        String logMessage = "setting farmer details recyclerview";
        logMessage(logMessage);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        FarmProfilesAdapter adapter = new FarmProfilesAdapter(getApplicationContext(), getFarmerProfile());
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }

    //getting farmer prfiles with farm profile later it should replaced with server details
    private List<FarmProfile> getFarmerProfile() {

        String logMessage = "getting Farmer profile details";
        logMessage(logMessage);
        List<FarmProfile> objects = new ArrayList<>();
        //objects.add(new FarmerProfile("Prashant Chikkalaki","www.google.com","9878945634","India","prashantchikkalaki108@gmail.com","savalasang from Bijapur"));
        objects.add(new FarmProfile("Farm1", "https://image.shutterstock.com/image-photo/bunch-bananas-isolated-on-white-260nw-96162077.jpg", "2", "Banana", "beginning", "4.5 quintol"));
        objects.add(new FarmProfile("Farm2", "https://5.imimg.com/data5/TF/US/MY-28264228/red-onion-500x500.jpg", "4.5 acre", "Onion", "harvest", "405 kg"));
        objects.add(new FarmProfile("Farm3", "https://5.imimg.com/data5/TF/US/MY-28264228/red-onion-500x500.jpg", "5 acre", "Potato", "Beginning", "100kg"));
        objects.add(new FarmProfile("Farm4", "https://image.shutterstock.com/image-photo/bunch-bananas-isolated-on-white-260nw-96162077.jpg", "6.5 acre", "Tomato", "Beginning", "600kg"));
        objects.add(new FarmProfile("Farm5", "https://5.imimg.com/data5/TF/US/MY-28264228/red-onion-500x500.jpg", "7.8 acre", "Orange", "harvest", "1000kg"));
        objects.add(new FarmProfile("Farm6", "https://5.imimg.com/data5/TF/US/MY-28264228/red-onion-500x500.jpg", "10 acre", "Mosambi", "harvest", "750 kg"));
        return objects;
    }

    //use this methos to ebugg messages
    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }
}
