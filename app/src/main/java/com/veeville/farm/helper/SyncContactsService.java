package com.veeville.farm.helper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SyncContactsService extends Service {
    private final String TAG = SyncContactsService.class.getSimpleName();
    public SyncContactsService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synContacts();
            }
        }).start();
        return START_STICKY;
    }

    private void synContacts(){

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(FarmChatDtabaseCredentials.Url + FarmChatDtabaseCredentials.database, FarmChatDtabaseCredentials.UserName, FarmChatDtabaseCredentials.Password);
            String query = "SELECT * from Contacts";
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            List<ChatContact> chatContacts = new ArrayList<>();
            while (resultSet.next()){

                String name = resultSet.getString("contact_name");
                String email = resultSet.getString("contact_email");
                String picUrl = resultSet.getString("contact_profile_link");
                ChatContact contact = new ChatContact(name,email,picUrl,"");
                chatContacts.add(contact);
            }
            Log.d(TAG, "insertChatMessageTodatabase: success"+query);
            ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());
            database.insertContacts(chatContacts);
            stmt.close();
            connection.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
