package com.veeville.farm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.veeville.farm.R;
import com.veeville.farm.helper.AppSingletonClass;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    private String TAG = "SplashActivity";
    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(SplashActivity.this);
            if (account != null) {
                Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                startActivity(intent);
                finish();
                Log.d(TAG, "onStart: user not yet registered");
            } else {
                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                startActivity(intent);
                finish();
                Log.d(TAG, "onStart: user already registered");
            }
            AppSingletonClass.logDebugMessage(TAG, "SplashActivity finished");
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_animation);
        ImageView title, animation;
        title = findViewById(R.id.title);
        animation = findViewById(R.id.animation_icon);
        Glide.with(getApplicationContext()).load(R.drawable.splash_icon_new_gif1).into(animation);
        Glide.with(getApplicationContext()).load(R.drawable.splash_icon_new_gif2).into(title);
        waitForSplashScreen();
        AppSingletonClass.logDebugMessage(TAG, "SplashActivity started");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

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