package com.veeville.farm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.veeville.farm.R;
import com.veeville.farm.adapter.WeatherActivityAdapter;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.ChatmessageDataClasses;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class WeatherActivity extends AppCompatActivity {

    private WeatherActivityAdapter adapter;
    private String TAG = "WeatherActivity";
    private ProgressBar progressBar;
    private List<ChatmessageDataClasses.WeatherData> weatherDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        setUpToolbar();
        setUpWeatherRecyclerview();
        progressBar = findViewById(R.id.image_upload_progressbar);
    }

    private void setUpToolbar() {

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Weather-Farm1");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    private void setUpWeatherRecyclerview() {

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView weatherRecyclerview = findViewById(R.id.weather_recyclerview);
        weatherRecyclerview.setLayoutManager(manager);
        adapter = new WeatherActivityAdapter(weatherDataList, getApplicationContext());
        weatherRecyclerview.setAdapter(adapter);
        String city = "Bengaluru";
        boolean hasCity = getIntent().hasExtra("city");
        if (hasCity) {
            city = getIntent().getStringExtra("city");
        }
        AppSingletonClass.logDebugMessage(TAG, "getting weather for the city:" + city);
        weatherApidata(city);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void weatherApidata(final String city) {
        String url = "http://api.worldweatheronline.com/premium/v1/weather.ashx?key=461942f4e95c4a1d8fd114744181009&q=" + city + "&format=json&num_of_days=10";
        AppSingletonClass.logDebugMessage(TAG, "weather request uri:" + url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AppSingletonClass.logDebugMessage(TAG, "weather successfully retrieved:\n" + response.toString());
                progressBar.setVisibility(View.GONE);
                performWeatherData(response, city);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                AppSingletonClass.logDebugMessage(TAG, error.toString());
                if (error instanceof NoConnectionError) {
                    Toast.makeText(WeatherActivity.this, "Please Check Net Connection", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(WeatherActivity.this, "something went wrong,please try again.", Toast.LENGTH_SHORT).show();
                    try{
                        String s = new String(error.networkResponse.data);
                        Log.d(TAG, "onErrorResponse: error message:"+s);
                    }catch (Exception e){
                        Log.e(TAG, "onErrorResponse: "+e.toString() );
                    }
                }
            }
        });
        AppSingletonClass.getInstance().addToRequestQueue(request);
    }

    private void performWeatherData(JSONObject jsonObject, String city) {


        try {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray weather = data.getJSONArray("weather");
            Log.d(TAG, "performWeatherData: days:" + weather.length());
            for (int i = 0; i < weather.length(); i++) {
                JSONObject oneDayDetails = weather.getJSONObject(i);
                String date = oneDayDetails.getString("date");
                String[] dates = date.split("-");
                int year = Integer.parseInt(dates[0]);
                int month = Integer.parseInt(dates[1]);
                int day = Integer.parseInt(dates[2]);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.YEAR, year);
                String format = "dd MMM yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
                date = dateFormat.format(calendar.getTime());
                if (i == 0) {
                    date = "Today";
                } else if (i == 1) {
                    date = "Tomorrow";
                }
                String maxTemperature = oneDayDetails.getString("maxtempC");
                List<List<String>> houlyDataLists = new ArrayList<>();
                String prec = null, hum = null, wind = null;
                String weatherIconUrl = null;
                JSONArray hourlyData = oneDayDetails.getJSONArray("hourly");
                for (int j = 0; j < hourlyData.length(); j++) {
                    JSONObject threeHourData = hourlyData.getJSONObject(j);

                    int time = Integer.parseInt(threeHourData.getString("time"));
                    int hour = time / 100;
                    int min = time % 100;
                    String timeThreeHour = hour + ":" + min;
                    prec = threeHourData.getString("precipMM");
                    hum = threeHourData.getString("humidity");
                    wind = threeHourData.getString("WindGustKmph");
                    String tempC = threeHourData.getString("tempC");
                    List<String> threeHOurlyStringList = new ArrayList<>();
                    threeHOurlyStringList.add(timeThreeHour);
                    threeHOurlyStringList.add(tempC);
                    JSONArray weatherIconUrlArray = threeHourData.getJSONArray("weatherIconUrl");
                    weatherIconUrl = weatherIconUrlArray.getJSONObject(0).getString("value");
                    houlyDataLists.add(threeHOurlyStringList);
                }
                Log.d(TAG, "performWeatherData: hourly size:" + houlyDataLists.size());
                long timestamp = System.currentTimeMillis();
                ChatmessageDataClasses.WeatherData weatherData = new ChatmessageDataClasses.WeatherData(date, city, weatherIconUrl, maxTemperature, prec, hum, wind, houlyDataLists,timestamp);
                weatherDataList.add(weatherData);

            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppSingletonClass.logDebugMessage(TAG, "ontop called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppSingletonClass.logDebugMessage(TAG, "onPause called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppSingletonClass.logDebugMessage(TAG, "onResume called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppSingletonClass.logDebugMessage(TAG, "onDestroy called");
    }
}