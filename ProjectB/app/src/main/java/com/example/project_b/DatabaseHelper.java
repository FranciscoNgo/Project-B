package com.example.project_b;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TripTracker.db";

    public static final String MEMORIES_TABLE_NAME = "memories";
    public static final String MEMORIES_COL1 = "MemoryID";
    public static final String MEMORIES_COL2 = "Item1";

    public static final String LOCATION_TABLE_NAME = "location";
    public static final String LOCATION_COL1 = "MemoryID";
    public static final String LOCATION_COL2 = "Latitude";
    public static final String LOCATION_COL3 = "Longitude";

    public DatabaseHelper(Context context){super(context,DATABASE_NAME,null,1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStories = "CREATE TABLE " + MEMORIES_TABLE_NAME + " (MemoryID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MEMORIES_COL2 + " TEXT)";
        db.execSQL(createTableStories);

        String createTableMarkers = "CREATE TABLE " + LOCATION_TABLE_NAME + " (" + LOCATION_COL1 + " INTEGER, " +
                LOCATION_COL2 + " REAL, " + LOCATION_COL3 + " REAL, " + "FOREIGN KEY(MemoryID) REFERENCES memories(MemoryID))";
        db.execSQL(createTableMarkers);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MEMORIES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE_NAME);

        onCreate(db);
    }

    public void restartDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + MEMORIES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LOCATION_TABLE_NAME);

        onCreate(db);
        Log.i("Test", "Restart Successful");
    }

    public int addMemory(String item1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEMORIES_COL2, item1);

        long result = db.insert(MEMORIES_TABLE_NAME,null, contentValues);

        int ID = -1;

        if(result != -1){

            Cursor data = db.rawQuery("SELECT last_insert_rowid()", null);
            if(data.moveToFirst()) {
                ID = data.getInt(0);
            }
        }

        return ID;
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + MEMORIES_TABLE_NAME, null);
        return data;
    }

    public void addLocation(double latitude, double longitude, int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATION_COL1, ID);
        contentValues.put(LOCATION_COL2, latitude);
        contentValues.put(LOCATION_COL3, longitude);

        db.insert(LOCATION_TABLE_NAME,null, contentValues);
    }

    public Cursor getLocations(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + MEMORIES_TABLE_NAME + " AS m" + ", " + LOCATION_TABLE_NAME + " AS l " + " WHERE " +
                "m." + MEMORIES_COL1 + " = l." + LOCATION_COL1 , null);
        return data;
    }


}
