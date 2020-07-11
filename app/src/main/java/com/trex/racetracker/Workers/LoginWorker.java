package com.trex.racetracker.Workers;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static com.trex.racetracker.Database.DbMethods.*;
import static com.trex.racetracker.StaticMethods.*;


/**
 * Created by Igor on 16.10.2016.
 */

public class LoginWorker extends AsyncTask<String,Void,String> {
    private Context context;
    private View fragmentLogin;
    private View viewRacers;
    private String queryUrl;
    private String Username;
    private String Password;
    private String Operator;
    private String DeviceID;
    private String LoginComment;
    private int responseCode;
    private SharedPreferences globals;
    AlertDialog alertDialog;
    Response response;


    //constructor
    public LoginWorker(Context ctx, View viewSync, View viewRacers) {
        context = ctx;
        this.fragmentLogin = viewSync;
        this.viewRacers = viewRacers;
        this.globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
        this.response = new Response();
    }

    @Override
    protected void onPreExecute() {
        TextView tvStatusTop = (TextView) fragmentLogin.findViewById(R.id.tvStatusTop);
        tvStatusTop.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        tvStatusTop.setText("Logging in ...");
        // alertDialog = new AlertDialog.Builder(context).create();
        // alertDialog.setTitle("Query returns");
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String type = params[0];
            String result = "";
            if (!type.equals("login")) {
                return result;
            }

            queryUrl = globals.getString("hostUrl", "") + "/timing/authenticate";
            Username = params[1];
            Password = params[2];
            Operator = params[3];
            DeviceID = params[4];
            LoginComment = params[5];

            if (TextUtils.isEmpty(Username) || TextUtils.isEmpty(Password)) {
                return "Username and Password cannot be empty.";
            }

            // POST Request
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("username", Username);
            postDataParams.put("password", Password);
            postDataParams.put("operator", Operator);
            postDataParams.put("deviceid", DeviceID);
            postDataParams.put("logincomment", LoginComment);

            response = RequestHelper.sendPost(queryUrl, postDataParams, globals, false);
            return response.getMessage();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        SharedPreferences.Editor editor = globals.edit();
        TextView tvStatusTop = (TextView) fragmentLogin.findViewById(R.id.tvStatusTop);
      //  StaticMethods methods = new StaticMethods();
       DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);

        //TODO IJ - jwt token store in editor.put()
        //all other info retrieved

        try {
            JSONObject jsonResult = new JSONObject(result);
            int responseCode = response.getResponseCode();
            if (responseCode >= 200 && responseCode <= 299) {

                editor.putBoolean("loggedIn",true);
                editor.putString("jwt-token",jsonResult.getString("token"));
                editor.putString("username",Username);
                editor.putString("password",Password);
                editor.putString("operator",Operator);

                editor.commit();

                tvStatusTop.setText("Login successful. Synchronizing Data ...");

                SyncDataWorker syncDataWorker = new SyncDataWorker(context, fragmentLogin, viewRacers);
                syncDataWorker.execute();

                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (responseCode >= 400 && responseCode <= 499) {
                String errorString;
                if (jsonResult.has("error")) {
                    errorString = jsonResult.getString("error");
                    if (TextUtils.isEmpty(errorString) || errorString == "null") {
                        errorString = "Error while authenticating user";
                    }
                } else {
                    errorString = "Error while authenticating user";
                }
                tvStatusTop.setText(errorString);
                tvStatusTop.setTextColor(Color.RED);
                return;
            }
                tvStatusTop.setText("Error parsing login response");
                tvStatusTop.setTextColor(Color.RED);
        } catch (JSONException e) {
            Toast.makeText(context, "Error logging in: " + result, Toast.LENGTH_SHORT).show();
            tvStatusTop.setText("Error logging in");
            tvStatusTop.setTextColor(Color.RED);
            e.printStackTrace();
        }

        EditText etUsername = (EditText) fragmentLogin.findViewById(R.id.etUsername);
        etUsername.setText("");

        EditText etPassword = (EditText) fragmentLogin.findViewById(R.id.etPassword);
        etPassword.setText("");

        EditText etOperator = (EditText) fragmentLogin.findViewById(R.id.etOperator);
        etOperator.setText("");

      //  tvStatusTop.setText(result);
        //  TextView tvStatus = (TextView) context.findViewById()
        //alertDialog.setMessage(result);
        //alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    private static String encodeParams(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
    public static String sendPost(String r_url , JSONObject postDataParams) throws Exception {
        URL url = new URL(r_url);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(20000);
        conn.setConnectTimeout(20000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8"));
        writer.write(encodeParams(postDataParams));
        writer.flush();
        writer.close();
        os.close();

        int responseCode=conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {

            BufferedReader in=new BufferedReader( new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line="";
            while((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            return sb.toString();
        }
        return null;
    }
}
