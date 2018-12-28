package com.veeville.farm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.adapter.TemperatureActivityAdapter;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.DashBoardDataClasses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;


/*
* this activity shows Temperature of a particular farm
* as of now data are generating randomly for soil temperature
* later data should come from Server
 */
public class TemperatureActivity extends AppCompatActivity {
    private final String TAG = TemperatureActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        setUpToolbar();
        setUpHumidityRecyclerview();
    }

    //setting up custom toolbar
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Temperature-Farm1");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    //settingup soil temperature recyclerview
    void setUpHumidityRecyclerview() {
        RecyclerView temperatureRecyclerview = findViewById(R.id.temperature_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        temperatureRecyclerview.setLayoutManager(manager);
        TemperatureActivityAdapter adapter = new TemperatureActivityAdapter(getApplicationContext(), formData());
        temperatureRecyclerview.setAdapter(adapter);
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

    //onBackPressed return to previous activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        logMessage(title);
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    //randomly generating todays temp value
    private List<DashBoardDataClasses.TemperatureData.TempValue> getTodayTempValueTillNow() {
        List<DashBoardDataClasses.TemperatureData.TempValue> soilMoistureValues1 = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Random random = new Random();
        for (int i = 0; i <= hour; i++) {
            int temp = random.nextInt(40 - 10);
            String time;
            if (i < 10) {
                time = "0" + i;
            } else {
                time = "" + i;
            }
            DashBoardDataClasses.TemperatureData.TempValue value = new DashBoardDataClasses.TemperatureData.TempValue(time, temp + "");
            soilMoistureValues1.add(value);
        }
        return soilMoistureValues1;
    }

    //forming random soil temperature locally as of now later it should come Server
    private List<Object> formData() {
        List<Object> soilMoistureDatas = new ArrayList<>();
        DashBoardDataClasses.TemperatureData data = new DashBoardDataClasses.TemperatureData("", "", getTodayTempValueTillNow());
        soilMoistureDatas.add(data);
        soilMoistureDatas.add(new DashBoardDataClasses.SensorGraph("1D", 0));
        return soilMoistureDatas;
    }


    //always use this method o log debugg message
    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }
}
