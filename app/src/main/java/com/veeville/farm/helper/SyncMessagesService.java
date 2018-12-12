package com.veeville.farm.helper;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Prashant C on 11/12/18.
 */
public class SyncMessagesService extends Service {
    private String TAG = SyncMessagesService.class.getSimpleName();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());
        final String fromAddress = database.getUserEmailAsFromAddress();
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadmessages(fromAddress);
            }
        }).start();

        return START_STICKY;
    }

    private void loadmessages(String fromAddress) {
        while (true) {
            try {

                long serverLatestMessageTimestamp=0;
                ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());


                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(FarmChatDtabaseCredentials.Url + FarmChatDtabaseCredentials.database, FarmChatDtabaseCredentials.UserName, FarmChatDtabaseCredentials.Password);
                Statement stmt = connection.createStatement();
                String query = "select * from ChatMessages where from_address = '"+fromAddress+"' OR to_address = '"+fromAddress+"';";
                ResultSet resultSet = stmt.executeQuery(query);
                long timestamp  = database.getMostupdatedTime();
                while (resultSet.next()){
                    String from = resultSet.getString("from_address");
                    String to = resultSet.getString("to_address");
                    String message = resultSet.getString("chat_message");
                    Date date = resultSet.getDate("chat_timestamp");
                    serverLatestMessageTimestamp  = date.getTime()/1000;
                    long messageId = resultSet.getLong("chat_message_id");
                    if(timestamp<messageId) {
                        ChatMessage chatMessage = new ChatMessage("", from, to, message, messageId);
                        database.insertChatMessage(chatMessage);
                        Log.d(TAG, "loadmessages: insertd new Message:"+message);
                    }else {
                        Log.d(TAG, "loadmessages: all messages are upto date");
                    }
                }
                database.close();
                Thread.sleep(5000);

            } catch (Exception e) {
                Log.e(TAG, "loadmessages:" + e.toString());
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
