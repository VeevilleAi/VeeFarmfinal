package com.veeville.farm.farmer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.veeville.farm.R;

import java.util.Objects;

public class FarmRegistrationActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    private String TAG = "FarmRegistrationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_registration);
        setUpToolbar();
        setUpListener();
        FloatingActionButton actionButton = findViewById(R.id.submit);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FarmsActivity.class);
                startActivity(intent);
            }
        });
    }

    void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Farm Registration");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setUpListener() {

        LinearLayout addFarmLocation = findViewById(R.id.add_farm_in_google_map);
        addFarmLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DrawFarmInMapActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestcode:" + requestCode + "\tresult code:" + resultCode);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            double[] longitudes = data.getDoubleArrayExtra("Longitudes");
            double[] latitudes = data.getDoubleArrayExtra("Latitudes");
            int acre = data.getIntExtra("acre", 1);
            int gunta = data.getIntExtra("gunta", 1);
            Log.d(TAG, "onActivityResult: ");
            Toast.makeText(this, acre + " acre " + gunta + " gunta", Toast.LENGTH_SHORT).show();
            showFarmInStaticMap(latitudes[0], longitudes[0]);
        } else {
            Log.d(TAG, "onActivityResult: ");
        }
    }

    private void showFarmInStaticMap(double latitude, double longitude) {

        String url = "https://maps.googleapis.com/maps/api/staticmap?maptype=hybrid&size=600x300&zoom=16&center=" + latitude + "," + longitude + "&key=AIzaSyBeilqJcTJPyZ--59DXSsK1mWrWL3guh8k";
        Log.d(TAG, "showFarmInStaticMap: " + url);
        ImageView imageView = findViewById(R.id.image);
        Glide.with(getApplicationContext()).load(url).into(imageView);

    }
}
