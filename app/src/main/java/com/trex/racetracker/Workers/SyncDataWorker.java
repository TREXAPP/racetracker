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
                if (jsonResult.has("checkpoints")) {
                    if (!insertIntoLoginInfo(jsonResult.getJSONArray("checkpoints"), context)) {
                        tvStatusTop.setText("Warning: Error while writing in SQLite, LoginInfo table. Contact the administrator;");
                    }
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


//        //do this:
//        //1. parse the json string result and insert its contents into sqlite, table ActiveRacers
//        //  first maybe check if those users are already in that sqlite, so either:
//        //  - delete them all, then insert them all; or
//        //  - check if it is already inserted AND its timestamp is the same (that means it is not updated) then skip that row
//        //      else delete it if it exists with older timestamp and insert it again
//        //2. Fill in a scrollable list view in tab Racers with record from the already inserted data into SQLite
//        //  (if there are records inserted already, clear them all before inserting
//        TextView tvStatusTop = (TextView) fragmentLogin.findViewById(R.id.tvStatusTop);
//        String error = "";
//        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);

//        try {
//            JSONObject jsonResult = new JSONObject(result);
//            if (jsonResult.has("issync")) {
//                if (jsonResult.getString("issync").equals("1")) {
//                    //TODO - a logic not to write only in sqlite, but also to check for update if the record already exists
//                    deleteAllFromActiveRacers(context);
//                    if (!insertIntoActiveRacers(context,jsonResult)) {
//                        error += "Error while writing in SQLite, ActiveRaces table. Contact the administrator;";
//                    }
//
//                } else {
//                    error += "Error while reading from web server sync.php. Contact the administrator;";
//                }
//            } else {
//                error += "Error with database! Contact the administrator.";
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        if (error.equals("")) {
//            //TODO - testing
//            //moved to loginworker!
//           // StaticMethods methods = new StaticMethods();
//           // SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
//           // methods.InitializeRacersFragment(context, viewRacers, globals);
//
//            tvStatusTop.setText("Logged in");
//        } else {
//            tvStatusTop.setText(error);
//        }
    }

}
