package com.veevillefarm.vfarm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.adapter.LightActivityAdapter;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.DashBoardDataClasses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;


/*
 * getting light value of particular farm and show with numbers and graphs
 * as of now generating randomly and added to recyclerview
 * handle all activity life cycle methods
 */
public class LightActivity extends AppCompatActivity {

    private final String TAG = LightActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        setUpToolbar();
        setUpSoilMoistureRecyclerview();
    }

    //setting up custom toolbar
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Light-Farm1");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    //settingup recyclerview for light values
    private void setUpSoilMoistureRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        RecyclerView lightRecyclerview = findViewById(R.id.soil_ph_recyclerview);
        lightRecyclerview.setLayoutManager(manager);
        LightActivityAdapter adapter = new LightActivityAdapter(getApplicationContext(), formData());
        lightRecyclerview.setAdapter(adapter);
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

    //perform action when option menu item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    //generate light values randomly and add to recyclerview but later it should come fom server
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

    //use this method to ebugg message everywhere
    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }
    private void logErrorMessage(String message){
        AppSingletonClass.logErrorMessage(TAG,message);
    }

}
