package com.veeville.farm.farmer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.adapter.CropMarketDescAdapter;
import com.veeville.farm.farmer.FarmerHelperClasses.CropMarketplace;
import com.veeville.farm.helper.AppSingletonClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
* this acivity shows available Mandi's near by based on Distance
* here he can search mandis
* it will be Having Prices of Particular Crop
 */
public class CropMarketDescActivity extends AppCompatActivity {

    private final String TAG = CropMarketDescActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_market_desc);
        setUpToolbar();
        setUpRecyclerview();
    }

    //setting up custom toolbar for Activity
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        String cropName = getIntent().getStringExtra("cropName");
        toolbar.setTitle("Market Price - " + cropName);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    //setting up recyclerview for Price of Crop in different mandis based on distnace(arranged)
    private void setUpRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(manager);
        CropMarketDescAdapter adapter = new CropMarketDescAdapter(getApplicationContext(), getMarketPlaceDetails());
        recyclerView.setAdapter(adapter);
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



    //as of now generating Mandis with distance and prices statically later it should come from Server
    private List<Object> getMarketPlaceDetails() {

        List<Object> objects = new ArrayList<>();
        List<CropMarketplace.CropMarketPlacePrice.SingleMarketPlacePrices> list = new ArrayList<>();
        list.add(new CropMarketplace.CropMarketPlacePrice.SingleMarketPlacePrices("Bangalore", "400", "50"));
        list.add(new CropMarketplace.CropMarketPlacePrice.SingleMarketPlacePrices("Mangalore", "200", "60"));
        list.add(new CropMarketplace.CropMarketPlacePrice.SingleMarketPlacePrices("Solapur", "150", "55"));
        list.add(new CropMarketplace.CropMarketPlacePrice.SingleMarketPlacePrices("Hubli", "180", "53"));
        list.add(new CropMarketplace.CropMarketPlacePrice.SingleMarketPlacePrices("Mysore", "890", "52"));
        list.add(new CropMarketplace.CropMarketPlacePrice.SingleMarketPlacePrices("Vijayapura", "450", "59"));
        list.add(new CropMarketplace.CropMarketPlacePrice.SingleMarketPlacePrices("Gulbarga", "110", "50"));
        list.add(new CropMarketplace.CropMarketPlacePrice.SingleMarketPlacePrices("Bidar", "170", "54"));
        list.add(new CropMarketplace.CropMarketPlacePrice.SingleMarketPlacePrices("Dharwad", "780", "56"));
        objects.add(new CropMarketplace.CropMarketPlacePrice(list));
        return objects;
    }

    // use this method to log debugg messages
    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

}
