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

    public static final String LOCATIONS_TABLE_NAME = "locations";
    public static final String LOCATIONS_COL1 = "MemoryID";
    public static final String LOCATIONS_COL2 = "Latitude";
    public static final String LOCATIONS_COL3 = "Longitude";

    public static final String PICTURES_TABLE_NAME = "pictures";
    public static final String PICTURES_COL1 = "MemoryID";
    public static final String PICTURES_COL2 = "Path";
    public static final String PICTURES_COL3 = "Filename";


    public DatabaseHelper(Context context){super(context,DATABASE_NAME,null,1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStories = "CREATE TABLE " + MEMORIES_TABLE_NAME + " (MemoryID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MEMORIES_COL2 + " TEXT)";
        db.execSQL(createTableStories);

        String createTableLocations = "CREATE TABLE " + LOCATIONS_TABLE_NAME + " (" + LOCATIONS_COL1 + " INTEGER, " +
                LOCATIONS_COL2 + " REAL, " + LOCATIONS_COL3 + " REAL, " + "FOREIGN KEY(MemoryID) REFERENCES memories(MemoryID))";
        db.execSQL(createTableLocations);

        String createTablePictures = "CREATE TABLE " + PICTURES_TABLE_NAME + " (" + PICTURES_COL1 + " INTEGER, " +
                PICTURES_COL2 + " TEXT, " + PICTURES_COL3 + " TEXT, " + "FOREIGN KEY(MemoryID) REFERENCES memories(MemoryID))";
        db.execSQL(createTablePictures);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MEMORIES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LOCATIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PICTURES_TABLE_NAME);

        onCreate(db);
    }

    public void restartDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + MEMORIES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LOCATIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PICTURES_TABLE_NAME);

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
        contentValues.put(LOCATIONS_COL1, ID);
        contentValues.put(LOCATIONS_COL2, latitude);
        contentValues.put(LOCATIONS_COL3, longitude);

        db.insert(LOCATIONS_TABLE_NAME,null, contentValues);
    }

    public Cursor getLocations() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + MEMORIES_TABLE_NAME + " AS m" + ", " + LOCATIONS_TABLE_NAME + " AS l " + " WHERE " +
                "m." + MEMORIES_COL1 + " = l." + LOCATIONS_COL1 , null);
        return data;
    }

    public Cursor getIDbyName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + MEMORIES_COL1 + " FROM " + MEMORIES_TABLE_NAME +
                " WHERE " + MEMORIES_COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public void addPicture(String path, String fileName , int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PICTURES_COL1, ID);
        contentValues.put(PICTURES_COL2, path);
        contentValues.put(PICTURES_COL3, fileName);

        db.insert(PICTURES_TABLE_NAME, null, contentValues);

    }

    public Cursor getPictures() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + MEMORIES_TABLE_NAME + " AS m" + ", " + PICTURES_TABLE_NAME + " AS p " + " WHERE " +
                "m." + MEMORIES_COL1 + " = p." + PICTURES_COL1 , null);
        return data;
    }

}
