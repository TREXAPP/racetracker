package com.trex.racetracker.Adapters;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.trex.racetracker.Activities.MainActivity;
import com.trex.racetracker.Services.SyncService;
import com.trex.racetracker.Workers.SyncEntriesWorker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

import static com.trex.racetracker.Database.DbMethods.*;
import static com.trex.racetracker.StaticMethods.disableEnableControls;

/**
 * Created by Igor on 18.4.2017.
 */

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */



public class SyncAdapter extends AbstractThreadedSyncAdapter {

    protected static final String TYPE_SYNC_PUSH_INSERT = "sync_push_insert";
    protected static final String TYPE_SYNC_PUSH_UPDATE = "sync_push_update";
    private Context serviceContext;
    private LocalBroadcastManager mBroadcaster;
    public static final int ACTION_INSERT = 1;
    public static final int ACTION_UPDATE = 2;
    public static final int ACTION_DELETE = 3;
    public static final int ACTION_RESTORE = 4;
    public static final int ACTION_INVALID = 5;
    public static final int ACTION_UNKNOWN = 9;

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        serviceContext = context;
        mContentResolver = context.getContentResolver();
        mBroadcaster = LocalBroadcastManager.getInstance(SyncService.getInstance());
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        serviceContext = context;
        mContentResolver = context.getContentResolver();
    }

    /*
     * Specify the code you want to run in the sync adapter. The entire
     * sync adapter runs in a background thread, so you don't have to set
     * up your own background processing.
     */
    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        /*
         * Put the data transfer code here.
         */
        //  Toast.makeText(getContext(), "Performing sync...", Toast.LENGTH_SHORT).show();
        Log.d("sync", "Performing sync...");

        synchronized (this) {
            final SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS, 0);
            SharedPreferences.Editor editor = globals.edit();
            try {

                boolean periodicSyncEnabled = globals.getBoolean("periodic_sync", true);
                boolean loggedIn = globals.getBoolean("loggedIn", false);
                boolean syncInProgress = globals.getBoolean("syncInProgress", false);

                if (periodicSyncEnabled && loggedIn && !syncInProgress) {

                    editor.putBoolean("syncInProgress", true);
                    editor.apply();

                    long lastPushInMillis = globals.getLong("lastPushInMillis", 0);
                    long currentTimeMillis = System.currentTimeMillis();

                    Cursor cursor = getEntriesForSync(getContext(), "myEntry = 1 AND Synced = 0", "");
                    int rowsNo = cursor.getCount();

                    if (rowsNo > 0) {
                        String updateSyncedWhereClause = generateUpdateSyncedWhereClause(cursor);
                        SyncEntriesWorker syncEntriesWorker = new SyncEntriesWorker(getContext(), updateSyncedWhereClause, currentTimeMillis, mBroadcaster);
                        String syncJsonString = null;

                        try {
                            syncJsonString = CreatePushJSONstring(cursor, globals, lastPushInMillis);
                        } catch (JSONException e) {
                            Log.d("error", "Error creating JSON string, PUSH");
                            e.printStackTrace();
                        }

                        syncEntriesWorker.execute(syncJsonString);
                    } else {
                        editor.putLong("lastPushInMillis", currentTimeMillis);
                        editor.putBoolean("syncInProgress", false);
                        editor.commit();

                        Intent intent = new Intent("com.trex.racetracker.REFRESH_UI");
                        mBroadcaster.sendBroadcast(intent);
                    }
                }

            } catch (Exception ex) {
                editor.putBoolean("syncInProgress", false);
                editor.commit();
            }
        }
    }

    private String generateUpdateSyncedWhereClause(Cursor cursor) {
        String whereClause = "";
        if (cursor.getCount() > 0) {
            whereClause = "myEntry = 1 AND (";
            boolean first = true;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (first) {
                    first = false;
                } else {
                    whereClause += " OR ";
                }
                whereClause += "LocalEntryID = '" + cursor.getString(0) + "'";
                cursor.moveToNext();
            }
            whereClause += ")";
        }
        return whereClause;
    }

    private String CreatePushJSONstring(Cursor cursor, SharedPreferences globals, long lastPushInMillis) throws JSONException {
        String result = "";
        JSONArray jsonArray = new JSONArray();
        int count=0;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String insertTimeStamp = cursor.getString(14);
                String updateTimeStamp = cursor.getString(15);
                String deleteTimeStamp = cursor.getString(16);
                int valid = cursor.getInt(11);

                if (insertTimeStamp != null) {
                    JSONObject rowJSON = generateJsonRow(cursor, globals, ACTION_INSERT, insertTimeStamp);
                    jsonArray.put(rowJSON);
                    if (deleteTimeStamp != null && Long.parseLong(deleteTimeStamp) > lastPushInMillis && valid == 0) {
                        long deleteTimestampLong = Long.parseLong(deleteTimeStamp) + 1;
                        JSONObject rowJSON2 = generateJsonRow(cursor, globals, ACTION_DELETE, Long.toString(deleteTimestampLong));
                        jsonArray.put(rowJSON2);
                    }
                } else {
                    // if update and no delete - update
                    // if delete and no update - delete or restore - check valid
                    // if update and delete - update + delete or restore - check valid
                    if (updateTimeStamp != null) {
                        JSONObject rowJSON = generateJsonRow(cursor, globals, ACTION_UPDATE, updateTimeStamp);
                        jsonArray.put(rowJSON);
                        if (deleteTimeStamp != null && Long.parseLong(deleteTimeStamp) > lastPushInMillis) {
                            int action = valid == 0 ? ACTION_DELETE : ACTION_RESTORE;
                            long deleteTimestampLong = Long.parseLong(deleteTimeStamp) + 1;
                            JSONObject rowJSON2 = generateJsonRow(cursor, globals, action, Long.toString(deleteTimestampLong));
                            jsonArray.put(rowJSON2);
                        }
                    } else {
                        if (deleteTimeStamp != null) {
                            int action = valid == 0 ? ACTION_DELETE : ACTION_RESTORE;
                            JSONObject rowJSON = generateJsonRow(cursor, globals, action, deleteTimeStamp);
                            jsonArray.put(rowJSON);
                        }
                    }
                }

                cursor.moveToNext();
                count++;
            }
        }
        cursor.close();
        return result = jsonArray.toString();
    }

    private JSONObject generateJsonRow(Cursor cursor, SharedPreferences globals, int actionType, String timestamp) throws JSONException {
        int valid =  cursor.getInt(11);
        if (valid == 0 && actionType != ACTION_DELETE) {
            actionType = ACTION_INVALID;
        }
        JSONObject rowJSON = new JSONObject();
        rowJSON.put("LocalEntryID",cursor.getString(0));
        //rowJSON.put("EntryID",insertCursor.getString(1));
        rowJSON.put("CPID",cursor.getString(2));
        rowJSON.put("CPNo",cursor.getString(3));
        rowJSON.put("UserID",cursor.getString(4));
        rowJSON.put("ActiveRacerID",cursor.getString(5));
        rowJSON.put("Barcode",cursor.getString(6));
        rowJSON.put("Time",cursor.getString(7));
        rowJSON.put("EntryTypeID",cursor.getString(8));
        rowJSON.put("Comment",cursor.getString(9));
        rowJSON.put("BIB",cursor.getString(10));
        rowJSON.put("Valid",cursor.getString(11));
        rowJSON.put("Operator",cursor.getString(12));
        rowJSON.put("ReasonInvalid",cursor.getString(13));
        rowJSON.put("Timestamp", timestamp);
        rowJSON.put("DeviceID",globals.getString("deviceid","N/A"));
        rowJSON.put("ActionType",actionType);

        return rowJSON;
    }

    private String CreateUpdateJSONstring(Cursor updateCursor) throws JSONException {
        String result = "";
        JSONObject jsonObj = new JSONObject();
        //jsonObj.put("type","sync_push_insert");
        //jsonObj.put("rows_no",insertCursor.getCount());
        int count=0;
//"LocalEntryID","EntryID","CPID","UserID","ActiveRacerID","Barcode","Time","EntryTypeID","Comment","BIB","Valid","Operator","ReasonInvalid","TimeStamp"
        if (updateCursor.getCount() > 0) {
            updateCursor.moveToFirst();
            while (!updateCursor.isAfterLast()) {
                JSONObject rowJSON = new JSONObject();
                //JSONObject rowJSON = new JSONObject();
                rowJSON.put("LocalEntryID",updateCursor.getString(0));
                rowJSON.put("EntryID",updateCursor.getString(1));
                rowJSON.put("CPID",updateCursor.getString(2));
                rowJSON.put("CPNo",updateCursor.getString(3));
                rowJSON.put("UserID",updateCursor.getString(4));
                rowJSON.put("ActiveRacerID",updateCursor.getString(5));
                rowJSON.put("Barcode",updateCursor.getString(6));
                rowJSON.put("Time",updateCursor.getString(7));
                rowJSON.put("EntryTypeID",updateCursor.getString(8));
                rowJSON.put("Comment",updateCursor.getString(9));
                rowJSON.put("BIB",updateCursor.getString(10));
                rowJSON.put("Valid",updateCursor.getString(11));
                rowJSON.put("Operator",updateCursor.getString(12));
                rowJSON.put("ReasonInvalid",updateCursor.getString(13));
                rowJSON.put("InsertTimeStamp",updateCursor.getString(14));
                rowJSON.put("UpdateTimeStamp",updateCursor.getString(15));
                rowJSON.put("DeleteTimeStamp",updateCursor.getString(16));

                jsonObj.put(String.valueOf(count),rowJSON);
                updateCursor.moveToNext();
                count++;
            }
        }



        updateCursor.close();
        result = jsonObj.toString();
        //result = result.replace("\"", "");
        return result;
    }

    private void SendEntriesToServer(long lastSyncInMiliSecs) {

    }

    private void GetNewEntriesFromServer(long lastSyncInMiliSecs) {
    }

}
