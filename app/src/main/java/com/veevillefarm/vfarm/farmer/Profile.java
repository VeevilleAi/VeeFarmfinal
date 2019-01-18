package com.veevillefarm.vfarm.farmer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.helper.AppSingletonClass;

public class Profile extends AppCompatActivity {

    private final String TAG = Profile.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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

    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }

}
