package com.veevillefarm.vfarm.helper;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

/**
 * Created by user on 10-07-2017.
 * application singlton class
 * used to ebugg and erro Messages
 * maintaining Volley RequestQueue here only
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
    public static  String folderPath;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        createFolder("VeeFarm-Media");
    }

    public static synchronized AppSingletonClass getInstance() {
        return mInstance;
    }

    public static void logMessage(String TAG, String messageToLog) {
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
    public void showToastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public void createFolder(String fname){
        String myfolder= Environment.getExternalStorageDirectory()+"/"+fname;
        File f=new File(myfolder);
        if(!f.exists()) {
            logMessage(TAG,"folder does not exists");
            if (!f.mkdir()) {
                logMessage(TAG,"failed to create directory");
            } else {
                logMessage(TAG,"successfully created folder");
            }
        }
        else {
            logMessage(TAG,"folder exists");
        }
        logMessage("AppSingleTon","file directory:"+myfolder);
        folderPath = myfolder;
    }


}
