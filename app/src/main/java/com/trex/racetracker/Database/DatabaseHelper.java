package com.trex.racetracker.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Igor_2 on 22.10.2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_RACETRACKER = "Racetracker.db";
    public static final String TABLE_1_ACTIVE_RACERS = "ActiveRacers";
    public static final String COL_1_ACTIVE_RACER_ID = "ActiveRacerID";
    public static final String COL_1_RACER_ID = "RacerID";
    public static final String COL_1_RACE_ID = "RaceID";
    public static final String COL_1_AGE = "Age";
    public static final String COL_1_BIB = "BIB";
    public static final String COL_1_CHIP_CODE = "ChipCode";
    public static final String COL_1_HIDE = "Hide";
    public static final String COL_1_REGISTERED = "Registered";
    public static final String COL_1_ACTIVE_RACERS_TS = "ActiveRacersTS";
    public static final String COL_1_ACTIVE_RACERS_COMMENT = "ActiveRacersComment";
    public static final String COL_1_FIRST_NAME = "FirstName";
    public static final String COL_1_LAST_NAME = "LastName";
    public static final String COL_1_GENDER = "Gender";
    public static final String COL_1_DATE_OF_BIRTH = "DateOfBirth";
    public static final String COL_1_YEAR_BIRTH = "YearBirth";
    public static final String COL_1_NATIONALITY = "Nationality";
    public static final String COL_1_COUNTRY = "Country";
    public static final String COL_1_TEAM_ID = "TeamID";
    public static final String COL_1_CITY_OF_RESIDENCE = "CityOfResidence";
    public static final String COL_1_T_SHIRT_SIZE = "TShirtSize";
    public static final String COL_1_EMAIL = "Email";
    public static final String COL_1_TEL = "Tel";
    public static final String COL_1_FOOD = "Food";
    public static final String COL_1_RACERS_TS = "RacersTS";
    public static final String COL_1_RACERS_COMMENT = "RacersComment";
    public static final String COL_1_TEAM_NAME = "TeamName";
    public static final String COL_1_TEAM_DESCRIPTION = "TeamDescription";

    public static final String TABLE_2_CP_ENTRIES = "CPEntries";
    public static final String COL_2_ENTRY_ID = "EntryID";
    public static final String COL_2_CP_ID = "CPID";
    public static final String COL_2_CP_NAME = "CPName";
    public static final String COL_2_USER_ID = "UserID";
    public static final String COL_2_ACTIVE_RACER_ID = "ActiveRacerID";
    public static final String COL_2_BARCODE = "Barcode";
    public static final String COL_2_TIME = "Time";
    public static final String COL_2_ENTRY_TYPE_ID = "EntryTypeID";
    public static final String COL_2_COMMENT = "Comment";
    public static final String COL_2_SYNCED = "Synced";
    public static final String COL_2_MY_ENTRY = "myEntry";
    public static final String COL_2_BIB = "BIB";
    public static final String COL_2_VALID = "Valid";
    public static final String COL_2_OPERATOR = "Operator";
    public static final String COL_2_CO_NO = "CPNo";
    public static final String COL_2_REASON_INVALID = "ReasonInvalid";
    public static final String COL_2_TIMESTAMP = "TimeStamp";
    public static final String COL_2_LOCAL_ENTRY_ID = "LocalEntryID";

    public static final String TABLE_3_LOGIN_INFO = "LoginInfo";
    public static final String COL_3_CP_ID = "CPID";
    public static final String COL_3_CP_COMMENT = "CPComment";
    public static final String COL_3_CP_NAME = "CPName";
    public static final String COL_3_CP_NO = "CPNo";
    public static final String COL_3_RACE_ID = "RaceID";
    public static final String COL_3_RACE_NAME = "RaceName";
    public static final String COL_3_RACE_DESCRIPTION = "RaceDescription";
    public static final String COL_3_USERS_COMMENT = "UsersComment";

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
        super(ctx, DATABASE_RACETRACKER, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable1Query = "";
        createTable1Query += "CREATE TABLE IF NOT EXISTS " + TABLE_1_ACTIVE_RACERS + "(";
        createTable1Query += COL_1_ACTIVE_RACER_ID + " VARCHAR,";
        createTable1Query += COL_1_RACER_ID + " VARCHAR,";
        createTable1Query += COL_1_RACE_ID + " VARCHAR,";
        createTable1Query += COL_1_AGE + " INTEGER,";
        createTable1Query += COL_1_BIB + " VARCHAR,";
        createTable1Query += COL_1_CHIP_CODE + " VARCHAR,";
        createTable1Query += COL_1_HIDE + " BOOLEAN,";
        createTable1Query += COL_1_REGISTERED + " BOOLEAN,";
        createTable1Query += COL_1_ACTIVE_RACERS_TS + " VARCHAR,";
        createTable1Query += COL_1_ACTIVE_RACERS_COMMENT + " TEXT,";
        createTable1Query += COL_1_FIRST_NAME + " VARCHAR,";
        createTable1Query += COL_1_LAST_NAME + " VARCHAR,";
        createTable1Query += COL_1_GENDER + " VARCHAR,";
        createTable1Query += COL_1_DATE_OF_BIRTH + " DATE,";
        createTable1Query += COL_1_YEAR_BIRTH + " VARCHAR,";
        createTable1Query += COL_1_NATIONALITY + " VARCHAR,";
        createTable1Query += COL_1_COUNTRY + " VARCHAR,";
        createTable1Query += COL_1_TEAM_ID + " VARCHAR,";
        createTable1Query += COL_1_CITY_OF_RESIDENCE + " VARCHAR,";
        createTable1Query += COL_1_T_SHIRT_SIZE + " VARCHAR,";
        createTable1Query += COL_1_EMAIL + " VARCHAR,";
        createTable1Query += COL_1_TEL + " VARCHAR,";
        createTable1Query += COL_1_FOOD + " VARCHAR,";
        createTable1Query += COL_1_RACERS_TS + " DATETIME,";
        createTable1Query += COL_1_RACERS_COMMENT + " TEXT,";
        createTable1Query += COL_1_TEAM_NAME + " VARCHAR,";
        createTable1Query += COL_1_TEAM_DESCRIPTION + " VARCHAR);";

        db.execSQL(createTable1Query);

        String createTable2Query = "";
        createTable2Query += "CREATE TABLE IF NOT EXISTS " + TABLE_2_CP_ENTRIES + "(";
        createTable2Query += COL_2_LOCAL_ENTRY_ID + " VARCHAR PRIMARY KEY ASC,";   //EntryID
        createTable2Query += COL_2_ENTRY_ID + " VARCHAR,";   //EntryID
        createTable2Query += COL_2_CP_ID + " VARCHAR,";     //CPID
        createTable2Query += COL_2_CP_NAME + " VARCHAR,";        //CPName
        createTable2Query += COL_2_USER_ID + " VARCHAR,";     //UserID
        createTable2Query += COL_2_ACTIVE_RACER_ID + " VARCHAR,";     //ActiveRacerID
        createTable2Query += COL_2_BARCODE + " VARCHAR,";        //Barcode
        createTable2Query += COL_2_TIME + " VARCHAR,";    //Time
        createTable2Query += COL_2_ENTRY_TYPE_ID + " VARCHAR,";     //EntryTypeID - ID of the type of entry used (DIRECT, INDIRECT, NFC, BARCODE, OTHER...)
        createTable2Query += COL_2_COMMENT + " TEXT,";        //Comment
        createTable2Query += COL_2_SYNCED + " BOOLEAN,";    //Synced - Only valid when myEntry = true, shows if it has been sent to the server. For entries from another devices is always true
        createTable2Query += COL_2_MY_ENTRY + " BOOLEAN,";   //myEntry - is this my entry, or from another device, synced from the server
        createTable2Query += COL_2_BIB + " VARCHAR,";   //BIB - BIB entered
        createTable2Query += COL_2_VALID + " BOOLEAN,";   //valid - double entries from one control points are stored as valid=0 and are ignored. those are entries that are entered before the globals("timebetweenentries") minutes passes after the previous entry
        createTable2Query += COL_2_OPERATOR + " VARCHAR,";   //Operator
        createTable2Query += COL_2_CO_NO + " VARCHAR,";   //CPNo
        createTable2Query += COL_2_REASON_INVALID + " TEXT,";    //ReasonInvalid
        createTable2Query += COL_2_TIMESTAMP + " integer(4) not null default (strftime('%s','now')));";   //TimeStamp

        db.execSQL(createTable2Query);

        String createTable3Query = "";
        createTable3Query += "CREATE TABLE IF NOT EXISTS " + TABLE_3_LOGIN_INFO + "(";
        createTable3Query += COL_3_CP_ID + " VARCHAR,";
        createTable3Query += COL_3_CP_COMMENT + " VARCHAR,";
        createTable3Query += COL_3_CP_NAME + " VARCHAR,";
        createTable3Query += COL_3_CP_NO + " VARCHAR,";
        createTable3Query += COL_3_RACE_ID + " VARCHAR,";
        createTable3Query += COL_3_RACE_NAME + " VARCHAR,";
        createTable3Query += COL_3_RACE_DESCRIPTION + " VARCHAR,";
        createTable3Query += COL_3_USERS_COMMENT + " VARCHAR);";

        db.execSQL(createTable3Query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_1_ACTIVE_RACERS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_2_CP_ENTRIES);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_3_LOGIN_INFO);
        onCreate(db);
    }

}