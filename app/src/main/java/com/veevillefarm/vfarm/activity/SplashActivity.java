package com.veevillefarm.vfarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.messaging.FirebaseMessaging;
import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.helper.AppSingletonClass;

import io.fabric.sdk.android.Fabric;


/*
 * splash screen time is 3.5 seconds
 * check for is user logged in
 * if no move to login page otherwise dashboard page
 */
public class SplashActivity extends AppCompatActivity {

    private String TAG = SplashActivity.class.getSimpleName();
    private int SPLASH_SCREEN_TIMEOUT;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        initializeVariables();
        makeActivityFullScreen();
        setContentView(R.layout.activity_animation);
        ImageView title, animation;
        title = findViewById(R.id.title);
        animation = findViewById(R.id.animation_icon);
        Glide.with(getApplicationContext()).load(R.drawable.splash_icon_new_gif1).into(animation);
        Glide.with(getApplicationContext()).load(R.drawable.splash_icon_new_gif2).into(title);
        waitForSplashScreen();
    }

    @Override
    protected void onStart() {
        super.onStart();
        logMessage("onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logMessage("onResume called ");
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(SplashActivity.this);
            // user islogged in
            if (account != null) {
                logMessage("user already logged in");
                Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                startActivity(intent);
                finish();
            }
            //user not logged in
            else {
                logMessage("user not yet logged in");
                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                startActivity(intent);
                finish();
            }
            AppSingletonClass.logMessage(TAG, "SplashActivity finished");
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        logMessage("onStop called");
    }

    void waitForSplashScreen() {
        handler = new Handler();
        handler.postDelayed(runnable, SPLASH_SCREEN_TIMEOUT);
    }

    //when user clicks back button emove all call backs
    @Override
    protected void onDestroy() {
        super.onDestroy();
        logMessage("onDestroy called");
        handler.removeCallbacks(runnable);

    }

    //method used to log messages
    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }

    //making activity full screen
    private void makeActivityFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    //intialize spash_screen_time in milli seconds and intilize crash analytics
    private void initializeVariables() {
        SPLASH_SCREEN_TIMEOUT = 3500;//milliseconds;
        //firebase crashanalytics to report crash
        Fabric.with(this, new Crashlytics());
    }

}