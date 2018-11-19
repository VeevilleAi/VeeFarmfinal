package com.veeville.farm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.adapter.SoilMoistureActivityAapter;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.DashBoardDataClasses;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu.size() == 0) {
            menu.add("Farm1");
            menu.add("Farm2");
            menu.add("Farm3");
            menu.add("Farm4");
            menu.add("Farm5");
        }
        return true;
    }

    private List<Object> formData() {

        List<DashBoardDataClasses.SoilMoistureData.SoilMoistureValues> soilMoistureValues1 = new ArrayList<>();
        List<Object> soilMoistureDatas = new ArrayList<>();
        List<String> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        months.add(DateFormat.format("MMMM", calendar.getTime()).toString());
        calendar.add(Calendar.MONTH, -1);
        months.add(DateFormat.format("MMMM", calendar.getTime()).toString());
        calendar.add(Calendar.MONTH, -1);
        months.add(DateFormat.format("MMMM", calendar.getTime()).toString());
        calendar.add(Calendar.MONTH, -1);
        months.add(DateFormat.format("MMMM", calendar.getTime()).toString());
        calendar.add(Calendar.MONTH, -1);
        months.add(DateFormat.format("MMMM", calendar.getTime()).toString());
        Log.d(TAG, "formData: " + Arrays.toString(months.toArray()));
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
        //soilMoistureDatas.add(soilMoistureData);
        Log.d(TAG, "formData: " + soilMoistureData);
        for (int i = 0; i < 1; i++) {
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
        soilMoistureDatas.add(new DashBoardDataClasses.SensorGraph("1D", 0));
        return soilMoistureDatas;
    }


}
