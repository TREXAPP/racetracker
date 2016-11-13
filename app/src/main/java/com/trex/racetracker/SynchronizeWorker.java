package com.trex.racetracker;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

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

/**
 * Created by Igor on 04.11.2016.
 */

public class SynchronizeWorker extends AsyncTask<String,Void,String> {
    private Context context;
    private View fragmentLogin;
    private View viewRacers;
    private String queryUrl;
    private String Username;
    private String Operator;
    private String DeviceID;
    private String SyncComment;
    private String mysqlQuery;

    //constructor
    public SynchronizeWorker(Context ctx, View view, View viewRacers) {
        this.context = ctx;
        this.fragmentLogin = view;
        this.viewRacers = viewRacers;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String result = "";

        if (type.equals("sync")) try {

            queryUrl = params[1];
            Username = params[2];
            Operator = params[3];
            DeviceID = params[4];
            SyncComment = params[5];

            //fetch all unique Races from SQLite, for which we will need the Racers from mysql
            DatabaseHelper db = new DatabaseHelper(context);
            Cursor rows = db.getDistinctRacesFromLoginInfo();
            String condition = "";
            rows.moveToFirst();
            while (!rows.isAfterLast()) {
                if (!condition.equals("")) {
                    condition += " OR ";
                }
                condition += "RaceID = '" + rows.getString(0) + "'";
                rows.moveToNext();
            }
            rows.close();

            mysqlQuery = "SELECT ";
            mysqlQuery += "ActiveRacerID, Racers.RacerID, RaceID, Age, BIB, ChipCode, Started, Registered, ActiveRacers.Timestamp AS ActiveRacersTS, ActiveRacers.Comment AS ActiveRacersComment, ";
            mysqlQuery += "FirstName, LastName, Gender, DateOfBirth, YearBirth, Nationality, Country, Teams.TeamID, CityOfResidence, TShirtSize, Email, Tel, Food, Racers.Timestamp AS RacersTS, Racers.Comment AS Racers_Comment,";
            mysqlQuery += "TeamName, TeamDescription ";
            mysqlQuery += "FROM ActiveRacers JOIN Racers ON ActiveRacers.RacerID = Racers.RacerID JOIN Teams ON Racers.TeamID = Teams.TeamID WHERE ";
            mysqlQuery += condition + ";";

            URL url = new URL(queryUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data = "";
            if (!Username.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(Username, "UTF-8");
            }
            if (!Operator.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("operator", "UTF-8") + "=" + URLEncoder.encode(Operator, "UTF-8");
            }
            if (!DeviceID.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("deviceid", "UTF-8") + "=" + URLEncoder.encode(DeviceID, "UTF-8");
            }
            if (!SyncComment.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("comment", "UTF-8") + "=" + URLEncoder.encode(SyncComment, "UTF-8");
            }

            if (!mysqlQuery.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("query", "UTF-8") + "=" + URLEncoder.encode(mysqlQuery, "UTF-8");
            }

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            //now get the response
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String line = "";
            //    result = bufferedReader.toString();
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        //TODO - set for testing, remove when done
        TextView tvStatusBottom = (TextView) fragmentLogin.findViewById(R.id.tvStatusBottom);
        tvStatusBottom.setText("Synchronization started, please wait ...");
    }

    @Override
    protected void onPostExecute(String result) {
        //do this:
        //1. parse the json string result and insert its contents into sqlite, table ActiveRacers
        //  first maybe check if those users are already in that sqlite, so either:
        //  - delete them all, then insert them all; or
        //  - check if it is already inserted AND its timestamp is the same (that means it is not updated) then skip that row
        //      else delete it if it exists with older timestamp and insert it again
        //2. Fill in a scrollable list view in tab Racers with record from the already inserted data into SQLite
        //  (if there are records inserted already, clear them all before inserting
        TextView tvStatusBottom = (TextView) fragmentLogin.findViewById(R.id.tvStatusBottom);
        String error = "";
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        try {
            JSONObject jsonResult = new JSONObject(result);
            if (jsonResult.has("issync")) {
                if (jsonResult.getString("issync").equals("1")) {
                    //TODO - a logic not to write only in sqlite, but also to check for update if the record already exists
                    dbHelper.deleteAllFromActiveRacers();
                    if (!dbHelper.insertIntoActiveRacers(jsonResult)) {
                        error += "Error while writing in SQLite, ActiveRaces table. Contact the administrator;";
                    };

                } else {
                    error += "Error while reading from web server sync.php. Contact the administrator;";
                }
            } else {
                error += "Error with database! Contact the administrator.";

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (error.equals("")) {
            //TODO - testing
            //moved to loginworker!
           // StaticMethods methods = new StaticMethods();
           // SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
           // methods.InitializeRacersFragment(context, viewRacers, globals);

            tvStatusBottom.setText("Sync done!");
        } else {
            tvStatusBottom.setText(error);
        }
    }

}
