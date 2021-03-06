package com.veevillefarm.vfarm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.adapter.SoilPHActivityAdapter;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.DashBoardDataClasses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


/*
 * this is same like soil moisture
 * here we are randomly generating Soil ph values and showing in this activity
 * data is showing in graph farm and with numbers and visuals
 */
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

    //settingup custom toolbar
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Soil pH - Farm1");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    //setting soil pH Values recyclerview
    void setUpSoilPhRecyclerview() {
        RecyclerView soilPHRecyclerview = findViewById(R.id.soil_ph_recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        soilPHRecyclerview.setLayoutManager(manager);
        SoilPHActivityAdapter adapter = new SoilPHActivityAdapter(formData(), SoilPhActivity.this);
        soilPHRecyclerview.setAdapter(adapter);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return super.onOptionsItemSelected(item);

    }

    //farming soil ph values randomly with given range
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
        logErrorMessage(days + " days");
        return dateFormat.format(calendar.getTime());
    }

    //must use this method to ebugg messages
    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }

    //must use this method to log error messages
    private void logErrorMessage(String logErrorMessage) {
        AppSingletonClass.logErrorMessage(TAG, logErrorMessage);
    }
}
