package com.veeville.farm.farmer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.veeville.farm.R;
import com.veeville.farm.adapter.MarketPlaceAdapter;
import com.veeville.farm.farmer.FarmerHelperClasses.MarketPlace;
import com.veeville.farm.helper.AppSingletonClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MarketPlaceActivity extends AppCompatActivity {

    private final String TAG = MarketPlaceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place);
        setUpToolbar();
        setUpRecyclerview();
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
        toolbar.setTitle("Market Place");
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
        MarketPlaceAdapter adapter = new MarketPlaceAdapter(MarketPlaceActivity.this, getMarketPlaceItems());
        recyclerView.setAdapter(adapter);
    }

    private List<MarketPlace> getMarketPlaceItems() {
        List<MarketPlace> marketPlaceList = new ArrayList<>();
        marketPlaceList.add(new MarketPlace("http://www.arrms.org/media/blogs/blog/banana.jpg?mtime=1417645870", "Banana", "60", "50", "Vijayapur", "600 Kg", "36000", "Bananas belong to the healthiest foods in the world. They contain potassium, which can lower your blood pressure, prevent heart diseases and reduce the risk of a stroke."));
        marketPlaceList.add(new MarketPlace("https://5.imimg.com/data5/TF/US/MY-28264228/red-onion-500x500.jpg", "Onion", "20", "15", "Indi", "500", "10000", "Onions can help reduce cancer and also Diabetes, Heart Disease and Tooth Decay"));
        marketPlaceList.add(new MarketPlace("https://img.freepik.com/free-vector/fresh-tomato_1053-566.jpg?size=338&ext=jpg", "Tomato", "6", "5", "Mangalore", "100", "600", "It reduces Cardiovascular disease, including heart attacks and strokes"));
        marketPlaceList.add(new MarketPlace("https://res.cloudinary.com/hellofresh/image/upload/f_auto,fl_lossy,q_auto,w_640/v1/hellofresh_s3/image/554a3abff8b25e1d268b456d.png", "Potato", "30", "25", "Bangalore", "1000", "30000", "potatoes are naturally fat free, cholesterol free, and low in sodium. In addition, potatoes are an excellent source of vitamin C, and those eaten with the skin are a good source of potassium"));
        return marketPlaceList;
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }
}
