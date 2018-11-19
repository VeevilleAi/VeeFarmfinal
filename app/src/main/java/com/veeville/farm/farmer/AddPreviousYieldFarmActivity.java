package com.veeville.farm.farmer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.adapter.AddFarmYieldAdapter;
import com.veeville.farm.farmer.FarmerHelperClasses.Yield;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddPreviousYieldFarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_previous_yield_farm);
        setUpToolbar();
        setUpRecyclerview();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Previous Years Yield");
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
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(manager);

        AddFarmYieldAdapter adapter = new AddFarmYieldAdapter(getPreviousYields());
        recyclerView.setAdapter(adapter);

    }

    private List<Yield> getPreviousYields() {
        List<Yield> yields = new ArrayList<>();
        yields.add(new Yield("2018", "Banana", "100 kg"));
        yields.add(new Yield("2017", "Mango", "45 kg"));
        yields.add(new Yield("2016", "Onion", "20 kg"));
        yields.add(new Yield("2015", "Potato", "26 kg"));
        yields.add(new Yield("2014", "Popcorn", "78 kg"));
        yields.add(new Yield("2013", "Orange", "90 kg"));
        yields.add(new Yield("2012", "Groundnut", "67 kg"));
        return yields;
    }

}
