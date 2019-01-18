package com.veevillefarm.vfarm.helper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
* this Service used to Sync contacts and storeing in Local Database
* */
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
            String query = "SELECT * from VeeFarmContacts";
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
            List<ChatContact> chatContacts = new ArrayList<>();
            while (resultSet.next()){

                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String fcmToken = resultSet.getString("fcm_token");
                String picUrl = resultSet.getString("profie_link_url");
                ChatContact contact = new ChatContact(name,email,picUrl,"",fcmToken);
                chatContacts.add(contact);
            }
            logMessage("insertChatMessageTodatabase: success"+query);
            ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());
            database.insertContacts(chatContacts);
            stmt.close();
            connection.close();

        } catch (ClassNotFoundException e) {
            logErrorMessage(e.toString());
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            logErrorMessage(e.toString());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void logMessage(String message){
        AppSingletonClass.logMessage(TAG,message);
    }

    private void logErrorMessage(String errorMessage){
        AppSingletonClass.logErrorMessage(TAG,errorMessage);
    }
}
