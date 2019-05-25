package com.example.project_b;


import android.content.Context;
import android.content.ContextWrapper;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.project_b.Create_Memory.bitmap;


public class Memory_Page extends AppCompatActivity {

    DatabaseHelper myDB;

    private String titleDB;
    private int idDB;

    public static double latitude;
    public static double longitude;

    public static boolean editchecker = false;
    public static boolean deletechecker = false;

    Button btnEdit, btnDelete, btnYes, btnNo;
    EditText editText;

    public static String fotoname;

    ImageView ImageView;

    private static final int CAM_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory__page);

        myDB = new DatabaseHelper(this);

        Intent receivedIntent = getIntent();

        //Verkrijgt ID
        idDB = receivedIntent.getIntExtra("id", -1); //NOTE: -1 is just the default value

        Cursor data = myDB.getMemory(idDB);

        data.moveToFirst();

        titleDB = data.getString(1);
        latitude = data.getDouble(3);
        longitude = data.getDouble(4);
        final String path = data.getString(6);
        fotoname = data.getString(7);

        final TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(titleDB);

        final TextView DeleteText = (TextView) findViewById(R.id.DeleteText);

        final EditText editText = (EditText) findViewById(R.id.EditText);

        loadImageFromStorage(path, fotoname);
        final ImageView ImageView = (ImageView) findViewById(R.id.ImageView);

        btnEdit = (Button) findViewById(R.id.EButton);
        btnDelete = (Button) findViewById(R.id.DButton);
        btnYes = (Button) findViewById(R.id.buttonYes);
        btnNo = (Button) findViewById(R.id.buttonNo);

        //ImageView.setOnClickListener(new btnTakePhotoClicker());

        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editchecker) {
                    textView.setVisibility(textView.GONE);
                    editText.setVisibility(editText.VISIBLE);
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editchecker) {
                    editchecker = !editchecker;
                    String item = editText.getText().toString();
                    if (!item.equals("")) {
                        myDB.updateTitle(item, idDB, titleDB);
                        textView.setVisibility(textView.VISIBLE);
                        editText.setVisibility(editText.GONE);
                        textView.setText(item);
                        btnEdit.setText("Edit memory");

                    } else {
                        textView.setVisibility(textView.VISIBLE);
                        editText.setVisibility(editText.GONE);
                        textView.setText(titleDB);
                        btnEdit.setText("Edit memory");
                    }
                } else {
                    editchecker = !editchecker;
                    btnEdit.setText("Change");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    textView.setVisibility(textView.GONE);
                    ImageView.setVisibility(ImageView.GONE);
                    DeleteText.setVisibility(DeleteText.VISIBLE);
                    btnDelete.setVisibility(btnDelete.GONE);
                    btnEdit.setVisibility(btnEdit.GONE);
                    btnYes.setVisibility(btnYes.VISIBLE);
                    btnNo.setVisibility(btnYes.VISIBLE);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setVisibility(textView.VISIBLE);
                ImageView.setVisibility(ImageView.VISIBLE);
                DeleteText.setVisibility(DeleteText.GONE);
                btnDelete.setVisibility(btnDelete.VISIBLE);
                btnEdit.setVisibility(btnEdit.VISIBLE);
                btnYes.setVisibility(btnYes.GONE);
                btnNo.setVisibility(btnYes.GONE);
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDB.deleteDatabase(idDB,titleDB, latitude, longitude, path, fotoname );
                deletedActivity();

            }
        });

        ImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("Adding", Boolean.toString(editchecker));
                if (editchecker == true) {
                    ImageView.setOnClickListener(new btnTakePhotoClicker());
                }
            }
        });
    }

    //Methode om een image te te loaden uit de internal storage.
    // Geef als argument de path. Hij load hem dan via een ImageView . R.id."..." is die naam van de imageview

    private void loadImageFromStorage(String path, String fileName) {

        try {
            File f = new File(path, fileName);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = findViewById(R.id.ImageView);
            img.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAM_REQUEST && data != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            ImageView img = findViewById(R.id.ImageView);
            img.setImageBitmap(bitmap);
            saveToInternalStorage(bitmap, fotoname);
            restartActivity();
        }
    }

    class btnTakePhotoClicker implements ImageView.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAM_REQUEST);
        }
    }

    public void restartActivity() {
        Intent mIntent = getIntent();
        finish();
        startActivity(mIntent);
    }

    public void deletedActivity() {
        Intent intent = new Intent(this, Collections.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "Memory deleted.", Toast.LENGTH_SHORT).show();
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

}
