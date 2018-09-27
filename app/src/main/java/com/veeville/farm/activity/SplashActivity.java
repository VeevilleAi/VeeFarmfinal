package com.veeville.farm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.veeville.farm.R;
import com.veeville.farm.helper.AppSingletonClass;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    private String TAG = "SplashActivity";
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_animation);
        ImageView title, animation;
        title = findViewById(R.id.title);
        animation = findViewById(R.id.animation_icon);
        Glide.with(getApplicationContext()).load(R.drawable.splash_icon_new_gif1).into(animation);
        Glide.with(getApplicationContext()).load(R.drawable.splash_icon_new_gif2).into(title);
        waitForSplashScreen();
        AppSingletonClass.logDebugMessage(TAG, "SplashActivity started");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            AppSingletonClass.logDebugMessage(TAG, "SplashActivity finished");
            Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
            startActivity(intent);
            finish();
        }
    };

    void waitForSplashScreen() {
        handler = new Handler();
        handler.postDelayed(runnable, 3500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }


}