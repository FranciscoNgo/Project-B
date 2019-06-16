package com.example.project_b;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    ImageButton buttonHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonHome = findViewById(R.id.buttonHome);
        buttonHome.setVisibility(View.INVISIBLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView toolbar_text = findViewById(R.id.toolbar_text);

        toolbar_text.setText("Home");

        myDB = new DatabaseHelper(this);

        //Reset database
        //myDB.restartDatabase();

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


    // Methode die checkt of er op een button is geklikt.
    public void buttonClicked(View v) {

        if (v.getId() == R.id.buttonMap) {
            openMap();
        } else if (v.getId() == R.id.buttonCollections) {
            openCollections();
        } else if (v.getId() == R.id.buttonSettings) {
            openSettings();
        } else if (v.getId() == R.id.buttonCreate_Memory) {
            openCreate_Memory();
        }
    }

    // Methode die je naar Maps brengt.
    public void openMap() {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }

    // Methode die je naar Collections brengt.
    public void openCollections() {
        Intent intent = new Intent(this, Collections.class);
        startActivity(intent);
    }

    public void openSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    // Methode die je naar Create Memory brengt.
    public void openCreate_Memory() {
        Intent intent = new Intent(this, Create_Memory.class);
        startActivity(intent);
    }


}
