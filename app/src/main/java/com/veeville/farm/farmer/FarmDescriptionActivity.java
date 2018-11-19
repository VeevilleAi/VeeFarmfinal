package com.veeville.farm.farmer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.activity.GetSensorDummyData;
import com.veeville.farm.adapter.FarmDescAdapter;
import com.veeville.farm.farmer.FarmerHelperClasses.FarmDesc;
import com.veeville.farm.farmer.FarmerHelperClasses.FarmDescMap;
import com.veeville.farm.farmer.FarmerHelperClasses.SoilTestResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FarmDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmers_farm);
        setUpToolbar();
        setUpRecyclerview();
    }


    private void setUpToolbar() {
        String farmName = getIntent().getStringExtra("farmName");
        String cropName = getIntent().getStringExtra("cropName");
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        String title = farmName + "-" + cropName;
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        String farmName = getIntent().getStringExtra("farmName");
        FarmDescAdapter adapter = new FarmDescAdapter(getApplicationContext(), getfarmDescriptions(), farmName);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private List<Object> getfarmDescriptions() {
        List<Object> objects = new ArrayList<>();
        objects.add(new FarmDesc());
        objects.add(new FarmDescMap());
        GetSensorDummyData data = new GetSensorDummyData();
        objects.add(data.insertSoilPh());
        objects.add(data.insertSoilTemperature());
        objects.add(data.insertLight());
        //objects.add(getSoilTestResults());
        objects.add(new SoilTestResult.SoilTestGraph());
        return objects;
    }
}
