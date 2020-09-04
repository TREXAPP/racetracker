package com.trex.racetracker.Workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.trex.racetracker.Activities.MainActivity;
import com.trex.racetracker.Database.DatabaseHelper;
import com.trex.racetracker.Helpers.RequestHelper;
import com.trex.racetracker.Models.Response;
import com.trex.racetracker.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static com.trex.racetracker.Database.DbMethods.deleteAllFromActiveRacers;
import static com.trex.racetracker.Database.DbMethods.deleteAllFromLoginInfo;
import static com.trex.racetracker.Database.DbMethods.getDistinctRacesFromLoginInfo;
import static com.trex.racetracker.Database.DbMethods.insertIntoActiveRacers;
import static com.trex.racetracker.Database.DbMethods.insertIntoLoginInfo;
import static com.trex.racetracker.StaticMethods.InitializeLoginFragment;

/**
 * Created by Igor on 04.11.2016.
 */

public class SyncDataWorker extends AsyncTask<String,Void,String> {
    private Context context;
    private View fragmentLogin;
    private View viewRacers;
    private String urlString;
    private String Username;
    private String Operator;
    private String DeviceID;
    private String SyncComment;
    private String mysqlQuery;
    private SharedPreferences globals;
    private Response response;

    public SyncDataWorker(Context ctx, View view, View viewRacers) {
        this.context = ctx;
        this.fragmentLogin = view;
        this.viewRacers = viewRacers;
        this.globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
    }

    @Override
    protected void onPreExecute() {
//
//        TextView tvStatusTop = (TextView) fragmentLogin.findViewById(R.id.tvStatusTop);
//        tvStatusTop.setText("Login successful. Synchronizing Racers ...");
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";

        try {

            urlString = globals.getString("hostUrl", "") + "/timing/mobile/sync-data";
            response = RequestHelper.sendGet(urlString, globals, true);
            return response.getMessage();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        TextView tvStatusTop = (TextView) fragmentLogin.findViewById(R.id.tvStatusTop);

        try {
            JSONObject jsonResult = new JSONObject(result);
            int responseCode = response.getResponseCode();
            if (responseCode >= 200 && responseCode <= 299) {
                // TODO v2 - insert into login info; then insert into active racers
                deleteAllFromLoginInfo(context);
                if (jsonResult.has("checkpoint") && jsonResult.has("races")) {
                    JSONObject checkpointJson = jsonResult.getJSONObject("checkpoint");
                    if (!insertIntoLoginInfo(checkpointJson, jsonResult.getJSONArray("races"), context)) {
                        tvStatusTop.setText("Warning: Error while writing in SQLite, LoginInfo table. Contact the administrator;");
                    }
                    SharedPreferences.Editor editor = globals.edit();
                    editor.putString("controlpoint", checkpointJson.getString("CPName"));
                    editor.commit();
                    //TODO v2 - instead of delete/insert - insert if not exist, update if exists
                    deleteAllFromActiveRacers(context);
                    if (!insertIntoActiveRacers(jsonResult.getJSONArray("runners"), context)) {
                        tvStatusTop.setText("Warning: Error while writing in SQLite, ActiveRacers table. Contact the administrator;");
                    }
                }

                InitializeLoginFragment(context, fragmentLogin, globals);
                tvStatusTop.setText("Synchronization complete!");
            }
        } catch (JSONException e) {
            Toast.makeText(context, "Error synchronizing data. Logging out", Toast.LENGTH_SHORT).show();
            //TODO v2 call logout
            tvStatusTop.setText("Error synchronizing data");
            tvStatusTop.setTextColor(Color.RED);
            e.printStackTrace();
        }

    }

}
