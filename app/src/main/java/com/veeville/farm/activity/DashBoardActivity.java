package com.veeville.farm.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.veeville.farm.R;
import com.veeville.farm.farmer.FarmsActivity;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.ChatBotDatabase;
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
        setContentView(R.layout.activity_main234);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("DashBoard");
        setSupportActionBar(toolbar);
        CardView humidityCard = findViewById(R.id.humidity_card);
        CardView weatherCard = findViewById(R.id.weather_card);
        CardView chatCard = findViewById(R.id.chat_card);
        CardView soilMoistureCard = findViewById(R.id.soil_moisture_card);
        CardView workFlow = findViewById(R.id.crop_yield_card);
        CardView soilPH = findViewById(R.id.soil_ph_card);
        CardView temperatureCard = findViewById(R.id.temperature_card);
        CardView lightCard = findViewById(R.id.light_card);
        CardView priceCard = findViewById(R.id.price_card);
        CardView farms = findViewById(R.id.farms);

        AppSingletonClass.logDebugMessage(TAG, "DashBoardActivity started");
        humidityCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSingletonClass.logDebugMessage(TAG, "selected HumidityCard");
                Intent intent = new Intent(getApplicationContext(), HumidityActivity.class);
                startActivity(intent);
            }
        });

        weatherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSingletonClass.logDebugMessage(TAG, "selected weatherCard");
                Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
                startActivity(intent);
            }
        });

        chatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSingletonClass.logDebugMessage(TAG, "selected chatCard");
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        soilMoistureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSingletonClass.logDebugMessage(TAG, "selected soilMoistureCard");
                Intent intent = new Intent(getApplicationContext(), SoilMoistureActivity.class);
                startActivity(intent);
            }
        });
        workFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSingletonClass.logDebugMessage(TAG, "selected workFlow");
                Intent intent = new Intent(getApplicationContext(), CropWorkFlow.class);
                startActivity(intent);
            }
        });
        soilPH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSingletonClass.logDebugMessage(TAG, "selected soilPH");
                Intent intent = new Intent(getApplicationContext(), SoilPhActivity.class);
                startActivity(intent);
            }
        });
        temperatureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSingletonClass.logDebugMessage(TAG, "selected temperatureCard");
                Intent intent = new Intent(getApplicationContext(), TemperatureActivity.class);
                startActivity(intent);

            }
        });
        lightCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSingletonClass.logDebugMessage(TAG, "selected lightCard");
                Intent intent = new Intent(getApplicationContext(), LightActivity.class);
                startActivity(intent);
            }
        });
        priceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PriceActivity.class);
                startActivity(intent);
            }
        });
        farms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FarmsActivity.class);
                startActivity(intent);
            }
        });

        checkDatabaseForPrice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ChatBotDatabase database = new ChatBotDatabase(getApplicationContext());
        database.deleteMobileNumber();
        AppSingletonClass.logDebugMessage(TAG, "user logged out");
        finish();
        return true;
    }

    void insertVegAndFruitToDatabase() {
        //insert fruit names
        List<Fruit> names = FruitNames.getFruitNames();
        ChatBotDatabase database = new ChatBotDatabase(getApplicationContext());
        database.insertVegetableAndFruitPrices(names);
        //insert vegetabl names
        List<Fruit> vegNames = VegetableNames.getAllVegetableNames();
        database.insertVegetableAndFruitPrices(vegNames);
        new AsyncTaskFruitPrice().execute();
    }
    private void checkDatabaseForPrice(){
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
    void updateDataBase(List<Fruit> fruits) {
        ChatBotDatabase database = new ChatBotDatabase(DashBoardActivity.this);
        database.updatePriceOfFruitAndVeg(fruits);
    }

}
