package com.example.project_b;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Create_Memory extends AppCompatActivity {

    DatabaseHelper myDB;
    Button btnAdd,btnView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__memory);

        myDB = new DatabaseHelper(this);

        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnView = (Button) findViewById(R.id.btnView);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Create_Memory.this, Collections.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = editText.getText().toString();
                if (editText.length() != 0){
                    AddData(newEntry);
                    editText.setText("");
                }
                else {
                    Toast.makeText(Create_Memory.this, "You must put something in the text field!",Toast.LENGTH_LONG).show();
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

        if (v.getId() == R.id.buttonPick_Location) {
            openPick_Location();
        }
    }

    public void AddData(String newEntry) {
        boolean insertData = myDB.addData(newEntry);

        if(insertData == true){
            Toast.makeText(Create_Memory.this, "Successfully Entered Data!",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(Create_Memory.this, "Something went wrong :(.",Toast.LENGTH_LONG).show();
        }
    }

    public void openPick_Location() {
        Intent intent = new Intent(this, Pick_Location.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "This is Pick Location", Toast.LENGTH_SHORT).show();
    }

}
