package com.example.project_b;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private static final String TAG = "ListDataActivity";

    public static ArrayList<String> theList;
    public static ArrayList<Integer> IDList;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);

        listView = (ListView) findViewById(R.id.ListView);
        myDB = new DatabaseHelper(this);

        getList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Haalt de ID door middel van de position in de IDList ArrayList
                int itemID = IDList.get(position);

                Log.d(TAG, "onItemClick: The ID is: " + itemID);
                Intent volgendeIntent = new Intent(Collections.this, Memory_Page.class);
                //Geeft ID mee aan volgende Activity.
                volgendeIntent.putExtra("id",itemID);
                startActivity(volgendeIntent);


            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView toolbar_text = findViewById(R.id.toolbar_text);

        toolbar_text.setText("Collections");


    }

    @Override
    protected void onResume() {
        super.onResume();

        getList();
    }

    public void getList() {

        theList = new ArrayList<>();
        IDList = new ArrayList<>();

        Cursor data = myDB.getListContents();

        if(data.getCount() == 0){
            listView.setAdapter(null);
            listView.deferNotifyDataSetChanged();

            listView.setEmptyView(findViewById(R.id.emptyElement));

        }

        else{
            while(data.moveToNext()){
                theList.add(data.getString(1));

                IDList.add(data.getInt(0));

            }

            ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,theList);
            listView.setAdapter(listAdapter);
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

        if (v.getId() == R.id.buttonMemory_Displayer) {
            openMemory_Displayer();
        }
    }

    public void openMemory_Displayer() {
        Intent intent = new Intent(this, Memory_Displayer.class);
        startActivity(intent);
    }


}