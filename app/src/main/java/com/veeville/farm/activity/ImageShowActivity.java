package com.veeville.farm.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.veeville.farm.R;

import java.util.Objects;

public class ImageShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Image");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }


}
