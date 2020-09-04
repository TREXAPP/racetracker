package com.trex.racetracker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.trex.racetracker.Adapters.EntriesInputListAdapter;
import com.trex.racetracker.Helpers.JSONtoSQLiteString;
import com.trex.racetracker.Models.EntryObj;
import com.trex.racetracker.Provider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

import static com.trex.racetracker.Database.DatabaseHelper.*;
import static com.trex.racetracker.StaticMethods.*;

/**
 * Created by Igor on 21.4.2017.
 */

public class DbMethods {

    private static Provider mProvider = new Provider();

    public DbMethods() {
        mProvider = new Provider();
    }

    public static boolean insertIntoLoginInfo(JSONObject checkpointJson, JSONArray racesJsonArray, Context context) throws JSONException {

        Uri resultUri;
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_3_LOGIN_INFO);

        String result="0";
        for (int i=0; i<racesJsonArray.length(); i++) {
            JSONObject raceJsonObj = racesJsonArray.getJSONObject(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_3_CP_ID, checkpointJson.has("CPID") ? checkpointJson.getString("CPID") : null);
            contentValues.put(COL_3_CP_COMMENT, checkpointJson.has("CPComment") ? checkpointJson.getString("CPComment") : null);
            contentValues.put(COL_3_CP_NAME, checkpointJson.has("CPName") ? checkpointJson.getString("CPName") : null);
            contentValues.put(COL_3_CP_NO, raceJsonObj.has("CPNo") ? raceJsonObj.getString("CPNo") : null);
            contentValues.put(COL_3_RACE_ID, raceJsonObj.has("RaceID") ? raceJsonObj.getString("RaceID") : null);
            contentValues.put(COL_3_RACE_NAME, raceJsonObj.has("RaceName") ? raceJsonObj.getString("RaceName") : null);
            contentValues.put(COL_3_RACE_DESCRIPTION, raceJsonObj.has("RaceDescription") ? raceJsonObj.getString("RaceDescription") : null);
            contentValues.put(COL_3_USERS_COMMENT, raceJsonObj.has("UsersComment") ? raceJsonObj.getString("UsersComment") : null);

            resultUri = context.getContentResolver().insert(uri, contentValues);
            result = resultUri != null ? resultUri.getLastPathSegment() : "1";
        }

        context.getContentResolver().notifyChange(uri, null, false);
        if (result.equals("-1")) return false;
        else return true;
    }

    public static int deleteAllFromLoginInfo(Context context) {
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_3_LOGIN_INFO);
        return context.getContentResolver().delete(uri,"",null);
    }

    public static Cursor getDistinctRacesFromLoginInfo(Context context) {
       // String query = "SELECT DISTINCT RaceID, RaceDescription FROM " + TABLE_3_NAME + ";";
        //SQLiteDatabase db = this.getWritableDatabase();
       // return db.rawQuery(query,null);

        String[] projection = new String[]{"DISTINCT RaceID", "RaceDescription", "RaceName"};
        String selection = null;
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_3_LOGIN_INFO);
        return context.getContentResolver().query(uri,projection,selection,null,"");
    }

    public static Cursor getDistinctCPNoFromLoginInfo(Context context, String selection) {
        /*
        String query = "SELECT DISTINCT CPNo FROM " + TABLE_3_NAME + " WHERE ";
        if (whereClause.equals("")) {
            query += "1";
        }
        else {
            query += whereClause;
        }
        query += " ORDER BY CPNo ASC;";
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(query,null);
*/
        String[] projection = new String[]{"DISTINCT CPNo"};
        String sortOrder = "CPNo ASC";
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_3_LOGIN_INFO);
        return context.getContentResolver().query(uri,projection,selection,null,sortOrder);
    }

    public static Cursor getDistinctCPFromEntries(Context context, String selection) {
        /*
        String query = "SELECT DISTINCT CPNo FROM " + TABLE_3_NAME + " WHERE ";
        if (whereClause.equals("")) {
            query += "1";
        }
        else {
            query += whereClause;
        }
        query += " ORDER BY CPNo ASC;";
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(query,null);
*/
        String[] projection = new String[]{"DISTINCT CPNo, CPName"};
        String sortOrder = "CPNo ASC";
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_2_CP_ENTRIES);
        return context.getContentResolver().query(uri,projection,selection,null,sortOrder);
    }

    public static boolean insertIntoActiveRacers(JSONArray racersJsonArray, Context context) {


        Uri resultUri;
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_1_ACTIVE_RACERS);

        String result="0";
        try {
            for (int i=0; i<racersJsonArray.length(); i++) {
                JSONObject racerJsonObj = racersJsonArray.getJSONObject(i);

                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_1_ACTIVE_RACER_ID, racerJsonObj.has("ActiveRacerID") ? racerJsonObj.getString("ActiveRacerID") : null);
                contentValues.put(COL_1_RACER_ID, racerJsonObj.has("RacerID") ? racerJsonObj.getString("RacerID") : null);
                contentValues.put(COL_1_RACE_ID, racerJsonObj.has("RaceID") ? racerJsonObj.getString("RaceID") : null);
                contentValues.put(COL_1_AGE, racerJsonObj.has("Age") ? racerJsonObj.getString("Age") : null);
                contentValues.put(COL_1_BIB, racerJsonObj.has("BIB") ? racerJsonObj.getString("BIB") : null);
                contentValues.put(COL_1_CHIP_CODE, racerJsonObj.has("ChipCode") ? racerJsonObj.getString("ChipCode") : null);
                contentValues.put(COL_1_HIDE, racerJsonObj.has("Hide") ? racerJsonObj.getString("Hide") : null);
                contentValues.put(COL_1_REGISTERED, racerJsonObj.has("Registered") ? racerJsonObj.getString("Registered") : null);
                contentValues.put(COL_1_ACTIVE_RACERS_TS, racerJsonObj.has("ActiveRacersTS") ? racerJsonObj.getString("ActiveRacersTS") : null);
                contentValues.put(COL_1_ACTIVE_RACERS_COMMENT, racerJsonObj.has("ActiveRacersComment") ? racerJsonObj.getString("ActiveRacersComment") : null);
                contentValues.put(COL_1_FIRST_NAME, racerJsonObj.has("FirstName") ? racerJsonObj.getString("FirstName") : null);
                contentValues.put(COL_1_LAST_NAME, racerJsonObj.has("LastName") ? racerJsonObj.getString("LastName") : null);
                contentValues.put(COL_1_GENDER, racerJsonObj.has("Gender") ? racerJsonObj.getString("Gender") : null);
                contentValues.put(COL_1_DATE_OF_BIRTH, racerJsonObj.has("DateOfBirth") ? racerJsonObj.getString("DateOfBirth") : null);
                contentValues.put(COL_1_YEAR_BIRTH, racerJsonObj.has("YearBirth") ? racerJsonObj.getString("YearBirth") : null);
                contentValues.put(COL_1_NATIONALITY, racerJsonObj.has("Nationality") ? racerJsonObj.getString("Nationality") : null);
                contentValues.put(COL_1_COUNTRY, racerJsonObj.has("Country") ? racerJsonObj.getString("Country") : null);
                contentValues.put(COL_1_TEAM_ID, racerJsonObj.has("TeamID") ? racerJsonObj.getString("TeamID") : null);
                contentValues.put(COL_1_CITY_OF_RESIDENCE, racerJsonObj.has("CityOfResidence") ? racerJsonObj.getString("CityOfResidence") : null);
                contentValues.put(COL_1_T_SHIRT_SIZE, racerJsonObj.has("TShirtSize") ? racerJsonObj.getString("TShirtSize") : null);
                contentValues.put(COL_1_EMAIL, racerJsonObj.has("Email") ? racerJsonObj.getString("Email") : null);
                contentValues.put(COL_1_TEL, racerJsonObj.has("Tel") ? racerJsonObj.getString("Tel") : null);
                contentValues.put(COL_1_FOOD, racerJsonObj.has("Food") ? racerJsonObj.getString("Food") : null);
                contentValues.put(COL_1_RACERS_TS, racerJsonObj.has("RacersTS") ? racerJsonObj.getString("RacersTS") : null);
                contentValues.put(COL_1_RACERS_COMMENT, racerJsonObj.has("RacersComment") ? racerJsonObj.getString("RacersComment") : null);
                contentValues.put(COL_1_TEAM_NAME, racerJsonObj.has("TeamName") ? racerJsonObj.getString("TeamName") : null);
                contentValues.put(COL_1_TEAM_DESCRIPTION, racerJsonObj.has("TeamDescription") ? racerJsonObj.getString("TeamDescription") : null);

                resultUri = context.getContentResolver().insert(uri, contentValues);
                result = resultUri != null ? resultUri.getLastPathSegment() : "1";
            }

            if (result.equals("-1")) return false;
            else return true;

        } catch (Exception ex) {
            return false;
        }

    }

    /**  get 6 columns from ActiveRacers to form the array used for the adapter on the listview
     (the last 2 columns will be from CPEntries)
     **/
    public static Cursor getActiveRacersForListView(Context context, String selection) {
        /*
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
        */
        String[] projection = new String[]{"BIB","FirstName","LastName","Country","DateOfBirth","Gender","ActiveRacerID"};
        String sortOrder = "BIB ASC";
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_1_ACTIVE_RACERS);
        return context.getContentResolver().query(uri,projection,selection,null,sortOrder);
    }

    public static int deleteAllFromActiveRacers(Context context) {
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_1_ACTIVE_RACERS);
        return context.getContentResolver().delete(uri,"",null);
    }

    public static Cursor getLastEntryRow(Context context, String ActiveRacerID) {
        /*
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT Time, CPNo, CPName FROM " + TABLE_2_NAME + " WHERE ActiveRacerID='" + ActiveRacerID + "' AND Valid=1 ORDER BY Time DESC LIMIT 1";
        return db.rawQuery(query,null);
        */

        String[] projection = new String[]{"Time","CPNo","CPName"};
        String selection = "ActiveRacerID='" + ActiveRacerID + "' AND Valid=1";
        String sortOrder = "Time DESC LIMIT 1";
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_2_CP_ENTRIES);
        return context.getContentResolver().query(uri,projection,selection,null,sortOrder);
    }

    public static Cursor getActiveRacerIDFromRacers(Context context, String inputedBIB) {
        /*
        String query = "SELECT ActiveRacerID, FirstName, LastName, Country, Gender, Age, RaceID FROM " + TABLE_1_NAME + " WHERE BIB = " +  inputedBIB + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        return cursor;
        */

        String[] projection = new String[]{"ActiveRacerID","FirstName","LastName", "Country", "Gender", "DateOfBirth", "RaceID"};
        String selection = "BIB = '" +  inputedBIB + "'";
        String sortOrder = "";
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_1_ACTIVE_RACERS);
        return context.getContentResolver().query(uri,projection,selection,null,sortOrder);
    }

    public static Cursor getEntryDataFromLoginInfo(Context context, String selection, String sortOrder) {
/*
        if (whereClause != null) {
            if (whereClause.equals("")) {
                whereClause = "1";
            } else {
                //ok
            }
        } else {
            whereClause = "1";
        }

        if (orderbyClause != null) {
            if (!orderbyClause.equals("")) {
                //ok
                orderbyClause = " ORDER BY " + orderbyClause;
            }
        } else {
            orderbyClause = "";
        }

        String query = "SELECT CPID, CPName, CPNo, RaceID FROM " + TABLE_3_NAME + " WHERE " + whereClause + orderbyClause + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        return cursor;
        */
        String[] projection = new String[]{"CPID","CPName","CPNo", "RaceID"};
        //String selection = "BIB = " +  inputedBIB;
        //String sortOrder = "";
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_3_LOGIN_INFO);
        return context.getContentResolver().query(uri,projection,selection,null,sortOrder);

    }

    public static boolean insertIntoCPEntries(Context context, EntryObj entryObj, Boolean notifyChange) {
        String result;
        Uri resultUri;
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_2_CP_ENTRIES);
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2_LOCAL_ENTRY_ID,UUID.randomUUID().toString());
        contentValues.put(COL_2_ENTRY_ID,entryObj.getEntryID());
        contentValues.put(COL_2_CP_ID,entryObj.getCPID());
        contentValues.put(COL_2_CP_NAME,entryObj.getCPName());
        contentValues.put(COL_2_USER_ID,entryObj.getUserID());
        contentValues.put(COL_2_ACTIVE_RACER_ID,entryObj.getActiveRacerID());
        contentValues.put(COL_2_BARCODE,entryObj.getBarcode());
        contentValues.put(COL_2_TIME,entryObj.getTime());
        contentValues.put(COL_2_ENTRY_TYPE_ID,entryObj.getEntryTypeID());
        contentValues.put(COL_2_COMMENT,entryObj.getComment());
        contentValues.put(COL_2_SYNCED,entryObj.isSynced());
        contentValues.put(COL_2_MY_ENTRY,entryObj.isMyEntry());
        contentValues.put(COL_2_BIB,entryObj.getBIB());
        contentValues.put(COL_2_VALID,entryObj.isValid());
        contentValues.put(COL_2_OPERATOR,entryObj.getOperator());
        contentValues.put(COL_2_CO_NO,entryObj.getCPNo());
        contentValues.put(COL_2_REASON_INVALID,entryObj.getReasonInvalid());
        contentValues.put(COL_2_INSERTTIMESTAMP,entryObj.getInsertTimeStamp());
        contentValues.put(COL_2_UPDATETIMESTAMP,entryObj.getUpdateTimeStamp());
        contentValues.put(COL_2_DELETETIMESTAMP,entryObj.getDeleteTimeStamp());

        resultUri = context.getContentResolver().insert(uri, contentValues);
        result = resultUri != null ? resultUri.getLastPathSegment() : "1";
        if (notifyChange) {
            context.getContentResolver().notifyChange(uri, null, false);
        }
        if(result.equals("-1"))
            return false;
        else
            return true;

    }

    public static Date getDateForLastEntry(Context context, String inputedBIB) {
        String[] projection = new String[]{"Time"};
        String selection = "BIB = '" +  inputedBIB + "' AND Valid = 1";
        String sortOrder = "Time DESC LIMIT 1";
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_2_CP_ENTRIES);
        Cursor cursor = context.getContentResolver().query(uri,projection,selection,null,sortOrder);
        Date entryTime = null;
        if ((cursor != null ? cursor.getCount() : 0) >0) {
            cursor.moveToFirst();
            entryTime = formatDateTime(cursor.getString(0));
        }
        cursor.close();
        return entryTime;
    }

    public static boolean hasRunnerPassedThroughCP(Context context, String inputedBIB, String cpNo) {
        boolean result = false;
        /*
        String query = "SELECT CPNo FROM " + TABLE_2_NAME + " WHERE BIB='" + inputedBIB + "' AND CPNo='" + cpNo + "' AND Valid=1;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.getCount()>0) {
            result = true;
        }
        return result;
        */
        String[] projection = new String[]{"CPNo"};
        String selection = "BIB = '" +  inputedBIB + "' AND CPNo='" + cpNo + "' AND Valid=1 AND myEntry=1";
        String sortOrder = "";
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_2_CP_ENTRIES);
        Cursor cursor = context.getContentResolver().query(uri,projection,selection,null,sortOrder);
        if ((cursor != null ? cursor.getCount() : 0) >0) {
            result = true;
        }
        if (cursor != null) {
            cursor.close();
        }
        return result;
    }

    public static Cursor getEntriesInput(Context context, boolean myEntriesOnly, boolean validOnly, Integer limit, String whereClause, String orderbyClause) {

        if (whereClause.equals("")) {
            whereClause = " 1 ";
        }

        String myEntriesClause = "";
        if (myEntriesOnly) {
            myEntriesClause = " AND myEntry=1 ";
        }

        String  validClause = "";
        if (validOnly) {
            validClause = " AND Valid=1 ";
        }

        String limitClause = "";

        if (limit != null) {
            if (orderbyClause.equals("")) {
                limitClause = " ASC LIMIT " + limit;
            } else {
                limitClause = " LIMIT " + limit;
            }
        }

 /*
        String query = "SELECT BIB, Time, TimeStamp FROM " + TABLE_2_NAME + " WHERE " + whereClause + myEntriesClause + validClause + orderbyClause + limitClause + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        return cursor;
        */
        String[] projection = new String[]{"CPEntries.BIB","Time","InsertTimeStamp","UpdateTimeStamp","DeleteTimeStamp","Synced"};
        String selection = whereClause + myEntriesClause + validClause;
        String sortOrder = orderbyClause + limitClause;
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_2_CP_ENTRIES);
        return context.getContentResolver().query(uri,projection,selection,null,sortOrder);

    }

    public static Cursor getEntries(Context context, String table, String[] projection, String selection, Integer limit, String orderBy) {
        String limitClause = "";

        if (limit != null) {
            if (orderBy.equals("")) {
                limitClause = " ASC LIMIT " + limit;
            } else {
                limitClause = " LIMIT " + limit;
            }
        }

       // String[] projection = new String[]{"BIB","Time","TimeStamp","Synced"};
       // String selection = whereClause + myEntriesClause + validClause;
        String sortOrder = orderBy + limitClause;
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,table);
        return context.getContentResolver().query(uri,projection,selection,null,sortOrder);

    }

    public static Cursor getEntriesForSync(Context context, String whereClause, String orderbyClause) {

        if (whereClause.equals("")) {
            whereClause = " 1 ";
        }

        String[] projection = new String[]{"LocalEntryID","EntryID","CPID","CPNo","UserID","ActiveRacerID","Barcode","Time","EntryTypeID","Comment","BIB","Valid","Operator","ReasonInvalid","InsertTimeStamp","UpdateTimeStamp","DeleteTimeStamp"};
        String selection = whereClause;
        String sortOrder = orderbyClause + " Time ASC LIMIT 10 ";
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_2_CP_ENTRIES);
        return context.getContentResolver().query(uri,projection,selection,null,sortOrder);
    }

    public static void setEntriesNotMine(Context context) {
        //SQLiteDatabase db = this.getWritableDatabase();
        //String query = "UPDATE " + TABLE_2_NAME + " SET myEntry=0 WHERE myEntry=1;";
        //db.execSQL(query);
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_2_CP_ENTRIES);
        ContentValues contentValues = new ContentValues();
        contentValues.put("myEntry","0");
        String selection = "myEntry=1";
        int result = context.getContentResolver().update(uri, contentValues,selection,null);
        //result = resultUri != null ? resultUri.getLastPathSegment() : "1";

        // return true;
    }

    public static int deleteEntry(Context context, String whereClause) {
        //SQLiteDatabase db = this.getWritableDatabase();
        //return db.delete(TABLE_2_NAME, whereClause,null);
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_2_CP_ENTRIES);
        return context.getContentResolver().delete(uri,whereClause,null);
    }

    public static void setEntryDeleted(Context context, String whereClause, Boolean notifyChange, int deleteBtnMode) {
        //SQLiteDatabase db = this.getWritableDatabase();
        //String query = "UPDATE " + TABLE_2_NAME + " SET Valid=0, ReasonInvalid='Code 03: Manually deleted' WHERE " + whereClause + ";";
        //db.execSQL(query);

        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_2_CP_ENTRIES);
        ContentValues contentValues = new ContentValues();
        contentValues.put("Synced","0");
        contentValues.put("DeleteTimestamp", System.currentTimeMillis());
        if (deleteBtnMode == EntriesInputListAdapter.RESTORE) {
            contentValues.put("Valid","1");
            contentValues.put("ReasonInvalid","Code 04: Restored from deleted.");
        } else {
            contentValues.put("Valid","0");
            contentValues.put("ReasonInvalid","Code 03: Manually deleted");
        }

        int result = context.getContentResolver().update(uri, contentValues,whereClause,null);
        if (notifyChange) {
            context.getContentResolver().notifyChange(uri, null, false);
        }
    }

    public static int updateEditedEntry(Context context, String BIB, String newDate, String whereClause, Boolean notifyChange) {
        //SQLiteDatabase db = this.getWritableDatabase();
        //String query = "UPDATE " + TABLE_2_NAME + " SET BIB='" + BIB + "', Time='" + newDate  + "' WHERE " + whereClause + ";";
        //db.execSQL(query);

        //check active racer
        String activeRacerID = "0";
        Cursor activeRacerCursor = getActiveRacerIDFromRacers(context,BIB);
        if (activeRacerCursor.getCount() == 1) {
            activeRacerCursor.moveToFirst();
            // ActiveRacerID, FirstName, LastName, Country, Gender, Age
            activeRacerID = activeRacerCursor.getString(0);
        }
        activeRacerCursor.close();

        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_2_CP_ENTRIES);
        ContentValues cv = new ContentValues();
        cv.put("BIB",BIB);
        cv.put("Time",newDate);
        cv.put("Synced","0");
        cv.put("ActiveRacerID",activeRacerID);
        cv.put("UpdateTimestamp", System.currentTimeMillis());

        if (notifyChange) {
            context.getContentResolver().notifyChange(uri, null, false);
        }
        return context.getContentResolver().update(uri, cv, whereClause, null);

    }

    public static int updateOldEntriesWhenLogin(Context context, ContentValues contentValues, String whereClause, Boolean notifyChange) {
        //SQLiteDatabase db = this.getWritableDatabase();
        //String query = "UPDATE " + TABLE_2_NAME + " SET BIB='" + BIB + "', Time='" + newDate  + "' WHERE " + whereClause + ";";
        //db.execSQL(query);

        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_2_CP_ENTRIES);
        /*
        ContentValues cv = new ContentValues();
        cv.put("BIB",BIB);
        cv.put("Time",newDate);
        cv.put("Synced","0");
        */
        if (notifyChange) {
            context.getContentResolver().notifyChange(uri, null, false);
        }
        return context.getContentResolver().update(uri, contentValues, whereClause, null);

    }

    public static boolean updateCPEntriesSynced(Context context, JSONObject jsonString) {

        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_2_CP_ENTRIES);

        JSONtoSQLiteString jsontosqlite = new JSONtoSQLiteString(jsonString);
        int result=0;
        int i=0;
        while (i < jsonString.length()-2) {
            if (result != -1) {

                String whereClause = "myEntry = 1 AND LocalEntryID = '" + jsontosqlite.LocalEntryID(i) + "'";
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_2_ENTRY_ID,jsontosqlite.EntryID(i));
                contentValues.put(COL_2_SYNCED,"1");
                result = context.getContentResolver().update(uri, contentValues, whereClause, null);
                //resultUri = context.getContentResolver().insert(uri, contentValues);
                //resultUri = mProvider.insert(uri, contentValues);
                //result = resultUri != null ? resultUri.getLastPathSegment() : "1";

                //result = db.insert(TABLE_1_NAME,null,contentValues);
            }
            i++;
        }
        if (result == -1) return false;
        else return true;
    }

    public static int updateCPEntriesSynced(Context context, String whereClause) {

        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI, TABLE_2_CP_ENTRIES);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2_SYNCED,"1");
        return context.getContentResolver().update(uri, contentValues, whereClause, null);
    }
}
