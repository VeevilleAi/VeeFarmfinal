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

/**
 * Created by Prashant C on 11/12/18.
 *
 * this service class used to Store user messages to Server Database like input text message
 */
public class InsertMessageToDatabase extends Service {

    private Handler handler;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: called");
    }

    private final String TAG = InsertMessageToDatabase.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final ChatMessage chatMessage = intent.getParcelableExtra("ChatMessage");
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                insertChatMessageTodatabase(chatMessage);
            }
        }).start();

        return START_NOT_STICKY;
    }

    private void insertChatMessageTodatabase(ChatMessage chatMessage) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(FarmChatDtabaseCredentials.Url + FarmChatDtabaseCredentials.database, FarmChatDtabaseCredentials.UserName, FarmChatDtabaseCredentials.Password);
            Statement stmt = connection.createStatement();
            String query = " INSERT INTO ChatTextMessages VALUES(" + chatMessage.messageId + ",'" + chatMessage.messageValueOrUri + "');";
            stmt.executeUpdate(query);
            String mainQuery = "INSERT into ChatMessages values(" + chatMessage.messageId + ",'" + chatMessage.messageType + "'," + chatMessage.messageValueId + ",'" + chatMessage.to + "','" + chatMessage.from + "'," + chatMessage.timestamp + ");";
            final int result = stmt.executeUpdate(mainQuery);
            stmt.close();
            connection.close();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "message sent successfully  "+result, Toast.LENGTH_SHORT).show();
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
