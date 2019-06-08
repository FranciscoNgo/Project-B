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

public class Memory_Page extends AppCompatActivity {

    DatabaseHelper myDB;

    private String titleDB;
    private int idDB;

    public static double latitude;
    public static double longitude;

    public boolean editchecker = false;
    //public static boolean deletechecker = false;

    Button btnEdit, btnDelete, btnYes, btnNo,btnShare;
    EditText editText;

    TextView emptyImageText;

    public String Story;

    public Bitmap bitmap;

    public String path;
    public String photoName;



    ImageView ImageView;

    private static final int CAM_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory__page);

        emptyImageText = findViewById(R.id.emptyImageText);

        bitmap = null;

        myDB = new DatabaseHelper(this);

        Intent receivedIntent = getIntent();

        //Verkrijgt ID
        idDB = receivedIntent.getIntExtra("id", -1); //NOTE: -1 is just the default value

        Cursor data = myDB.getMemory(idDB);

        data.moveToFirst();

        Story = myDB.getStorybyID(idDB);
        Log.i("Update", Story);
        titleDB = data.getString(1);
        latitude = data.getDouble(3);
        longitude = data.getDouble(4);


        if (data.getColumnCount() > 5) {
            path = data.getString(6);
            photoName = data.getString(7);

            loadImageFromStorage(path, photoName);
        }

        else {
            photoName = idDB + "-1.jpg";
        }

        final TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(titleDB);

        final TextView textView1 = (TextView) findViewById(R.id.storyView);
        textView1.setText(Story);

        final TextView DeleteText = (TextView) findViewById(R.id.DeleteText);

        final EditText editText = (EditText) findViewById(R.id.EditText);
        editText.setText(titleDB);

        final EditText editText1 = (EditText) findViewById(R.id.editStory);
        editText1.setText(Story);

        final ImageView ImageView = (ImageView) findViewById(R.id.ImageView);

        btnEdit = (Button) findViewById(R.id.EButton);
        btnDelete = (Button) findViewById(R.id.DButton);
        btnYes = (Button) findViewById(R.id.buttonYes);
        btnNo = (Button) findViewById(R.id.buttonNo);
        btnShare = (Button) findViewById(R.id.SButton);

        //ImageView.setOnClickListener(new btnTakePhotoClicker());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editchecker) {


                    String item = editText.getText().toString();
                    String item1 = editText1.getText().toString();
                    if ((!item.equals("") && !item.equals(titleDB)) || bitmap != null || (!item1.equals("") && !item1.equals(titleDB))) {

                        if ((!item.equals("") && !item.equals(titleDB))) {
                            myDB.updateTitle(item, idDB);
                            Log.i("Update", "title updated to: " + item);
                            textView.setText(item);
                        }

                        if ((!item1.equals("") && !item1.equals(titleDB))) {
                            myDB.updateStory(item1, idDB);
                            Log.i("Update", "title updated to: " + item);
                            textView1.setText(item1);
                        }

                        if (bitmap != null) {
                            path = saveToInternalStorage(bitmap, photoName);

                            if (!myDB.checkForPicture(photoName)) {
                                myDB.addPicture(path, photoName, idDB);
                            }

                            Log.i("Update", "photo updated");
                        }
                    }

                    if (emptyImageText.getVisibility() == TextView.VISIBLE) {
                        emptyImageText.setVisibility(TextView.GONE);
                    }
                    textView.setVisibility(textView.VISIBLE);
                    textView1.setVisibility(textView.VISIBLE);
                    editText.setVisibility(editText.GONE);
                    editText1.setVisibility(editText.GONE);
                    btnEdit.setText("Edit memory");

                }
                else {
                    if (path == null) {
                        emptyImageText.setVisibility(TextView.VISIBLE);
                    }
                    textView.setVisibility(textView.GONE);
                    textView1.setVisibility(textView1.GONE);
                    editText.setVisibility(editText.VISIBLE);
                    editText1.setVisibility(editText.VISIBLE);
                    btnEdit.setText("Change");
                }
                editchecker = !editchecker;
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    textView.setVisibility(textView.GONE);
                    textView1.setVisibility(textView.GONE);
                    ImageView.setVisibility(ImageView.GONE);
                    DeleteText.setVisibility(DeleteText.VISIBLE);
                    btnDelete.setVisibility(btnDelete.GONE);
                    btnEdit.setVisibility(btnEdit.GONE);
                    btnYes.setVisibility(btnYes.VISIBLE);
                    btnNo.setVisibility(btnYes.VISIBLE);
                    editText.setVisibility(editText.GONE);
                    emptyImageText.setVisibility(TextView.GONE);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView.setVisibility(ImageView.VISIBLE);
                textView1.setVisibility(textView.VISIBLE);
                DeleteText.setVisibility(DeleteText.GONE);
                btnDelete.setVisibility(btnDelete.VISIBLE);
                btnEdit.setVisibility(btnEdit.VISIBLE);
                btnYes.setVisibility(btnYes.GONE);
                btnNo.setVisibility(btnYes.GONE);
                if (editchecker) {
                    editText.setVisibility(editText.VISIBLE);
                    if (bitmap == null) {
                        emptyImageText.setVisibility(TextView.VISIBLE);
                    }
                }
                else {
                    textView.setVisibility(textView.VISIBLE);
                }

            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDB.deleteRecord(idDB);

                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                File file = new File(directory, photoName);
                boolean deleted = file.delete();

                Log.i("Delete", "Delete file " + photoName + " from internal storage outcome: " + deleted);

                deletedActivity();

            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Your body here";
                String shareSub = "Test";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "Share your stuff"));
            }
        });

    }

    //Methode om een image te uploaden uit de internal storage.
    //Geef als argument de path. Hij load hem dan via een ImageView . R.id."..." is die naam van de imageview

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

            if (emptyImageText.getVisibility() == TextView.VISIBLE); {
                emptyImageText.setVisibility(TextView.GONE);
            }

            img.setImageBitmap(bitmap);

        }
    }

    public void ImgClicked(View view) {
        if (editchecker) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAM_REQUEST);
        }

    }

    public void deletedActivity() {
        finish();
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
