package com.veeville.farm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.adapter.TemperatureActivityAdapter;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.DashBoardDataClasses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class TemperatureActivity extends AppCompatActivity {
    private final String TAG = TemperatureActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        setUpToolbar();
        setUpHumidityRecyclerview();
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
        toolbar.setTitle("Temperature-Farm1");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    void setUpHumidityRecyclerview() {

        RecyclerView temperatureRecyclerview = findViewById(R.id.temperature_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        temperatureRecyclerview.setLayoutManager(manager);
        TemperatureActivityAdapter adapter = new TemperatureActivityAdapter(getApplicationContext(), formData());
        temperatureRecyclerview.setAdapter(adapter);

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
        String title = item.getTitle().toString();
        logMessage(title);
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private List<Object> formData() {

        List<DashBoardDataClasses.TemperatureData.TempValue> soilMoistureValues1 = new ArrayList<>();
        List<Object> soilMoistureDatas = new ArrayList<>();
        List<String> months = new ArrayList<>();
        months.add("Today");
        months.add("Yesterday");
        months.add(getDate(-2));
        months.add(getDate(-3));
        months.add(getDate(-4));
        for (int j = 0; j < 24; j++) {
            String value;
            if (j < 10) {
                value = "0" + j;
            } else {
                value = j + "";
            }
            Random r = new Random();
            int i1 = r.nextInt(27 - 23) + 23;
            DashBoardDataClasses.TemperatureData.TempValue values = new DashBoardDataClasses.TemperatureData.TempValue(value, "" + i1);
            soilMoistureValues1.add(values);
        }
        DashBoardDataClasses.TemperatureData soilMoistureData = new DashBoardDataClasses.TemperatureData(months.get(0), "Bengaluru", soilMoistureValues1);
        //soilMoistureDatas.add(soilMoistureData);
        for (int i = 0; i < 1; i++) {
            List<DashBoardDataClasses.TemperatureData.TempValue> soilMoistureValues2 = new ArrayList<>();
            for (int j = 5; j < 20; j++) {
                Random r = new Random();
                int i1 = r.nextInt(27 - 23) + 23;
                String value;
                if (j < 10) {
                    value = "0" + j;
                } else {
                    value = j + "";
                }
                DashBoardDataClasses.TemperatureData.TempValue values = new DashBoardDataClasses.TemperatureData.TempValue(value, "" + i1);
                soilMoistureValues2.add(values);
            }
            DashBoardDataClasses.TemperatureData soilMoistureData1 = new DashBoardDataClasses.TemperatureData(months.get(i), "Bengaluru", soilMoistureValues2);
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

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }
}
