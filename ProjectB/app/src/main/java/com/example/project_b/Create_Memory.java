package com.example.project_b;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import com.google.android.gms.maps.model.Marker;

public class Create_Memory extends AppCompatActivity {

    public static Marker marker;
    public Bitmap bitmap = null;

    DatabaseHelper myDB;
    Button btnAdd,btnpic;
    EditText editText, editStory;

    ImageView imgTakenPic;
    private static final int CAM_REQUEST = 1313;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__memory);

        marker = null;

        myDB = new DatabaseHelper(this);

        // picture items
        imgTakenPic = (ImageView)findViewById(R.id.rpick);
        //imgTakenPic.setOnClickListener(new btnTakePhotoClicker());

        editText = (EditText) findViewById(R.id.editText);
        editStory = (EditText) findViewById(R.id.editStory);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnpic = (Button) findViewById(R.id.button);
        imgTakenPic = (ImageView)findViewById(R.id.rpick);
        btnpic.setOnClickListener(new btnTakePhotoClicker());


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = editText.getText().toString();
                String newStory = editStory.getText().toString();
                if (editText.length() != 0){
                    if (editStory.length() != 0){
                        AddData(newEntry, newStory);
                    }
                    else {
                        Toast.makeText(Create_Memory.this, "You must give the memory a Story.",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(Create_Memory.this, "You must give the memory a Title.",Toast.LENGTH_LONG).show();
                }
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView toolbar_text = findViewById(R.id.toolbar_text);

        toolbar_text.setText("Create Memory");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAM_REQUEST && data != null){
            bitmap = (Bitmap) data.getExtras().get("data");

            imgTakenPic.setImageBitmap(bitmap);
        }
    }


    class btnTakePhotoClicker implements  Button.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAM_REQUEST);
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

        if (v.getId() == R.id.buttonPick_Location) {
            openPick_Location();
        }
    }

    public void AddData(String newTitle, String newStory) {
        if(marker != null) {

            int ID = myDB.addMemory(newTitle, newStory);

            if (ID == -1) {
                Toast.makeText(this, "Something went wrong :(", Toast.LENGTH_LONG).show();

            } else {

                myDB.addLocation(marker.getPosition().latitude, marker.getPosition().longitude, ID);
                Log.i("Adding", newTitle + " " + marker.getPosition().latitude + " " + marker.getPosition().longitude);
                Log.i("Adding", newStory);

                if (bitmap != null) {
                    String fileName = ID + "-1.jpg";

                    String picturePath = saveToInternalStorage(bitmap, fileName);

                    myDB.addPicture(picturePath, fileName, ID);

                    Log.i("Adding", picturePath + " " + fileName);
                }

                editText.setText("");
                Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show();

                this.finish();
            }
        }

        else {
            Toast.makeText(Create_Memory.this, "Pick a location.", Toast.LENGTH_LONG).show();
        }
    }

    public void openPick_Location() {
        Intent intent = new Intent(this, Pick_Location.class);

        intent.putExtra("activity", "Create Memory");

        if (marker != null) {
            intent.putExtra("latitude", marker.getPosition().latitude);
            intent.putExtra("longitude", marker.getPosition().longitude);

        }

        startActivity(intent);
    }


    private String saveToInternalStorage(Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory,fileName);


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();

    }

 //Methode om een image te te loaden uit de internal storage.
    // Geef als argument de path. Hij load hem dan via een ImageView . R.id."..." is die naam van de imageview

    private void loadImageFromStorage(String path, String fileName)
    {

        try {
            File f=new File(path, fileName);
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
