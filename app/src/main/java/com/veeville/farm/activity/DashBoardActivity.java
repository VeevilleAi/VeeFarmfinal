package com.veeville.farm.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.adapter.AppDashBoardAdapter;
import com.veeville.farm.farmer.FarmerProfileActivity;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.ChatBotDatabase;
import com.veeville.farm.helper.DashBoardClass;
import com.veeville.farm.helper.Fruit;
import com.veeville.farm.helper.FruitNames;
import com.veeville.farm.helper.VegetableNames;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DashBoardActivity extends AppCompatActivity {
    private String TAG = "DashBoardActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setUpToolbar();
        setupRecyclerview();
        checkDatabaseForPrice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("DashBoard");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        switch (title) {
            case "Logout":
                ChatBotDatabase database = new ChatBotDatabase(getApplicationContext());
                database.deleteMobileNumber();
                AppSingletonClass.logDebugMessage(TAG, "user logged out");
                finish();
                break;
            case "Profile":
                Intent intent = new Intent(getApplicationContext(), FarmerProfileActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    private void insertVegAndFruitToDatabase() {
        List<Fruit> names = FruitNames.getFruitNames();
        ChatBotDatabase database = new ChatBotDatabase(getApplicationContext());
        database.insertVegetableAndFruitPrices(names);
        //insert vegetabl names
        List<Fruit> vegNames = VegetableNames.getAllVegetableNames();
        database.insertVegetableAndFruitPrices(vegNames);
        new AsyncTaskFruitPrice().execute();
    }

    private void checkDatabaseForPrice() {
        ChatBotDatabase database = new ChatBotDatabase(getApplicationContext());
        List<Fruit> fruitList = database.getAllPrices();
        if (fruitList.size() == 0) {
            insertVegAndFruitToDatabase();
        }

    }


    class AsyncTaskVegPrice extends AsyncTask<Void, Void, List<Fruit>> {


        @Override
        protected List<Fruit> doInBackground(Void... voids) {
            Document doc;
            List<Fruit> names = new ArrayList<>();
            try {
                doc = Jsoup.connect("https://www.livechennai.com/Vegetable_price_chennai.asp").get();
                Log.d(TAG, "scrapeWebsite: " + doc.title());
                Element table = doc.select("table").get(1);
                Elements rows = table.select("tr");
                Log.d(TAG, "doInBackground: rows:" + rows.size());
                long timestamp = System.currentTimeMillis() / 1000;
                for (int i = 1; i < rows.size(); i++) {
                    try {
                        Elements columns = rows.get(i).select("td");
                        Log.d(TAG, "doInBackground: column size:" + columns.size());
                        String nameWithValue = columns.get(1).text();
                        String[] array = nameWithValue.split("\\(");
                        String name = array[0].trim();
                        String pieceorKg = "(" + array[1];
                        String price = columns.get(2).text();
                        Fruit fruit = new Fruit(name, price, pieceorKg, "", false, timestamp);
                        Log.d(TAG, "doInBackground: " + fruit.toString());
                        names.add(fruit);

                    } catch (Exception e) {
                        Log.d(TAG, "doInBackground: " + e.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return names;
        }

        @Override
        protected void onPostExecute(List<Fruit> fruitList) {
            super.onPostExecute(fruitList);
            updateDataBase(fruitList);
        }
    }

    private class AsyncTaskFruitPrice extends AsyncTask<Void, Void, List<Fruit>> {

        @Override
        protected List<Fruit> doInBackground(Void... voids) {
            Document doc;
            List<Fruit> names = new ArrayList<>();
            try {
                doc = Jsoup.connect("https://www.livechennai.com/Fruits_price_chennai.asp").get();
                Log.d(TAG, "scrapeWebsite: " + doc.title());
                Element table = doc.select("table").get(1);
                Elements rows = table.select("tr");
                Log.d(TAG, "doInBackground: rows:" + rows.size());
                long timestamp = System.currentTimeMillis() / 1000;
                for (int i = 1; i < rows.size(); i++) {

                    try {
                        Elements columns = rows.get(i).select("td");
                        Log.d(TAG, "doInBackground: column size:" + columns.size());
                        String nameWithValue = columns.get(1).text();
                        String[] array = nameWithValue.split("\\(");
                        String name = array[0].trim();
                        String pieceorKg = "(" + array[1];
                        String price = columns.get(2).text();
                        Fruit fruit = new Fruit(name, price, pieceorKg, "", true, timestamp);
                        Log.d(TAG, "doInBackground: " + fruit.toString());
                        names.add(fruit);

                    } catch (Exception e) {
                        Log.d(TAG, "doInBackground: " + e.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return names;
        }

        @Override
        protected void onPostExecute(List<Fruit> fruits) {
            super.onPostExecute(fruits);
            updateDataBase(fruits);
            new AsyncTaskVegPrice().execute();

        }
    }

    private void updateDataBase(List<Fruit> fruits) {
        ChatBotDatabase database = new ChatBotDatabase(DashBoardActivity.this);
        database.updatePriceOfFruitAndVeg(fruits);
    }

    private void setupRecyclerview() {
        LinearLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(manager);
        AppDashBoardAdapter adapter = new AppDashBoardAdapter(getApplicationContext(), getDetails());
        recyclerView.setAdapter(adapter);

    }

    private List<DashBoardClass> getDetails() {
        DashBoardClass chat = new DashBoardClass("Chat", "Cerebro", "", R.drawable.cerebro_iocn);
        DashBoardClass workFlow = new DashBoardClass("WorkFlow", "WorkFLow", "", R.drawable.workflow_icon);
        DashBoardClass soilPh = new DashBoardClass("SoilpH", "Soil pH", "", R.drawable.ph_icon);
        DashBoardClass humidity = new DashBoardClass("Humidity", "Humidity", "", R.drawable.humidity_icon);
        DashBoardClass temperature = new DashBoardClass("Temperature", "Soil Temp", "", R.drawable.temperature_icon);
        DashBoardClass weather = new DashBoardClass("Weather", "Weather", "", R.drawable.partly_cloudy_weathericon);
        DashBoardClass light = new DashBoardClass("Light", "Light", "", R.drawable.lux_icon);
        DashBoardClass soilMoisture = new DashBoardClass("SoilMoisture", "Soil Moisture", "", R.drawable.soil_moisture_icon);
        DashBoardClass farms = new DashBoardClass("Farm", "Farms", "", R.drawable.farms_icon);
        DashBoardClass marketPlace = new DashBoardClass("MarketPlace", "Market Place", "", R.drawable.market_icon);


        List<DashBoardClass> dashBoardClasses = new ArrayList<>();
        dashBoardClasses.add(chat);
        dashBoardClasses.add(workFlow);
        dashBoardClasses.add(soilPh);
        dashBoardClasses.add(humidity);
        dashBoardClasses.add(temperature);
        dashBoardClasses.add(weather);
        dashBoardClasses.add(light);
        dashBoardClasses.add(soilMoisture);
        dashBoardClasses.add(farms);
        dashBoardClasses.add(marketPlace);
        return dashBoardClasses;

    }
}
