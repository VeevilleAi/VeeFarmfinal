package com.veeville.farm.farmer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.veeville.farm.R;

import java.util.Objects;

public class ShowFarmInMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        setUpToolbar();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(17.141728, 75.729168), 15));
        addAllFarmsToMap();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Farms");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void addAllFarmsToMap() {

        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.add(new LatLng(17.143327, 75.730190));
        polygonOptions.add(new LatLng(17.143041, 75.730268));
        polygonOptions.add(new LatLng(17.142509, 75.730383));
        polygonOptions.add(new LatLng(17.141872, 75.730527));
        polygonOptions.add(new LatLng(17.141777, 75.729855));
        polygonOptions.add(new LatLng(17.141728, 75.729168));
        polygonOptions.add(new LatLng(17.142521, 75.729575));
        polygonOptions.add(new LatLng(17.143051, 75.729903));
        polygonOptions.add(new LatLng(17.143292, 75.730125));
        polygonOptions.strokeColor(Color.YELLOW);
        polygonOptions.fillColor(Color.GREEN);
        polygonOptions.strokeWidth(1);
        mMap.addPolygon(polygonOptions);

        PolygonOptions polygonOptions1 = new PolygonOptions();
        polygonOptions1.add(new LatLng(17.137240, 75.727592));
        polygonOptions1.add(new LatLng(17.137459, 75.727539));
        polygonOptions1.add(new LatLng(17.137661, 75.727511));
        polygonOptions1.add(new LatLng(17.137715, 75.727887));
        polygonOptions1.add(new LatLng(17.137788, 75.728277));
        polygonOptions1.add(new LatLng(17.137598, 75.728411));
        polygonOptions1.add(new LatLng(17.137422, 75.728480));
        polygonOptions1.add(new LatLng(17.137306, 75.728030));
        polygonOptions1.add(new LatLng(17.137247, 75.727622));
        polygonOptions1.fillColor(Color.GREEN);
        polygonOptions1.strokeColor(Color.YELLOW);
        polygonOptions1.strokeWidth(1);
        mMap.addPolygon(polygonOptions1);


        PolygonOptions polygonOptions2 = new PolygonOptions();
        polygonOptions2.add(new LatLng(17.137697, 75.727328));
        polygonOptions2.add(new LatLng(17.137756, 75.727751));
        polygonOptions2.add(new LatLng(17.137860, 75.728194));
        polygonOptions2.add(new LatLng(17.138335, 75.728140));
        polygonOptions2.add(new LatLng(17.138597, 75.728069));
        polygonOptions2.add(new LatLng(17.138687, 75.727896));
        polygonOptions2.add(new LatLng(17.138486, 75.727724));
        polygonOptions2.add(new LatLng(17.138267, 75.727609));
        polygonOptions2.add(new LatLng(17.137971, 75.727406));
        polygonOptions2.add(new LatLng(17.137756, 75.727326));
        polygonOptions2.fillColor(Color.GREEN);
        polygonOptions2.strokeColor(Color.YELLOW);
        polygonOptions2.strokeWidth(1);
        mMap.addPolygon(polygonOptions2);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu.size() == 0) {
            menu.add("Farm1");
            menu.add("Farm2");
            menu.add("Farm3");
            menu.add("Farm4");
            menu.add("Farm5");
            menu.add("Farm6");
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
