package com.veeville.farm.farmer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.veeville.farm.R;
import com.veeville.farm.farmer.FarmerHelperClasses.Farmer;
import com.veeville.farm.helper.AppSingletonClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class FarmerRegistration extends AppCompatActivity {

    private final String TAG = FarmerRegistration.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_registration);
        getDataFromForm();
        setUpToolbar();
        String logMessage = "onCreate Called";
        logMessage(logMessage);
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

    private void getDataFromForm() {
        String name, address, phoneNumber, emailId, profilepic;
        name = address = phoneNumber = emailId = profilepic = "";
        Farmer farmer = new Farmer(name, address, phoneNumber, emailId, profilepic);
        uploadFarmerDetails(farmer);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String logMessage = "Menu Item Selected Called";
        logMessage(logMessage);
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void uploadFarmerDetails(Farmer farmer) {

        String logMessage = "uploading farmer regitration details";
        logMessage(logMessage);
        String url = "";
        JSONObject farmerObject = new JSONObject();
        try {
            farmerObject.put("farmerName", farmer.name);
            farmerObject.put("farmerAddress", farmer.address);
            farmerObject.put("farmerEmailId", farmer.emailId);
            farmerObject.put("farmerMobileNumber", farmer.mobileNumber);
            farmerObject.put("farmerProfilePic", farmer.profilePicUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, farmerObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                logMessage("Response from server:" + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logErrorMessage("Error Message from Server:" + error.toString());
            }
        });
        AppSingletonClass.getInstance().addToRequestQueue(request);
    }



    void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Registration");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

    private void logErrorMessage(String logErrorMessage) {
        AppSingletonClass.logErrorMessage(TAG, logErrorMessage);
    }

}
