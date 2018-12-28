package com.veeville.farm.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
    private String TAG = DashBoardActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        logMessage("onCreate Called");
        setUpToolbar();
        setupRecyclerview();
        checkDatabaseForPrice();

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
        logMessage("onPause Called");
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
    protected void onRestart() {
        super.onRestart();
        logMessage("onRestart called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logMessage("onSaveInstanceState called");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        logMessage("onRestoreInstanceState called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash_board_menu, menu);
        return true;
    }


    //setup custom toolbar
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

    private List<DashBoardClass> getDetails() {
        DashBoardClass chat = new DashBoardClass("Chat", "Cerebro", "", R.drawable.cerebro_iocn);
        DashBoardClass workFlow = new DashBoardClass("WorkFlow", "WorkFlow", "", R.drawable.workflow_icon);
        DashBoardClass soilPh = new DashBoardClass("SoilpH", "Soil pH", "", R.drawable.ph_icon);
        DashBoardClass humidity = new DashBoardClass("Humidity", "Humidity", "", R.drawable.humidity_icon);
        DashBoardClass temperature = new DashBoardClass("Temperature", "Soil Temp", "", R.drawable.temperature_icon);
        DashBoardClass weather = new DashBoardClass("Weather", "Weather", "", R.drawable.partly_cloudy_weathericon);
        DashBoardClass light = new DashBoardClass("Light", "Light", "", R.drawable.lux_icon);
        DashBoardClass soilMoisture = new DashBoardClass("SoilMoisture", "Soil Moisture", "", R.drawable.soil_moisture_icon);
        DashBoardClass farms = new DashBoardClass("Farm", "Farms", "", R.drawable.farms_icon);
        DashBoardClass marketPlace = new DashBoardClass("MarketPlace", "Market Place", "", R.drawable.market_icon);

        DashBoardClass agriNews = new DashBoardClass("News", "News", "", R.drawable.market_icon);

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
        dashBoardClasses.add(agriNews);
        return dashBoardClasses;

    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

    private void updateDataBase(List<Fruit> fruits) {
        ChatBotDatabase database = new ChatBotDatabase(DashBoardActivity.this);
        database.updatePriceOfFruitAndVeg(fruits);
    }

    private void setupRecyclerview() {
        LinearLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(manager);
        AppDashBoardAdapter adapter = new AppDashBoardAdapter(this, getDetails());
        recyclerView.setAdapter(adapter);
    }

    private void logErrorMessage(String logErrorMessage) {
        AppSingletonClass.logErrorMessage(TAG, logErrorMessage);
    }


    //asynctask for downloading vegetable price list and inserting into database
    // have to do web scrolling
    class AsyncTaskVegPrice extends AsyncTask<Void, Void, List<Fruit>> {

        @Override
        protected List<Fruit> doInBackground(Void... voids) {
            Document doc;
            List<Fruit> names = new ArrayList<>();
            try {
                doc = Jsoup.connect("https://www.livechennai.com/Vegetable_price_chennai.asp").get();
                Element table = doc.select("table").get(0);
                Elements rows = table.select("tr");
                long timestamp = System.currentTimeMillis() / 1000;
                for (int i = 1; i < rows.size(); i++) {
                    try {
                        logMessage("vegetable");
                        Elements columns = rows.get(i).select("td");
                        String nameWithValue = columns.get(1).text();
                        String[] array = nameWithValue.split("\\(");
                        String name = array[0].trim();
                        String pieceorKg = "(" + array[1];
                        String price = columns.get(2).text();
                        Fruit fruit = new Fruit(name, price, pieceorKg, "", false, timestamp);
                        names.add(fruit);

                    } catch (Exception e) {
                        logMessage(e.toString());
                    }
                }
            } catch (IOException e) {
                logMessage(e.toString());
            }
            return names;
        }

        @Override
        protected void onPostExecute(List<Fruit> fruitList) {
            super.onPostExecute(fruitList);
            updateDataBase(fruitList);
        }
    }


    //asynctask for downloading fruits price and inserting into database
    // have to do web scrolling
    private class AsyncTaskFruitPrice extends AsyncTask<Void, Void, List<Fruit>> {

        @Override
        protected List<Fruit> doInBackground(Void... voids) {
            Document doc;
            List<Fruit> names = new ArrayList<>();
            try {
                doc = Jsoup.connect("https://www.livechennai.com/Fruits_price_chennai.asp").get();
                Element table = doc.select("table").get(1);
                Elements rows = table.select("tr");
                long timestamp = System.currentTimeMillis() / 1000;
                for (int i = 1; i < rows.size(); i++) {
                    logMessage("Fruit");
                    try {
                        Elements columns = rows.get(i).select("td");
                        String nameWithValue = columns.get(1).text();
                        String[] array = nameWithValue.split("\\(");
                        String name = array[0].trim();
                        String pieceorKg = "(" + array[1];
                        String price = columns.get(2).text();
                        Fruit fruit = new Fruit(name, price, pieceorKg, "", true, timestamp);
                        names.add(fruit);

                    } catch (Exception e) {
                        logErrorMessage(e.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                logErrorMessage(e.toString());
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

}
