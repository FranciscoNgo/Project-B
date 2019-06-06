package com.example.project_b;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


public class Pick_Location extends AppCompatActivity implements OnMapReadyCallback {
    public static Marker latest = null;
    public static Boolean buttonVisible;

    public static String activity;

    private int LOCATION_PERMISSION_CODE = 1;

    Button confirm;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick__location);

        activity = getIntent().getStringExtra("activity");

        buttonVisible = false;

        confirm = findViewById(R.id.buttonPick_LocationConfirm);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView toolbar_text = findViewById(R.id.toolbar_text);

        toolbar_text.setText("Pick Location");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request for permission
            Log.i("Test", "Request for permission");

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);

        } else {
            Log.i("Test", "Already have permission");
            // already permission granted
        }

        Log.i("Previous Activity", activity);

        if(Create_Memory.marker == null && activity.equals("Create Memory")) {

            Log.i("Test", "Try get last location");

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.i("Test", "Got Location");
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                LatLng latLng = new LatLng(latitude, longitude);

                                latest = mMap.addMarker(new MarkerOptions().position(latLng));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                                confirm.setVisibility(View.VISIBLE);
                                buttonVisible = true;
                            }
                        }
                    });
        }
        else {

            Log.i("Test", "Else");

            double latitude = getIntent().getDoubleExtra("latitude", 0.0);
            double longitude = getIntent().getDoubleExtra("longitude", 0.0);

            LatLng latLng = new LatLng(latitude, longitude);

            latest = mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            confirm.setVisibility(View.VISIBLE);
            buttonVisible = true;
        }


        //Add marker by clicking/tapping
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(latest != null){
                    Log.i("Test", "Marker Removed");
                    latest.remove();
                }

                Log.i("Test", "Marker Added");
                latest = mMap.addMarker(new MarkerOptions().position(latLng));

                if(latest != null && !buttonVisible) {
                    confirm.setVisibility(View.VISIBLE);
                    buttonVisible = true;
                }
            }
        });
    }

    public void ConfirmClicked(View v) {
        Log.i("Test", "Button clicked");

        if (activity.equals("Create Memory")) {
            Create_Memory.marker = latest;
        }
        else if (activity.equals("Memory Page")) {
            Memory_Page.latitude = latest.getPosition().latitude;
            Memory_Page.longitude = latest.getPosition().longitude;
        }
        this.finish();
    }


    public void HomeClicked(View v) {
        if (v.getId() == R.id.buttonHome) {
            openHome();
        }
    }

    public void openHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
