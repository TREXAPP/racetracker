package com.trex.racetracker;

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
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import layout.Input;

import static com.trex.racetracker.DbMethods.*;
import static com.trex.racetracker.StaticMethods.PopulateInputEntriesListView;
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
    protected static final String TYPE_SYNC_PULL = "sync_pull";
    protected static final String URL_SYNC_PUSH = "http://app.trex.mk/syncEntries_push.php";
    protected static final String URL_SYNC_PULL = "http://app.trex.mk/syncEntries_pull.php";
    private Context serviceContext;
    private LocalBroadcastManager mBroadcaster;

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
    Log.d("sync","Performing sync...");

        synchronized (this) {
            final SharedPreferences globals = getContext().getSharedPreferences(MainActivity.GLOBALS,0);
            if (globals.getBoolean("periodic_sync",true)) {
                if (globals.getBoolean("islogin", false)) {
                    long lastPullInMillis = globals.getLong("lastPullInMillis", 0);
                    long lastPushInMillis = globals.getLong("lastPushInMillis", 0);
                    SyncEntriesWorker syncEntriesWorker_insert = new SyncEntriesWorker(getContext());
                    SyncEntriesWorker syncEntriesWorker_update = new SyncEntriesWorker(getContext());
                    SyncEntriesWorker syncEntriesWorker_pull = new SyncEntriesWorker(getContext());
                    Boolean syncSuccess = true;


                    //1.INSERT
                    Cursor insertCursor = getEntriesForSync(getContext(), "myEntry = 1 AND EntryID IS NULL AND Synced = 0", "");
                    int insertRowsNo = insertCursor.getCount();
                    if (insertRowsNo > 0) {
                        String insertJSON = null;

                        try {
                            insertJSON = CreateInsertJSONstring(insertCursor);
                        } catch (JSONException e) {
                            Log.d("error", "Error creating JSON string for inserting, PUSH");
                            e.printStackTrace();
                            syncSuccess = false;
                        }
                        syncEntriesWorker_insert.execute(TYPE_SYNC_PUSH_INSERT, URL_SYNC_PUSH, String.valueOf(insertRowsNo), insertJSON);
                    }
                    //2. UPDATE
                    Cursor updateCursor = getEntriesForSync(getContext(), "myEntry = 1 AND EntryID IS NOT NULL AND Synced = 0", "");
                    int updateRowsNo = updateCursor.getCount();
                    if (updateRowsNo > 0) {
                        String updateJSON = null;
                        try {
                            updateJSON = CreateUpdateJSONstring(updateCursor);
                        } catch (JSONException e) {
                            Log.d("error", "Error creating JSON string for updating, PUSH");
                            e.printStackTrace();
                            syncSuccess = false;
                        }

                        syncEntriesWorker_update.execute(TYPE_SYNC_PUSH_UPDATE, URL_SYNC_PUSH, String.valueOf(updateRowsNo), updateJSON);
                        //syncEntriesWorker.execute(TYPE_SYNC_PULL, URL_SYNC_PULL, String.valueOf(rowsNo), updateJSON);
                    }

                    if (syncSuccess) {
                    /*
                    SharedPreferences.Editor editor = globals.edit();
                    editor.putLong("lastPushInMillis", System.currentTimeMillis());
                    //editor.putLong("lastPullInMillis",newLastPull_temp);
                    editor.apply();
                    */

                    }

                    //3.PULL
                    String username = globals.getString("username", "");
                    syncEntriesWorker_pull.execute(TYPE_SYNC_PULL, URL_SYNC_PULL, username, String.valueOf(lastPullInMillis));



            /*
            syncEntriesWorker.execute(TYPE_LOGIN,URL_LOGIN,etUsername.getText().toString(),etPassword.getText().toString(),etOperator.getText().toString(),DeviceID,COMMENT_LOGIN);
            SendEntriesToServer(lastPullInMiliSecs);
            GetNewEntriesFromServer(lastPushInMiliSecs);


            */
                }
            }


            //wait 2 seconds before sending the intent
            try {
                wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent("com.trex.racetracker.REFRESH_LIST_INPUT");
            mBroadcaster.sendBroadcast(intent);

            //wait 2 seconds before sending the intent
            try {
                wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            intent.setAction("com.trex.racetracker.UPDATE_LAST_SYNC");
            mBroadcaster.sendBroadcast(intent);

            //wait 5 seconds before sending the intent
            try {
                wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            intent.setAction("com.trex.racetracker.REFRESH_LIST_ENTRIES");
            mBroadcaster.sendBroadcast(intent);

        }


/*
            Connecting to a server
                Although you can assume that the network is available when your data transfer starts,
                the sync adapter framework doesn't automatically connect to a server.
            Downloading and uploading data
                A sync adapter doesn't automate any data transfer tasks.
                If you want to download data from a server and store it in a content provider,
                you have to provide the code that requests the data, downloads it, and inserts it in the provider.
                Similarly, if you want to send data to a server, you have to read it from a file, database, or provider, and send the necessary upload request.
                You also have to handle network errors that occur while your data transfer is running.
            Handling data conflicts or determining how current the data is
                A sync adapter doesn't automatically handle conflicts between data on the server and data on the device.
                Also, it doesn't automatically detect if the data on the server is newer than the data on the device, or vice versa.
                Instead, you have to provide your own algorithms for handling this situation.
            Clean up.
                Always close connections to a server and clean up temp files and caches at the end of your data transfer.
     */



    }

    private String CreateInsertJSONstring(Cursor insertCursor) throws JSONException {
        String result = "";
        JSONObject jsonObj = new JSONObject();
        //jsonObj.put("type","sync_push_insert");
        //jsonObj.put("rows_no",insertCursor.getCount());
        int count=0;
//"LocalEntryID","EntryID","CPID","UserID","ActiveRacerID","Barcode","Time","EntryTypeID","Comment","BIB","Valid","Operator","ReasonInvalid","TimeStamp"
        if (insertCursor.getCount() > 0) {
            insertCursor.moveToFirst();
            while (!insertCursor.isAfterLast()) {
                JSONObject rowJSON = new JSONObject();
                //JSONObject rowJSON = new JSONObject();
                rowJSON.put("LocalEntryID",insertCursor.getString(0));
                //rowJSON.put("EntryID",insertCursor.getString(1));
                rowJSON.put("CPID",insertCursor.getString(2));
                rowJSON.put("CPNo",insertCursor.getString(3));
                rowJSON.put("UserID",insertCursor.getString(4));
                rowJSON.put("ActiveRacerID",insertCursor.getString(5));
                rowJSON.put("Barcode",insertCursor.getString(6));
                rowJSON.put("Time",insertCursor.getString(7));
                rowJSON.put("EntryTypeID",insertCursor.getString(8));
                rowJSON.put("Comment",insertCursor.getString(9));
                rowJSON.put("BIB",insertCursor.getString(10));
                rowJSON.put("Valid",insertCursor.getString(11));
                rowJSON.put("Operator",insertCursor.getString(12));
                rowJSON.put("ReasonInvalid",insertCursor.getString(13));
                rowJSON.put("TimeStamp",insertCursor.getString(14));

                jsonObj.put(String.valueOf(count),rowJSON);
                insertCursor.moveToNext();
                count++;
            }
        }



        insertCursor.close();
        result = jsonObj.toString();
        //result = result.replace("\"", "");
        return result;
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
                rowJSON.put("TimeStamp",updateCursor.getString(14));

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
