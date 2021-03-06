package com.veevillefarm.vfarm.farmer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.activity.GetSensorDummyData;
import com.veevillefarm.vfarm.adapter.FarmDescAdapter;
import com.veevillefarm.vfarm.farmer.FarmerHelperClasses.FarmDesc;
import com.veevillefarm.vfarm.farmer.FarmerHelperClasses.FarmDescMap;
import com.veevillefarm.vfarm.farmer.FarmerHelperClasses.SoilTestResult;
import com.veevillefarm.vfarm.helper.AppSingletonClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/*
* in this activity we will show full description of farm like current crop,farm location in google map ....... */

public class FarmDescriptionActivity extends AppCompatActivity {

    private final String TAG = FarmDescriptionActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmers_farm);
        setUpToolbar();
        setUpRecyclerview();
    }

    //setting up custom toolbar for activity
    private void setUpToolbar() {
        String farmName = getIntent().getStringExtra("farmName");
        String cropName = getIntent().getStringExtra("cropName");
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        String title = farmName + "-" + cropName;
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        String logMessage = "Setting toolbar";
        logMessage(logMessage);
    }

    //setting up crop desc recyclerview
    private void setUpRecyclerview() {
        String logMessage = "Farm Desc recyclerview  Called";
        logMessage(logMessage);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        String farmName = getIntent().getStringExtra("farmName");
        FarmDescAdapter adapter = new FarmDescAdapter(getApplicationContext(), getfarmDescriptions(), farmName);
        recyclerView.setLayoutManager(manager);
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


    // when user click option menu back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }



    //getting farm desc from server as of now generating locally
    private List<Object> getfarmDescriptions() {
        String logMessage = "getting Farm description";
        logMessage(logMessage);
        List<Object> objects = new ArrayList<>();
        objects.add(new FarmDesc());
        objects.add(new FarmDescMap());
        GetSensorDummyData data = new GetSensorDummyData();
        objects.add(data.insertSoilPh());
        objects.add(data.insertSoilTemperature());
        objects.add(data.insertLight());
        objects.add(new SoilTestResult.SoilTestGraph());
        return objects;
    }

    //use this function to ebugg message
    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }

    //use this function to log begugg error messages
    private void logErrorMessage(String logErrorMessage) {
        AppSingletonClass.logErrorMessage(TAG, logErrorMessage);
    }
}
