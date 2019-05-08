package com.example.project_b;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseHelper myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        myDB = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView toolbar_text = findViewById(R.id.toolbar_text);

        toolbar_text.setText("Map");



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

        // Add a marker in Rotterdam and move the camera
        //LatLng rotterdam = new LatLng(51.9334597, 4.3468513);
        //mMap.addMarker(new MarkerOptions().position(rotterdam).title("Marker in Rotterdam"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(rotterdam));

        //Display markers
        Cursor dataLocation = myDB.getLocations();

        while(dataLocation.moveToNext()) {
            String title = dataLocation.getString(1);
            double latitude = dataLocation.getDouble(3);
            double longitude = dataLocation.getDouble(4);
            LatLng latLng = new LatLng(latitude, longitude);

            Log.i("Calling", title + " "+ latitude + " " + longitude);

            mMap.addMarker(new MarkerOptions().position(latLng).title(title));
        }

    }


    public void HomeClicked(View v) {
        if (v.getId() == R.id.buttonHome) {
            openHome();
        }
    }

    public void openHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "This is Menu", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonClicked(View v) {

        if (v.getId() == R.id.buttonMap_Filter) {
            openMap_Filter();
        }
    }

    public void openMap_Filter() {
        Intent intent = new Intent(this, Map_Filter.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "This is Map Filter", Toast.LENGTH_SHORT).show();
    }
}
