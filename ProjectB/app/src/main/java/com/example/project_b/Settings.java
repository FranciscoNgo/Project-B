package com.example.project_b;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper myDB;

        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView toolbar_text = findViewById(R.id.toolbar_text);

        toolbar_text.setText("Settings");

        myDB = new DatabaseHelper(this);

        //Displays the latest picture (test, only happens when the activity opens)
        Cursor dataPictures = myDB.getPictures();

        int count = dataPictures.getCount();

        Log.i("Test", Integer.toString(count));

        dataPictures.moveToLast();

        if (dataPictures.getCount() > 0) {

            String col4 = dataPictures.getString(3);

            Log.i("Test", col4);

            String path = dataPictures.getString(3);

            loadImageFromStorage(path);
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


    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = findViewById(R.id.rpick);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

}