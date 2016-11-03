package com.trex.racetracker;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

import layout.Sync;

/**
 * Created by Igor on 16.10.2016.
 */

public class LoginWorker extends AsyncTask<String,Void,String> {
    private Context context;
    private View rootView;
    private String queryUrl;
    private String Username;
    private String Password;
    private String Operator;
    private String DeviceID;
    private String LoginComment;
    AlertDialog alertDialog;


    //constructor
    public LoginWorker(Context ctx, View view) {
        context = ctx;
        this.rootView = view;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String result = "";
        //params[0] tells us the type of of call we are doing
        //params[1] tells us the url to the web server that we are calling
        //depending on params[0], if it is 'query', then params[2] will be the actual query string
        //in this case the LoginWorker should be called in this manner: backgroundWorker.excecute(type,url,query)
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


        //if params[0] is 'login', then
        //params[2]=Username
        //params[3]=Password
        //params[4]=operator
        //params[5]=DeviceID
        //params[6]=loginComment
        //in this case the LoginWorker should be called in this manner: backgroundWorker.excecute(type,url,username,password,operator,deviceid,logincomment)
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
        TextView tvStatusTop = (TextView) rootView.findViewById(R.id.tvStatusTop);
        tvStatusTop.setText("Logging in ...");
       // alertDialog = new AlertDialog.Builder(context).create();
       // alertDialog.setTitle("Query returns");
    }

    @Override
    protected void onPostExecute(String result) {
        SharedPreferences globals = context.getSharedPreferences(MainActivity.GLOBALS,0);
        SharedPreferences.Editor editor = globals.edit();
        TextView tvStatusTop = (TextView) rootView.findViewById(R.id.tvStatusTop);

        try {
            JSONObject jsonResult = new JSONObject(result);
            if (jsonResult.has("islogin")) {
                if (jsonResult.getString("islogin").equals("1")) {
                    String controlPoint = "PrisadTEST"; //TODO
                    editor.putString("islogin","1");
                    editor.putString("username",Username);
                    editor.putString("operator",Operator);
                    editor.putString("controlpoint",controlPoint);
                    editor.commit();
                    Methods methods = new Methods();
                    methods.InitializeSyncFragment(context,rootView,globals);

                    Toast.makeText(context, "Login Successful!\nWelcome " + Operator + " at control point " + controlPoint + "!", Toast.LENGTH_SHORT).show();
                } else {
                    tvStatusTop.setText(jsonResult.getString("loginerror"));
                }
            } else {
                tvStatusTop.setText("Error with database! Contact the administrator.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



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
