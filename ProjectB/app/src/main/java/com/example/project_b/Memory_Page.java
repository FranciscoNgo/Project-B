package com.example.project_b;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Memory_Page extends AppCompatActivity {

    DatabaseHelper myDB;

    private String titleDB;
    private int idDB;

    public static double latitude;
    public static double longitude;

    public boolean editchecker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory__page);

        myDB = new DatabaseHelper(this);

        Intent receivedIntent = getIntent();

        //Verkrijgt ID
        idDB = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        Cursor data = myDB.getMemory(idDB);

        data.moveToFirst();

        titleDB = data.getString(1);
        latitude = data.getDouble(3);
        longitude = data.getDouble(4);
        String path = data.getString(6);
        String fileName = data.getString(7);

        final TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(titleDB);

        final EditText editText = (EditText) findViewById(R.id.EditText);

        loadImageFromStorage(path, fileName);
        ImageView ImageView = (ImageView) findViewById(R.id.ImageView);


        textView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.i("Adding", "Text worked !");
                textView.setVisibility(textView.GONE);
                editText.setVisibility(editText.VISIBLE);
            }
        });


        ImageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.i("Adding", "Picture worked!");

            }
        });

    }

    //Methode om een image te te loaden uit de internal storage.
    // Geef als argument de path. Hij load hem dan via een ImageView . R.id."..." is die naam van de imageview

    private void loadImageFromStorage(String path, String fileName)
    {

        try {
            File f=new File(path, fileName);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = findViewById(R.id.ImageView);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    public void buttonClicked(View v) {

        //delete button en edit button
        if (v.getId() == R.id.DButton) {
        //opens double check delete push notification; still has to be writen
        } else if (v.getId() == R.id.EButton) {
            editchecker =  !editchecker;
        }
    }

}
