package com.trex.racetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Igor_2 on 22.10.2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Racetracker.db";
    public static final String TABLE_1_NAME = "ActiveRacers";
    public static final String COL_1_1 = "ActiveRacerID";
    public static final String COL_1_2 = "RacerID";
    public static final String COL_1_3 = "RaceID";
    public static final String COL_1_4 = "Age";
    public static final String COL_1_5 = "BIB";
    public static final String COL_1_6 = "ChipCode";
    public static final String COL_1_7= "Started";
    public static final String COL_1_8 = "Registered";
    public static final String COL_1_9 = "ActiveRacersTS";
    public static final String COL_1_10 = "ActiveRacersComment";
    public static final String COL_1_11 = "FirstName";
    public static final String COL_1_12 = "LastName";
    public static final String COL_1_13 = "Gender";
    public static final String COL_1_14 = "DateOfBirth";
    public static final String COL_1_15 = "YearBirth";
    public static final String COL_1_16 = "Nationality";
    public static final String COL_1_17 = "Country";
    public static final String COL_1_18 = "TeamID";
    public static final String COL_1_19 = "CityOfResidence";
    public static final String COL_1_20 = "TShirtSize";
    public static final String COL_1_21 = "Email";
    public static final String COL_1_22 = "Tel";
    public static final String COL_1_23 = "Food";
    public static final String COL_1_24 = "RacersTS";
    public static final String COL_1_25 = "Racers_Comment";
    public static final String COL_1_26 = "TeamName";
    public static final String COL_1_27 = "TeamDescription";

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
    public static final String COL_2_11 = "myEntry";

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
        createTable1Query += COL_1_1 + " INTEGER,";
        createTable1Query += COL_1_2 + " INTEGER,";
        createTable1Query += COL_1_3 + " INTEGER,";
        createTable1Query += COL_1_4 + " INTEGER,";
        createTable1Query += COL_1_5 + " VARCHAR,";
        createTable1Query += COL_1_6 + " VARCHAR,";
        createTable1Query += COL_1_7 + " BOOLEAN,";
        createTable1Query += COL_1_8 + " BOOLEAN,";
        createTable1Query += COL_1_9 + " VARCHAR,";
        createTable1Query += COL_1_10 + " TEXT,";
        createTable1Query += COL_1_11 + " VARCHAR,";
        createTable1Query += COL_1_12 + " VARCHAR,";
        createTable1Query += COL_1_13 + " VARCHAR,";
        createTable1Query += COL_1_14 + " DATE,";
        createTable1Query += COL_1_15 + " VARCHAR,";
        createTable1Query += COL_1_16 + " VARCHAR,";
        createTable1Query += COL_1_17 + " VARCHAR,";
        createTable1Query += COL_1_18 + " INTEGER,";
        createTable1Query += COL_1_19 + " VARCHAR,";
        createTable1Query += COL_1_20 + " VARCHAR,";
        createTable1Query += COL_1_21 + " VARCHAR,";
        createTable1Query += COL_1_22 + " VARCHAR,";
        createTable1Query += COL_1_23 + " VARCHAR,";
        createTable1Query += COL_1_24 + " DATETIME,";
        createTable1Query += COL_1_25 + " TEXT,";
        createTable1Query += COL_1_26 + " VARCHAR,";
        createTable1Query += COL_1_27 + " VARCHAR);";

        db.execSQL(createTable1Query);

        String createTable2Query = "";
        createTable2Query += "CREATE TABLE IF NOT EXISTS " + TABLE_2_NAME + "(";
        createTable2Query += COL_2_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,";   //EntryID
        createTable2Query += COL_2_2 + " INTEGER,";     //CPID
        createTable2Query += COL_2_3 + " TEXT,";        //CPName
        createTable2Query += COL_2_4 + " INTEGER,";     //UserID
        createTable2Query += COL_2_5 + " INTEGER,";     //ActiveRacerID
        createTable2Query += COL_2_6 + " TEXT,";        //BIBCode
        createTable2Query += COL_2_7 + " DATETIME,";    //Time
        createTable2Query += COL_2_8 + " INTEGER,";     //EntryTypeID - ID of the type of entry used (DIRECT, INDIRECT, NFC, BARCODE, OTHER...)
        createTable2Query += COL_2_9 + " TEXT,";        //Comment
        createTable2Query += COL_2_10 + " BOOLEAN,";    //Synced - Only valid when myEntry = true, shows if it has been sent to the server. For entries from another devices is always true
        createTable2Query += COL_2_11 + " BOOLEAN);";   //myEntry - is this my entry, or from another device, synced from the server

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

        JSONtoSQLiteString jsontosqlite = new JSONtoSQLiteString(jsonString);
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

    public Cursor getDistinctRacesFromLoginInfo() {
        String query = "SELECT DISTINCT RaceID, RaceDescription FROM " + TABLE_3_NAME + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(query,null);
    }

    public boolean insertIntoActiveRacers(JSONObject jsonString) {
        SQLiteDatabase db = this.getWritableDatabase();

        JSONtoSQLiteString jsontosqlite = new JSONtoSQLiteString(jsonString);
        long result=0;
        int i=0;
        while (i < jsonString.length()-3) {
            if (result != -1) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_1_1,jsontosqlite.ActiveRacerID(i));
                contentValues.put(COL_1_2,jsontosqlite.RacerID(i));
                contentValues.put(COL_1_3,jsontosqlite.RaceID(i));
                contentValues.put(COL_1_4,jsontosqlite.Age(i));
                contentValues.put(COL_1_5,jsontosqlite.BIB(i));
                contentValues.put(COL_1_6,jsontosqlite.ChipCode(i));
                contentValues.put(COL_1_7,jsontosqlite.Started(i));
                contentValues.put(COL_1_8,jsontosqlite.Registered(i));
                contentValues.put(COL_1_9,jsontosqlite.ActiveRacersTS(i));
                contentValues.put(COL_1_10,jsontosqlite.ActiveRacersComment(i));
                contentValues.put(COL_1_11,jsontosqlite.FirstName(i));
                contentValues.put(COL_1_12,jsontosqlite.LastName(i));
                contentValues.put(COL_1_13,jsontosqlite.Gender(i));
                contentValues.put(COL_1_14,jsontosqlite.DateOfBirth(i));
                contentValues.put(COL_1_15,jsontosqlite.YearBirth(i));
                contentValues.put(COL_1_16,jsontosqlite.Nationality(i));
                contentValues.put(COL_1_17,jsontosqlite.Country(i));
                contentValues.put(COL_1_18,jsontosqlite.TeamID(i));
                contentValues.put(COL_1_19,jsontosqlite.CityOfResidence(i));
                contentValues.put(COL_1_20,jsontosqlite.TShirtSize(i));
                contentValues.put(COL_1_21,jsontosqlite.Email(i));
                contentValues.put(COL_1_22,jsontosqlite.Tel(i));
                contentValues.put(COL_1_23,jsontosqlite.Food(i));
                contentValues.put(COL_1_24,jsontosqlite.RacersTS(i));
                contentValues.put(COL_1_25,jsontosqlite.Racers_Comment(i));
                contentValues.put(COL_1_26,jsontosqlite.TeamName(i));
                contentValues.put(COL_1_27,jsontosqlite.TeamDescription(i));

                result = db.insert(TABLE_1_NAME,null,contentValues);
            }
            i++;
        }
        if (result == -1) return false;
        else return true;
    }

    /**  get 6 columns from ActiveRacers to form the array used for the adapter on the listview
        (the last 2 columns will be gotten from CPEntries)
    **/
    public Cursor getActiveRacersForListView(String whereClause) {

        String query = "SELECT BIB, FirstName, LastName, Country, Age, Gender, ActiveRacerID FROM " + TABLE_1_NAME + " WHERE ";
        if (whereClause.equals("")) {
            query += "1";
        }
        else {
            query += whereClause;
        }
        query += " ORDER BY BIB ASC;";
                SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(query,null);
    }

    public int deleteAllFromActiveRacers() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_1_NAME, "1",null);
    }

    public Cursor getLastEntryRow(String ActiveRacerID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Time, CPID, CPName FROM " + TABLE_2_NAME + " WHERE ActiveRacerID='" + ActiveRacerID + "' ORDER BY Time DESC LIMIT 1";
        return db.rawQuery(query,null);
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

