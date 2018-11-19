package com.veeville.farm.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.adapter.VegPriceAdapter;
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
import java.util.Objects;

public class PriceActivity extends AppCompatActivity {

    private String TAG = "PriceActivity";
    private List<Fruit> fruits = new ArrayList<>();
    private VegPriceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);
        setUpToolbar();
        setUpRecycerview();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        String city = getIntent().getStringExtra("city");
        toolbar.setTitle(city);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setUpRecycerview() {
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ChatBotDatabase database = new ChatBotDatabase(getApplicationContext());
        List<Fruit> fruitList = database.getAllPrices();
        if (fruitList.size() > 0) {
            fruits.addAll(fruitList);
        } else {
            Log.d(TAG, "setUpRecycerview: no data found");
            insertVegAndFruitToDatabase();
        }
        adapter = new VegPriceAdapter(fruits, getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                updateFilturedVeg(query);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void updateFilturedVeg(String query) {
        List<Fruit> fruitList = new ArrayList<>();
        for (Fruit fruit : fruits) {
            if (fruit.name.toLowerCase().contains(query.toLowerCase())) {
                fruitList.add(fruit);
            }
        }
        if (fruitList.size() > 0) {
            adapter.changeDataset(fruitList);
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
        ChatBotDatabase database = new ChatBotDatabase(PriceActivity.this);
        database.updatePriceOfFruitAndVeg(fruits);
        for (Fruit fruit : fruits) {
            Log.d(TAG, "updateDataBase: " + fruit.toString());

        }
        updateRecyclerview();
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

    private void updateRecyclerview() {
        for (int i = 0; i < fruits.size(); i++) {
            fruits.remove(0);
        }
        ChatBotDatabase database = new ChatBotDatabase(getApplicationContext());
        List<Fruit> fruitList = database.getAllPrices();
        for (Fruit fruit : fruitList) {
            if (fruit.price != null) {
                fruits.add(fruit);
            }
        }
        //fruits.addAll(fruitList);
        adapter.notifyDataSetChanged();
    }

    private void insertVegAndFruitToDatabase() {
        //insert fruit names
        List<Fruit> names = FruitNames.getFruitNames();
        ChatBotDatabase database = new ChatBotDatabase(getApplicationContext());
        database.insertVegetableAndFruitPrices(names);
        //insert vegetabl names
        List<Fruit> vegNames = VegetableNames.getAllVegetableNames();
        database.insertVegetableAndFruitPrices(vegNames);
        new AsyncTaskFruitPrice().execute();
    }
}
