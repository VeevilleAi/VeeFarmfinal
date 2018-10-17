package com.veeville.farm.farmer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.veeville.farm.R;
import com.veeville.farm.adapter.FarmAdapter;
import com.veeville.farm.helper.Farm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FarmsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farms_dash_board);
        setUpToolbar();
        setUpRecyclerview();
        addNewFarm();
    }

    private void addNewFarm() {
        FloatingActionButton addFarm = findViewById(R.id.add_new_farm);
        addFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FarmRegistrationActivity.class);
                startActivity(intent);
            }
        });


    }

    void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Farms");
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
        FarmAdapter adapter = new FarmAdapter(getFarms(), getApplicationContext());
        recyclerView.setAdapter(adapter);

    }


    private List<List<Farm>> getFarms() {

        List<List<Farm>> lists = new ArrayList<>();
        for (int i = 1; i <= 1; i++) {
            List<Farm> farms = new ArrayList<>();
            farms.add(new Farm("Farm" + i));
            farms.add(new Farm("Farm" + (i + 1)));
            lists.add(farms);
        }
        return lists;
    }
}
