package com.veeville.farm.farmer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.veeville.farm.R;
import com.veeville.farm.adapter.FarmProfilesAdapter;
import com.veeville.farm.farmer.FarmerHelperClasses.FarmProfile;
import com.veeville.farm.helper.CircleTransform;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FarmerProfileActivity extends AppCompatActivity {

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
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setUpFarmsProfile() {

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        FarmProfilesAdapter adapter = new FarmProfilesAdapter(getFarmerProfile());
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }

    private List<Object> getFarmerProfile() {
        List<Object> objects = new ArrayList<>();
        //objects.add(new FarmerProfile("Prashant Chikkalaki","www.google.com","9878945634","India","prashantchikkalaki108@gmail.com","savalasang from Bijapur"));
        objects.add(new FarmProfile("Farm1", "fdfdf", "2", "Banana", "beginning", "4.5 quintol"));
        objects.add(new FarmProfile("Farm2", "", "4.5 acre", "Onion", "harvest", "405 kg"));
        objects.add(new FarmProfile("Farm3", "", "5 acre", "Potato", "Beginning", "100kg"));
        objects.add(new FarmProfile("Farm4", "", "6.5 acre", "Tomato", "Beginning", "600kg"));
        objects.add(new FarmProfile("Farm5", "", "7.8 acre", "Orange", "harvest", "1000kg"));
        objects.add(new FarmProfile("Farm6", "", "10 acre", "Mosambi", "harvest", "750 kg"));
        return objects;
    }
}
