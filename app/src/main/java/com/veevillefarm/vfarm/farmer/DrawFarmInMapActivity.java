package com.veevillefarm.vfarm.farmer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.helper.AppSingletonClass;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


/*
 * this activity used to draw farmer farm in google map
 * and calculating area of farm using google map
 */
public class DrawFarmInMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int PERMISSION_REQUEST_CODE = 101, REQUEST_CHECK_SETTINGS = 102;
    private final String TAG = DrawFarmInMapActivity.class.getSimpleName();
    private GoogleMap map;
    private List<LatLng> latLngs = new ArrayList<>();
    private DrawFarmInMapActivity view;
    private Polygon polygon;

    //
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

    //settingup custom toolbar
    private void setUpToolbar() {
        String logMessage = "settingup toolbar";
        logMessage(logMessage);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("draw your farm");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
    }

    //when google map loaded fully move the camera to current location
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

    //when user selects menu item done then calucalte area in acre and gunta and return it previous activity
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

    //when user clicks on maps get selected point location and add it to list and draw polygon
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
        polygon = map.addPolygon(polygonOptions);
    }

    //check weather user granted location permission
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    //request location permission
    private void requestPermission() {
        view = this;
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

    }

    //handle the results like location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
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

                        if (VERSION.SDK_INT >= VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel(
                                        new DialogInterface.OnClickListener() {
                                            @RequiresApi(api = VERSION_CODES.M)
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                requestPermissions(new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);

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

    //this is dialog to get current location when user click deny permission
    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        String message = "You need to allow access the permission";
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


    //this is used to create option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_farm_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //setting up user current location which will help to  draw farm on his current location
    private void setUpLocationManager() {
        android.location.LocationListener listener1 = new android.location.LocationListener() {

            //whenever location change move map to his current location
            @Override
            public void onLocationChanged(Location location) {
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

    //moving user to location setting to enable location for this application
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
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(DrawFarmInMapActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            logErrorMessage(e.toString());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    //use this function to ebugg messages
    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }

    //use to ebugg error messages
    private void logErrorMessage(String logErrorMessage) {
        AppSingletonClass.logErrorMessage(TAG, logErrorMessage);
    }
}
