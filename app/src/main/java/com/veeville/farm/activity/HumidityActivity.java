package com.veeville.farm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.adapter.HumidityActivityAdapter;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.DashBoardDataClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        toolbar.setTitle("Humidity-Farm1");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        AppSingletonClass.logDebugMessage(TAG, "toolbar setup done");
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
    void setUpHumidityRecyclerview() {

        RecyclerView humidityRecyclerview = findViewById(R.id.humidity_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        humidityRecyclerview.setLayoutManager(manager);
        List<Object> lists = new ArrayList<>();
        lists.add(new DashBoardDataClasses.HumidityData(null, null, null));
        lists.add(new DashBoardDataClasses.SensorGraph("1D", 0));
        HumidityActivityAdapter temp = new HumidityActivityAdapter(getApplicationContext(), lists);
        humidityRecyclerview.setAdapter(temp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        AppSingletonClass.logDebugMessage(TAG, "Humidity activity finished");
        return super.onOptionsItemSelected(item);
    }

}