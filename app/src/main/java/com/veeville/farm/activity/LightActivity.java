package com.veeville.farm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.adapter.LightActivityAdapter;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.DashBoardDataClasses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class LightActivity extends AppCompatActivity {

    private final String TAG = LightActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        setUpToolbar();
        setUpSoilMoistureRecyclerview();
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

    private void setUpToolbar() {

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Light-Farm1");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    private void setUpSoilMoistureRecyclerview() {

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        RecyclerView lightRecyclerview = findViewById(R.id.soil_ph_recyclerview);
        lightRecyclerview.setLayoutManager(manager);
        LightActivityAdapter adapter = new LightActivityAdapter(getApplicationContext(), formData());
        lightRecyclerview.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return super.onOptionsItemSelected(item);
    }



    private List<Object> formData() {

        List<Object> soilMoistureDatas = new ArrayList<>();
        List<DashBoardDataClasses.LightData.LightValues> values = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Random random = new Random();
        String time;
        for (int i = 0; i <= hour; i++) {
            int light = random.nextInt(1000 - 100);
            if (i < 10) {
                time = "0" + i;
            } else {
                time = "" + i;
            }
            DashBoardDataClasses.LightData.LightValues values1 = new DashBoardDataClasses.LightData.LightValues(time, light + "");
            values.add(values1);
        }
        soilMoistureDatas.add(new DashBoardDataClasses.LightData("", "", values));
        soilMoistureDatas.add(new DashBoardDataClasses.SensorGraph("1D", 0));
        return soilMoistureDatas;
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

}
