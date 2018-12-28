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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.veeville.farm.R;
import com.veeville.farm.activity.VerifyMobileNumberActivity;
import com.veeville.farm.farmer.FarmerHelperClasses.Farmer;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.FarmerDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//this activity is to take farmer registration like first name, last name, verified mobile number etc.
public class FarmerRegistrationActivity extends AppCompatActivity {

    private final String TAG = FarmerRegistrationActivity.class.getSimpleName();

    //getting famrer name and email from google sign activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_registration);
        setUpToolbar();
        String first_name, last_name, email;
        first_name = last_name = email = "";
        if (getIntent() != null) {
            String temp = getIntent().getStringExtra("FarmerName");
            String s[] = temp.split(" ");
            first_name = s[0];
            last_name = s[1];
            email = getIntent().getStringExtra("FarmerEmail");
        }

        final EditText firstName = findViewById(R.id.farmer_first_name);
        final EditText lastName = findViewById(R.id.farmer_last_name);
        final EditText farmerAddress = findViewById(R.id.farmer_address);
        final EditText farmerEmail = findViewById(R.id.farmer_email);
        final EditText mobileNumber = findViewById(R.id.farmer_mobile_number);

        firstName.setText(first_name);
        lastName.setText(last_name);
        farmerEmail.setText(email);

        FloatingActionButton doRegistration = findViewById(R.id.submit);
        doRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName, lName, address, email, mNumber;
                fName = firstName.getText().toString();
                lName = lastName.getText().toString();
                address = farmerAddress.getText().toString();
                email = farmerEmail.getText().toString();
                mNumber = mobileNumber.getText().toString();
                if (!fName.equals("")) {
                    if (!lName.equals("")) {
                        if (!email.equals("")) {
                            if (isValidMail(email)) {
                                if (!mNumber.equals("")) {
                                    if (isMobileNumberValid(mNumber)) {
                                        if (!address.equals("")) {
                                            Toast.makeText(FarmerRegistrationActivity.this, "success", Toast.LENGTH_SHORT).show();
                                            FarmerDetails details = new FarmerDetails(fName, lName, email, mNumber, address);
                                            startVerifyingMobileNumberWithOTP(details);
                                        }else {
                                            mobileNumber.requestFocus();
                                        }
                                    } else {
                                        mobileNumber.requestFocus();
                                    }
                                } else {
                                    mobileNumber.requestFocus();
                                }
                            } else {
                                farmerEmail.requestFocus();
                            }
                        } else {
                            farmerEmail.requestFocus();
                        }
                    } else {
                        lastName.requestFocus();
                    }
                } else {
                    firstName.requestFocus();
                }
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

    //uplading farmer details to Server
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


    //setting up custom toolbar
    void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Registration");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    //use this method to log debugg messages
    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

    //use this method to log debugg error Messages
    private void logErrorMessage(String logErrorMessage) {
        AppSingletonClass.logErrorMessage(TAG, logErrorMessage);
    }

    //using pattern matcher validate syntax of email
    private boolean isValidMail(String email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        if (!check) {
            Toast.makeText(this, "invalid email", Toast.LENGTH_SHORT).show();
        }
        return check;
    }

    // validate mobile number
    private boolean isMobileNumberValid(String s) {
        Pattern p = Pattern.compile("(0/91)?[6-9][0-9]{9}");
        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    //start next activity to verify mobile number with OTP
    private void startVerifyingMobileNumberWithOTP(FarmerDetails details) {
        Intent intent = new Intent(getApplicationContext(), VerifyMobileNumberActivity.class);
        intent.putExtra("FarmerDetails", details);
        startActivity(intent);
    }

}
