package com.trex.racetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;
import com.trex.racetracker.Provider;
import static com.trex.racetracker.DatabaseHelper.*;
import static java.security.AccessController.getContext;

/**
 * Created by Igor on 21.4.2017.
 */

public class DbMethods {

    private static Provider mProvider = new Provider();

    public DbMethods() {
        mProvider = new Provider();
    }

    public static boolean insertIntoLoginInfoDbM(JSONObject jsonString, Context context) throws JSONException {

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
        if (!result.equals("-1")) return false;
        else return true;
    }

    public static int deleteAllFromLoginInfoDbM(Context context) {
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_3_NAME);
        return context.getContentResolver().delete(uri,"",null);
    }

    public static Cursor getDistinctRacesFromLoginInfoDbM(Context context) {
       // String query = "SELECT DISTINCT RaceID, RaceDescription FROM " + TABLE_3_NAME + ";";
        //SQLiteDatabase db = this.getWritableDatabase();
       // return db.rawQuery(query,null);

        String[] projection = new String[]{"DISTINCT RaceID", "RaceDescription"};
        String selection = null;
        Uri uri = Uri.withAppendedPath(mProvider.CONTENT_URI,TABLE_3_NAME);
        return context.getContentResolver().query(uri,projection,selection,null,"");
    }

    public static Cursor getDistinctCPNoFromLoginInfoDbM(Context context, String selection) {
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

}
