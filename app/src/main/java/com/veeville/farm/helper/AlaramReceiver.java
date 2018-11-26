package com.veeville.farm.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.veeville.farm.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by BOB on 14/09/17.
 */

public class AlaramReceiver extends BroadcastReceiver {
    private final String TAG = AlaramReceiver.class.getSimpleName();
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Toast.makeText(context, "Howdy partner", Toast.LENGTH_LONG).show();
        String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");
        showNotification(title, body);
    }

    private void showNotification(String title, String body) {

        Intent intent = new Intent("veeville.veevillefarm");
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
        Notification noti = new Notification.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.humidity_icon)
                .setContentText(body).setSmallIcon(R.drawable.humidity_icon)
                .setContentIntent(pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        assert notificationManager != null;
        notificationManager.notify(0, noti);
    }
}
