package com.veeville.farm.helper;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Prashant C on 11/12/18.
 */
public class InsertMessageToDatabase extends Service{

    Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: called");
    }

    private final String TAG = InsertMessageToDatabase.class.getSimpleName();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        handler = new Handler();
        final String fromAddress = intent.getStringExtra("fromAddress");
        final String toAddress = intent.getStringExtra("toAddress");
        final String message = intent.getStringExtra("chatMessage");
        final long timestamp = intent.getLongExtra("timestamp",0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp*1000);
        final String datetime = dateFormat.format(calendar.getTime());
        Log.d(TAG, "onStartCommand: datetime:"+datetime);
        new Thread(new Runnable() {
            @Override
            public void run() {
                insertChatMessageTodatabase(fromAddress,toAddress,message,datetime,timestamp);
            }
        }).start();

        return START_NOT_STICKY;
    }

    private void insertChatMessageTodatabase(String fromAddress,String toAddress,String chatMessage,String  datetime,long timstamp){

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(FarmChatDtabaseCredentials.Url + FarmChatDtabaseCredentials.database, FarmChatDtabaseCredentials.UserName, FarmChatDtabaseCredentials.Password);
            String query = "INSERT INTO ChatMessages (chat_message_id,from_address,to_address,chat_message,chat_timestamp) VALUES("+timstamp+",'"+fromAddress+"','"+toAddress+"','"+chatMessage+"','"+datetime+"');";
            Log.d(TAG, "insertChatMessageTodatabase: success-------"+query);
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            Log.d(TAG, "insertChatMessageTodatabase: success"+query);
            stmt.close();
            connection.close();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "message sent successfully", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stopSelf();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: service destroyed");
    }
}
