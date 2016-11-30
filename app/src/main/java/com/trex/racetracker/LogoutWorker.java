package com.trex.racetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
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

import static com.trex.racetracker.StaticMethods.*;

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


    //constructor
    public LogoutWorker(Context ctx, View fragmentLogin, View fragmentRacers) {
        context = ctx;
        this.fragmentLogin = fragmentLogin;
        this.fragmentRacers = fragmentRacers;

    }

    public LogoutWorker(Context ctx, View fragmentLogin, View fragmentRacers, Handler handler) {
        context = ctx;
        this.fragmentLogin = fragmentLogin;
        this.fragmentRacers = fragmentRacers;
        this.handler = handler;
    }



    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String result = "";

        //if params[0] is 'login', then
        //params[2]=Username
        //params[3]=operator
        //params[4]=DeviceID
        //params[5]=logoutComment
        //in this case the LoginWorker should be called in this manner: backgroundWorker.execute(type,url,username,password,operator,deviceid,logincomment)
        if (type.equals("logout")) try {

            queryUrl = params[1];
            Username = params[2];
            Operator = params[3];
            DeviceID = params[4];
            LogoutComment = params[5];
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
            if (!LogoutComment.equals("")) {
                if (!post_data.equals("")) post_data += "&";
                post_data += URLEncoder.encode("logoutcomment", "UTF-8") + "=" + URLEncoder.encode(LogoutComment, "UTF-8");
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
        tvStatusTop.setText("Logging out ...");
        // alertDialog = new AlertDialog.Builder(context).create();
        // alertDialog.setTitle("Query returns");
    }

    @Override
    protected void onPostExecute(String result) {
        SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
        SharedPreferences.Editor editor = globals.edit();
        TextView tvStatusTop = (TextView) fragmentLogin.findViewById(R.id.tvStatusTop);
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);

        try {
            JSONObject jsonResult = new JSONObject(result);
            if (jsonResult.has("islogout")) {
                if (jsonResult.getString("islogout").equals("1")) {
                    editor.putBoolean("islogin",false);
                    editor.putString("username","");
                    editor.putString("password","");
                    editor.putString("operator","");
                    editor.putString("controlpoint","");
                    editor.commit();

                //    InitializeLoginFragment(context, fragmentLogin,globals);
                    dbHelper.deleteAllFromLoginInfo();
                    dbHelper.setEntriesNotMine();

                    Toast.makeText(context, "Logout Successful!", Toast.LENGTH_SHORT).show();
                } else {
                    tvStatusTop.setText(jsonResult.getString("logouterror"));
                }
            } else {
                tvStatusTop.setText("Error with database! Contact the administrator.");
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

        dbHelper.deleteAllFromActiveRacers();
        handler.sendEmptyMessage(0);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
