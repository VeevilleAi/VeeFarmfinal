package com.veeville.farm.farmer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.veeville.farm.R;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DrawFarmInMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int PERMISSION_REQUEST_CODE = 101, REQUEST_CHECK_SETTINGS = 102;
    String TAG = "DrawFarmInMapActivity";
    GoogleMap map;
    List<LatLng> latLngs = new ArrayList<>();
    DrawFarmInMapActivity view;
    Polygon polygon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_farm_in_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpToolbar();
        displayLocationSettingsRequest(getApplicationContext());
        setUpLocationManager();
    }

    void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("draw your farm");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setUpLocationManager();
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.236655, 78.072064), 15));
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (checkPermission()) {
            map.setMyLocationEnabled(true);
        } else {
            requestPermission();
        }
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map.addMarker(new MarkerOptions().position(latLng).draggable(true).title(latLngs.size() + 1 + ""));
                latLngs.add(latLng);
                drawPolylines();
                map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                        int position = Integer.parseInt(marker.getTitle());
                        latLngs.set(position - 1, marker.getPosition());
                        drawPolylines();
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        int position = Integer.parseInt(marker.getTitle());
                        latLngs.set(position - 1, marker.getPosition());
                        drawPolylines();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().toString().equals("add")) {
            double area = SphericalUtil.computeArea(latLngs);

            double onegunta = 101;
            double oneAcre = 4046;
            int acre = (int) area / 4046;
            int gunta = (int) ((area % oneAcre) / onegunta);
            Intent intent = new Intent();
            double[] latitudes = new double[latLngs.size()];
            double longitudes[] = new double[latLngs.size()];
            for (int i = 0; i < latLngs.size(); i++) {
                latitudes[i] = latLngs.get(i).latitude;
                longitudes[i] = latLngs.get(i).longitude;
            }
            intent.putExtra("Latitudes", latitudes);
            intent.putExtra("Longitudes", longitudes);
            intent.putExtra("acre", acre);
            intent.putExtra("gunta", gunta);
            setResult(RESULT_OK, intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    private void drawPolylines() {
        if (polygon != null) {
            polygon.remove();
        }
        PolylineOptions options = new PolylineOptions();
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.addAll(latLngs);
        polygonOptions.strokeColor(Color.GREEN);
        polygonOptions.strokeWidth(5);
        options.addAll(latLngs);
        options.color(Color.YELLOW);
        options.width(5);
        //map.addPolyline(options);
        polygon = map.addPolygon(polygonOptions);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        view = this;
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted) {
                        if (checkPermission()) {
                            map.setMyLocationEnabled(true);
                            map.getUiSettings().setMyLocationButtonEnabled(true);
                        }
                    }
                    //Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    else {

                        // Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access the permission",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DrawFarmInMapActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Toast.makeText(view, "thank you", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                })
                .create()
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_farm_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setUpLocationManager() {
        android.location.LocationListener listener1 = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged: " + location.getLongitude() + "\t" + location.getLatitude());
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (checkPermission() && manager != null)
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 500, listener1);

    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        final LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        setUpLocationManager();
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(DrawFarmInMapActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
}
