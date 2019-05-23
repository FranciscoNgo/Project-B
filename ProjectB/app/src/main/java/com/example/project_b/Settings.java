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

            String path = dataPictures.getString(3);
            String fileName = dataPictures.getString(4);

            loadImageFromStorage(path, fileName, 1);

            if (dataPictures.getCount() > 1) {

                dataPictures.moveToPrevious();

                path = dataPictures.getString(3);
                fileName = dataPictures.getString(4);

                loadImageFromStorage(path, fileName, 2);

            }
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


    private void loadImageFromStorage(String path, String fileName, int ID)
    {

        try {
            File f=new File(path, fileName);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            ImageView img;

            if (ID == 1) {
                img = findViewById(R.id.rpick);
            }
            else {
                img = findViewById(R.id.rpick2);
            }

            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

}