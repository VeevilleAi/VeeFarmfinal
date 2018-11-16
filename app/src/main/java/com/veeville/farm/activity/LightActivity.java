package com.veeville.farm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.adapter.LightActivityAdapter;
import com.veeville.farm.helper.DashBoardDataClasses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class LightActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        setUpToolbar();
        setUpSoilMoistureRecyclerview();
    }

    private void setUpToolbar(){

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Light-Farm1");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }
    void setUpSoilMoistureRecyclerview() {

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        RecyclerView lightRecyclerview = findViewById(R.id.soil_ph_recyclerview);
        lightRecyclerview.setLayoutManager(manager);
        LightActivityAdapter adapter = new LightActivityAdapter(getApplicationContext(), formData());
        lightRecyclerview.setAdapter(adapter);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    List<Object> formData() {

        List<Object> soilMoistureDatas = new ArrayList<>();
        List<String> months = new ArrayList<>();
        months.add("Today");
        months.add("Yesterday");
        months.add(getDate(-2));
        months.add(getDate(-3));
        months.add(getDate(-4));
        for (int j = 5; j < 15; j++) {
            String value;
            if (j < 10) {
                value = "0" + j;
            } else {
                value = j + "";
            }
            Random r = new Random();
            int i1 = r.nextInt(1075 - 1000) + 1000;
            DashBoardDataClasses.LightData.LightValues values = new DashBoardDataClasses.LightData.LightValues(value, "" + i1);
        }
        for (int i = 0; i < 1; i++) {
            List<DashBoardDataClasses.LightData.LightValues> soilMoistureValues2 = new ArrayList<>();
            for (int j = 5; j < 20; j++) {
                Random r = new Random();
                int i1 = r.nextInt(1075 - 1000) + 1000;
                String value;
                if (j < 10) {
                    value = "0" + j;
                } else {
                    value = j + "";
                }
                DashBoardDataClasses.LightData.LightValues values = new DashBoardDataClasses.LightData.LightValues(value, "" + i1);
                soilMoistureValues2.add(values);
            }
            DashBoardDataClasses.LightData soilMoistureData1 = new DashBoardDataClasses.LightData(months.get(i), "Bengaluru", soilMoistureValues2);
            soilMoistureDatas.add(soilMoistureData1);
        }
        soilMoistureDatas.add(new DashBoardDataClasses.SensorGraph("1D", 0));
        return soilMoistureDatas;
    }

    String getDate(int days) {

        String format = "dd MMM yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return dateFormat.format(calendar.getTime());

    }
}
