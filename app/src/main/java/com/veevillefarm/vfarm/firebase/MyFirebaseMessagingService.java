package com.veevillefarm.vfarm.firebase;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Base64;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.activity.OneToOneChatActivity;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.ChatMediaType;
import com.veevillefarm.vfarm.helper.ChatMessage;
import com.veevillefarm.vfarm.helper.ChatMessageDatabase;
import com.veevillefarm.vfarm.helper.ChatmessageDataClasses;

import java.util.List;
import java.util.Map;

/**
 * Created by Prashant C on 03/01/19.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private String CHANNEL_ID = MyFirebaseMessagingService.class.getCanonicalName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        logMessage("onMessageReceived: got downstream messages");
        Map<String,String> map = remoteMessage.getData();
        String title = map.get("messageTitle");
        String body = map.get("messageBody");
        String messageType = map.get("messageType");
        String from = map.get("fromAddress");
        String to = map.get("toAddress");
        String fcmTokenOther = map.get("fcmTokenOther");
        logMessage("title:"+title+"\tbody:"+body+"\tmessageType:"+messageType+"\tfrom:"+from+"\tto:"+to);
        switch (messageType){
            case ChatMediaType.TYPE_TEXT:
                insertMessageToLocalDatabase(body,from,to);
                break;
            case ChatMediaType.TYPE_IMAGE:
                String imageID = map.get("imageId");
                logMessage("imageid:"+imageID);
                insertReceivedImageMessage(convertStringToByteArray(body),from,to,imageID);
                break;
        }

        if(isAppIsInBackground(getApplicationContext())) {
            createNotificationChannel();
            if(messageType.equals(ChatMediaType.TYPE_IMAGE))
            showNotification(title, "Image",fcmTokenOther,from,to);

        }
    }


    private byte[] convertStringToByteArray(String encodedImage){
        return Base64.decode(encodedImage, Base64.NO_WRAP);
    }

    private void insertReceivedImageMessage(byte[] image, String fromAddress, String toAddress,String awsId){
        ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());
        long timestamp = System.currentTimeMillis() / 1000;
        ChatmessageDataClasses.InputBitMapImage bitMapImage = new ChatmessageDataClasses.InputBitMapImage(timestamp,awsId,false,null,image);
        database.insertInputImage(bitMapImage);
        ChatMessage chatMessage = new ChatMessage(timestamp, ChatMediaType.TYPE_IMAGE, fromAddress, toAddress, timestamp, timestamp, "");
        database.insertChatMessage(chatMessage);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        logMessage("onNewToken: "+s);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fcm_token",s);
        editor.apply();
        editor.commit();
    }
    private void insertMessageToLocalDatabase(String message, String from, String to) {
        ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());
        long timestamp = System.currentTimeMillis() / 1000;
        database.insertChatTextMessage(timestamp, message);
        ChatMessage chatMessage = new ChatMessage(timestamp, ChatMediaType.TYPE_TEXT, from, to, timestamp, timestamp, message);
        database.insertChatMessage(chatMessage);
        //insertmessageToServerDatabase(chatMessage);
    }

    private void logMessage(String message){
        AppSingletonClass.logMessage(TAG,message);
    }

    private void logErrorMessage(String errorMessage){
        AppSingletonClass.logErrorMessage(TAG,errorMessage);
    }

    private void showNotification(String title,String body,String fcmToken,String fromAddress,String toAddress){

        Intent intent = new Intent(this, OneToOneChatActivity.class);
        logMessage("from:"+fromAddress+"\tto:"+toAddress+"\tfcmToken:"+fcmToken);
        intent.putExtra("fcmToken",fcmToken);
        intent.putExtra("to",fromAddress);
        intent.putExtra("from",toAddress);
        intent.putExtra("name",title);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.app_new_logo_flower)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = (int) System.currentTimeMillis();
        notificationManager.notify(0, mBuilder.build());
    }
    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert am != null;
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(context.getPackageName())) {
                        isInBackground = false;
                    }
                }
            }
        }
        return isInBackground;
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
