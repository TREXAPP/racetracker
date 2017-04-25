package com.trex.racetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.trex.racetracker.StaticMethods.*;

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
    public static final String COL_2_6 = "Barcode";
    public static final String COL_2_7 = "Time";
    public static final String COL_2_8 = "EntryTypeID";
    public static final String COL_2_9 = "Comment";
    public static final String COL_2_10 = "Synced";
    public static final String COL_2_11 = "myEntry";
    public static final String COL_2_12 = "BIB";
    public static final String COL_2_13 = "Valid";
    public static final String COL_2_14 = "Operator";
    public static final String COL_2_15 = "CPNo";
    public static final String COL_2_16 = "ReasonInvalid";
    public static final String COL_2_17 = "TimeStamp";

    public static final String TABLE_3_NAME = "LoginInfo";
    public static final String COL_3_1 = "CPID";
    public static final String COL_3_2 = "CPComment";
    public static final String COL_3_3 = "CPName";
    public static final String COL_3_4 = "CPNo";
    public static final String COL_3_5 = "RaceID";
    public static final String COL_3_6 = "RaceName";
    public static final String COL_3_7 = "RaceDescription";
    public static final String COL_3_8 = "UsersComment";

    private static Context context = null;

    private static DatabaseHelper mInstance = null;

    public static DatabaseHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        if (context == null) {
            context = ctx;
        }
        return mInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static factory method "getInstance()" instead.
     */
    private DatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, 1);
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
        createTable2Query += COL_2_1 + " INTEGER,";   //EntryID
        createTable2Query += COL_2_2 + " INTEGER,";     //CPID
        createTable2Query += COL_2_3 + " VARCHAR,";        //CPName
        createTable2Query += COL_2_4 + " INTEGER,";     //UserID
        createTable2Query += COL_2_5 + " INTEGER,";     //ActiveRacerID
        createTable2Query += COL_2_6 + " VARCHAR,";        //Barcode
        createTable2Query += COL_2_7 + " VARCHAR,";    //Time
        createTable2Query += COL_2_8 + " INTEGER,";     //EntryTypeID - ID of the type of entry used (DIRECT, INDIRECT, NFC, BARCODE, OTHER...)
        createTable2Query += COL_2_9 + " TEXT,";        //Comment
        createTable2Query += COL_2_10 + " BOOLEAN,";    //Synced - Only valid when myEntry = true, shows if it has been sent to the server. For entries from another devices is always true
        createTable2Query += COL_2_11 + " BOOLEAN,";   //myEntry - is this my entry, or from another device, synced from the server
        createTable2Query += COL_2_12 + " VARCHAR,";   //BIB - BIB entered
        createTable2Query += COL_2_13 + " BOOLEAN,";   //valid - double entries from one control points are stored as valid=0 and are ignored. those are entries that are entered before the globals("timebetweenentries") minutes passes after the previous entry
        createTable2Query += COL_2_14 + " VARCHAR,";   //Operator
        createTable2Query += COL_2_15 + " VARCHAR,";   //CPNo
        createTable2Query += COL_2_16 + " TEXT,";    //ReasonInvalid
        createTable2Query += COL_2_17 + " integer(4) not null default (strftime('%s','now')));";   //TimeStamp

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

}