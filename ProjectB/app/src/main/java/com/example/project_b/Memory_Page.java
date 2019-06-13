package com.example.project_b;


import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

    private int STORAGE_PERMISSION_CODE = 1;
    private String titleDB;
    private int idDB;

    public static double latitude;
    public static double longitude;

    public boolean editchecker = false;
    //public static boolean deletechecker = false;

    Button btnEdit, btnDelete, btnYes, btnNo,btnShare;
    EditText editText;

    TextView emptyImageText;

    public String story;

    public Bitmap bitmap;

    public String path;
    public String photoName;
    
    ImageView ImageView;

    private static final int CAM_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory__page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView toolbar_text = findViewById(R.id.toolbar_text);

        toolbar_text.setText("Your Memory");

        emptyImageText = findViewById(R.id.emptyImageText);

        bitmap = null;

        myDB = new DatabaseHelper(this);

        Intent receivedIntent = getIntent();

        //Verkrijgt ID
        idDB = receivedIntent.getIntExtra("id", -1); //NOTE: -1 is just the default value

        Cursor data = myDB.getMemory(idDB);

        data.moveToFirst();
        
        titleDB = data.getString(1);
        story = data.getString(2);
        latitude = data.getDouble(4);
        longitude = data.getDouble(5);
        Log.i("Update", story);

        if (data.getColumnCount() > 6) {
            path = data.getString(7);
            photoName = data.getString(8);

            loadImageFromStorage(path, photoName);
        }

        else {
            photoName = idDB + "-1.jpg";
        }

        final TextView titleView = (TextView) findViewById(R.id.textView);
        titleView.setText(titleDB);

        final TextView storyView = (TextView) findViewById(R.id.storyView);
        storyView.setText(story);

        final TextView DeleteText = (TextView) findViewById(R.id.DeleteText);

        final EditText titleEdit = (EditText) findViewById(R.id.EditText);
        titleEdit.setText(titleDB);

        final EditText storyEdit = (EditText) findViewById(R.id.editStory);
        storyEdit.setText(story);

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


                    String item = titleEdit.getText().toString();
                    String item1 = storyEdit.getText().toString();
                    if ((!item.equals("") && !item.equals(titleDB)) || bitmap != null || (!item1.equals("") && !item1.equals(story))) {

                        if ((!item.equals("") && !item.equals(titleDB))) {
                            myDB.updateTitle(item, idDB);
                            Log.i("Update", "title updated to: " + item);
                            titleView.setText(item);
                        }

                        if ((!item1.equals("") && !item1.equals(titleDB))) {
                            myDB.updateStory(item1, idDB);
                            Log.i("Update", "title updated to: " + item1);
                            storyView.setText(item1);
                        }

                        if (bitmap != null) {
                            path = saveToInternalStorage(bitmap, photoName);

                            if (!myDB.checkForPicture(photoName)) {
                                myDB.addPicture(path, photoName, idDB);
                            }

                            Log.i("Update", "photo updated");
                        }

                        Toast.makeText(Memory_Page.this, "Saved!", Toast.LENGTH_LONG).show();
                    }

                    if (emptyImageText.getVisibility() == TextView.VISIBLE) {
                        emptyImageText.setVisibility(TextView.GONE);
                    }

                    toolbar_text.setText("Your Memory");
                    titleView.setVisibility(titleView.VISIBLE);
                    storyView.setVisibility(titleView.VISIBLE);
                    titleEdit.setVisibility(titleEdit.GONE);
                    storyEdit.setVisibility(titleEdit.GONE);

                    btnDelete.setVisibility(btnDelete.VISIBLE);
                    btnShare.setVisibility(btnShare.VISIBLE);

                    btnEdit.setText("Edit");

                }
                else {
                    if (path == null) {
                        emptyImageText.setVisibility(TextView.VISIBLE);
                    }
                    toolbar_text.setText("Edit Mode");
                    titleView.setVisibility(titleView.GONE);
                    storyView.setVisibility(storyView.GONE);
                    titleEdit.setVisibility(titleEdit.VISIBLE);
                    storyEdit.setVisibility(titleEdit.VISIBLE);

                    btnDelete.setVisibility(btnDelete.GONE);
                    btnShare.setVisibility(btnShare.GONE);

                    btnEdit.setText("Save");
                }
                editchecker = !editchecker;
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    titleView.setVisibility(titleView.GONE);
                    storyView.setVisibility(titleView.GONE);
                    ImageView.setVisibility(ImageView.GONE);
                    DeleteText.setVisibility(DeleteText.VISIBLE);
                    btnDelete.setVisibility(btnDelete.GONE);
                    btnEdit.setVisibility(btnEdit.GONE);
                    btnYes.setVisibility(btnYes.VISIBLE);
                    btnNo.setVisibility(btnYes.VISIBLE);
                    btnShare.setVisibility(btnShare.GONE);
                    titleEdit.setVisibility(titleEdit.GONE);
                    emptyImageText.setVisibility(TextView.GONE);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView.setVisibility(ImageView.VISIBLE);
                storyView.setVisibility(titleView.VISIBLE);
                DeleteText.setVisibility(DeleteText.GONE);
                btnDelete.setVisibility(btnDelete.VISIBLE);
                btnEdit.setVisibility(btnEdit.VISIBLE);
                btnYes.setVisibility(btnYes.GONE);
                btnNo.setVisibility(btnYes.GONE);
                btnShare.setVisibility(btnShare.VISIBLE);
                if (editchecker) {
                    titleEdit.setVisibility(titleEdit.VISIBLE);
                    if (bitmap == null) {
                        emptyImageText.setVisibility(TextView.VISIBLE);
                    }
                }
                else {
                    titleView.setVisibility(titleView.VISIBLE);
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
                //Button vraagt WRITE_EXTERNAL_STORAGE permission aan
                if (ContextCompat.checkSelfPermission(Memory_Page.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragePermission();
                } 

                HideButtons();
                //new share function voor screenshot
                shareit();
                ShowButtons();

            }
        });

    }
    //requestStoragePermission aanmaken
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for to access external programs")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Memory_Page.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
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
        Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT).show();
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

    public void shareit(){
        View view =  getWindow().getDecorView().getRootView();
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            File picDir  = new File(Environment.getExternalStorageDirectory()+ "/myPic");
            if (!picDir.exists())
            {
                picDir.mkdir();
            }
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache(true);
            Bitmap bitmap = view.getDrawingCache();
//          Date date = new Date();
            String fileName = "Screenshot" + ".jpg";
            File picFile = new File(picDir + "/" + fileName);
            try
            {
                picFile.createNewFile();
                FileOutputStream picOut = new FileOutputStream(picFile);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), (int)(bitmap.getHeight()/1.2));
                boolean saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, picOut);
                if (saved)
                {
                    Toast.makeText(getApplicationContext(), "Image is saved in your gallery!", Toast.LENGTH_SHORT).show();
                } else
                {
                    Toast.makeText(getApplicationContext(), "Error while saving image", Toast.LENGTH_SHORT).show();
                }
                picOut.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            view.destroyDrawingCache();

            // share via intent
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("image/jpeg");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(picFile.getAbsolutePath()));
            startActivity(Intent.createChooser(sharingIntent, "Share via"));


        } else {
            //Error

        }
    }

    //Hides the buttons btnDelete, btnEdit, btnShare.
    public void HideButtons(){
        btnDelete.setVisibility(btnDelete.GONE);
        btnEdit.setVisibility(btnEdit.GONE);
        btnShare.setVisibility(btnShare.GONE);
    }

    //Shows the buttons btnDelete, btnEdit, btnShare.
    public void ShowButtons(){
        btnDelete.setVisibility(btnDelete.VISIBLE);
        btnEdit.setVisibility(btnEdit.VISIBLE);
        btnShare.setVisibility(btnShare.VISIBLE);
    }

}
