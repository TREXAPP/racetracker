package com.trex.racetracker.Workers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.trex.racetracker.Activities.MainActivity;
import com.trex.racetracker.Helpers.RequestHelper;
import com.trex.racetracker.Models.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import static com.trex.racetracker.Database.DbMethods.*;

/**
 * Created by Igor on 27.4.2017.
 */

public class SyncEntriesWorker extends AsyncTask<String,Void,String> {

    private Context context;
    private String type;
    private String payload;
    private SharedPreferences globals;
    private String urlString;
    private Response response;
    private String updateSyncedWhereClause;
    private long currentTimeMillis;
    private LocalBroadcastManager mBroadcaster;


    public SyncEntriesWorker(Context context, String updateSyncedWhereClause, long currentTimeMillis, LocalBroadcastManager mBroadcaster) {
        this.context = context;
        this.globals = context.getSharedPreferences(MainActivity.GLOBALS, 0);
        this.updateSyncedWhereClause = updateSyncedWhereClause;
        this.currentTimeMillis = currentTimeMillis;
        this.mBroadcaster = mBroadcaster;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        payload = params[0];
        String result = "";

        try {
            urlString = globals.getString("hostUrl", "") + "/timing/mobile/instruction";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("payload", payload);
            response = RequestHelper.sendPost(urlString, jsonObject, globals, true);
            return response.getMessage();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        SharedPreferences.Editor editor = globals.edit();
        if (response != null) {
            int responseCode = response.getResponseCode();
            if (responseCode >= 200 && responseCode <= 299) {
                int updated = updateCPEntriesSynced(context, updateSyncedWhereClause);
                if (updated <= 0) {
                    Log.d("SyncEntriesWorker", "Sync completed, but no entries were updated as synced");
                }
            } else {
                Log.e("SyncEntriesWorker", "Sync completed, but error returned from server");
            }
        }

        editor.putLong("lastPushInMillis",currentTimeMillis);
        editor.putBoolean("syncInProgress", false);
        editor.commit();

        Intent intent = new Intent("com.trex.racetracker.REFRESH_LIST_INPUT");
        mBroadcaster.sendBroadcast(intent);
        intent.setAction("com.trex.racetracker.UPDATE_LAST_SYNC");
        mBroadcaster.sendBroadcast(intent);
        intent.setAction("com.trex.racetracker.REFRESH_LIST_ENTRIES");
        mBroadcaster.sendBroadcast(intent);

        super.onPostExecute(result);

        //debug
//
//        if (result != null) {
//
//
//            String error = "";
//            if (type.equals("sync_push_insert") || type.equals("sync_push_update")) {
//
//                try {
//                    JSONObject jsonResult = new JSONObject(result);
//                    if (jsonResult.has("success")) {
//                        if (jsonResult.getString("success").equals("1")) {
//
//                            if (!updateCPEntriesSynced(context, jsonResult)) {
//                                error += "Error while writing in SQLite, CPEntries table. Contact the administrator;";
//                            }
//                            ;
//
//                        } else {
//                            if (jsonResult.has("error")) {
//                                error += jsonResult.getString("error");
//                            } else {
//                                error += "Error with webserver! Contact the administrator.";
//                            }
//                        }
//                    } else {
//                        error += "Error with database! Contact the administrator.";
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }

//
//            Boolean changeDetected = false;
//
//            if (!error.equals("")) {
//                Log.e("Sync error", error);
//                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
//            } else {
//                SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
//                SharedPreferences.Editor editor = globals.edit();
//                Long timeNow = System.currentTimeMillis();
//
//                editor.putLong("lastPushInMillis",timeNow);
//
//                editor.commit();
//            }
        //}
    }
}
