package com.trex.racetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static com.trex.racetracker.DatabaseHelper.*;
import static com.trex.racetracker.StaticMethods.*;
import static java.sql.Types.NULL;

/**
 * Created by Igor on 21.4.2017.
 */

public class DbMethods {

    private static Provider mProvider = new Provider();

    public DbMethods() {
        mProvider = new Provider();
    }

    public static boolean insertIntoLoginInfo(JSONObject jsonString, Context context) throws JSONException {

        JSONtoSQLiteString jsontosqlite = new JSONtoSQLiteString(jsonString);
        Uri resultUri;
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_3_NAME);

        String result="0";
        int i=0;
        while (i < jsonString.length()-2) {
            if (!result.equals("-1")) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_3_1,jsontosqlite.CPID(i));
                contentValues.put(COL_3_2,jsontosqlite.CPComment(i));
                contentValues.put(COL_3_3,jsontosqlite.CPName(i));
                contentValues.put(COL_3_4,jsontosqlite.CPNo(i));
                contentValues.put(COL_3_5,jsontosqlite.RaceID(i));
                contentValues.put(COL_3_6,jsontosqlite.RaceName(i));
                contentValues.put(COL_3_7,jsontosqlite.RaceDescription(i));
                contentValues.put(COL_3_8,jsontosqlite.UsersComment(i));

                resultUri = context.getContentResolver().insert(uri, contentValues);
                //resultUri = mProvider.insert(uri, contentValues);
                result = resultUri != null ? resultUri.getLastPathSegment() : "1";
            }
            i++;
        }
        context.getContentResolver().notifyChange(uri, null, false);
        if (!result.equals("-1")) return false;
        else return true;
    }

    public static int deleteAllFromLoginInfo(Context context) {
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_3_NAME);
        return context.getContentResolver().delete(uri,"",null);
    }

    public static Cursor getDistinctRacesFromLoginInfo(Context context) {
       // String query = "SELECT DISTINCT RaceID, RaceDescription FROM " + TABLE_3_NAME + ";";
        //SQLiteDatabase db = this.getWritableDatabase();
       // return db.rawQuery(query,null);

        String[] projection = new String[]{"DISTINCT RaceID", "RaceDescription", "RaceName"};
        String selection = null;
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_3_NAME);
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
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_3_NAME);
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
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_2_NAME);
        return context.getContentResolver().query(uri,projection,selection,null,sortOrder);
    }

    public static boolean insertIntoActiveRacers(Context context, JSONObject jsonString) {
        //SQLiteDatabase db = this.getWritableDatabase();
        Uri resultUri;
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_1_NAME);

        JSONtoSQLiteString jsontosqlite = new JSONtoSQLiteString(jsonString);
        String result="0";
        int i=0;
        while (i < jsonString.length()-3) {
            if (result != "-1") {
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

                resultUri = context.getContentResolver().insert(uri, contentValues);
                //resultUri = mProvider.insert(uri, contentValues);
                result = resultUri != null ? resultUri.getLastPathSegment() : "1";

                //result = db.insert(TABLE_1_NAME,null,contentValues);
            }
            i++;
        }
        if (result.equals("-1")) return false;
        else return true;
    }

    /**  get 6 columns from ActiveRacers to form the array used for the adapter on the listview
     (the last 2 columns will be gotten from CPEntries)
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
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_1_NAME);
        return context.getContentResolver().query(uri,projection,selection,null,sortOrder);
    }

    public static int deleteAllFromActiveRacers(Context context) {
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_1_NAME);
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
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_2_NAME);
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
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_1_NAME);
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
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_3_NAME);
        return context.getContentResolver().query(uri,projection,selection,null,sortOrder);

    }

    public static boolean insertIntoCPEntries(Context context, EntryObj entryObj, Boolean notifyChange) {
        String result;
        Uri resultUri;
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_2_NAME);
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2_1,entryObj.getEntryID());
        contentValues.put(COL_2_2,entryObj.getCPID());
        contentValues.put(COL_2_3,entryObj.getCPName());
        contentValues.put(COL_2_4,entryObj.getUserID());
        contentValues.put(COL_2_5,entryObj.getActiveRacerID());
        contentValues.put(COL_2_6,entryObj.getBarcode());
        contentValues.put(COL_2_7,entryObj.getTime());
        contentValues.put(COL_2_8,entryObj.getEntryTypeID());
        contentValues.put(COL_2_9,entryObj.getComment());
        contentValues.put(COL_2_10,entryObj.isSynced());
        contentValues.put(COL_2_11,entryObj.isMyEntry());
        contentValues.put(COL_2_12,entryObj.getBIB());
        contentValues.put(COL_2_13,entryObj.isValid());
        contentValues.put(COL_2_14,entryObj.getOperator());
        contentValues.put(COL_2_15,entryObj.getCPNo());
        contentValues.put(COL_2_16,entryObj.getReasonInvalid());
        contentValues.put(COL_2_17,entryObj.getTimeStamp());

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
        /*
        String query = "SELECT Time FROM " + TABLE_2_NAME + " WHERE Valid=1 AND BIB='" + inputedBIB + "' ORDER BY Time DESC LIMIT 1;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        Date entryTime = null;
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            entryTime = formatDateTime(cursor.getString(0));
        }
        cursor.close();
        return entryTime;
*/
        String[] projection = new String[]{"Time"};
        String selection = "BIB = '" +  inputedBIB + "' AND Valid = 1";
        String sortOrder = "Time DESC LIMIT 1";
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_2_NAME);
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
        String selection = "BIB = '" +  inputedBIB + "' AND CPNo='" + cpNo + "' AND Valid=1";
        String sortOrder = "";
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_2_NAME);
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
        String[] projection = new String[]{"CPEntries.BIB","Time","TimeStamp","Synced"};
        String selection = whereClause + myEntriesClause + validClause;
        String sortOrder = orderbyClause + limitClause;
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_2_NAME);
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

        String[] projection = new String[]{"LocalEntryID","EntryID","CPID","CPNo","UserID","ActiveRacerID","Barcode","Time","EntryTypeID","Comment","BIB","Valid","Operator","ReasonInvalid","TimeStamp"};
        String selection = whereClause;
        String sortOrder = orderbyClause;
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_2_NAME);
        return context.getContentResolver().query(uri,projection,selection,null,sortOrder);

    }

    public static void setEntriesNotMine(Context context) {
        //SQLiteDatabase db = this.getWritableDatabase();
        //String query = "UPDATE " + TABLE_2_NAME + " SET myEntry=0 WHERE myEntry=1;";
        //db.execSQL(query);
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_2_NAME);
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
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_2_NAME);
        return context.getContentResolver().delete(uri,whereClause,null);
    }

    public static void setEntryDeleted(Context context, String whereClause, Boolean notifyChange, int deleteBtnMode) {
        //SQLiteDatabase db = this.getWritableDatabase();
        //String query = "UPDATE " + TABLE_2_NAME + " SET Valid=0, ReasonInvalid='Code 03: Manually deleted' WHERE " + whereClause + ";";
        //db.execSQL(query);

        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_2_NAME);
        ContentValues contentValues = new ContentValues();
        contentValues.put("Synced","0");
        if (deleteBtnMode == EntriesInputListAdapter.RESTORE) {
            contentValues.put("Valid","1");
        } else {
            contentValues.put("Valid","0");
        }

        contentValues.put("ReasonInvalid","Code 03: Manually deleted");
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

        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_2_NAME);
        ContentValues cv = new ContentValues();
        cv.put("BIB",BIB);
        cv.put("Time",newDate);
        cv.put("Synced","0");
        cv.put("ActiveRacerID",activeRacerID);

        if (notifyChange) {
            context.getContentResolver().notifyChange(uri, null, false);
        }
        return context.getContentResolver().update(uri, cv, whereClause, null);

    }

    public static int updateOldEntriesWhenLogin(Context context, ContentValues contentValues, String whereClause, Boolean notifyChange) {
        //SQLiteDatabase db = this.getWritableDatabase();
        //String query = "UPDATE " + TABLE_2_NAME + " SET BIB='" + BIB + "', Time='" + newDate  + "' WHERE " + whereClause + ";";
        //db.execSQL(query);

        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_2_NAME);
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

        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_2_NAME);

        JSONtoSQLiteString jsontosqlite = new JSONtoSQLiteString(jsonString);
        int result=0;
        int i=0;
        while (i < jsonString.length()-2) {
            if (result != -1) {

                String whereClause = "myEntry = 1 AND LocalEntryID = " + jsontosqlite.LocalEntryID(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_2_1,jsontosqlite.EntryID(i));
                contentValues.put(COL_2_10,"1");
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

    public static boolean insertPulledEntries(Context context, JSONObject jsonString) {
        //SQLiteDatabase db = this.getWritableDatabase();
        Uri resultUri;
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_2_NAME);

        JSONtoSQLiteString jsontosqlite = new JSONtoSQLiteString(jsonString);
        String result="0";
        int i=0;
        while (i < jsonString.length()-3) {
            if (!result.equals("-1")) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_2_1,jsontosqlite.EntryID(i));
                contentValues.put(COL_2_2,jsontosqlite.CPID(i));
                contentValues.put(COL_2_3,jsontosqlite.CPName(i));
                contentValues.put(COL_2_4,jsontosqlite.UserID(i));
                contentValues.put(COL_2_5,jsontosqlite.ActiveRacerID(i));
                contentValues.put(COL_2_6,jsontosqlite.Barcode(i));
                contentValues.put(COL_2_7,jsontosqlite.Time(i));
                contentValues.put(COL_2_8,jsontosqlite.EntryTypeID(i));
                contentValues.put(COL_2_9,jsontosqlite.CPComment(i));
                contentValues.put(COL_2_10,"1");
                contentValues.put(COL_2_11,"0");
                contentValues.put(COL_2_12,jsontosqlite.BIB(i));
                contentValues.put(COL_2_13,"1");
                contentValues.put(COL_2_14,jsontosqlite.Operator(i));
                contentValues.put(COL_2_15,jsontosqlite.CPNo(i));
                contentValues.put(COL_2_16,NULL);
                contentValues.put(COL_2_17,jsontosqlite.Timestamp(i));
                //contentValues.put(COL_2_18,jsontosqlite.LocalEntryID(i));


                resultUri = context.getContentResolver().insert(uri, contentValues);
                //resultUri = mProvider.insert(uri, contentValues);
                result = resultUri != null ? resultUri.getLastPathSegment() : "1";

                //result = db.insert(TABLE_1_NAME,null,contentValues);
            }
            i++;
        }
        if (result.equals("-1")) return false;
        else return true;
    }
}
