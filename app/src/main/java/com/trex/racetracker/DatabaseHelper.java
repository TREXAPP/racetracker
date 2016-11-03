package com.trex.racetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

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

    public static final String TABLE_3_NAME = "LoginInfo";
    public static final String COL_3_1 = "CPID";
    public static final String COL_3_2 = "CPComment";
    public static final String COL_3_3 = "CPName";
    public static final String COL_3_4 = "CPNo";
    public static final String COL_3_5 = "RaceID";
    public static final String COL_3_6 = "RaceName";
    public static final String COL_3_7 = "RaceDescription";
    public static final String COL_3_8 = "UsersComment";


    public DatabaseHelper(Context context) {
//Environment.getExternalStorageDirectory() + File.separator + "dbtest" + File.separator + DATABASE_NAME
        super(context, DATABASE_NAME, null, 1);
        //testing:
        //SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + File.separator + DATABASE_NAME,null);

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

        String createTable3Query = "";
        createTable3Query += "CREATE TABLE IF NOT EXISTS " + TABLE_3_NAME + "(";
        createTable3Query += COL_3_1 + " VARCHAR,";
        createTable3Query += COL_3_2 + " VARCHAR,";
        createTable3Query += COL_3_3 + " VARCHAR,";
        createTable3Query += COL_3_4 + " VARCHAR,";
        createTable3Query += COL_3_5 + " VARCHAR,";
        createTable3Query += COL_3_6 + " VARCHAR,";
        createTable3Query += COL_3_7 + " VARCHAR,";
        createTable3Query += COL_3_8 + " VARCHAR);";

        db.execSQL(createTable3Query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_1_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_2_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_3_NAME);
        onCreate(db);
    }

    public boolean insertIntoLoginInfo(JSONObject jsonString) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();

        JSONtoSQLiteLoginString jsontosqlite = new JSONtoSQLiteLoginString(jsonString);
        long result=0;
        int i=0;
        while (i < jsonString.length()-2) {
            if (result != -1) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_3_1,jsontosqlite.CPID(i));
                contentValues.put(COL_3_2,jsontosqlite.CPComment(i));
                contentValues.put(COL_3_3,jsontosqlite.CPName(i));
                contentValues.put(COL_3_4,jsontosqlite.CPNo(i));
                contentValues.put(COL_3_5,jsontosqlite.RaceID(i));
                contentValues.put(COL_3_6,jsontosqlite.RaceName(i));
                contentValues.put(COL_3_7,jsontosqlite.RaceDescription(i));
                contentValues.put(COL_3_8,jsontosqlite.UsersComment(i));

                result = db.insert(TABLE_3_NAME,null,contentValues);
            }
            i++;
        }
        if (result == -1) return false;
        else return true;
    }

    public int deleteAllFromLoginInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_3_NAME, "",null);
    }

    /**
    Globals used to be managed with SQLite, but now it is done with SharedPreferences, the code is left for reference with SQLite db management

    public boolean insertValueIntoGlobals (String name, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3_1,name);
        contentValues.put(COL_3_2,value);
        long result = db.insert(TABLE_3_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getValueFromGlobals(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_3_NAME + " WHERE NAME='" + name + "';",null);
        return res;
    }

    public boolean updateValueInGlobals(String name,String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_3_1,name);
        contentValues.put(COL_3_2,value);
        db.update(TABLE_3_NAME, contentValues, "NAME = ?",new String[] { name });
        return true;
    }

    public Integer deleteValueFromGlobals (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_3_NAME, "NAME = ?",new String[] { name });
    }

 **/
}

