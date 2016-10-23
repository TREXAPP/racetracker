package com.trex.racetracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

/**
 * Created by Igor_2 on 22.10.2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Racetracker.db";
    public static final String TABLE_1_NAME = "ActiveRacers";
    public static final String COL_1_1 = "ActiveRacerID";
    public static final String COL_1_2 = "FirstName";
    public static final String COL_1_3 = "LastName";
    public static final String COL_1_4 = "Gender";
    public static final String COL_1_5 = "DateOfBirth";
    public static final String COL_1_6 = "Nationality";
    public static final String COL_1_7 = "Country";
    public static final String COL_1_8 = "CityOfResidence";
    public static final String COL_1_9 = "TShirtSize";
    public static final String COL_1_10 = "Email";
    public static final String COL_1_11 = "Tel";
    public static final String COL_1_12 = "Food";
    public static final String COL_1_13 = "Comment_Racers";
    public static final String COL_1_14 = "BIB";
    public static final String COL_1_15 = "BarCode";
    public static final String COL_1_16 = "Comment_ActiveRacers";
    public static final String COL_1_17 = "Started";
    public static final String COL_1_18 = "Registered";
    public static final String COL_1_19 = "TeamName";

    public static final String TABLE_2_NAME = "CPEntries";
    public static final String COL_2_1 = "EntryID";
    public static final String COL_2_2 = "CPID";
    public static final String COL_2_3 = "CPName";
    public static final String COL_2_4 = "UserID";
    public static final String COL_2_5 = "ActiveRacerID";
    public static final String COL_2_6 = "BIBCODE";
    public static final String COL_2_7 = "Time";
    public static final String COL_2_8 = "EntryTypeID";
    public static final String COL_2_9 = "Comment";
    public static final String COL_2_10 = "Synced";


    public DatabaseHelper(Context context) {
//Environment.getExternalStorageDirectory() + File.separator + "dbtest" + File.separator + DATABASE_NAME
        super(context, DATABASE_NAME, null, 1);
        //testing:
        //SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + File.separator + DATABASE_NAME,null);
        SQLiteDatabase db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable1Query = "";
        createTable1Query += "CREATE TABLE IF NOT EXISTS " + TABLE_1_NAME + "(";
        createTable1Query += COL_1_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,";
        createTable1Query += COL_1_2 + " TEXT,";
        createTable1Query += COL_1_3 + " TEXT,";
        createTable1Query += COL_1_4 + " TEXT,";
        createTable1Query += COL_1_5 + " TEXT,";
        createTable1Query += COL_1_6 + " TEXT,";
        createTable1Query += COL_1_7 + " TEXT,";
        createTable1Query += COL_1_8 + " TEXT,";
        createTable1Query += COL_1_9 + " TEXT,";
        createTable1Query += COL_1_10 + " TEXT,";
        createTable1Query += COL_1_11 + " TEXT,";
        createTable1Query += COL_1_12 + " TEXT,";
        createTable1Query += COL_1_13 + " TEXT,";
        createTable1Query += COL_1_14 + " TEXT,";
        createTable1Query += COL_1_15 + " TEXT,";
        createTable1Query += COL_1_16 + " TEXT,";
        createTable1Query += COL_1_17 + " BOOLEAN,";
        createTable1Query += COL_1_18 + " BOOLEAN,";
        createTable1Query += COL_1_19 + " TEXT);";

        db.execSQL(createTable1Query);

        String createTable2Query = "";
        createTable2Query += "CREATE TABLE IF NOT EXISTS " + TABLE_2_NAME + "(";
        createTable2Query += COL_2_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,";
        createTable2Query += COL_2_2 + " INTEGER,";
        createTable2Query += COL_2_3 + " TEXT,";
        createTable2Query += COL_2_4 + " INTEGER,";
        createTable2Query += COL_2_5 + " INTEGER,";
        createTable2Query += COL_2_6 + " TEXT,";
        createTable2Query += COL_2_7 + " DATETIME,";
        createTable2Query += COL_2_8 + " INTEGER";
        createTable2Query += COL_2_9 + " TEXT,";
        createTable2Query += COL_2_10 + " BOOLEAN);";

        db.execSQL(createTable2Query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_1_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_2_NAME);
        onCreate(db);
    }
}
