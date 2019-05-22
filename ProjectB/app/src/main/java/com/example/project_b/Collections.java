package com.example.project_b;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Collections extends AppCompatActivity {

    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);

        ListView listView = (ListView) findViewById(R.id.ListView);
        myDB = new DatabaseHelper(this);

        ArrayList<String> theList = new ArrayList<>();

        Cursor data = myDB.getListContents();


        if(data.getCount() == 0){
            Toast.makeText(Collections.this, "The Database was empty :(.",Toast.LENGTH_LONG).show();
        }
        else{
            while(data.moveToNext()){
                theList.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,theList);
                listView.setAdapter(listAdapter);

            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Collections.this, Memory_Page.class);

                startActivity(intent);
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView toolbar_text = findViewById(R.id.toolbar_text);

        toolbar_text.setText("Collections");




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



    public void buttonClicked(View v) {

        if (v.getId() == R.id.buttonMemory_Displayer) {
            openMemory_Displayer();
        }
    }

    public void openMemory_Displayer() {
        Intent intent = new Intent(this, Memory_Displayer.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "This is Memory Displayer", Toast.LENGTH_SHORT).show();
    }





}