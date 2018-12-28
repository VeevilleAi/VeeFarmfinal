package com.veeville.farm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.adapter.SoilMoistureActivityAapter;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.DashBoardDataClasses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;


/*
 * showing particular farm soil moisture details
 * with graph and numbers
 * as of now generating soil moisture values randomly
 * later it should be replace with Fetching values from Server
 */
public class SoilMoistureActivity extends AppCompatActivity {

    private final String TAG = SoilMoistureActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_moisture);
        setUpUpToolbar();
        setUpSoilMoistureRecyclerview();
        logMessage("onCreate called");
    }

    //settingup custom toolbar
    private void setUpUpToolbar() {
        Toolbar toolbar;
        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Soil Moisture-Farm1");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    //setting soil moisture values recyclerview
    private void setUpSoilMoistureRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        RecyclerView soilMoistureRecyclerview = findViewById(R.id.soil_moisture_recyclerview);
        soilMoistureRecyclerview.setLayoutManager(manager);
        SoilMoistureActivityAapter adapter = new SoilMoistureActivityAapter(getApplicationContext(), formData());
        soilMoistureRecyclerview.setAdapter(adapter);
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


    //when option menu item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        AppSingletonClass.logDebugMessage(TAG, TAG + " finished");
        return super.onOptionsItemSelected(item);
    }

    //farming random soil moisture values locally
    private List<Object> formData() {

        List<DashBoardDataClasses.SoilMoistureData.SoilMoistureValues> soilMoistureValues1 = new ArrayList<>();
        List<Object> soilMoistureDatas = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String time;
        Random random = new Random();
        for (int i = 0; i < hour; i++) {
            if (i < 10) {
                time = "0" + i;
            } else {
                time = "" + i;
            }

            int value1 = random.nextInt(100 - 10);
            int value2 = random.nextInt(100 - 10);
            DashBoardDataClasses.SoilMoistureData.SoilMoistureValues values = new DashBoardDataClasses.SoilMoistureData.SoilMoistureValues(time, "" + value1, "" + value2);
            soilMoistureValues1.add(values);
        }
        DashBoardDataClasses.SoilMoistureData data = new DashBoardDataClasses.SoilMoistureData("", "", soilMoistureValues1);
        soilMoistureDatas.add(data);
        soilMoistureDatas.add(new DashBoardDataClasses.SensorGraph("1D", 0));
        return soilMoistureDatas;
    }

    //must use this methos to log message all over
    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }


}
