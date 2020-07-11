package com.trex.racetracker.Workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trex.racetracker.Database.DatabaseHelper;
import com.trex.racetracker.Activities.MainActivity;
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

import static com.trex.racetracker.Database.DbMethods.*;

/**
 * Created by Igor on 03.11.2016.
 */


public class LogoutWorker extends AsyncTask<String,Void,String> {
    private Context context;
    private View fragmentLogin;
    private View fragmentRacers;
    private String queryUrl;
    private String Username;
    private String Password;
    private String Operator;
    private String DeviceID;
    private String LogoutComment;
    private Handler handler;
    private SharedPreferences globals;
    Response response;


    //constructor
    public LogoutWorker(Context ctx, View fragmentLogin, View fragmentRacers) {
        context = ctx;
        this.fragmentLogin = fragmentLogin;
        this.fragmentRacers = fragmentRacers;
        this.globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
    }

    public LogoutWorker(Context ctx, View fragmentLogin, View fragmentRacers, Handler handler) {
        context = ctx;
        this.fragmentLogin = fragmentLogin;
        this.fragmentRacers = fragmentRacers;
        this.handler = handler;
        this.globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
    }

    @Override
    protected void onPreExecute() {
        TextView tvStatusTop = (TextView) fragmentLogin.findViewById(R.id.tvStatusTop);
        tvStatusTop.setText("Logging out ...");
        // alertDialog = new AlertDialog.Builder(context).create();
        // alertDialog.setTitle("Query returns");
    }

    @Override
    protected String doInBackground(String... params) {

        try {

            queryUrl = globals.getString("hostUrl", "") + "/timing/mobile/logout";
            Username = params[0];
            Operator = params[1];
            DeviceID = params[2];
            LogoutComment = params[3];
            // POST Request
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("username", Username);
            postDataParams.put("operator", Operator);
            postDataParams.put("deviceid", DeviceID);
            postDataParams.put("logincomment", LogoutComment);

            response = RequestHelper.sendPost(queryUrl, postDataParams, globals, true);
            return response.getMessage();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.getMessage();
    }

    @Override
    protected void onPostExecute(String result) {
        SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
        SharedPreferences.Editor editor = globals.edit();
        TextView tvStatusTop = (TextView) fragmentLogin.findViewById(R.id.tvStatusTop);
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);

        try {

            int responseCode = response.getResponseCode();
            if (responseCode >= 200 && responseCode <= 299) {
                editor.putBoolean("loggedIn",false);
                editor.putString("username","");
                editor.putString("password","");
                editor.putString("jwt-token","");
                editor.putString("operator","");
                editor.putString("controlpoint","");
                editor.commit();

                deleteAllFromLoginInfo(context);
                setEntriesNotMine(context);

                Toast.makeText(context, "Logout Successful!", Toast.LENGTH_SHORT).show();

            } else {
                tvStatusTop.setText("Error with database! Contact the administrator.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EditText etUsername = (EditText) fragmentLogin.findViewById(R.id.etUsername);
        etUsername.setText("");

        EditText etPassword = (EditText) fragmentLogin.findViewById(R.id.etPassword);
        etPassword.setText("");

        EditText etOperator = (EditText) fragmentLogin.findViewById(R.id.etOperator);
        etOperator.setText("");

        //clean up states for Racers expandableListView
        if (globals.contains("elvRacersState0")) {
            int i=0;
            SharedPreferences.Editor editor1 = globals.edit();
            boolean moreToClear = true;
            while (moreToClear) {
                if (globals.contains("elvRacersState" + i)) {
                    editor1.remove("elvRacersState" + i);
                } else {
                    moreToClear = false;
                }
                i++;
            }
            editor1.commit();
        }

        deleteAllFromActiveRacers(context);
        handler.sendEmptyMessage(0);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
