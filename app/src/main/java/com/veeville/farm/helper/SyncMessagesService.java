package com.veeville.farm.helper;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Prashant C on 11/12/18.
 * this Service used to Sync Message between  Local Storage and Sevr Database Messages like Text,Image,Audio,Video etc.....
 */
public class SyncMessagesService extends Service {
    private String TAG = SyncMessagesService.class.getSimpleName();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());

        final String fromAddress = database.getUserEmailAsFromAddress();
        Log.d(TAG, "onStartCommand: from address:"+fromAddress);

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
                Log.d(TAG, "loadmessages: beginning");
                ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(FarmChatDtabaseCredentials.Url + FarmChatDtabaseCredentials.database, FarmChatDtabaseCredentials.UserName, FarmChatDtabaseCredentials.Password);
                Statement stmt = connection.createStatement();
                Statement stmtInner = connection.createStatement();
                String query = "select * from ChatMessages where from_address = '"+fromAddress+"' OR to_address = '"+fromAddress+"';";
                ResultSet resultSet = stmt.executeQuery(query);
                long timestamp  = database.getMostupdatedTime();
                while (resultSet.next()){
                    Log.d(TAG, "loadmessages: has messages size:few");
                    long chatMessageId = resultSet.getLong("chat_message_id");
                    String messageType = resultSet.getString("chat_media_type");
                    String from = resultSet.getString("from_address");
                    String to = resultSet.getString("to_address");
                    long chatMessageValueId = resultSet.getLong("media_type_value_id");
                    long timeStamp = resultSet.getLong("timestamp");
                    String messageValueOrUri = null;
                    byte[] imageBlob = null;
                    Log.d(TAG, "loadmessages: chatMessageId:"+chatMessageId+"\tmessageType:"+messageType+"\tfrom:"+from+"\t to:"+to);
                    switch (messageType){
                        case ChatMediaType.TYPE_TEXT:
                            String queryForValue = "select chat_message from ChatTextMessages where chat_message_id ="+chatMessageValueId;
                            ResultSet resultSet1 = stmtInner.executeQuery(queryForValue);
                            while (resultSet1.next()){
                                messageValueOrUri = resultSet1.getString(1);

                            }
                            break;
                        case ChatMediaType.TYPE_IMAGE:
                            queryForValue = "select chat_message_image from ChatImages where chat_message_image_id="+chatMessageValueId;
                            Log.d(TAG, "loadmessages: image request query:"+queryForValue);
                            ResultSet resultSetImage = stmtInner.executeQuery(queryForValue);
                            while (resultSetImage.next()){
                                imageBlob = resultSetImage.getBytes(1);
                                Log.d(TAG, "loadmessages: images size:"+imageBlob.length);
                            }
                            break;
                        case ChatMediaType.TYPE_AUDIO:
                            break;
                        case ChatMediaType.TYPE_VIDEO:
                            break;
                    }
                    if(timestamp<timeStamp) {
                        switch (messageType){
                            case ChatMediaType.TYPE_TEXT:
                                database.insertChatTextMessage(chatMessageValueId,messageValueOrUri);
                                break;
                            case ChatMediaType.TYPE_IMAGE:
                                database.insertImageIntoDatabase(chatMessageValueId,imageBlob);
                                break;
                        }
                        ChatMessage chatMessage = new ChatMessage(chatMessageId,messageType,from,to,chatMessageValueId,timeStamp,messageValueOrUri);
                        database.insertChatMessage(chatMessage);
                    }else {
                        Log.d(TAG, "loadmessages: all messages are upto date");
                    }
                    resultSet.next();
                    timestamp = database.getMostupdatedTime();
                }
                resultSet.close();
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
