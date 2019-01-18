package com.veevillefarm.vfarm.helper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


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
        final String fcmToken = intent.getStringExtra("fcmToken");
        new Thread(new Runnable() {
            @Override
            public void run() {
                logMessage("starting a thread from Service");
                uploadContact(contactName,email,photoUrl,fcmToken);
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void uploadContact(String name,String email,String photoUrl,String fcmToken){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            logMessage(email+name+fcmToken+photoUrl);
            Connection connection = DriverManager.getConnection(com.veevillefarm.vfarm.helper.FarmChatDtabaseCredentials.Url + FarmChatDtabaseCredentials.database, FarmChatDtabaseCredentials.UserName, FarmChatDtabaseCredentials.Password);
            String query = "INSERT INTO VeeFarmContacts Values('"+email+"','"+name+"','"+fcmToken+"','"+photoUrl+"');";
            logMessage("insertChatMessageTodatabase: success-------"+query);
            Statement stmt = connection.createStatement();
            String deleteQuery = "delete from VeeFarmContacts where email = '"+email+"'";
            stmt.executeUpdate(deleteQuery);
            logMessage("delete query:"+deleteQuery);
            stmt.executeUpdate(query);
            stmt.close();
            connection.close();

        }catch (ClassNotFoundException e){
            logErrorMessage(e.toString());
            }catch (SQLException e) {
            logErrorMessage(e.toString());

        }
        stopSelf();

    }


    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    //use this function everywhere for logging debug mesage
    private void logMessage(String message){
        AppSingletonClass.logMessage(TAG,message);
    }

    private void logErrorMessage(String errorMessage){
        AppSingletonClass.logErrorMessage(TAG,errorMessage);
    }
}
