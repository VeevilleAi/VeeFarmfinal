package com.veeville.farm.helper;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

/**
 * Created by Prashant C on 14/12/18.
 */
public class UploadImageService  extends Service{
    private final String TAG = UploadImageService.class.getSimpleName();

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
        Uri uri = Uri.parse(intent.getStringExtra("imageUri"));
        isImageUploaded = false;
        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            String stringImage = getStringImage(bm);
            upLoadImage(stringImage);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }

    private void upLoadImage(String imgString){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    isImageUploaded = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopSelf();
            }
        }).start();
        Log.d(TAG, "upLoadImage: "+imgString);

    }
    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public boolean isImageUploadSuccess(){

        return isImageUploaded;

    }

}
