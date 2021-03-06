package com.veevillefarm.vfarm.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.helper.AppSingletonClass;

import java.util.Objects;

public class ImageShowActivity extends AppCompatActivity {

    private final String TAG = ImageShowActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        logMessage("onCreate called");
        setUpToolbar();
        showSelectedImageFully();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Image");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }


    //get the image and show with full screen
    private void showSelectedImageFully() {
        ImageView imageView = findViewById(R.id.imageview);
        if (getIntent().hasExtra("image_data")) {
            String imagedata = getIntent().getStringExtra("image_data");
            byte[] decodedString = Base64.decode(imagedata, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        } else if (getIntent().hasExtra("image_url")) {
            String url = getIntent().getStringExtra("image_url");
            Picasso.with(getApplicationContext()).load(url).into(imageView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        logMessage("onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logMessage("onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logMessage("onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logMessage("onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logMessage("onDestroy called");
    }

    //perform action when option menu item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    //always use this method to ebugg message
    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }

}
