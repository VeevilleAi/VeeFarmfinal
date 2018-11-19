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

    private final String TAG = "FarmerRegistration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_registration);
        getDataFromForm();
    }

    private void getDataFromForm() {
        String name, address, phoneNumber, emailId, profilepic;
        name = address = phoneNumber = emailId = profilepic = "";
        Farmer farmer = new Farmer(name, address, phoneNumber, emailId, profilepic);
        setUpToolbar();
        uploadFarmerDetails(farmer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void uploadFarmerDetails(Farmer farmer) {

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void logMessage(String message) {
        AppSingletonClass.logDebugMessage(TAG, message);
    }

    private void logErrorMessage(String errorMessage) {
        AppSingletonClass.logErrorMessage(TAG, errorMessage);
    }

    void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Registration");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
