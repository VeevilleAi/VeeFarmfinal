package com.veeville.farm.helper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*this service to add contact to Server database*/
public class AddContactToServerService extends Service {
    private final String TAG = AddContactToServerService.class.getSimpleName();
    public AddContactToServerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String contactName = intent.getStringExtra("name");
        final String email = intent.getStringExtra("email");
        final String photoUrl = intent.getStringExtra("photoUrl");
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadContact(contactName,email,photoUrl);
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void uploadContact(String name,String email,String photoUrl){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(FarmChatDtabaseCredentials.Url + FarmChatDtabaseCredentials.database, FarmChatDtabaseCredentials.UserName, FarmChatDtabaseCredentials.Password);
            String query = "INSERT INTO Contacts Values(0,'"+name+"','"+email+"','"+photoUrl+"');";
            Log.d(TAG, "insertChatMessageTodatabase: success-------"+query);
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            connection.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
        stopSelf();

    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }
}
