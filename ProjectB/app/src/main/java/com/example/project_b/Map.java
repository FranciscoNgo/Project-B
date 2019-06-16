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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.example.project_b.Create_Memory.marker;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseHelper myDB;

    public ArrayList<Marker> markerList;
    public ArrayList<Integer> IDList;

    private boolean start = false;

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

        refreshMarkers();

        if (markerList.size() != 0 && !start) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerList.get(markerList.size() - 1).getPosition(), 14));
            start = true;
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent nextIntent = new Intent(Map.this, Memory_Page.class);

                Log.i("Latitude", Double.toString(marker.getPosition().latitude));
                Log.i("Longitude", Double.toString(marker.getPosition().longitude));

                Log.i("markerList Size", Integer.toString(markerList.size()));
                Log.i("IDList Size", Integer.toString(IDList.size()));

                int index = markerList.indexOf(marker);

                Log.i("Index", Integer.toString(index));

                Log.i("ID", Integer.toString(IDList.get(index)));


                //Geeft ID mee aan volgende Activity.
                nextIntent.putExtra("id", IDList.get(index));
                startActivity(nextIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        Log.i("Test", "onResume called");

        if (mMap != null) {
            mMap.clear();
        }
    }

    public void refreshMarkers() {

        Log.i("Calling", "RefreshMarkers");

        markerList = new ArrayList<>();
        IDList = new ArrayList<>();

        //Display markers
        Cursor dataLocation = myDB.getLocations();

        while(dataLocation.moveToNext()) {
            int id = dataLocation.getInt(0);
            String title = dataLocation.getString(1);
            double latitude = dataLocation.getDouble(4);
            double longitude = dataLocation.getDouble(5);
            LatLng latLng = new LatLng(latitude, longitude);

            Log.i("Calling", title + " "+ latitude + " " + longitude);

            markerList.add(mMap.addMarker(new MarkerOptions().position(latLng).title(title)));
            IDList.add(id);

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
    }

}
