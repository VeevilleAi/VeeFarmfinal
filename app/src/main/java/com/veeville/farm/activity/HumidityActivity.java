package com.veeville.farm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.adapter.HumidityActivityAdapter;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.DashBoardDataClasses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;


/*
 * showing humidy with graph and numbers
 * Humidity data should come from server
 * as of now generating locally
 */


public class HumidityActivity extends AppCompatActivity {

    private final String TAG = HumidityActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity);
        logMessage("onCreate called");
        setUpToolbar();
        setUpHumidityRecyclerview();
    }

    //settingup custom toolbar
    private void setUpToolbar() {
        Toolbar toolbar;
        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Humidity-Farm1");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    //setting up humidity cads recyclerview
    private void setUpHumidityRecyclerview() {
        RecyclerView humidityRecyclerview = findViewById(R.id.humidity_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        humidityRecyclerview.setLayoutManager(manager);
        List<Object> lists = new ArrayList<>();
        lists.add(new DashBoardDataClasses.HumidityData("", "", getTodayValuesTillNow()));
        lists.add(new DashBoardDataClasses.SensorGraph("1D", 0));
        HumidityActivityAdapter temp = new HumidityActivityAdapter(getApplicationContext(), lists);
        humidityRecyclerview.setAdapter(temp);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        AppSingletonClass.logDebugMessage(TAG, "Humidity activity finished");
        return super.onOptionsItemSelected(item);
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


    //generate random values for Humidity
    private List<DashBoardDataClasses.HumidityData.HumidityDataValues> getTodayValuesTillNow() {
        List<DashBoardDataClasses.HumidityData.HumidityDataValues> humidityDataValues = new ArrayList<>();
        Random random = new Random();

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String time;

        for (int i = 0; i <= hour; i++) {
            if (i < 10) {
                time = "0" + i;
            } else {
                time = "" + i;
            }
            int absolute = random.nextInt(100 - 20);
            int relative = random.nextInt(70 - 10);
            DashBoardDataClasses.HumidityData.HumidityDataValues value = new DashBoardDataClasses.HumidityData.HumidityDataValues(time, absolute + "", "" + relative);
            humidityDataValues.add(value);
        }
        return humidityDataValues;

    }

    //use this method only  to log debug message
    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

}