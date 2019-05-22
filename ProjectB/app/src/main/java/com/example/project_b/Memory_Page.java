package com.example.project_b;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;



public class Memory_Page extends AppCompatActivity {

    DatabaseHelper myDB;

    private String titleDB;
    private int idDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory__page);

        myDB = new DatabaseHelper(this);


        Intent receivedIntent = getIntent();


        //Verkrijgt ID
        idDB = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        //Verkrijgt Name
        titleDB = receivedIntent.getStringExtra("name");

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(titleDB);
    }


}
