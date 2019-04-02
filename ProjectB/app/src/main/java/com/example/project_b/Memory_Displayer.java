package com.example.project_b;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Memory_Displayer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory__displayer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView toolbar_text = findViewById(R.id.toolbar_text);

        toolbar_text.setText("Memory Displayer");

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

        if (v.getId() == R.id.buttonEdit_Memory) {
            openEdit_Memory();
        }
        else if (v.getId() == R.id.buttonMemory_Filter_Sorter) {
            openMemory_Filter_Sorter();
        }
    }

    public void openEdit_Memory() {
        Intent intent = new Intent(this, Edit_Memory.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "This is Edit Memory", Toast.LENGTH_SHORT).show();
    }

    public void openMemory_Filter_Sorter() {
        Intent intent = new Intent(this, Memory_Filter_Sorter.class);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "This is Memory Filter/Sorter", Toast.LENGTH_SHORT).show();
    }
}
