package com.veeville.farm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.adapter.SoilPHActivityAdapter;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.DashBoardDataClasses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SoilPhActivity extends AppCompatActivity {

    private final String TAG = SoilPhActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_ph);
        setUpToolbar();
        setUpSoilPhRecyclerview();
        logMessage("onCreate called");

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
        toolbar.setTitle("Soil pH - Farm1");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    void setUpSoilPhRecyclerview() {

        RecyclerView soilPHRecyclerview = findViewById(R.id.soil_ph_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        soilPHRecyclerview.setLayoutManager(manager);
        SoilPHActivityAdapter adapter = new SoilPHActivityAdapter(formData(), SoilPhActivity.this);
        soilPHRecyclerview.setAdapter(adapter);

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

        List<Object> soilPHList = new ArrayList<>();
        List<String> months = new ArrayList<>();

        months.add(getDate(0));
        months.add(getDate(-7));
        months.add(getDate(-14));
        months.add(getDate(-21));
        for (int i = 0; i < 1; i++) {
            DashBoardDataClasses.SoilPH ph = new DashBoardDataClasses.SoilPH(months.get(i), "Bengaluru", "6.1");
            soilPHList.add(ph);
        }
        soilPHList.add(new DashBoardDataClasses.SensorGraph("1D", 0));
        return soilPHList;

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

    private void logErrorMessage(String logErrorMessage) {
        AppSingletonClass.logErrorMessage(TAG, logErrorMessage);
    }
}
