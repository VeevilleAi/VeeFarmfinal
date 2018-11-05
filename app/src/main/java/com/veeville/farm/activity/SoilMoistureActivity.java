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

public class SoilMoistureActivity extends AppCompatActivity {

    private final String TAG = "SoilMoistureActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_moisture);
        setUpUpToolbar();
        setUpSoilMoistureRecyclerview();
        AppSingletonClass.logDebugMessage(TAG, TAG + " started");
    }

    private void setUpUpToolbar() {
        Toolbar toolbar;
        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Soil Moisture-Farm1");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    private void setUpSoilMoistureRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        RecyclerView soilMoistureRecyclerview = findViewById(R.id.soil_moisture_recyclerview);
        soilMoistureRecyclerview.setLayoutManager(manager);
        SoilMoistureActivityAapter adapter = new SoilMoistureActivityAapter(getApplicationContext(), formData());
        soilMoistureRecyclerview.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        AppSingletonClass.logDebugMessage(TAG, TAG + " finished");
        return super.onOptionsItemSelected(item);
    }

    private List<Object> formData() {

        List<DashBoardDataClasses.SoilMoistureData.SoilMoistureValues> soilMoistureValues1 = new ArrayList<>();
        List<Object> soilMoistureDatas = new ArrayList<>();
        List<String> months = new ArrayList<>();
        months.add(getMonth(getDate(0)));
        months.add(getMonth(getDate(-1)));
        months.add(getMonth(getDate(-2)));
        months.add(getMonth(getDate(-3)));
        months.add(getMonth(getDate(-4)));
        for (int j = 1; j < 2; j++) {
            String value;
            value = "0" + j;
            Random r = new Random();
            int i1 = r.nextInt(47 - 44) + 44;
            int i2 = i1 + 20;
            DashBoardDataClasses.SoilMoistureData.SoilMoistureValues values = new DashBoardDataClasses.SoilMoistureData.SoilMoistureValues(value, "" + i1, "" + i2);
            soilMoistureValues1.add(values);
        }
        DashBoardDataClasses.SoilMoistureData soilMoistureData = new DashBoardDataClasses.SoilMoistureData(months.get(0), "Bengaluru", soilMoistureValues1);
        soilMoistureDatas.add(soilMoistureData);
        for (int i = 1; i < 1; i++) {
            List<DashBoardDataClasses.SoilMoistureData.SoilMoistureValues> soilMoistureValues2 = new ArrayList<>();
            for (int j = 1; j < 31; j++) {
                String value;
                if (j < 10) {
                    value = "0" + j;
                } else {
                    value = j + "";
                }
                Random r = new Random();
                int i1 = r.nextInt(47 - 44) + 44;
                int i2 = i1 + 20;
                DashBoardDataClasses.SoilMoistureData.SoilMoistureValues values = new DashBoardDataClasses.SoilMoistureData.SoilMoistureValues(value, "" + i1, "" + i2);
                soilMoistureValues2.add(values);
            }
            DashBoardDataClasses.SoilMoistureData soilMoistureData1 = new DashBoardDataClasses.SoilMoistureData(months.get(i), "Bengaluru", soilMoistureValues2);
            soilMoistureDatas.add(soilMoistureData1);
        }
        soilMoistureDatas.add(new DashBoardDataClasses.SoilMoistureData.SoilMoistureGraphMonthData());
        soilMoistureDatas.add(new DashBoardDataClasses.SoilMoistureData.SoilMoistureGraphyearData());
        return soilMoistureDatas;
    }

    private int getDate(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        return calendar.get(Calendar.MONTH);
    }

    private String getMonth(int month) {
        String monthOfYear = null;
        switch (month) {
            case 0:
                monthOfYear = "January";
                break;
            case 1:
                monthOfYear = "February";
                break;
            case 2:
                monthOfYear = "March";
                break;
            case 3:
                monthOfYear = "April";
                break;
            case 4:
                monthOfYear = "May";
                break;
            case 5:
                monthOfYear = "June";
                break;
            case 6:
                monthOfYear = "July";
                break;
            case 7:
                monthOfYear = "August";
                break;
            case 8:
                monthOfYear = "September";
                break;
            case 9:
                monthOfYear = "October";
                break;
            case 10:
                monthOfYear = "November";
                break;
            case 11:
                monthOfYear = "December";
                break;
        }
        return monthOfYear;
    }
}
