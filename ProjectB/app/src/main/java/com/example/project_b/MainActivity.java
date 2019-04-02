package com.example.project_b;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView toolbar_text = findViewById(R.id.toolbar_text);

        toolbar_text.setText("Home");

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

    public void openMap() {
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "This is Map", Toast.LENGTH_SHORT).show();
    }

    public void openCollections() {
        Intent intent = new Intent(this, Collections.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "This is Collections", Toast.LENGTH_SHORT).show();
    }

    public void openSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "This is Settings", Toast.LENGTH_SHORT).show();
    }

    public void openCreate_Memory() {
        Intent intent = new Intent(this, Create_Memory.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "This is Create Memory", Toast.LENGTH_SHORT).show();
    }
}
