package com.veeville.farm.farmer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.veeville.farm.R;
import com.veeville.farm.adapter.FarmAdapter;
import com.veeville.farm.farmer.FarmerHelperClasses.Farm;
import com.veeville.farm.helper.AppSingletonClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*this activity shows list farmer farms as Cards
* with short desc, crop name,crop image and listeners*/
public class FarmsActivity extends AppCompatActivity {

    private final String TAG = FarmsActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farms_dash_board);
        setUpToolbar();
        setUpRecyclerview();
        addNewFarm();
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

    //function to add new Farm
    private void addNewFarm() {
        String logMessage = "add Farm Called";
        logMessage(logMessage);
        FloatingActionButton addFarm = findViewById(R.id.add_new_farm);
        addFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FarmRegistrationActivity.class);
                startActivity(intent);
            }
        });

    }

    //setting custom toolbar to FarmsActivty
    private void setUpToolbar() {
        String logMessage = "setting up toolbar";
        logMessage(logMessage);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Farms-Crop");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    //when user selected option menu item like back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String logMessage = "Menu Item Selected";
        logMessage(logMessage);
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    //setting up farms recyclerview to show farms
    private void setUpRecyclerview() {
        LinearLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(manager);
        FarmAdapter adapter = new FarmAdapter(getFarms(), getApplicationContext());
        recyclerView.setAdapter(adapter);
        String logMessage = "setting up Farms Recyclerview";
        logMessage(logMessage);
    }

    //loading farms to showin UI later it should come from Server
    private List<Farm> getFarms() {
        List<Farm> farms = new ArrayList<>();
        Farm farm1 = new Farm("Farm1", "Banana", "https://image.shutterstock.com/image-photo/bunch-bananas-isolated-on-white-260nw-96162077.jpg", "growing");
        Farm farm2 = new Farm("Farm1", "Apple", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQL2JZYGnK-uWQBxo0yZi9GFXGBogxwySGSzLwP-C2mTcIdfVdt", "growing");
        Farm farm3 = new Farm("Farm1", "Onion", "https://5.imimg.com/data5/TF/US/MY-28264228/red-onion-500x500.jpg", "growing");
        Farm farm4 = new Farm("Farm2", "Onion", "https://5.imimg.com/data5/TF/US/MY-28264228/red-onion-500x500.jpg", "growing");
        Farm farm5 = new Farm("Farm3", "Onion", "https://5.imimg.com/data5/TF/US/MY-28264228/red-onion-500x500.jpg", "growing");
        farms.add(farm1);
        farms.add(farm2);
        farms.add(farm3);
        farms.add(farm4);
        farms.add(farm5);
        String logMessage = "getting Farms ";
        logMessage(logMessage);
        return farms;
    }

    //use below method to log debugg message
    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

}
