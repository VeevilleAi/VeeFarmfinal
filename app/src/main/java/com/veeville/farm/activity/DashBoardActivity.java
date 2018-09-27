package com.veeville.farm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.veeville.farm.R;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.ChatBotDatabase;

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

}
