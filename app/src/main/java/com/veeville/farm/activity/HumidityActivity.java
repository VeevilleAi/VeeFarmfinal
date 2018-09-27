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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class HumidityActivity extends AppCompatActivity {

    private String TAG = "HumidityActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity);
        setUpToolbar();
        setUpHumidityRecyclerview();
        AppSingletonClass.logDebugMessage(TAG, "HumidityActivity started");
    }

    private void setUpToolbar() {
        Toolbar toolbar;
        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Humidity");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        AppSingletonClass.logDebugMessage(TAG, "toolbar setup done");
    }

    void setUpHumidityRecyclerview() {

        RecyclerView humidityRecyclerview = findViewById(R.id.humidity_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        humidityRecyclerview.setLayoutManager(manager);
        List<DashBoardDataClasses.HumidityData> humidityDataList = formData();
        HumidityActivityAdapter adapter = new HumidityActivityAdapter(getApplicationContext(), humidityDataList);
        humidityRecyclerview.setAdapter(adapter);
        AppSingletonClass.logDebugMessage(TAG, "recyclerview setup done");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        AppSingletonClass.logDebugMessage(TAG, "Humidity activity finished");
        return super.onOptionsItemSelected(item);
    }

    List<DashBoardDataClasses.HumidityData> formData() {
        List<DashBoardDataClasses.HumidityData.HumidityDataValues> values = new ArrayList<>();
        List<DashBoardDataClasses.HumidityData> humidityDataList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String value;
            if (i < 10) {
                value = "0" + i;
            } else {
                value = i + "";
            }
            Random r = new Random();
            int i1 = r.nextInt(75 - 70) + 70;
            int i2 = i1 + 2;
            DashBoardDataClasses.HumidityData.HumidityDataValues val0 = new DashBoardDataClasses.HumidityData.HumidityDataValues(value, i1 + "", i2 + "");
            values.add(val0);
        }

        DashBoardDataClasses.HumidityData humidityData1 = new DashBoardDataClasses.HumidityData("Today", "Bengaluru", values);
        DashBoardDataClasses.HumidityData humidityData2 = new DashBoardDataClasses.HumidityData("Yesterday", "Bengaluru", values);
        DashBoardDataClasses.HumidityData humidityData3 = new DashBoardDataClasses.HumidityData(getDate(-2), "Bengaluru", values);
        DashBoardDataClasses.HumidityData humidityData4 = new DashBoardDataClasses.HumidityData(getDate(-3), "Bengaluru", values);
        humidityDataList.add(humidityData1);
        humidityDataList.add(humidityData2);
        humidityDataList.add(humidityData3);
        humidityDataList.add(humidityData4);
        return humidityDataList;
    }

    String getDate(int days) {

        String format = "dd MMM yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return dateFormat.format(calendar.getTime());
    }
}