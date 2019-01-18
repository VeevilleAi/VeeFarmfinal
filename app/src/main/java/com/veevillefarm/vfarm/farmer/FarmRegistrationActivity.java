package com.veevillefarm.vfarm.farmer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.helper.AppSingletonClass;

import java.util.Objects;
/*thi activity used to add new Farm or register new farm like name,area, drawing in google map etc...*/
public class FarmRegistrationActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    private final String TAG = FarmRegistrationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_registration);
        setUpToolbar();
        setUpListener();
        FloatingActionButton actionButton = findViewById(R.id.submit);
        Button button = findViewById(R.id.add_previous_yield);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddPreviousYieldFarmActivity.class);
                startActivity(intent);

            }
        });
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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

    //setting up custom toolbar for this activity
    private void setUpToolbar() {
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

    //when user done with filling all items update it to server
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    //when user got all points(Lat and Lng) to calculate area
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            double[] longitudes = data.getDoubleArrayExtra("Longitudes");
            double[] latitudes = data.getDoubleArrayExtra("Latitudes");
            int acre = data.getIntExtra("acre", 1);
            int gunta = data.getIntExtra("gunta", 1);
            Toast.makeText(this, acre + " acre " + gunta + " gunta", Toast.LENGTH_SHORT).show();
            showFarmInStaticMap(latitudes[0], longitudes[0]);
            updateArea(acre + "acre " + gunta + " gunta");
        } else {
            logMessage("onActivity result didnt gt result");
        }
    }

    //creating static google map using lat lng of user selected
    private void showFarmInStaticMap(double latitude, double longitude) {
        String url = "https://maps.googleapis.com/maps/api/staticmap?maptype=hybrid&size=600x300&zoom=16&center=" + latitude + "," + longitude + "&key=AIzaSyBeilqJcTJPyZ--59DXSsK1mWrWL3guh8k";
        ImageView imageView = findViewById(R.id.image);
        CardView cardView = findViewById(R.id.card);
        cardView.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext()).load(url).into(imageView);
    }

    //updating area of farm
    private void updateArea(String area) {
        TextInputLayout textInputLayout = findViewById(R.id.area_id);
        textInputLayout.setVisibility(View.VISIBLE);
        EditText areaTemp = findViewById(R.id.area);
        areaTemp.setText(area);
    }

    //use this method to ebugg message
    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }
}
