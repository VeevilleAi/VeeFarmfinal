package com.veeville.farm.helper;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by user on 10-07-2017.
 */

public class AppSingletonClass extends Application {
    private final String TAG = AppSingletonClass.class.getSimpleName();
    private RequestQueue mRequestQueue;
    public static String MODEL_FILE;
    public static String LABEL_FILE;
    public static String TOOL_BAR_TITLE;
    private static AppSingletonClass mInstance;
    public static boolean isOTPsent = false;
    public static String verifixationIdMain;
    public static FirebaseAuth mFirebaseAuth;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppSingletonClass getInstance() {
        return mInstance;
    }

    public static void logDebugMessage(String TAG, String messageToLog) {
        Log.d("VeevilleFarm " + TAG, messageToLog);
    }


    public static void logErrorMessage(String TAG, String errorMessageToPrint) {
        Log.e("VeevilleFarm " + TAG, errorMessageToPrint);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public <T> void removeFromRequestQueue() {
        getRequestQueue().cancelAll(TAG);
    }
}
