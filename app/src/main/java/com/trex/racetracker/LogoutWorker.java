package com.trex.racetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

/**
 * Created by Igor on 03.11.2016.
 */


public class LogoutWorker extends AsyncTask<String,Void,String> {
    private Context context;
    private View fragmentSync;
    private View fragmentRacers;
    private String queryUrl;
    private String Username;
    private String Password;
    private String Operator;
    private String DeviceID;
    private String LogoutComment;
    AlertDialog alertDialog;


    //constructor
    public LogoutWorker(Context ctx, View fragmentSync, View fragmentRacers) {
        context = ctx;
        this.fragmentSync = fragmentSync;
        this.fragmentRacers = fragmentRacers;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String result = "";
        //params[0] tells us the type of of call we are doing
        //params[1] tells us the url to the web server that we are calling
        //depending on params[0], if it is 'query', then params[2] will be the actual query string
        //in this case the LoginWorker should be called in this manner: backgroundWorker.excecute(type,url,query)
        /*
        if (type.equals("query")) {
            try {

                String query = params[1];
                String queryUrl = params[2]; //String queryUrl = "http://app.trex.mk/query.php"

                URL url = new URL(queryUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("query","UTF-8")+"="+URLEncoder.encode(query,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //now get the response
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String line="";
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
        }
        */


        //if params[0] is 'login', then
        //params[2]=Username
        //params[3]=operator
        //params[4]=DeviceID
        //params[5]=logoutComment
        //in this case the LoginWorker should be called in this manner: backgroundWorker.excecute(type,url,username,password,operator,deviceid,logincomment)
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
        TextView tvStatusTop = (TextView) fragmentSync.findViewById(R.id.tvStatusTop);
        tvStatusTop.setText("Logging out ...");
        // alertDialog = new AlertDialog.Builder(context).create();
        // alertDialog.setTitle("Query returns");
    }

    @Override
    protected void onPostExecute(String result) {
        SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
        SharedPreferences.Editor editor = globals.edit();
        TextView tvStatusTop = (TextView) fragmentSync.findViewById(R.id.tvStatusTop);
        Methods methods = new Methods();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        try {
            JSONObject jsonResult = new JSONObject(result);
            if (jsonResult.has("islogout")) {
                if (jsonResult.getString("islogout").equals("1")) {
                    editor.putString("islogin","0");
                    editor.putString("username","");
                    editor.putString("operator","");
                    editor.putString("controlpoint","");
                    editor.commit();

                    methods.InitializeSyncFragment(context,fragmentSync,globals);
                    dbHelper.deleteAllFromLoginInfo();

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
        EditText etUsername = (EditText) fragmentSync.findViewById(R.id.etUsername);
        etUsername.setText("");

        EditText etPassword = (EditText) fragmentSync.findViewById(R.id.etPassword);
        etPassword.setText("");

        EditText etOperator = (EditText) fragmentSync.findViewById(R.id.etOperator);
        etOperator.setText("");

        dbHelper.deleteAllFromActiveRacers();
        methods.InitializeRacersFragment(context, fragmentRacers, globals);

        // Methods methods = new Methods();
        // SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
        // methods.InitializeRacersFragment(context, viewRacers, globals);


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
