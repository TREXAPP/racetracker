package com.trex.racetracker;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.trex.racetracker.DbMethods.*;
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
    AlertDialog alertDialog;


    //constructor
    public LoginWorker(Context ctx, View viewSync, View viewRacers) {
        context = ctx;
        this.fragmentLogin = viewSync;
        this.viewRacers = viewRacers;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String result = "";

        if (type.equals("login")) try {

            queryUrl = params[1];
            Username = params[2];
            Password = params[3];
            Operator = params[4];
            DeviceID = params[5];
            LoginComment = params[6];
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
            if (!Password.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(Password, "UTF-8");
            }
            if (!Operator.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("operator", "UTF-8") + "=" + URLEncoder.encode(Operator, "UTF-8");
            }
            if (!DeviceID.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("deviceid", "UTF-8") + "=" + URLEncoder.encode(DeviceID, "UTF-8");
            }
            if (!LoginComment.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("logincomment", "UTF-8") + "=" + URLEncoder.encode(LoginComment, "UTF-8");
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
        TextView tvStatusTop = (TextView) fragmentLogin.findViewById(R.id.tvStatusTop);
        tvStatusTop.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        tvStatusTop.setText("Logging in ...");
       // alertDialog = new AlertDialog.Builder(context).create();
       // alertDialog.setTitle("Query returns");
    }

    @Override
    protected void onPostExecute(String result) {
        SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
        SharedPreferences.Editor editor = globals.edit();
        TextView tvStatusTop = (TextView) fragmentLogin.findViewById(R.id.tvStatusTop);
      //  StaticMethods methods = new StaticMethods();
       DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);


        try {
            JSONObject jsonResult = new JSONObject(result);
            if (jsonResult.has("islogin")) {
                if (jsonResult.getString("islogin").equals("1")) {
                    String controlPoint = jsonResult.getJSONObject("0").getString("CPName");
                    editor.putBoolean("islogin",true);
                    editor.putString("username",Username);
                    editor.putString("password",Password);
                    editor.putString("operator",Operator);
                    editor.putString("controlpoint",controlPoint);


                //Provider provider = new Provider();
                    if (insertIntoLoginInfoDbM(jsonResult, context)) {
                        tvStatusTop.setText("Warning: Error while writing in SQLite, LoginInfo table. Contact the administrator;");
                    }
/*
                    //initialize globals for races
                    Cursor racesCursor = dbHelper.getDistinctRacesFromLoginInfo();
                    if (racesCursor.getCount() > 0) {
                        racesCursor.moveToFirst();
                        while (!racesCursor.isAfterLast()) {
                            if (!racesCursor.getString(0).equals("")) {
                                editor.putString("showRacers" + racesCursor.getString(0),"1");
                            }
                            racesCursor.moveToNext();
                        }

                    }
                    racesCursor.close();
*/

                    editor.commit();
                    InitializeLoginFragment(context, fragmentLogin,globals);

                    //login successful. Now synchronize the racers
                    final String TYPE_SYNC = "sync";
                    final String URL_SYNC = "http://app.trex.mk/sync.php";
                    final String COMMENT_SYNC = "";

                    SynchronizeWorker synchronizeWorker = new SynchronizeWorker(context, fragmentLogin, viewRacers);
                    synchronizeWorker.execute(TYPE_SYNC,URL_SYNC,Username,Operator,DeviceID,COMMENT_SYNC);

                    Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show();
                } else {
                   tvStatusTop.setText(FormatErrorString(jsonResult.getString("loginerror")));
                   tvStatusTop.setTextColor(Color.RED);
                }
            } else {
                tvStatusTop.setText("Error with database! Contact the administrator.");
                tvStatusTop.setTextColor(Color.RED);
            }
        } catch (JSONException e) {
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
}
