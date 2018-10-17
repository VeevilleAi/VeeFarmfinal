package com.veeville.farm.farmer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.veeville.farm.R;

import java.util.Objects;

public class FarmDescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmers_farm);
        setUpToolbar();

        ImageView imageView = findViewById(R.id.image);
        String url = "https://maps.googleapis.com/maps/api/staticmap?maptype=hybrid&size=600x300&zoom=15&key=AIzaSyBeilqJcTJPyZ--59DXSsK1mWrWL3guh8k&markers=13.053990,77.572937";
        Glide.with(getApplicationContext()).load(url).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShowFarmInMapActivity.class);
                startActivity(intent);
            }
        });
    }


    void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Farms");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu.size() == 0) {
            menu.add("edit").setIcon(R.drawable.ic_edit_arrow).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        } else {
            if (menu.getItem(0).getTitle().toString().equals("edit")) {
                menu.removeItem(0);
                menu.add("save").setIcon(R.drawable.ic_edit_arrow).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

            } else {
                menu.removeItem(0);
                menu.add("edit").setIcon(R.drawable.ic_done_white_arrow).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
