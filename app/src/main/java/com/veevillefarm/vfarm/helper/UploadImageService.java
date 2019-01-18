package com.veevillefarm.vfarm.helper;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import android.widget.Toast;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Created by Prashant C on 14/12/18.
 * this Service class used to upload image to Server Database
 */
public class UploadImageService  extends Service{
    private final String TAG = UploadImageService.class.getSimpleName();
    private Handler handler;
    private boolean isImageUploaded = true;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return myBinder;
    }

    public class MyBinder extends Binder{
        public UploadImageService getService(){
            return UploadImageService.this;
        }
    }
    MyBinder myBinder = new MyBinder();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        final ChatMessage chatMessage = intent.getParcelableExtra("ChatMessage");
        new Thread(new Runnable() {
            @Override
            public void run() {

                    isImageUploaded = false;
                    uploadImageToDatabase(chatMessage);
            }
        }).start();
        return START_NOT_STICKY;
    }


    private void uploadImageToDatabase(ChatMessage chatMessage)  {
        Uri uri = Uri.parse(chatMessage.messageValueOrUri);
        try {
        InputStream inputStream = getContentResolver().openInputStream(uri);
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(FarmChatDtabaseCredentials.Url + FarmChatDtabaseCredentials.database, FarmChatDtabaseCredentials.UserName, FarmChatDtabaseCredentials.Password);
            String query = " INSERT INTO ChatImages(chat_message_image_id,chat_message_image) VALUES(?,?)";

            Statement stmt = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1,chatMessage.timestamp);
            preparedStatement.setBinaryStream(2,inputStream);
            int id= preparedStatement.executeUpdate();

            logMessage("uploadImageToDatabase: mini success");
            String mainQuery = "INSERT into ChatMessages values(" + chatMessage.messageId + ",'" + chatMessage.messageType + "'," + chatMessage.messageValueId + ",'" + chatMessage.to + "','" + chatMessage.from + "'," + chatMessage.timestamp + ");";
            stmt.executeUpdate(mainQuery);
            handler.post(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getApplicationContext(), "image uploaded", Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            logErrorMessage("uploadImageToDatabase: "+e.toString() );
        }
        isImageUploaded = true;
        stopSelf();
    }
    public boolean isImageUploadSuccess(){

        return isImageUploaded;

    }
    private void logMessage(String message){
        AppSingletonClass.logMessage(TAG,message);
    }

    private void logErrorMessage(String errorMessage){
        AppSingletonClass.logErrorMessage(TAG,errorMessage);
    }

}
