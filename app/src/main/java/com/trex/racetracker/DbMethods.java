package com.trex.racetracker;

import android.content.ContentValues;
import android.content.Context;
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

}
